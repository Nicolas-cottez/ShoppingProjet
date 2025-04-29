package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.UtilisateurDAO;
import com.example.shoppingprojet.DAO.UtilisateurDAOImpl;
import com.example.shoppingprojet.Modele.Utilisateur;
import com.example.shoppingprojet.Modele.Client;
import com.example.shoppingprojet.Modele.Commande;
import com.example.shoppingprojet.Modele.ClientSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

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

                if ("admin".equalsIgnoreCase(user.getRole())) {
                    loader = new FXMLLoader(getClass().getResource("/com/example/shoppingprojet/admin-accueil.fxml"));
                } else {
                    // Créer un Client et stocker en session
                    Client client = new Client(
                            user.getIdUtilisateur(),
                            user.getNom(),
                            user.getPrenom(),
                            user.getEmail()
                    );
                    ClientSession.setClient(client);

                    // Créer une commande vide
                    Commande commande = new Commande(
                            0,
                            LocalDate.now(),
                            LocalTime.now(),
                            0.0f,
                            client,
                            new ArrayList<>()
                    );
                    ClientSession.setCommande(commande);

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

    public void goToRegister() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shoppingprojet/register.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Créer un compte");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
