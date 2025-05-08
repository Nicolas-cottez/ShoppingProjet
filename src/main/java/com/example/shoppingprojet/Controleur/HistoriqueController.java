package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.*;
import com.example.shoppingprojet.Modele.*;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HistoriqueController implements ControlledScreen {
    private MainController mainController;

    @FXML private TableView<Commande>          tableHistorique;
    @FXML private TableColumn<Commande,Integer> colIdCommande;
    @FXML private TableColumn<Commande,java.time.LocalDate> colDate;
    @FXML private TableColumn<Commande,java.time.LocalTime> colHeure;
    @FXML private TableColumn<Commande,Float>   colMontant;

    @FXML private Label dateLabel, heureLabel, adresseLabel;

    @FXML private TableView<LigneCommande>      tableDetail;
    @FXML private TableColumn<LigneCommande,String>  colArticleDetail;
    @FXML private TableColumn<LigneCommande,Integer> colQteDetail;
    @FXML private TableColumn<LigneCommande,Float>   colPrixDetail;

    private final CommandeDAO      commandeDAO      = new CommandeDAOImpl();
    private final LigneCommandeDAO ligneCommandeDAO = new LigneCommandeDAOImpl();
    private final DateTimeFormatter dateFmt  = DateTimeFormatter.ISO_DATE;
    private final DateTimeFormatter timeFmt  = DateTimeFormatter.ISO_TIME;

    @FXML
    public void initialize() {
        // colonnes de la table “Historique”
        colIdCommande.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getIdCommande()).asObject());
        colDate.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getDateCommande()));
        colHeure.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getHeureCommande()));
        colMontant.setCellValueFactory(c ->
                new SimpleFloatProperty(c.getValue().getMontantTotal()).asObject());

        // charger les commandes du user
        var user = UtilisateurSession.getUtilisateur();
        if (user != null) {
            tableHistorique.setItems(
                    FXCollections.observableArrayList(
                            commandeDAO.getCommandesByUtilisateur(user.getIdUtilisateur())
                    )
            );
        }

        // colonnes du détail
        colArticleDetail.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getArticle().getNom()));
        colQteDetail.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getQuantite()).asObject());
        // préfixe “xN”
        colQteDetail.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Integer qty, boolean empty) {
                super.updateItem(qty, empty);
                setText(empty || qty==null ? null : "x" + qty);
            }
        });
        colPrixDetail.setCellValueFactory(c ->
                // prix unitaire * quantité
                new SimpleFloatProperty(
                        c.getValue().getArticle().getPrixUnitaire() * c.getValue().getQuantite()
                ).asObject()
        );

        // au clic sur une commande, afficher détail et en-tête
        tableHistorique.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldCmd, selCmd) -> {
                    if (selCmd != null) showDetails(selCmd);
                    else {
                        tableDetail.getItems().clear();
                        dateLabel.setText("");
                        heureLabel.setText("");
                        adresseLabel.setText("");
                    }
                });
    }

    private void showDetails(Commande cmd) {
        // Si aucune commande sélectionnée, on vide tout
        if (cmd == null) {
            tableDetail.getItems().clear();
            dateLabel.setText("");
            heureLabel.setText("");
            adresseLabel.setText("");
            return;
        }

        // 1) Mettre à jour les labels date / heure / adresse
        dateLabel.setText(cmd.getDateCommande().format(dateFmt));
        heureLabel.setText(cmd.getHeureCommande().format(timeFmt));
        adresseLabel.setText(cmd.getAdresseLivraison());

        // 2) Récupérer la liste brute de lignes de commande
        List<LigneCommande> rawLines = ligneCommandeDAO.getByCommande(cmd.getIdCommande());

        // 3) Agréger les quantités par article
        Map<Integer, Integer> mapQte = new LinkedHashMap<>();
        for (LigneCommande ln : rawLines) {
            int artId = ln.getArticle().getIdArticle();
            mapQte.put(artId, mapQte.getOrDefault(artId, 0) + ln.getQuantite());
        }

        // 4) Reconstruire une liste agrégée en appelant le constructeur à 4 args
        List<LigneCommande> aggregated = mapQte.entrySet().stream()
                .map(e -> {
                    int artId = e.getKey();
                    int qty   = e.getValue();
                    // retrouver l'objet Article complet
                    Article art = rawLines.stream()
                            .filter(ln -> ln.getArticle().getIdArticle() == artId)
                            .findFirst()
                            .orElseThrow()
                            .getArticle();
                    // calculer le prix de la ligne
                    float prixLigne = art.getPrixUnitaire() * qty;
                    // construire la ligne agrégée
                    return new LigneCommande(
                            cmd.getIdCommande(),  // idCommande
                            art,                  // l’article
                            qty,                  // quantité totale
                            prixLigne             // prix ligne (quantité × prix unitaire)
                    );
                })
                .collect(Collectors.toList());

        // 5) Injecter dans la TableView des détails
        tableDetail.setItems(FXCollections.observableArrayList(aggregated));
    }


    @Override
    public void setMainController(MainController mc) {
        this.mainController = mc;
    }
}
