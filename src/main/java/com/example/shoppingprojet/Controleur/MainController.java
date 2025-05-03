package com.example.shoppingprojet.Controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML private AnchorPane contentPane;

    @FXML public void initialize() {
        showBoutique();
    }

    @FXML private void showBoutique() {
        loadUI("/com/example/shoppingprojet/boutique.fxml");
    }

    @FXML private void showPanier() {
        loadUI("/com/example/shoppingprojet/panier.fxml");
    }

    @FXML private void showHistorique() {
        loadUI("/com/example/shoppingprojet/historique.fxml");
    }

    @FXML
    private void logout() {
        try {
            // Récupère la fenêtre courante
            Stage stage = (Stage) contentPane.getScene().getWindow();
            // Charge le FXML de la page de login
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/shoppingprojet/login.fxml")
            );
            // Crée une nouvelle scène et la place dans le stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Connexion");
            stage.show();
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUI(String fxml) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxml));
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
