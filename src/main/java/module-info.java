module com.example.shoppingprojet {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.shoppingprojet to javafx.fxml;
    exports com.example.shoppingprojet;
}