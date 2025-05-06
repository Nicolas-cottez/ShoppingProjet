package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.CommandeDAO;
import com.example.shoppingprojet.DAO.CommandeDAOImpl;
import com.example.shoppingprojet.DAO.LigneCommandeDAO;
import com.example.shoppingprojet.DAO.LigneCommandeDAOImpl;
import com.example.shoppingprojet.Modele.Commande;
import com.example.shoppingprojet.Modele.LigneCommande;
import com.example.shoppingprojet.Modele.ClientSession;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class HistoriqueController implements ControlledScreen {
    private MainController mainController;

    @FXML private TableView<Commande>       tableHistorique;
    @FXML private TableColumn<Commande,Integer> colIdCommande;
    @FXML private TableColumn<Commande,java.time.LocalDate> colDate;
    @FXML private TableColumn<Commande,java.time.LocalTime> colHeure;
    @FXML private TableColumn<Commande,Float>   colMontant;

    // nouveau : table de détail
    @FXML private TableView<LigneCommande>   tableDetail;
    @FXML private TableColumn<LigneCommande,String>  colArticleDetail;
    @FXML private TableColumn<LigneCommande,Integer> colQteDetail;
    @FXML private TableColumn<LigneCommande,Float>   colPrixDetail;

    private final CommandeDAO      commandeDAO     = new CommandeDAOImpl();
    private final LigneCommandeDAO ligneCommandeDAO = new LigneCommandeDAOImpl();

    @FXML
    public void initialize() {
        // configuration de la table des commandes
        colIdCommande.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getIdCommande()).asObject());
        colDate.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getDateCommande()));
        colHeure.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getHeureCommande()));
        colMontant.setCellValueFactory(c ->
                new SimpleFloatProperty(c.getValue().getMontantTotal()).asObject());

        int idClient = ClientSession.getClient().getIdUtilisateur();
        tableHistorique.setItems(FXCollections.observableArrayList(
                commandeDAO.getCommandesByClient(idClient)
        ));

        // config de la table de détail
        colArticleDetail.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getArticle().getNom()));
        colQteDetail.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getQuantite()).asObject());
        colPrixDetail.setCellValueFactory(c ->
                new SimpleFloatProperty(c.getValue().getPrixLigne()).asObject());

        // listener de sélection
        tableHistorique.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                loadDetail(sel.getIdCommande());
            } else {
                tableDetail.getItems().clear();
            }
        });
    }

    /** Charge et affiche les lignes pour la commande passée en paramètre */
    private void loadDetail(int idCommande) {
        // récupère les LigneCommande depuis le DAO
        tableDetail.setItems(FXCollections.observableArrayList(
                ligneCommandeDAO.getByCommande(idCommande)
        ));
    }

    @Override
    public void setMainController(MainController mc) {
        this.mainController = mc;
    }
}
