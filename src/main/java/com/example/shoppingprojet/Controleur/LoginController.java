package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.UtilisateurDAO;
import com.example.shoppingprojet.DAO.UtilisateurDAOImpl;
import com.example.shoppingprojet.Modele.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAOImpl();

    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        Utilisateur user = utilisateurDAO.findByEmailAndPassword(email, password);

        if (user != null) {
            try {
                Stage stage = (Stage) emailField.getScene().getWindow();
                FXMLLoader loader;

                if (user.getRole().equals("admin")) {
                    loader = new FXMLLoader(getClass().getResource("/com/example/shoppingprojet/admin-accueil.fxml"));
                } else {
                    loader = new FXMLLoader(getClass().getResource("/com/example/shoppingprojet/client-accueil.fxml"));
                }



                Parent root = loader.load();
                stage.setScene(new Scene(root));
                stage.setTitle("Accueil - " + user.getRole());
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            errorLabel.setText("Email ou mot de passe incorrect");
        }
    }

}
