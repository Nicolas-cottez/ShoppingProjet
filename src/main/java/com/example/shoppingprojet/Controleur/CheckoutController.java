package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.*;
import com.example.shoppingprojet.Modele.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CheckoutController implements ControlledScreen {
    private MainController mainController;

    @FXML private TextArea livraisonField;
    @FXML private TextArea facturationField;
    @FXML private ComboBox<String> paiementBox;
    @FXML private Button annulerButton;
    @FXML private Button confirmerButton;

    private final CommandeDAO commandeDAO = new CommandeDAOImpl();
    private final ArticleDAO   articleDAO  = new ArticleDAOImpl();

    @FXML
    public void initialize() {
        paiementBox.getItems().addAll("Carte bancaire", "PayPal", "Chèque");

        annulerButton.setOnAction(e -> mainController.showPanier());

        confirmerButton.setOnAction(e -> {
            // 1) Récupérer et mettre à jour la commande
            Commande currentCmd = UtilisateurSession.getCommande();
            currentCmd.setDateCommande(LocalDate.now());
            currentCmd.setHeureCommande(LocalTime.now());
            float total = (float) commandeDAO.calculerPrixCommande(currentCmd);
            currentCmd.setMontantTotal(total);

            // 2) Enregistrer la commande
            commandeDAO.ajouterCommande(currentCmd);

            // 3) Décrémenter le stock pour chaque ligne de commande
            for (ArticlePanier ap : currentCmd.getArticles()) {
                int idArt = ap.getArticle().getIdArticle();
                int qte   = ap.getQuantite();
                articleDAO.decreaseStock(idArt, qte);
            }

            // 4) Vider le panier (nouvelle commande vide)
            Commande newCmd = new Commande(
                    0,
                    LocalDate.now(),
                    LocalTime.now(),
                    0f,
                    UtilisateurSession.getUtilisateur(),
                    new ArrayList<>()
            );
            UtilisateurSession.setCommande(newCmd);

            // 5) Retourner à la boutique
            mainController.showBoutique();
        });
    }

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
