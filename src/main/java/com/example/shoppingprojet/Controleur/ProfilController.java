package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.*;
import com.example.shoppingprojet.Modele.*;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ProfilController implements ControlledScreen {
    private MainController mainController;

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextArea  adresseField;

    // DAO pour mettre à jour la BDD
    private final UtilisateurDAO userDAO = new UtilisateurDAOImpl();


    @FXML
    public void initialize() {
        // Pré-remplir avec la session en cours
        Utilisateur u = UtilisateurSession.getUtilisateur();
        if (u != null) {
            nomField.setText(u.getNom());
            prenomField.setText(u.getPrenom());
            emailField.setText(u.getEmail());
            adresseField.setText(u.getAdressePostal());
        }
    }

    @FXML
    public void handleCancel() {
        // Retour au menu historique ou boutique
        mainController.showBoutique();
    }

    @FXML
    public void handleSave() {
        // Validation basique
        if (nomField.getText().isBlank()
                || prenomField.getText().isBlank()
                || emailField.getText().isBlank()
                || adresseField.getText().isBlank()) {
            new Alert(Alert.AlertType.ERROR,
                    "Tous les champs doivent être remplis.").showAndWait();
            return;
        }

        // Mettre à jour l'objet et la BDD
        Utilisateur u = UtilisateurSession.getUtilisateur();
        u.setNom(nomField.getText().trim());
        u.setPrenom(prenomField.getText().trim());
        u.setEmail(emailField.getText().trim());
        u.setAdressePostal(adresseField.getText().trim());

        userDAO.modifierUtilisateur(u);


        // <-- ici on appelle la méthode existante dans le DAO
        userDAO.modifierUtilisateur(u);

        new Alert(Alert.AlertType.INFORMATION,
                "Profil mis à jour avec succès.").showAndWait();
    }

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
