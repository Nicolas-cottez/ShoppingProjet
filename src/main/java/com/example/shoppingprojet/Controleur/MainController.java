package com.example.shoppingprojet.Controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;

public class MainController {

    @FXML
    private AnchorPane centerPane;   // injected from main.fxml

    /** called right after FXML is loaded */
    @FXML
    public void initialize() {
        // show the default view on startup
        try {
            onShowBoutique();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onShowBoutique() throws Exception {
        swapCenter("/com/example/shoppingprojet/boutique.fxml");
    }

    @FXML
    private void onShowPanier() throws Exception {
        swapCenter("/com/example/shoppingprojet/panier.fxml");
    }

    @FXML
    private void onShowHistorique() throws Exception {
        swapCenter("/com/example/shoppingprojet/historique.fxml");
    }

    @FXML
    private void onLogout() throws Exception {
        // you can either swap centerPane to login.fxml
        // or replace the entire Scene root:
        Parent loginRoot = FXMLLoader.load(
                getClass().getResource("/com/example/shoppingprojet/login.fxml")
        );
        centerPane.getScene().setRoot(loginRoot);
    }

    /** helper: load an FXML and place it into the centerPane */
    private void swapCenter(String fxmlPath) throws Exception {
        Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
        centerPane.getChildren().setAll(view);
        // if you want it to grow to fit:
        AnchorPane.setTopAnchor(view,    0.0);
        AnchorPane.setRightAnchor(view,  0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view,   0.0);
    }
}
