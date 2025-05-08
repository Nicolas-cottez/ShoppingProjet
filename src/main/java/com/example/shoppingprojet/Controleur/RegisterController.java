package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.UtilisateurDAO;
import com.example.shoppingprojet.DAO.UtilisateurDAOImpl;
import com.example.shoppingprojet.Modele.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class RegisterController {
    @FXML private TextField    nomField;
    @FXML private TextField    prenomField;
    @FXML private TextField    emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label         statusLabel;   // doit correspondre à fx:id="statusLabel" dans le FXML
    @FXML private Button        submitButton;
    @FXML private Button        backButton;

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAOImpl();

    @FXML
    public void initialize() {
        // Permet au bouton « ← Retour » de revenir à la vue login
        backButton.setOnAction(e -> inject("login.fxml"));
    }

    @FXML
    private void handleRegister() {
        // 1) Validation basique
        String nom    = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email  = emailField.getText().trim();
        String mdp    = passwordField.getText();
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
            statusLabel.setText("Tous les champs sont obligatoires.");
            return;
        }

        // 2) Construction de l'objet Utilisateur
        Utilisateur u = new Utilisateur(
                0,                  // id auto-généré
                nom,
                prenom,
                email,
                mdp,
                LocalDate.now(),
                LocalTime.now(),
                "",                 // adressePostal (champ optionnel ici)
                null,               // pas de commandes initiales
                "client"            // rôle
        );

        // 3) Tentative d'insertion en base
        boolean ok = utilisateurDAO.createUser(u);
        if (!ok) {
            statusLabel.setText("Erreur lors de l'inscription. Réessayez.");
            return;
        }

        // 4) Retour à l'écran de connexion
        inject("login.fxml");
    }

    @FXML
    private void handleBack() {
        // Méthode pour onAction="#handleBack" dans le FXML
        inject("login.fxml");
    }

    /**
     * Charge dynamiquement la vue fxmlName dans l'AnchorPane racine.
     */
    private void inject(String fxmlName) {
        try {
            Parent view = FXMLLoader.load(
                    getClass().getResource("/com/example/shoppingprojet/" + fxmlName)
            );
            AnchorPane root = (AnchorPane) nomField.getScene().getRoot();
            root.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
