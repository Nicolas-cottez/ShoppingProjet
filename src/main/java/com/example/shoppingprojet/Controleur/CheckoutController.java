package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.CommandeDAOImpl;
import com.example.shoppingprojet.DAO.FactureDAOImpl;
import com.example.shoppingprojet.DAO.LigneCommandeDAOImpl;
import com.example.shoppingprojet.DAO.RemiseDAOImpl;
import com.example.shoppingprojet.Modele.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class CheckoutController {

    @FXML private TextArea    livraisonField;
    @FXML private TextArea    facturationField;
    @FXML private ComboBox<String> paiementBox;
    @FXML private HBox        cbBox;
    @FXML private TextField   cardNumberField, expField, cvvField;
    @FXML private Button      annulerButton, confirmerButton;

    @FXML
    public void initialize() {
        // 1) Setup moyens de paiement
        ObservableList<String> moyens = FXCollections.observableArrayList(
                "Carte bancaire", "PayPal", "Virement"
        );
        paiementBox.setItems(moyens);

        // 2) Masquer la HBox CB par défaut
        cbBox.setManaged(false);
        cbBox.setVisible(false);

        // 3) Afficher/masquer la HBox CB sur sélection
        paiementBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    boolean cb = "Carte bancaire".equals(newVal);
                    cbBox.setManaged(cb);
                    cbBox.setVisible(cb);
                });

        // 4) Actions des boutons
        annulerButton.setOnAction(e -> injectView("/com/example/shoppingprojet/panier.fxml"));
        confirmerButton.setOnAction(e -> confirmerCommande());
    }

    /** Valide, persiste commande + facture, puis retourne à la boutique */
    private void confirmerCommande() {
        Commande cmd = ClientSession.getCommande();
        // (validation superficielle omise pour la brièveté)

        // Calcul total avec remises
        double total = cmd.getArticles().stream()
                .mapToDouble(ap -> {
                    Remise r = new RemiseDAOImpl()
                            .findByArticle(ap.getArticle().getIdArticle());
                    double ligne = ap.getTotal();
                    if (r != null && ap.getQuantite() >= r.getSeuil()) {
                        ligne *= (1 - r.getPourcentageDeRemise() / 100.0);
                    }
                    return ligne;
                })
                .sum();

        // Persistance
        int idCmd = new CommandeDAOImpl().ajouterCommande(cmd);
        cmd.getArticles().forEach(ap ->
                new LigneCommandeDAOImpl().ajouterLigneCommande(
                        new LigneCommande(idCmd, ap.getArticle(), ap.getQuantite(), (float)ap.getTotal())
                )
        );
        new FactureDAOImpl().creerFacture(
                new Facture(0, idCmd, LocalDate.now(), LocalTime.now(), (float)total)
        );

        // Reset et retour boutique
        ClientSession.clearCommande();
        injectView("/com/example/shoppingprojet/boutique.fxml");
    }

    /** Injecte une vue dans le centre du BorderPane sans changer de Stage */
    private void injectView(String fxml) {
        try {
            // On part de la scène actuelle
            AnchorPane center = (AnchorPane) ((BorderPane)
                    livraisonField.getScene().getRoot()
            ).getCenter();
            Node view = FXMLLoader.load(getClass().getResource(fxml));
            center.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
