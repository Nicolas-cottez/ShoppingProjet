package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.Modele.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ClientController {

    @FXML
    private TableView<Utilisateur> tableClients;
    @FXML
    private TableColumn<Utilisateur, String> colNom;
    @FXML
    private TableColumn<Utilisateur, String> colPrenom;
    @FXML
    private TableColumn<Utilisateur, String> colEmail;
    @FXML
    private TableColumn<Utilisateur, String> colAdresse;
    @FXML
    private TableColumn<Utilisateur, String> colDateInscription;

    @FXML
    private ComboBox<String> clientRoleComboBox;

    @FXML
    private TextField clientNomField;
    @FXML
    private TextField clientPrenomField;
    @FXML
    private TextField clientEmailField;
    @FXML
    private TextField clientAdresseField;
    @FXML
    private TextField clientDateInscriptionField;
    @FXML
    private TextField clientIdField; // champ pour l'ID du client

    private ObservableList<Utilisateur> utilisateursList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialiser les colonnes de la TableView
        colNom.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNom()));
        colPrenom.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPrenom()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        colAdresse.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAdressePostal()));
        colDateInscription.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDateInscription().toString()));

        // Charger les clients dans la table (cette méthode sera à adapter pour récupérer les clients depuis la base de données)
        loadClients();
    }

    private void loadClients() {
        // Simuler des clients pour l'exemple
        utilisateursList.add(new Utilisateur(1, "Dupont", "Jean", "jean.dupont@mail.com", "password123", LocalDate.now(), LocalTime.now(), "123 rue de Paris", new ArrayList<>(), "client"));
        utilisateursList.add(new Utilisateur(2, "Durand", "Marie", "marie.durand@mail.com", "password456", LocalDate.now(), LocalTime.now(), "456 rue de Lyon", new ArrayList<>(), "admin"));

        // Ajouter la liste d'utilisateurs à la table
        tableClients.setItems(utilisateursList);
    }

    // Méthodes de gestion des clients
    @FXML
    private void handleAjouterClient() {
        String nom = clientNomField.getText();
        String prenom = clientPrenomField.getText();
        String email = clientEmailField.getText();
        String adresse = clientAdresseField.getText();
        String role = clientRoleComboBox.getValue();

        // Ajouter un nouveau client dans la base de données
        Utilisateur newClient = new Utilisateur(
                utilisateursList.size() + 1, // Un ID généré (simulé ici)
                nom,
                prenom,
                email,
                "password123", // mot de passe à gérer de manière sécurisée
                LocalDate.now(),
                LocalTime.now(),
                adresse,
                new ArrayList<>(),
                role
        );

        utilisateursList.add(newClient);
        tableClients.refresh(); // Rafraîchir la table
    }

    @FXML
    private void handleModifierClient() {
        Utilisateur selectedClient = tableClients.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            // Mettre à jour les champs avec les nouvelles valeurs
            selectedClient.setNom(clientNomField.getText());
            selectedClient.setPrenom(clientPrenomField.getText());
            selectedClient.setEmail(clientEmailField.getText());
            selectedClient.setAdressePostal(clientAdresseField.getText());

            // Récupérer le rôle du ComboBox et mettre à jour le rôle du client
            String newRole = clientRoleComboBox.getValue();

            // Mettre à jour le rôle du client, mais sans modifier la classe Utilisateur
            updateClientRole(selectedClient, newRole);

            // Rafraîchir la table
            tableClients.refresh();
        }
    }

    // Méthode pour mettre à jour le rôle dans le contrôleur sans modifier la classe Utilisateur
    private void updateClientRole(Utilisateur selectedClient, String role) {
        // Gérer le rôle du client dans le contrôleur
        // (Aucune modification directe de l'objet Utilisateur, juste une gestion interne)
        System.out.println("Le rôle de " + selectedClient.getNom() + " a été modifié en : " + role);

        // Si nécessaire, tu peux stocker cette information ailleurs temporairement
        // ou l'afficher directement sans toucher à l'objet Utilisateur
    }

    @FXML
    private void handleSupprimerClient() {
        Utilisateur selectedClient = tableClients.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            utilisateursList.remove(selectedClient);
            tableClients.refresh(); // Rafraîchir la table
        }
    }
}
