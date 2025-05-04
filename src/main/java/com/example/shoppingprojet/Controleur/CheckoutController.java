package com.example.shoppingprojet.Controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class CheckoutController {
    @FXML private TextArea livraisonField, facturationField;
    @FXML private ComboBox<String> paiementBox;
    @FXML private TextField cardNumberField, expField, cvvField;
    @FXML private Button annulerButton, confirmerButton;

    @FXML public void initialize() {
        annulerButton.setOnAction(e -> injectCenter("panier.fxml"));
        confirmerButton.setOnAction(e -> {
            // votre logique de validation → retour en boutique
            injectCenter("boutique.fxml");
        });
    }

    private void injectCenter(String fxml) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(
                    "/com/example/shoppingprojet/" + fxml));
            BorderPane root = (BorderPane) livraisonField.getScene().getRoot();
            root.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
