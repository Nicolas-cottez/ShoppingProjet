package com.example.shoppingprojet.Controleur;
import com.example.shoppingprojet.DAO.*;
import com.example.shoppingprojet.Modele.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String pwd   = passwordField.getText();
        Utilisateur user = utilisateurDAO.findByEmailAndPassword(email, pwd);

        if (user == null) {
            errorLabel.setText("Email ou mot de passe incorrect");
            return;
        }

        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/com/example/shoppingprojet/main.fxml"));

            if (!"admin".equalsIgnoreCase(user.getRole())) {
                // on ne gère que le client ici
                Utilisateur utilisateur = new Utilisateur(
                        user.getIdUtilisateur(),
                        user.getNom(),
                        user.getPrenom(),
                        user.getEmail(),
                        user.getMotDePasse(),
                        user.getDateInscription(),
                        user.getHeureInscription(),
                        user.getAdressePostal(),
                        user.getCommandes(),
                        user.getRole()
                );
                UtilisateurSession.setUtilisateur(utilisateur);
                Commande cmd = new Commande(
                        0,
                        LocalDate.now(),
                        LocalTime.now(),
                        0f,
                        utilisateur.getAdressePostal(),
                        utilisateur,
                        new ArrayList<>()
                );
                UtilisateurSession.setCommande(cmd);
            }

            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setTitle(user.getRole().equalsIgnoreCase("admin")
                    ? "Admin" : "ShoppingApp");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToRegister() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            Parent reg = FXMLLoader.load(getClass().getResource("/com/example/shoppingprojet/register.fxml"));
            stage.getScene().setRoot(reg);
            stage.setTitle("Inscription");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




