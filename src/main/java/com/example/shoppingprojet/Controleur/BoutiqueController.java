package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.DBConnection;
import com.example.shoppingprojet.Modele.Article;
import com.example.shoppingprojet.Modele.ClientSession;
import com.example.shoppingprojet.Modele.Commande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    @FXML
    private TableView<Article> tableArticles;

    @FXML
    private TableColumn<Article, String> colNom;

    @FXML
    private TableColumn<Article, String> colDescription;

    @FXML
    private TableColumn<Article, Float> colPrix;

    @FXML
    private TableColumn<Article, Integer> colStock;

    @FXML
    private TableColumn<Article, String> colMarque;

    @FXML
    private TableColumn<Article, Void> colAction;

    private ObservableList<Article> articles = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadArticlesFromDatabase();

        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("nomMarque"));

        // Ajouter un bouton "Ajouter au panier"
        Callback<TableColumn<Article, Void>, TableCell<Article, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Article, Void> call(final TableColumn<Article, Void> param) {
                final TableCell<Article, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Ajouter");

                    {
                        btn.setOnAction(event -> {
                            Article article = getTableView().getItems().get(getIndex());
                            ajouterAuPanier(article);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colAction.setCellFactory(cellFactory);

        tableArticles.setItems(articles);
    }

    private void loadArticlesFromDatabase() {
        String query =
                "SELECT a.idArticle, a.idMarque, a.nom, a.description, " +
                        "       a.prixUnitaire, a.stock, a.imageURL, m.nomMarque AS nomMarque " +
                        "FROM article a " +
                        "JOIN marque m ON a.idMarque = m.idMarque";


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("idArticle"),
                        rs.getInt("idMarque"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getFloat("prixUnitaire"),
                        rs.getInt("stock"),
                        rs.getString("imageURL")
                );
                article.setNomMarque(rs.getString("nomMarque"));  // <— nouveau
                articles.add(article);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ajouterAuPanier(Article article) {
        Commande commande = ClientSession.getCommande();
        if (commande != null) {
            commande.ajouterArticle(article, 1);  // 1 = quantité par défaut
            System.out.println("Ajouté au panier : " + article.getNom());
        } else {
            System.out.println("Erreur : Aucune commande active !");
        }
    }

    @FXML
    private void ouvrirPanierDepuisBoutique() {
        try {
            Stage stage = (Stage) tableArticles.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/shoppingprojet/panier.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Mon Panier");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
