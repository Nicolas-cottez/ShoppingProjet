package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.UtilisateurDAO;
import com.example.shoppingprojet.DAO.UtilisateurDAOImpl;
import com.example.shoppingprojet.Modele.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField    emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label         errorLabel;

    private final UtilisateurDAO dao = new UtilisateurDAOImpl();

    @FXML
    public void handleLogin() {
        String email = emailField.getText();
        String pass  = passwordField.getText();
        Utilisateur u = dao.findByEmailAndPassword(email, pass);

        if (u != null) {
            try {
                // On remplace toute la racine de la scène par main.fxml
                Stage stage = (Stage) emailField.getScene().getWindow();
                Parent main = FXMLLoader.load(getClass().getResource("/com/example/shoppingprojet/main.fxml"));
                stage.getScene().setRoot(main);
                stage.setTitle("ShoppingApp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Email ou mot de passe incorrect");
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
