package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.*;
import com.example.shoppingprojet.Modele.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.event.ActionEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CheckoutController implements ControlledScreen {
    private MainController mainController;

    //–– FXML injections ––
    @FXML private TextArea livraisonField;
    @FXML private ComboBox<String> paiementBox;
    @FXML private GridPane cbPane;               // conteneur des champs CB
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryField;
    @FXML private PasswordField cvvField;
    @FXML private Label subtotalLabel;
    @FXML private Label shippingLabel;
    @FXML private Label totalLabel;
    @FXML private VBox summaryBox;

    private final CommandeDAO commandeDAO = new CommandeDAOImpl();
    private final LigneCommandeDAO ligneDAO = new LigneCommandeDAOImpl();
    private final ArticleDAO articleDAO = new ArticleDAOImpl();

    @FXML
    public void initialize() {
        // 1) Adresse pré-remplie et non éditable
        livraisonField.setText(UtilisateurSession.getUtilisateur().getAdressePostal());
        livraisonField.setEditable(false);

        // 2) Moyens de paiement
        paiementBox.getItems().setAll("Carte bancaire", "PayPal", "Chèque");
        paiementBox.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            // on affiche le panneau CB seulement si "Carte bancaire"
            cbPane.setVisible("Carte bancaire".equals(newV));
        });

        // 3) Remplir le récapitulatif
        refreshSummary();
    }

    /** Appelé quand on clique sur "Modifier" pour rendre l’adresse éditable */
    @FXML
    private void handleEditAddress(MouseEvent ev) {
        livraisonField.setEditable(true);
        livraisonField.requestFocus();
    }

    /** Appelé quand on clique sur "Passer au paiement" */
    @FXML
    private void handleConfirm(ActionEvent ev) {
        Commande cmd = UtilisateurSession.getCommande();

        // a) mettre à jour l’adresse
        cmd.setAdresseLivraison(livraisonField.getText());

        // b) vérifier choix de paiement
        String moyen = paiementBox.getValue();
        if (moyen == null) {
            new Alert(Alert.AlertType.ERROR, "Veuillez choisir un moyen de paiement.").showAndWait();
            return;
        }

        // c) si CB, vérifier champ remplis
        if ("Carte bancaire".equals(moyen)) {
            if (cardNumberField.getText().isBlank() ||
                    expiryField.getText().isBlank() ||
                    cvvField.getText().isBlank()) {
                new Alert(Alert.AlertType.ERROR, "Merci de remplir numéro, date d’expiration et CVV.").showAndWait();
                return;
            }
        }

        // d) horodatage + total
        cmd.setDateCommande(LocalDate.now());
        cmd.setHeureCommande(LocalTime.now());
        float total = (float) commandeDAO.calculerPrixCommande(cmd);
        cmd.setMontantTotal(total);

        // e) persister commande + lignes
        commandeDAO.ajouterCommande(cmd);
        for (ArticlePanier ap : cmd.getArticles()) {
            float prixLigne = ap.getArticle().getPrixUnitaire() * ap.getQuantite();
            LigneCommande lc = new LigneCommande(
                    cmd.getIdCommande(),
                    ap.getArticle(),
                    ap.getQuantite(),
                    prixLigne
            );
            ligneDAO.ajouterLigneCommande(lc);
            // mettre à jour stock
            articleDAO.decreaseStock(ap.getArticle().getIdArticle(), ap.getQuantite());
        }

        // f) réinitialiser le panier (vide + adresse préremplie)
        UtilisateurSession.clearCommande();
        // vous pouvez aussi recréer un nouvel utilisateur/nouvelle session ici si besoin

        // g) retour à la boutique
        mainController.showBoutique();
    }

    /** Met à jour la partie “Récapitulatif” à droite */
    private void refreshSummary() {
        Commande cmd = UtilisateurSession.getCommande();
        double sousTotal = commandeDAO.calculerPrixCommande(cmd);
        double frais = 0.0;         // ou logique de calcul
        double total    = sousTotal + frais;

        subtotalLabel.setText(String.format("%.2f €", sousTotal));
        shippingLabel.setText(frais == 0 ? "Gratuit" : String.format("%.2f €", frais));
        totalLabel.setText(String.format("%.2f €", total));

        summaryBox.getChildren().clear();
        for (ArticlePanier ap : cmd.getArticles()) {
            HBox row = new HBox(10);
            // Image miniature
            ImageView iv = new ImageView(
                    new Image(getClass().getResourceAsStream("/com/example/shoppingprojet/image/" + ap.getArticle().getImageURL())));
            iv.setFitWidth(50);
            iv.setPreserveRatio(true);

            // Description texte
            VBox info = new VBox(2);
            Label title = new Label(ap.getArticle().getNom());
            Label qty   = new Label(
                    String.format("Quantité : %d @ %.2f €", ap.getQuantite(), ap.getArticle().getPrixUnitaire())
            );
            Label prix  = new Label(
                    String.format("%.2f €", ap.getQuantite() * ap.getArticle().getPrixUnitaire())
            );
            info.getChildren().addAll(title, qty, prix);

            // étirer pour alignement
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            row.getChildren().addAll(iv, info, spacer);
            summaryBox.getChildren().add(row);
        }
    }

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
