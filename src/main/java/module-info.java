module com.example.shoppingprojet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.shoppingprojet to javafx.fxml;
    opens com.example.shoppingprojet.Controleur to javafx.fxml;

    exports com.example.shoppingprojet;
    exports com.example.shoppingprojet.Controleur;
}
