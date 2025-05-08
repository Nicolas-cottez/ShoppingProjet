package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.ArticleDAO;
import com.example.shoppingprojet.DAO.ArticleDAOImpl;
import com.example.shoppingprojet.Modele.Article;
import com.example.shoppingprojet.Modele.UtilisateurSession;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.List;

public class BoutiqueController implements ControlledScreen {
    private MainController mainController;

    @FXML private TilePane tilePane;

    private final ArticleDAO articleDAO = new ArticleDAOImpl();

    @FXML
    public void initialize() {
        // on récupère tous les articles
        List<Article> list = articleDAO.getAllArticles();
        // puis on crée une card par article
        for (Article art : list) {
            tilePane.getChildren().add(createCard(art));
        }
    }

    private Node createCard(Article art) {
        // 1) L'image du produit
        ImageView imgView = new ImageView(
                new Image(getClass().getResourceAsStream(
                        "/com/example/shoppingprojet/image/" + art.getImageURL()
                ))
        );
        imgView.setFitWidth(120);
        imgView.setPreserveRatio(true);

        // 2) Nom et prix
        Label name  = new Label(art.getNom());
        name.setWrapText(true);
        name.setMaxWidth(120);
        Label price = new Label(String.format("%.2f €", art.getPrixUnitaire()));

        // 3) Quantité + bouton “Ajouter”
        TextField qty = new TextField("1");
        qty.setPrefWidth(40);
        Button add = new Button("Ajouter");
        add.setOnAction(e -> {
            int q = Integer.parseInt(qty.getText());
            UtilisateurSession.getCommande().ajouterArticle(art, q);
            // tu peux éventuellement afficher une notif ici
        });
        HBox controls = new HBox(5, qty, add);
        controls.setAlignment(Pos.CENTER);

        // 4) Mise en forme de la “card”
        VBox card = new VBox(8, imgView, name, price, controls);
        card.setAlignment(Pos.TOP_CENTER);
        card.setStyle("""
            -fx-border-color: black;
            -fx-border-radius: 5;
            -fx-padding: 10;
            -fx-background-radius: 5;
        """);

        return card;
    }

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
