package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.Modele.ArticlePanier;
import com.example.shoppingprojet.Modele.ClientSession;
import com.example.shoppingprojet.Modele.Commande;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PanierController implements ControlledScreen {
    private MainController mainController;

    @FXML private TableView<ArticlePanier> tablePanier;
    @FXML private TableColumn<ArticlePanier,String>  colArticle;
    @FXML private TableColumn<ArticlePanier,Integer> colQuantite;
    @FXML private TableColumn<ArticlePanier,Float>   colPrixUnitaire, colPrixTotal;
    @FXML private Button retourButton, passerCommandeButton;

    @FXML public void initialize() {
        colArticle
                .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArticle().getNom()));
        colQuantite
                .setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getQuantite()));
        colPrixUnitaire
                .setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getArticle().getPrixUnitaire()));
        colPrixTotal
                .setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getTotal()));

        Commande cmd = ClientSession.getCommande();
        tablePanier.setItems(FXCollections.observableArrayList(cmd.getArticles()));
    }

    @FXML public void handleRetour()       { mainController.showBoutique(); }
    @FXML public void handlePasserCommande() { mainController.showCheckout(); }

    @Override public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
