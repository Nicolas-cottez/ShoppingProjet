package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.CommandeDAOImpl;
import com.example.shoppingprojet.DAO.LigneCommandeDAOImpl;
import com.example.shoppingprojet.Modele.ArticlePanier;
import com.example.shoppingprojet.Modele.ClientSession;
import com.example.shoppingprojet.Modele.Commande;
import com.example.shoppingprojet.Modele.LigneCommande;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PanierController {

    @FXML private TableView<ArticlePanier> panierTable;
    @FXML private TableColumn<ArticlePanier, String> colNom;
    @FXML private TableColumn<ArticlePanier, Integer> colQuantite;
    @FXML private TableColumn<ArticlePanier, Float> colPrixUnitaire;
    @FXML private TableColumn<ArticlePanier, Float> colPrixTotal;

    @FXML private Button retourButton;
    @FXML private Button validerButton;

    private ObservableList<ArticlePanier> panierItems;

    @FXML
    public void initialize() {
        // 1) Récupérer la liste brute
        List<ArticlePanier> brut = ClientSession.getCommande().getArticles();

        // 2) Fusionner les articles identiques
        Map<Integer, ArticlePanier> fusion = new LinkedHashMap<>();
        for (ArticlePanier ap : brut) {
            int id = ap.getArticle().getIdArticle();
            if (fusion.containsKey(id)) {
                ArticlePanier exist = fusion.get(id);
                exist.setQuantite(exist.getQuantite() + ap.getQuantite());
            } else {
                fusion.put(id, new ArticlePanier(ap.getArticle(), ap.getQuantite()));
            }
        }

        // 3) Créer l’ObservableList
        panierItems = FXCollections.observableArrayList(fusion.values());
        panierTable.setItems(panierItems);

        // 4) Colonnes
        colNom.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getArticle().getNom())
        );
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colPrixUnitaire.setCellValueFactory(cd ->
                new SimpleFloatProperty(cd.getValue().getArticle().getPrixUnitaire()).asObject()
        );
        colPrixTotal.setCellValueFactory(cd ->
                new SimpleFloatProperty(cd.getValue().getTotal()).asObject()
        );

        // Boutons
        retourButton.setOnAction(e -> injectView("/com/example/shoppingprojet/boutique.fxml"));
        validerButton.setOnAction(e -> injectView("/com/example/shoppingprojet/checkout.fxml"));
    }

    /**
     * Injecte une vue FXML dans la zone centrale de main.fxml (BorderPane).
     */
    private void injectView(String fxml) {
        try {
            BorderPane root = (BorderPane) validerButton.getScene().getRoot();
            AnchorPane content = (AnchorPane) root.getCenter();
            Node view = FXMLLoader.load(getClass().getResource(fxml));
            content.getChildren().setAll(view);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
