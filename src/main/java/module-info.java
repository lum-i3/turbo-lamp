module com.example.gestionusuarios {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires ucp;
    requires java.desktop;
    requires jdk.jfr;

    opens com.example.gestionusuarios to javafx.fxml;
    exports com.example.gestionusuarios;
}