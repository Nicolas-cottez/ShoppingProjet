package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.DAO.CommandeDAO;
import com.example.shoppingprojet.DAO.CommandeDAOImpl;
import com.example.shoppingprojet.Modele.ClientSession;
import com.example.shoppingprojet.Modele.Commande;
import com.example.shoppingprojet.Modele.LigneCommande;
import javafx.beans.property.*;
        import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

        import java.time.LocalDate;
import java.time.LocalTime;

public class HistoriqueController implements ControlledScreen {
    private MainController mainController;
    @FXML private TableView<Commande> tableHistorique;
    @FXML private TableColumn<Commande,Integer>   colIdCommande;
    @FXML private TableColumn<LigneCommande,String> colArticle;
    @FXML private TableColumn<Commande, LocalDate> colDate;
    @FXML private TableColumn<Commande, LocalTime> colHeure;
    @FXML private TableColumn<Commande,Float>     colMontant;

    private final CommandeDAO commandeDAO = new CommandeDAOImpl();

    @FXML public void initialize() {
        colIdCommande.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getIdCommande()).asObject());
        colDate.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getDateCommande()));
        colHeure.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getHeureCommande()));
        colMontant.setCellValueFactory(c ->
                new SimpleFloatProperty(c.getValue().getMontantTotal()).asObject());

        int idClient = ClientSession.getClient().getIdUtilisateur();
        tableHistorique.setItems(FXCollections.observableArrayList(
                commandeDAO.getCommandesByClient(idClient)
        ));
    }

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}

