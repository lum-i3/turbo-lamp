module com.example.gestionusuarios {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gestionusuarios to javafx.fxml;
    exports com.example.gestionusuarios;
}