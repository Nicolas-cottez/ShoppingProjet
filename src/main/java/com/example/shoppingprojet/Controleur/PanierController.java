package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.Modele.ArticlePanier;
import com.example.shoppingprojet.Modele.ClientSession;
import com.example.shoppingprojet.Modele.Commande;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class PanierController implements Initializable {

    @FXML private TableView<ArticlePanier> tablePanier;
    @FXML private TableColumn<ArticlePanier, String>  colArticle;
    @FXML private TableColumn<ArticlePanier, Integer> colQuantite;
    @FXML private TableColumn<ArticlePanier, Float>   colPrixUnitaire;
    @FXML private TableColumn<ArticlePanier, Float>   colPrixTotal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colArticle.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getArticle().getNom()));
        colQuantite.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getQuantite()));
        colPrixUnitaire.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getArticle().getPrixUnitaire()));
        colPrixTotal.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getTotal()));

        Commande cmd = ClientSession.getCommande();
        ObservableList<ArticlePanier> list =
                FXCollections.observableArrayList(cmd.getArticles());
        tablePanier.setItems(list);
    }
}
