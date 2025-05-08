package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.Modele.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML private AnchorPane contentPane;  // correspond à <AnchorPane fx:id="contentPane"/>

    @FXML
    public void initialize() {
        showBoutique();  // page par défaut
    }

    @FXML public void showBoutique()   { loadCenter("boutique.fxml"); }
    @FXML public void showPanier()     { loadCenter("panier.fxml"); }
    @FXML public void showHistorique() { loadCenter("historique.fxml"); }
    @FXML public void showCheckout()   { loadCenter("checkout.fxml"); }
    @FXML public void showProfil()     { loadCenter("profil.fxml"); }

    @FXML
    public void handleLogout() {
        UtilisateurSession.clearSession();

        try {
            Stage stage = (Stage) contentPane.getScene().getWindow();
            Parent root  = FXMLLoader.load(
                    getClass().getResource("/com/example/shoppingprojet/login.fxml")
            );
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadCenter(String fxmlName) {
        try {
            // construit soi-même l’URL absolue
            String resource = "/com/example/shoppingprojet/" + fxmlName;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(resource));

            Parent view = loader.load();    // maintenant loader a bien sa location

            // injection du contrôleur parent si besoin
            Object ctl = loader.getController();
            if (ctl instanceof ControlledScreen) {
                ((ControlledScreen)ctl).setMainController(this);
            }

            contentPane.getChildren().setAll(view);
            // pour être sûr que la vue prenne tout l’AnchorPane :
            AnchorPane.setTopAnchor(view,    0d);
            AnchorPane.setBottomAnchor(view, 0d);
            AnchorPane.setLeftAnchor(view,   0d);
            AnchorPane.setRightAnchor(view,  0d);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
