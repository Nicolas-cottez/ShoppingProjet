package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.LigneCommandeDAOImpl;
import com.example.shoppingprojet.DAO.CommandeDAOImpl;
import com.example.shoppingprojet.Modele.LigneCommande;
import com.example.shoppingprojet.Modele.Commande;
import com.example.shoppingprojet.Modele.ClientSession;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class HistoriqueController {

    @FXML private TableView<Commande> tableCommandes;
    @FXML private TableColumn<Commande,Integer> colNum;
    @FXML private TableColumn<Commande,String>  colDate;
    @FXML private TableColumn<Commande,String>  colHeure;
    @FXML private TableColumn<Commande,Float>   colTotal;

    @FXML private TableView<LigneCommande> tableLignes;
    @FXML private TableColumn<LigneCommande,String> colArticle;
    @FXML private TableColumn<LigneCommande,Integer> colQuantite;
    @FXML private TableColumn<LigneCommande,Float> colPrixLigne;

    @FXML
    public void initialize() {
        colNum.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getIdCommande())
        );
        colDate.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDateCommande().toString())
        );
        colHeure.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getHeureCommande().toString())
        );
        colTotal.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getMontantTotal())
        );

        // on charge l'historique du client
        int idClient = ClientSession.getClient().getIdUtilisateur();
        ObservableList<Commande> commandes = FXCollections
                .observableArrayList(new CommandeDAOImpl().getCommandesByClient(idClient));
        tableCommandes.setItems(commandes);

        // au clic sur une commande, on affiche ses lignes
        tableCommandes.getSelectionModel().selectedItemProperty()
                .addListener((obs,old,nouv) -> {
                    if (nouv!=null) {
                        List<LigneCommande> lignes =
                                new LigneCommandeDAOImpl().getLignesByCommande(nouv.getIdCommande());
                        ObservableList<LigneCommande> data =
                                FXCollections.observableArrayList(lignes);
                        colArticle.setCellValueFactory(c ->
                                new SimpleStringProperty(c.getValue().getArticle().getNom()));
                        colQuantite.setCellValueFactory(c ->
                                new SimpleObjectProperty<>(c.getValue().getQuantite()));
                        colPrixLigne.setCellValueFactory(c ->
                                new SimpleObjectProperty<>(c.getValue().getPrixLigne()));
                        tableLignes.setItems(data);
                    } else {
                        tableLignes.getItems().clear();
                    }
                });
    }
}
