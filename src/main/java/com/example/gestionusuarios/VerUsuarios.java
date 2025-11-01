package com.example.gestionusuarios;

import com.example.gestionusuarios.modelo.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class VerUsuarios implements Initializable {

    @FXML
    private TextField nombreCompleto;
    @FXML
    private TextField correo;
    @FXML
    private DatePicker fechaNacimiento;
    @FXML
    private TextField estado;
    @FXML
    private Button volver;

    private Usuario usuario;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        volver.setOnAction(event -> {
            Stage stage = (Stage) volver.getScene().getWindow();
            stage.close();
        });
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        if (usuario != null) {
            nombreCompleto.setText(usuario.getNombre());
            correo.setText(usuario.getCorreo());

            if (usuario.getFechaNacimiento() != null) {
                fechaNacimiento.setValue(usuario.getFechaNacimiento().toLocalDate());
            }

            // Mostrar estado como texto (Activo/Inactivo) en lugar de true/false
            estado.setText(usuario.getEstado() ? "Activo" : "Inactivo");
        }
    }
}
