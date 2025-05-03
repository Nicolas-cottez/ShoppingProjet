package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.CommandeDAO;
import com.example.shoppingprojet.DAO.CommandeDAOImpl;
import com.example.shoppingprojet.Modele.ArticlePanier;
import com.example.shoppingprojet.Modele.ClientSession;
import com.example.shoppingprojet.Modele.Commande;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class HistoriqueController {

    @FXML private TableView<Commande> tableCommandes;
    @FXML private TableColumn<Commande, Integer> colNum;
    @FXML private TableColumn<Commande, String>  colDate;
    @FXML private TableColumn<Commande, String>  colHeure;
    @FXML private TableColumn<Commande, Float>   colTotal;

    @FXML private TableView<ArticlePanier> tableLignes;
    @FXML private TableColumn<ArticlePanier, String> colNomArt;
    @FXML private TableColumn<ArticlePanier, Integer> colQteArt;
    @FXML private TableColumn<ArticlePanier, Float>   colTotalLigne;

    @FXML
    public void initialize() {
        CommandeDAO dao = new CommandeDAOImpl();
        var commandes = FXCollections.observableArrayList(
                dao.getCommandesByClient(ClientSession.getClient().getIdUtilisateur())
        );

        colNum.setCellValueFactory(new PropertyValueFactory<>("idCommande"));
        colDate.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDateCommande().toString()));
        colHeure.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getHeureCommande().toString()));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));

        tableCommandes.setItems(commandes);

        // Quand on sélectionne une commande, on affiche ses lignes
        tableCommandes.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, sel) -> {
                    if (sel != null) {
                        tableLignes.setItems(
                                FXCollections.observableArrayList(sel.getArticles())
                        );
                    }
                });

        colNomArt.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getArticle().getNom()));
        colQteArt.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colTotalLigne.setCellValueFactory(c ->
                new SimpleFloatProperty(c.getValue().getTotal()).asObject());
    }
}
