package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.Modele.ArticlePanier;
import com.example.shoppingprojet.Modele.ClientSession;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class PanierController {

    @FXML
    private TableView<ArticlePanier> panierTable;

    @FXML
    private TableColumn<ArticlePanier, String> colNom;

    @FXML
    private TableColumn<ArticlePanier, Integer> colQuantite;

    @FXML
    private TableColumn<ArticlePanier, Float> colPrixUnitaire;

    @FXML
    private TableColumn<ArticlePanier, Float> colPrixTotal;

    @FXML
    private Button retourButton;

    @FXML
    private Button validerButton;

    private ObservableList<ArticlePanier> panierItems;

    @FXML
    public void initialize() {
        // 🔥 Lier directement aux articles du ClientSession
        panierItems = FXCollections.observableArrayList(ClientSession.getCommande().getArticles());
        panierTable.setItems(panierItems);

        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArticle().getNom()));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colPrixUnitaire.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getArticle().getPrixUnitaire()).asObject());
        colPrixTotal.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getTotal()).asObject());

        retourButton.setOnAction(event -> retournerBoutique());
        validerButton.setOnAction(event -> validerCommande());
    }

    private void retournerBoutique() {
        try {
            Stage stage = (Stage) panierTable.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/shoppingprojet/boutique.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Catalogue");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void validerCommande() {
        System.out.println("Commande validée avec " + panierItems.size() + " articles !");
        // Ici, tu pourras plus tard appeler DAO pour enregistrer en base
    }
}
