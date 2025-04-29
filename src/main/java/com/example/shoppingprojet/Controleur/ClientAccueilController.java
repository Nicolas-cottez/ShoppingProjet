package com.example.shoppingprojet.Controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientAccueilController {

    @FXML
    private Button catalogueButton;

    @FXML
    private Button panierButton;

    @FXML
    private Button commandesButton;

    @FXML
    public void initialize() {
        catalogueButton.setOnAction(event -> ouvrirPage("/com/example/shoppingprojet/boutique.fxml", "Catalogue"));
        panierButton.setOnAction(event -> ouvrirPage("/com/example/shoppingprojet/panier.fxml", "Mon Panier"));
        commandesButton.setOnAction(event -> ouvrirPage("/com/example/shoppingprojet/commandes.fxml", "Mes Commandes"));
    }


    private void ouvrirPage(String cheminFXML, String titre) {
        try {
            Stage stage = (Stage) catalogueButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(cheminFXML));
            stage.setScene(new Scene(root));
            stage.setTitle(titre);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
