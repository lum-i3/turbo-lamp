package com.example.gestionusuarios;

import com.example.gestionusuarios.modelo.Usuario;
import com.example.gestionusuarios.modelo.dao.UsuarioDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;

public class RegistrarUsuarios {
    @FXML
    private TextField nombreCompleto;
    @FXML
    private TextField correo;
    @FXML
    private DatePicker fechaNacimiento;
    @FXML
    private PasswordField contrasenia;
    @FXML
    private PasswordField confirmarContrasenia;

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void agregarUsuario(ActionEvent event) {
        //Recortar espacios sobrantes
        nombreCompleto.setText(nombreCompleto.getText().trim().replaceAll("\\s{2,}", " "));
        correo.setText(correo.getText().trim().replaceAll("\\s{2,}", " "));
        contrasenia.setText(contrasenia.getText().trim().replaceAll("\\s{2,}", " "));
        confirmarContrasenia.setText(confirmarContrasenia.getText().trim().replaceAll("\\s{2,}", " "));

        //Validar campos obligatorios
        if (nombreCompleto.getText().isEmpty() ||
                correo.getText().isEmpty() ||
                contrasenia.getText().isEmpty() ||
                confirmarContrasenia.getText().isEmpty() ||
                fechaNacimiento.getValue() == null) {
            mostrarAlerta("Error!", "Todos los campos son obligatorios", Alert.AlertType.ERROR);
            return;
        }

        //Validar que las contraseñas coincidan
        if (!contrasenia.getText().equals(confirmarContrasenia.getText())) {
            mostrarAlerta("Error!", "Las contraseñas no coinciden", Alert.AlertType.ERROR);
            return;
        }

        //Validar formato de nombre
        if (!nombreCompleto.getText().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ]+(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$")) {
            mostrarAlerta("Error!", "El nombre solo debe contener letras y un espacio entre palabras", Alert.AlertType.ERROR);
            return;
        }
        //Validar formato de correo
        if (!correo.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            mostrarAlerta("Error!", "El formato del correo electrónico no es válido", Alert.AlertType.ERROR);
            return;
        }
        //Validar que la fecha de nacimiento sea en el pasado
        if (fechaNacimiento.getValue().isAfter(java.time.LocalDate.now())) {
            mostrarAlerta("Error!", "La fecha de nacimiento no puede ser futura", Alert.AlertType.ERROR);
            return;
        }
        // Validar que el usuario sea mayor de edad (18+ años)
        if (fechaNacimiento.getValue().plusYears(18).isAfter(LocalDate.now())) {
            mostrarAlerta("Error!", "El usuario debe ser mayor de edad (18+ años)", Alert.AlertType.ERROR);
            return;
        }
        //Validar que la contraseña tenga al menos 8 caracteres
        if (contrasenia.getText().length() < 8) {
            mostrarAlerta("Error!", "La contraseña debe tener al menos 6 caracteres", Alert.AlertType.ERROR);
            return;
        }
        try {
            //Obtener el proximo ID
            UsuarioDao dao = new UsuarioDao();
            int proximoId = dao.obtenerProximoId();
            Usuario usuario = new Usuario(
                    proximoId,
                    nombreCompleto.getText(),
                    correo.getText(),
                    Date.valueOf(fechaNacimiento.getValue()),
                    contrasenia.getText(),
                    true
            );
            //Intentar guardar en la base de datos
            if (dao.createUsuario(usuario)) {
                //Limpiar campos
                nombreCompleto.clear();
                correo.clear();
                fechaNacimiento.setValue(null);
                contrasenia.clear();
                confirmarContrasenia.clear();

                mostrarAlerta("Éxito", "Usuario registrado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error!", "No se pudo registrar el usuario", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Error!", "Ocurrió un error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void regresarMenu(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
