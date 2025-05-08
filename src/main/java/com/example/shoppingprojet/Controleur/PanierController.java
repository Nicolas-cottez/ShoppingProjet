package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.*;
import com.example.shoppingprojet.Modele.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.List;

public class PanierController implements ControlledScreen {
    private MainController mainController;

    @FXML private VBox    articlesContainer;
    @FXML private Label   lblSubtotalBefore;
    @FXML private Label   lblSubtotalAfter;
    @FXML private Label   lblTotal;

    private final RemiseDAO remiseDAO = new RemiseDAOImpl();

    @FXML public void initialize() {
        // 1) charger les articles du panier
        Commande cmd = UtilisateurSession.getCommande();
        List<ArticlePanier> items = cmd.getArticles();

        // vider avant d’ajouter
        articlesContainer.getChildren().clear();
        for (ArticlePanier ap : items) {
            articlesContainer.getChildren().add(createItemNode(ap));
        }

        // 2) calculer et afficher le récapitulatif
        double subtotalBefore = items.stream()
                .mapToDouble(ArticlePanier::getTotal)
                .sum();

        double subtotalAfter = 0;
        for (ArticlePanier ap : items) {
            float total = ap.getTotal();
            Remise r = remiseDAO.findByArticle(ap.getArticle().getIdArticle());
            if (r!=null && ap.getQuantite()>=r.getSeuil()) {
                subtotalAfter += total * (1 - r.getPourcentageDeRemise()/100.0);
            } else {
                subtotalAfter += total;
            }
        }

        lblSubtotalBefore.setText(String.format("%.2f €", subtotalBefore));
        lblSubtotalAfter .setText(String.format("%.2f €", subtotalAfter));
        lblTotal         .setText(String.format("%.2f €", subtotalAfter)); // frais port offerts
    }

    /** construit le HBox pour un ArticlePanier donné */
    private HBox createItemNode(ArticlePanier ap) {
        Article art = ap.getArticle();

        // ➜ miniature
        ImageView thumb = new ImageView(
                new Image(getClass().getResourceAsStream("/com/example/shoppingprojet/image/" + art.getImageURL())));
        thumb.setFitWidth(100);
        thumb.setPreserveRatio(true);

        // ➜ détails texte
        Label name = new Label(art.getNom());
        name.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        Label desc = new Label(art.getDescription());
        desc.setWrapText(true);
        Label price = new Label(String.format("%.2f €", art.getPrixUnitaire()));
        VBox info = new VBox(5, name, desc, price);

        // ➜ contrôle quantité + suppression
        Spinner<Integer> spinner = new Spinner<>(1, 100, ap.getQuantite());
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs,o,n) -> {
            ap.setQuantite(n);
            initialize(); // actualiser récap
        });

        Button remove = new Button("🗑");
        remove.setOnAction(e -> {
            UtilisateurSession.getCommande().getArticles().remove(ap);
            initialize();
        });

        HBox controls = new HBox(5, remove, spinner);
        controls.setAlignment(Pos.CENTER);

        // assembler
        HBox box = new HBox(15, thumb, info, controls);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle("-fx-border-color: #ddd; -fx-border-radius:4; -fx-background-radius:4;");
        HBox.setHgrow(info, Priority.ALWAYS);

        return box;
    }

    @FXML public void handlePasserCommande() {
        mainController.showCheckout();
    }

    @Override
    public void setMainController(MainController mc) {
        this.mainController = mc;
    }
}
