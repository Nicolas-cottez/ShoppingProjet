package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.CommandeDAOImpl;
import com.example.shoppingprojet.DAO.FactureDAOImpl;
import com.example.shoppingprojet.DAO.LigneCommandeDAOImpl;
import com.example.shoppingprojet.DAO.RemiseDAOImpl;
import com.example.shoppingprojet.Modele.ArticlePanier;
import com.example.shoppingprojet.Modele.ClientSession;
import com.example.shoppingprojet.Modele.Commande;
import com.example.shoppingprojet.Modele.Facture;
import com.example.shoppingprojet.Modele.LigneCommande;
import com.example.shoppingprojet.Modele.Remise;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class CheckoutController {

    @FXML private TextArea livraisonField;
    @FXML private TextArea facturationField;
    @FXML private ComboBox<String> paiementBox;
    @FXML private TextField cardNumberField;
    @FXML private TextField expField;
    @FXML private TextField cvvField;
    @FXML private Button annulerButton;    // ← revient au panier
    @FXML private Button confirmerButton;  // ← valide et persiste

    @FXML
    public void initialize() {
        // 1) Setup des moyens de paiement
        ObservableList<String> moyens = FXCollections.observableArrayList(
                "Carte bancaire", "PayPal", "Virement"
        );
        paiementBox.setItems(moyens);

        // 2) Champs CB masqués par défaut
        cardNumberField.setManaged(false); cardNumberField.setVisible(false);
        expField       .setManaged(false); expField       .setVisible(false);
        cvvField       .setManaged(false); cvvField       .setVisible(false);

        paiementBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    boolean cb = "Carte bancaire".equals(newVal);
                    cardNumberField.setManaged(cb); cardNumberField.setVisible(cb);
                    expField       .setManaged(cb); expField       .setVisible(cb);
                    cvvField       .setManaged(cb); cvvField       .setVisible(cb);
                });

        // 3) On retire ici tout setOnAction car on le gère via onAction dans le FXML
    }

    /**
     * Méthode liée à onAction="#annuler" dans checkout.fxml
     */
    @FXML
    public void annuler() {
        injectView("/com/example/shoppingprojet/panier.fxml");
    }

    /**
     * Méthode liée à onAction="#confirmerCommande" dans checkout.fxml
     * Valide la commande, applique les remises, persiste commande+lignes+facture, et revient au catalogue.
     */
    @FXML
    public void confirmerCommande() {
        // a) Validation minimale des champs
        String liv = livraisonField.getText().trim();
        String fac = facturationField.getText().trim();
        String pay = paiementBox.getValue();
        if (liv.isEmpty() || fac.isEmpty() || pay == null) {
            System.out.println("⚠ Veuillez remplir adresse et mode de paiement.");
            return;
        }
        if ("Carte bancaire".equals(pay)) {
            if (cardNumberField.getText().trim().isEmpty()
                    || expField.getText().trim().isEmpty()
                    || cvvField.getText().trim().isEmpty()) {
                System.out.println("⚠ Veuillez remplir les infos de carte.");
                return;
            }
        }

        // b) Calcul du total avec remises
        Commande cmd = ClientSession.getCommande();
        double total = 0;
        RemiseDAOImpl remiseDao = new RemiseDAOImpl();
        for (ArticlePanier ap : cmd.getArticles()) {
            Remise r = remiseDao.findByArticle(ap.getArticle().getIdArticle());
            double ligne = ap.getTotal();
            if (r != null && ap.getQuantite() >= r.getSeuil()) {
                ligne *= (1 - r.getPourcentageDeRemise() / 100.0);
            }
            total += ligne;
        }

        // c) Persister commande + lignes
        int idCmd = new CommandeDAOImpl().ajouterCommande(cmd);
        LigneCommandeDAOImpl ligneDao = new LigneCommandeDAOImpl();
        for (ArticlePanier ap : cmd.getArticles()) {
            ligneDao.ajouterLigneCommande(
                    new LigneCommande(idCmd, ap.getArticle(), ap.getQuantite(), (float) ap.getTotal())
            );
        }

        // d) Persister facture (attention : créez bien la table `factures` en base !)
        new FactureDAOImpl().creerFacture(new Facture(
                0, idCmd, LocalDate.now(), LocalTime.now(), (float) total
        ));

        // e) Réinitialiser la session et revenir au catalogue
        ClientSession.clearCommande();
        injectView("/com/example/shoppingprojet/boutique.fxml");
    }

    /**
     * Injecte la vue fxml passée en param dans le centre de main.fxml (BorderPane).
     */
    private void injectView(String fxml) {
        try {
            // Récupère le BorderPane racine (main.fxml)
            BorderPane root = (BorderPane) livraisonField.getScene().getRoot();
            AnchorPane content = (AnchorPane) root.getCenter();
            Node view = FXMLLoader.load(getClass().getResource(fxml));
            content.getChildren().setAll(view);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
