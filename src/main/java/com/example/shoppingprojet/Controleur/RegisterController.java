package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.UtilisateurDAO;
import com.example.shoppingprojet.DAO.UtilisateurDAOImpl;
import com.example.shoppingprojet.Modele.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class RegisterController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button backButton;

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAOImpl();

    public void handleRegister() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Tous les champs sont obligatoires.");
            return;
        }

        // Créer un nouvel utilisateur
        Utilisateur utilisateur = new Utilisateur(
                0, // id auto
                nom,
                prenom,
                email,
                password,
                LocalDate.now(),
                LocalTime.now()
        );

        boolean success = utilisateurDAO.createUser(utilisateur);

        if (success) {
            try {
                Stage stage = (Stage) nomField.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shoppingprojet/login.fxml"));
                Parent root = loader.load();
                stage.setScene(new Scene(root));
                stage.setTitle("Connexion");
                stage.show();
                stage.setMaximized(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Erreur lors de l'inscription.");
        }
    }

    @FXML
    private void goToLogin() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/shoppingprojet/login.fxml")
            );
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion");
            stage.setMaximized(true);     // passe en plein écran
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
