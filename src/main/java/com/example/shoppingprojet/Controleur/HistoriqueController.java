package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.CommandeDAOImpl;
import com.example.shoppingprojet.DAO.LigneCommandeDAOImpl;
import com.example.shoppingprojet.Modele.LigneCommande;
import com.example.shoppingprojet.Modele.ClientSession;
import com.example.shoppingprojet.Modele.Commande;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueController {
    @FXML private TableView<LigneCommande> tableHistorique;
    @FXML private TableColumn<LigneCommande,String> colArticle;
    @FXML private TableColumn<LigneCommande,Integer> colQuantite;
    @FXML private TableColumn<LigneCommande,Float> colPrixLigne;

    @FXML public void initialize() {
        colArticle.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getArticle().getNom()));
        colQuantite.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getQuantite()));
        colPrixLigne.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getPrixLigne()));

        // récupérer l'historique du client
        int clientId = ClientSession.getClient().getIdUtilisateur();
        List<Commande> commandes =
                new CommandeDAOImpl().getCommandesByClient(clientId);

        // pour chaque commande, on affiche ses lignes
        List<LigneCommande> toutesLignes = new ArrayList<>();
        for (Commande cmd : commandes) {
            toutesLignes.addAll(
                    new LigneCommandeDAOImpl().getLignesByCommande(cmd.getIdCommande()));
        }
        tableHistorique.setItems(FXCollections.observableArrayList(toutesLignes));
    }
}
