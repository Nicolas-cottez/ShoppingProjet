package com.example.shoppingprojet.Controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML private BorderPane rootPane;
    @FXML private AnchorPane centerPane;

    @FXML
    public void initialize() {
        // On affiche la boutique par défaut
        showBoutique();
    }

    @FXML
    private void showBoutique() {
        loadCenter("/com/example/shoppingprojet/boutique.fxml");
    }

    @FXML
    private void showPanier() {
        loadCenter("/com/example/shoppingprojet/panier.fxml");
    }

    @FXML
    private void showHistorique() {
        loadCenter("/com/example/shoppingprojet/historique.fxml");
    }

    @FXML
    private void logout() {
        // Simplement retourner à la login view
        try {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Parent login = FXMLLoader.load(getClass().getResource("/com/example/shoppingprojet/login.fxml"));
            stage.getScene().setRoot(login);
            stage.setTitle("Connexion");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge un fichier FXML dans la partie centrale du BorderPane.
     */
    private void loadCenter(String fxmlPath) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));
            rootPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
