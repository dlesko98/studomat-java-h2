module com.example.prvisamostalniprojekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.studomat to javafx.fxml;
    exports com.example.studomat;
    exports com.example.studomat.database;
    opens com.example.studomat.database to javafx.fxml;
}