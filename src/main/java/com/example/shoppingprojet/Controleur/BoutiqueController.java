package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.ArticleDAO;
import com.example.shoppingprojet.DAO.ArticleDAOImpl;
import com.example.shoppingprojet.Modele.Article;
import com.example.shoppingprojet.Modele.ArticlePanier;
import com.example.shoppingprojet.Modele.ClientSession;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.List;

public class BoutiqueController implements ControlledScreen {
    private MainController mainController;

    @FXML private TableView<Article>            tableArticles;
    @FXML private TableColumn<Article, String>  colNom;
    @FXML private TableColumn<Article, String>  colDescription;
    @FXML private TableColumn<Article, Float>   colPrix;
    @FXML private TableColumn<Article, Integer> colStock;
    @FXML private TableColumn<Article, String>  colMarque;
    @FXML private TableColumn<Article, Integer> colQuantiteCart;
    @FXML private TableColumn<Article, Void>    colAction;

    private final ArticleDAO articleDAO = new ArticleDAOImpl();

    @FXML
    public void initialize() {
        // nom, description, prix, stock, marque
        colNom.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNom())
        );
        colDescription.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getDescription())
        );
        colPrix.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getPrixUnitaire())
        );
        colStock.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getStock())
        );
        colMarque.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getNomMarque())
        );

        // quantité déjà présente dans le panier
        colQuantiteCart.setCellValueFactory(c -> {
            int idArt = c.getValue().getIdArticle();
            int qte = ClientSession.getCommande().getArticles().stream()
                    .filter(ap -> ap.getArticle().getIdArticle() == idArt)
                    .map(ArticlePanier::getQuantite)
                    .findFirst()
                    .orElse(0);
            return new SimpleObjectProperty<>(qte);
        });

        // bouton "Ajouter"
        colAction.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Article, Void> call(TableColumn<Article, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Ajouter");
                    {
                        btn.setOnAction(e -> {
                            Article art = getTableView().getItems().get(getIndex());
                            ClientSession.getCommande().ajouterArticle(art, 1);
                            tableArticles.refresh();
                        });
                    }
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                };
            }
        });

        // chargement initial
        List<Article> list = articleDAO.getAllArticles();
        tableArticles.setItems(FXCollections.observableArrayList(list));
    }

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
