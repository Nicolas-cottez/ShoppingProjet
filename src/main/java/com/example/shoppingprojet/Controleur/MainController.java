package com.example.shoppingprojet.Controleur;

import com.example.shoppingprojet.Modele.ClientSession;
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

    @FXML
    public void showBoutique() {
        loadCenter("/com/example/shoppingprojet/boutique.fxml");
    }

    @FXML
    public void showPanier() {
        loadCenter("/com/example/shoppingprojet/panier.fxml");
    }

    @FXML
    public void showHistorique() {
        loadCenter("/com/example/shoppingprojet/historique.fxml");
    }

    @FXML
    public void showCheckout() {
        loadCenter("/com/example/shoppingprojet/checkout.fxml");
    }

    @FXML
    public void handleLogout() {
        // 1) Vider les informations de session
        ClientSession.setClient(null);
        ClientSession.setCommande(null);

        // 2) Recharger l'écran de connexion
        try {
            Stage stage = (Stage) /* récupère la fenêtre courante */
                    // si vous étiez dans un AnchorPane nommé contentPane :
                    contentPane.getScene().getWindow();
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/shoppingprojet/login.fxml")
            );
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Connexion");
            stage.setMaximized(true); // ou false si vous préférez
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCenter(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();

            // Passe-toi-même en tant que contrôleur parent si besoin :
            Object ctl = loader.getController();
            if (ctl instanceof ControlledScreen) {
                ((ControlledScreen)ctl).setMainController(this);
            }

            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
