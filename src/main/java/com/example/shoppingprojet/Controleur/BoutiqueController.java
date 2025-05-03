package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.DBConnection;
import com.example.shoppingprojet.Modele.Article;
import com.example.shoppingprojet.Modele.ClientSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BoutiqueController {

    @FXML private TableView<Article> tableArticles;
    @FXML private TableColumn<Article, String> colNom;
    @FXML private TableColumn<Article, String> colDescription;
    @FXML private TableColumn<Article, Float> colPrix;
    @FXML private TableColumn<Article, Integer> colStock;
    @FXML private TableColumn<Article, String> colMarque;
    @FXML private TableColumn<Article, Void> colAction;

    private final ObservableList<Article> articles = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Charger de la BDD
        String sql = """
          SELECT a.idArticle, a.idMarque, a.nom, a.description,
                 a.prixUnitaire, a.stock, a.imageURL, m.nomMarque
            FROM article a
            JOIN marque m ON a.idMarque = m.idMarque
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Article art = new Article(
                        rs.getInt("idArticle"),
                        rs.getInt("idMarque"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getFloat("prixUnitaire"),
                        rs.getInt("stock"),
                        rs.getString("imageURL")
                );
                art.setNomMarque(rs.getString("nomMarque"));
                articles.add(art);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Colonnes
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("nomMarque"));

        // Boutons “Ajouter”
        Callback<TableColumn<Article, Void>, TableCell<Article, Void>> factory = col -> {
            TableCell<Article, Void> cell = new TableCell<>() {
                private final Button btn = new Button("Ajouter");
                {
                    btn.setOnAction(evt -> {
                        Article a = getTableView().getItems().get(getIndex());
                        ClientSession.getCommande().ajouterArticle(a, 1);
                    });
                }
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : btn);
                }
            };
            return cell;
        };
        colAction.setCellFactory(factory);

        tableArticles.setItems(articles);
    }
}
