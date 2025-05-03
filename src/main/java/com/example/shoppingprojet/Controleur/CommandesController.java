package com.example.shoppingprojet.Controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class CommandesController {

    @FXML private TableView<?> commandesTable;
    @FXML private TableColumn<?, ?> colDate;
    @FXML private TableColumn<?, ?> colMontant;
    @FXML private TableColumn<?, ?> colEtat;
    @FXML private Button retourButton;

    @FXML
    public void initialize() {
        retourButton.setOnAction(event -> retournerAccueil());
    }

    private void retournerAccueil() {
        try {
            Stage stage = (Stage) retourButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/shoppingprojet/client-accueil.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Accueil Client");
            stage.show();
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
