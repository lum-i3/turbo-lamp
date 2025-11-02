package com.example.gestionusuarios;

import com.example.gestionusuarios.modelo.Usuario;
import com.example.gestionusuarios.modelo.dao.UsuarioDao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;


public class EditarUsuarios implements Initializable {

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
    @FXML
    private Button editarUsuario;
    @FXML
    private Button borrarUsuario;

    private Usuario usuario;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Conectar el botón al metodo editarUsuario
        editarUsuario.setOnAction(event -> editarUsuario());
        borrarUsuario.setOnAction(event -> borrarUsuario());
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            System.out.println("Usuario recibido: " + usuario.getNombre());
            cargarDatosUsuario();
        } else {
            System.out.println("Usuario es NULL!");
        }
    }

    private void cargarDatosUsuario() {
        try {
            if (usuario != null) {
                //Cargar nombre
                if (nombreCompleto != null) {
                    nombreCompleto.setText(usuario.getNombre());
                }
                //Cargar correo
                if (correo != null) {
                    correo.setText(usuario.getCorreo());
                }
                //Cargar fecha de nacimiento (con verificación de null)
                if (fechaNacimiento != null && usuario.getFechaNacimiento() != null) {
                    fechaNacimiento.setValue(usuario.getFechaNacimiento().toLocalDate());
                    System.out.println("Fecha cargada: " + usuario.getFechaNacimiento().toLocalDate());
                } else {
                    System.out.println("No se pudo cargar la fecha - DatePicker: " +
                            (fechaNacimiento == null ? "NULL" : "OK") +
                            ", Fecha usuario: " +
                            (usuario.getFechaNacimiento() == null ? "NULL" : usuario.getFechaNacimiento()));
                }
                //Las contraseñas no se cargan por seguridad
                if (contrasenia != null) {
                    contrasenia.clear();
                }
                if (confirmarContrasenia != null) {
                    confirmarContrasenia.clear();
                }
            } else {
                System.out.println("Usuario es null en cargarDatosUsuario");
            }
        } catch (Exception e) {
            System.out.println("Error en cargarDatosUsuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void editarUsuario(){
        //Validaciones básicas
        if (nombreCompleto.getText().isEmpty() || correo.getText().isEmpty()) {
            mostrarAlerta("Error", "Nombre y correo son obligatorios", Alert.AlertType.ERROR);
            return;
        }
        if (!contrasenia.getText().isEmpty() && !contrasenia.getText().equals(confirmarContrasenia.getText())) {
            mostrarAlerta("Error", "Las contraseñas no coinciden", Alert.AlertType.ERROR);
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

        try {
            //Actualizar datos del usuario
            usuario.setNombre(nombreCompleto.getText().trim());
            usuario.setCorreo(correo.getText().trim());

            if (fechaNacimiento.getValue() != null) {
                usuario.setFechaNacimiento(java.sql.Date.valueOf(fechaNacimiento.getValue()));
            }

            //Actualizar contraseña solo si se proporcionó una nueva
            if (!contrasenia.getText().isEmpty()) {
                usuario.setContrasenia(contrasenia.getText());
            }

            //Guardar en la base de datos
            UsuarioDao dao = new UsuarioDao();
            if (dao.updateUsuario(usuario)) {
                mostrarAlerta("Éxito", "Usuario actualizado correctamente", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) editarUsuario.getScene().getWindow();
                stage.close();
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el usuario", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void borrarUsuario() {
        if (confirmDelete()) {
            try {
                UsuarioDao dao = new UsuarioDao();
                if (dao.deleteUsuario(usuario.getIdUsuario())) {
                    mostrarAlerta("Éxito", "Usuario eliminado correctamente", Alert.AlertType.INFORMATION);
                    Stage stage = (Stage) borrarUsuario.getScene().getWindow();
                    stage.close();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el usuario", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al eliminar: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    private boolean confirmDelete(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación de Eliminación");
        alert.setHeaderText("¿Estás seguro de eliminar este usuario?");
        alert.setContentText("Esta acción no se puede deshacer. Se eliminarán todos los datos del usuario: " + usuario.getNombre());

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}