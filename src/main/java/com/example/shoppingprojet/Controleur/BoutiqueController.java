// BoutiqueController.java
package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.DBConnection;
import com.example.shoppingprojet.Modele.Article;
import com.example.shoppingprojet.Modele.ArticlePanier;
import com.example.shoppingprojet.Modele.ClientSession;
import com.example.shoppingprojet.Modele.Commande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BoutiqueController {

    @FXML private TableView<Article> tableArticles;
    @FXML private TableColumn<Article, String>  colNom;
    @FXML private TableColumn<Article, String>  colDescription;
    @FXML private TableColumn<Article, Float>   colPrix;
    @FXML private TableColumn<Article, Integer> colStock;
    @FXML private TableColumn<Article, String>  colMarque;
    @FXML private TableColumn<Article, Void>    colAction;

    private final ObservableList<Article> articles = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadArticlesFromDatabase();

        colNom        .setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrix       .setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colStock      .setCellValueFactory(new PropertyValueFactory<>("stock"));
        colMarque     .setCellValueFactory(new PropertyValueFactory<>("nomMarque"));

        // colonne « Action » avec bouton « Ajouter »
        Callback<TableColumn<Article,Void>,TableCell<Article,Void>> cellFactory = col -> new TableCell<>() {
            private final Button btn = new Button("Ajouter");
            {
                btn.setOnAction(evt -> {
                    Article art = getTableView().getItems().get(getIndex());
                    Commande cmd = ClientSession.getCommande();
                    if (cmd != null) cmd.ajouterArticle(art, 1);
                });
                btn.getStyleClass().add("primary-button");
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        };
        colAction.setCellFactory(cellFactory);

        tableArticles.setItems(articles);
    }

    private void loadArticlesFromDatabase() {
        String sql = """
            SELECT a.idArticle, a.idMarque, a.nom, a.description,
                   a.prixUnitaire, a.stock, a.imageURL
              FROM article a
             JOIN marque   m ON a.idMarque = m.idMarque
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                articles.add(new Article(
                        rs.getInt("idArticle"),
                        rs.getInt("idMarque"),        // <— ajouté !
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getFloat("prixUnitaire"),
                        rs.getInt("stock"),
                        rs.getString("imageURL")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ouvrirPanier() {
        injectView("/com/example/shoppingprojet/panier.fxml");
    }

    private void injectView(String fxml) {
        try {
            Stage stage = (Stage) tableArticles.getScene().getWindow();
            Parent view = FXMLLoader.load(getClass().getResource(fxml));
            stage.getScene().setRoot(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
