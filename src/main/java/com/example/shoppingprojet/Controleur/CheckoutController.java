package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.ArticleDAO;
import com.example.shoppingprojet.DAO.ArticleDAOImpl;
import com.example.shoppingprojet.DAO.CommandeDAO;
import com.example.shoppingprojet.DAO.CommandeDAOImpl;
import com.example.shoppingprojet.Modele.ArticlePanier;
import com.example.shoppingprojet.Modele.Commande;
import com.example.shoppingprojet.Modele.UtilisateurSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CheckoutController implements ControlledScreen {
    private MainController mainController;

    @FXML private TextArea      livraisonField;
    @FXML private ComboBox<String> paiementBox;
    @FXML private Label         cardNumberLabel, expiryLabel, cvvLabel;
    @FXML private TextField     cardNumberField, expiryField;
    @FXML private PasswordField cvvField;
    @FXML private Button        annulerButton, confirmerButton;

    private final CommandeDAO commandeDAO = new CommandeDAOImpl();
    private final ArticleDAO articleDAO   = new ArticleDAOImpl();

    @FXML
    public void initialize() {
        // 1) Pré-remplir l’adresse
        livraisonField.setText(UtilisateurSession.getUtilisateur().getAdressePostal());

        // 2) Moyens de paiement
        paiementBox.getItems().setAll("Carte bancaire", "PayPal", "Chèque");

        // 3) Cacher les champs CB
        cardNumberLabel.setVisible(false);
        cardNumberField.setVisible(false);
        expiryLabel.setVisible(false);
        expiryField.setVisible(false);
        cvvLabel.setVisible(false);
        cvvField.setVisible(false);

        // 4) Afficher/masquer CB selon sélection
        paiementBox.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldV, newV) -> {
                    boolean cb = "Carte bancaire".equals(newV);
                    cardNumberLabel.setVisible(cb);
                    cardNumberField.setVisible(cb);
                    expiryLabel.setVisible(cb);
                    expiryField.setVisible(cb);
                    cvvLabel.setVisible(cb);
                    cvvField.setVisible(cb);
                });

        // 5) Annuler → panier
        annulerButton.setOnAction(e -> mainController.showPanier());

        // 6) Confirmer → validation & enregistrement
        confirmerButton.setOnAction(e -> {
            Commande currentCmd = UtilisateurSession.getCommande();

            // a) Adresse de livraison modifiable
            currentCmd.setAdresseLivraison(livraisonField.getText());

            // b) Vérification du moyen de paiement sélectionné
            String moyen = paiementBox.getValue();
            if (moyen == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Veuillez choisir un moyen de paiement.");
                alert.showAndWait();
                return;
            }

            // c) Si CB : tous les champs doivent être renseignés
            if ("Carte bancaire".equals(moyen)) {
                if (cardNumberField.getText().isEmpty()
                        || expiryField.getText().isEmpty()
                        || cvvField.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Paiement incomplet");
                    alert.setHeaderText(null);
                    alert.setContentText(
                            "Merci de remplir numéro, date d’expiration et CVV.");
                    alert.showAndWait();
                    return;
                }
            }

            // d) Date, heure et total
            currentCmd.setDateCommande(LocalDate.now());
            currentCmd.setHeureCommande(LocalTime.now());
            float total = (float) commandeDAO.calculerPrixCommande(currentCmd);
            currentCmd.setMontantTotal(total);

            // e) Persister la commande (adresse + user)
            commandeDAO.ajouterCommande(currentCmd);

            // f) Mettre à jour les stocks
            for (ArticlePanier ap : currentCmd.getArticles()) {
                articleDAO.decreaseStock(
                        ap.getArticle().getIdArticle(),
                        ap.getQuantite()
                );
            }

            // g) Réinitialiser le panier (vide + adresse préremplie)
            Commande newCmd = new Commande(
                    0,
                    LocalDate.now(),
                    LocalTime.now(),
                    0f,
                    UtilisateurSession.getUtilisateur().getAdressePostal(),
                    UtilisateurSession.getUtilisateur(),
                    new ArrayList<>()
            );
            UtilisateurSession.setCommande(newCmd);

            // h) Retour à la boutique
            mainController.showBoutique();
        });
    }

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
