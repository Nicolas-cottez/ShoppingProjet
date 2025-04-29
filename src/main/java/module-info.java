module com.example.shoppingprojet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.shoppingprojet.Modele to javafx.base;  // OUVERT pour JavaFX
    opens com.example.shoppingprojet.Controleur to javafx.fxml; // Pour tes controleurs FXML

    exports com.example.shoppingprojet.Modele;
    exports com.example.shoppingprojet.Controleur;
}
