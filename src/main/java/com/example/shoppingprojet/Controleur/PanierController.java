package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.RemiseDAO;
import com.example.shoppingprojet.DAO.RemiseDAOImpl;
import com.example.shoppingprojet.Modele.*;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PanierController implements ControlledScreen {
    private MainController mainController;

    @FXML private TableView<ArticlePanier>    tablePanier;
    @FXML private TableColumn<ArticlePanier,String>  colArticle;
    @FXML private TableColumn<ArticlePanier,Integer> colQuantite;
    @FXML private TableColumn<ArticlePanier,Float>   colPrixUnitaire;
    @FXML private TableColumn<ArticlePanier,Float>   colPrixAvant;
    @FXML private TableColumn<ArticlePanier,Float>   colPrixApres;
    @FXML private Button retourButton, passerCommandeButton;

    private final RemiseDAO remiseDAO = new RemiseDAOImpl();

    @FXML public void initialize() {
        // Nom, quantité, prix unitaire
        colArticle.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getArticle().getNom()));
        colQuantite.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getQuantite()));
        colPrixUnitaire.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getArticle().getPrixUnitaire()));

        // Prix avant remise = prixUnitaire * quantite
        colPrixAvant.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getTotal()));

        // Prix après remise si applicable
        colPrixApres.setCellValueFactory(c -> {
            ArticlePanier ap = c.getValue();
            Remise r = remiseDAO.findByArticle(ap.getArticle().getIdArticle());
            float total = ap.getTotal();
            if (r != null && ap.getQuantite() >= r.getSeuil()) {
                float taux = r.getPourcentageDeRemise() / 100f;
                return new SimpleObjectProperty<>(total * (1 - taux));
            } else {
                return new SimpleObjectProperty<>(total);
            }
        });

        // Charger les lignes
        Commande cmd = UtilisateurSession.getCommande();
        tablePanier.setItems(FXCollections.observableArrayList(cmd.getArticles()));
    }

    @FXML public void handleRetour()            { mainController.showBoutique(); }
    @FXML public void handlePasserCommande()    { mainController.showCheckout(); }

    @Override public void setMainController(MainController mc) {
        this.mainController = mc;
    }
}
