package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.ArticleDAO;
import com.example.shoppingprojet.DAO.ArticleDAOImpl;
import com.example.shoppingprojet.Modele.Article;
import com.example.shoppingprojet.Modele.UtilisateurSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BoutiqueController implements ControlledScreen {
    private MainController mainController;

    @FXML private TilePane tilePane;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> marqueBox;
    @FXML private ComboBox<String> sortBox;

    private final ArticleDAO articleDAO = new ArticleDAOImpl();
    private ObservableList<Article> masterList;

    @FXML
    public void initialize() {
        // 1) Chargement initial
        List<Article> all = articleDAO.getAllArticles();
        masterList = FXCollections.observableArrayList(all);

        // 2) Remplir la liste des marques
        marqueBox.getItems().add("Toutes");
        all.stream()
                .map(Article::getNomMarque)
                .distinct()
                .sorted()
                .forEach(marqueBox.getItems()::add);
        marqueBox.getSelectionModel().selectFirst(); // "Toutes"

        // 3) Affichage initial
        refreshTiles(masterList);
    }

    /** Appliquer recherche, filtre et tri */
    @FXML
    private void applyFilters() {
        String keyword = searchField.getText().trim().toLowerCase();
        String marque  = marqueBox.getValue();
        String ordre   = sortBox.getValue();

        List<Article> filtered = masterList.stream()
                // recherche par mot-clé
                .filter(a -> a.getNom().toLowerCase().contains(keyword))
                // filtre par marque
                .filter(a -> "Toutes".equals(marque) || a.getNomMarque().equals(marque))
                .collect(Collectors.toList());

        // tri par prix
        if ("Décroissant".equals(ordre)) {
            filtered.sort(Comparator.comparing(Article::getPrixUnitaire).reversed());
        } else {
            filtered.sort(Comparator.comparing(Article::getPrixUnitaire));
        }

        refreshTiles(FXCollections.observableArrayList(filtered));
    }

    /** Remet tout à zéro */
    @FXML
    private void resetFilters() {
        searchField.clear();
        marqueBox.getSelectionModel().selectFirst();
        sortBox.getSelectionModel().clearSelection();
        refreshTiles(masterList);
    }

    /** (Re)construit toutes les vignettes dans le TilePane */
    private void refreshTiles(ObservableList<Article> liste) {
        tilePane.getChildren().clear();
        for (Article art : liste) {
            tilePane.getChildren().add(createCard(art));
        }
    }

    /** Crée une “card” pour chaque produit */
    private Node createCard(Article art) {
        // --- Image plus grosse (200px) ---
        ImageView img = new ImageView(
                new Image(getClass().getResourceAsStream("/com/example/shoppingprojet/image/" + art.getImageURL())));
        img.setFitWidth(400);
        img.setPreserveRatio(true);

        // Nom et prix
        Label name  = new Label(art.getNom());
        name.setWrapText(true);
        name.setMaxWidth(200);

        Label price = new Label(String.format("%.2f €", art.getPrixUnitaire()));

        // Quantité + bouton
        TextField qty = new TextField("1");
        qty.setPrefWidth(50);
        Button add = new Button("Ajouter");
        add.setOnAction(e -> {
            int q = Integer.parseInt(qty.getText());
            UtilisateurSession.getCommande().ajouterArticle(art, q);
            // tu peux afficher une notif si tu veux
        });
        HBox ctrls = new HBox(5, qty, add);
        ctrls.setAlignment(Pos.CENTER);

        // Assemblage
        VBox card = new VBox(10, img, name, price, ctrls);
        card.setAlignment(Pos.TOP_CENTER);
        card.setStyle("""
            -fx-border-color: #ccc;
            -fx-border-radius: 5;
            -fx-padding: 10;
            -fx-background-radius: 5;
            -fx-background-color: white;
        """);
        return card;
    }

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
