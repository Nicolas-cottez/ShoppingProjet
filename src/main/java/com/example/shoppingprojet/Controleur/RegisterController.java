package com.example.shoppingprojet.Controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class RegisterController {
    @FXML private TextField nomField, prenomField, emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    @FXML private Button submitButton, backButton;

    @FXML public void initialize() {
        backButton.setOnAction(e -> inject("/com/example/shoppingprojet/login.fxml"));
    }

    @FXML
    private void handleRegister() {
        // votre logique d'inscription → puis retour au login
        statusLabel.setText("Inscription réussie !");
        inject("/com/example/shoppingprojet/login.fxml");
    }

    private void inject(String fxml) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxml));
            BorderPane root = (BorderPane) nomField.getScene().getRoot();
            root.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
