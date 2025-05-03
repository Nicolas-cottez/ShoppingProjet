package com.example.shoppingprojet.Controleur;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/shoppingprojet/login.fxml"));

        primaryStage.setTitle("Connexion");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.show();
        Platform.runLater(() -> {
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
