module com.example.gestionusuarios {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires ucp;
    requires java.desktop;
    requires jdk.jfr;
    requires javafx.base;
    requires javafx.graphics;

    opens com.example.gestionusuarios to javafx.fxml;
    opens com.example.gestionusuarios.modelo to javafx.fxml;
    exports com.example.gestionusuarios;
    exports com.example.gestionusuarios.modelo;
    exports com.example.gestionusuarios.modelo.dao;
}