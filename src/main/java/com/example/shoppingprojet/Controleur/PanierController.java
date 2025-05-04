package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.Modele.ArticlePanier;
import com.example.shoppingprojet.Modele.ClientSession;
import com.example.shoppingprojet.Modele.Commande;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class PanierController {

    @FXML private TableView<ArticlePanier>            tablePanier;
    @FXML private TableColumn<ArticlePanier,String>   colArticle;
    @FXML private TableColumn<ArticlePanier,Number>   colQuantite;
    @FXML private TableColumn<ArticlePanier,Number>   colPrixUnitaire;
    @FXML private TableColumn<ArticlePanier,Number>   colPrixTotal;
    @FXML private Button                              retourButton;
    @FXML private Button                              passerCommandeButton;

    @FXML
    public void initialize() {
        // 1) liaison des colonnes
        colArticle.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getArticle().getNom())
        );
        colQuantite.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getQuantite())
        );
        colPrixUnitaire.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getArticle().getPrixUnitaire())
        );
        colPrixTotal.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getTotal())
        );

        // 2) chargement du contenu du panier
        Commande cmd = ClientSession.getCommande();
        ObservableList<ArticlePanier> data =
                FXCollections.observableArrayList(cmd.getArticles());
        tablePanier.setItems(data);

        // 3) navigation par remplacement du centre du BorderPane
        retourButton.setOnAction(e ->
                injectCenter("/com/example/shoppingprojet/boutique.fxml")
        );
        passerCommandeButton.setOnAction(e ->
                injectCenter("/com/example/shoppingprojet/checkout.fxml")
        );
    }

    /**
     * Remplace la vue centrale du BorderPane (root) par le FXML indiqué.
     */
    private void injectCenter(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));

            // on récupère le BorderPane racine pour n'en changer que le centre
            BorderPane root = (BorderPane) tablePanier
                    .getScene()
                    .getRoot();

            root.setCenter(view);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
