package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.DBConnection;
import com.example.shoppingprojet.Modele.Article;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class BoutiqueController implements Initializable {

    @FXML private TableView<Article> tableArticles;
    @FXML private TableColumn<Article, String> colNom;
    @FXML private TableColumn<Article, String> colDescription;
    @FXML private TableColumn<Article, Float>  colPrix;
    @FXML private TableColumn<Article, Integer> colStock;
    @FXML private TableColumn<Article, String> colMarque;
    @FXML private TableColumn<Article, Void>    colAction;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("nomMarque"));

        // bouton Ajouter
        colAction.setCellFactory((Callback<TableColumn<Article, Void>, TableCell<Article, Void>>) col ->
                new TableCell<>() {
                    private final Button btn = new Button("Ajouter");
                    {
                        btn.setOnAction(e -> {
                            Article a = getTableView().getItems().get(getIndex());
                            System.out.println("Ajouté : " + a.getNom());
                            // TODO : mettre dans le panier
                        });
                    }
                    @Override
                    protected void updateItem(Void v, boolean empty) {
                        super.updateItem(v, empty);
                        setGraphic(empty ? null : btn);
                    }
                }
        );

        loadArticles();
    }

    private void loadArticles() {
        ObservableList<Article> data = FXCollections.observableArrayList();
        String sql = """
            SELECT a.idArticle, a.idMarque, a.nom, a.description,
                   a.prixUnitaire, a.stock, a.imageURL, m.nomMarque
              FROM article a
              JOIN marque m ON a.idMarque = m.idMarque
        """;
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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
                data.add(art);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tableArticles.setItems(data);
    }
}
