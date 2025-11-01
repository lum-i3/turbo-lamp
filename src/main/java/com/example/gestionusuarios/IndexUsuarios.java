package com.example.gestionusuarios;

import com.example.gestionusuarios.modelo.Usuario;
import com.example.gestionusuarios.modelo.dao.UsuarioDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class IndexUsuarios implements Initializable {
    @FXML
    private TextField buscador;
    @FXML
    private Button botonAgregar;
    @FXML
    private ChoiceBox<String> filtros;
    @FXML
    private TableView<Usuario> tablaUsuario;
    @FXML
    private TableColumn<Usuario, String> tablaNombre;
    @FXML
    private TableColumn<Usuario, String> tablaCorreo;
    @FXML
    private TableColumn<Usuario, Date> tablaFecha;
    @FXML
    private TableColumn<Usuario, Boolean> tablaEstado;
    @FXML
    private TableColumn<Usuario, String> tablaAcciones;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Configurar filtros
        filtros.setItems(FXCollections.observableArrayList("Todos", "Activo", "Inactivo"));
        filtros.getSelectionModel().selectFirst();
        //Cargar datos
        UsuarioDao dao = new UsuarioDao();
        List<Usuario> datos = dao.readUsuarios();
        ObservableList<Usuario> datosObservables = FXCollections.observableArrayList(datos);
        tablaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tablaCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        tablaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        tablaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        //Configurar columnas personalizadas
        configurarColumnaEstado();
        configurarColumnaAcciones();
        //Pintar los datos
        tablaUsuario.setItems(datosObservables);
    }

    private void configurarColumnaEstado() {
        tablaEstado.setCellFactory(new Callback<TableColumn<Usuario, Boolean>, TableCell<Usuario, Boolean>>() {
            @Override
            public TableCell<Usuario, Boolean> call(TableColumn<Usuario, Boolean> param) {
                return new TableCell<Usuario, Boolean>() {
                    private final Label estadoLabel = new Label();
                    {
                        estadoLabel.setStyle("-fx-padding: 5px 10px; -fx-background-radius: 10px; -fx-text-fill: white; -fx-font-weight: bold;");
                    }
                    @Override
                    protected void updateItem(Boolean estado, boolean empty) {
                        super.updateItem(estado, empty);
                        if (empty || estado == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            if (estado) {
                                estadoLabel.setText("Activo");
                                estadoLabel.setStyle("-fx-background-color: #4CAF50; -fx-padding: 5px 10px; -fx-background-radius: 10px; -fx-text-fill: white; -fx-font-weight: bold;");
                            } else {
                                estadoLabel.setText("Inactivo");
                                estadoLabel.setStyle("-fx-background-color: #F44336; -fx-padding: 5px 10px; -fx-background-radius: 10px; -fx-text-fill: white; -fx-font-weight: bold;");
                            }
                            setGraphic(estadoLabel);
                        }
                    }
                };
            }
        });
    }

    private void configurarColumnaAcciones() {
        tablaAcciones.setCellFactory(new Callback<TableColumn<Usuario, String>, TableCell<Usuario, String>>() {
            @Override
            public TableCell<Usuario, String> call(TableColumn<Usuario, String> param) {
                return new TableCell<Usuario, String>() {
                    private final HBox botonesContainer = new HBox(5);
                    private final Button btnEditar = new Button("Editar");
                    private final Button btnCambiar = new Button("Cambiar estado");
                    private final Button btnVer = new Button("Ver");

                    {
                        botonesContainer.getChildren().addAll(btnEditar, btnCambiar, btnVer);
                        //Agregar eventos a los botones
                        btnEditar.setOnAction(event -> abrirEditarUsuario());
                        btnVer.setOnAction(event -> abrirVerUsuario());
                        btnCambiar.setOnAction(event -> cambiarEstadoUsuario());
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(botonesContainer);
                        }
                    }
                };
            }
        });
    }

    private void abrirEditarUsuario() {
        try {
            Usuario usuarioSeleccionado = tablaUsuario.getSelectionModel().getSelectedItem();
            if (usuarioSeleccionado != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditarUsuario.fxml"));
                Parent root = loader.load();

                //Pasar el usuario al controlador de edición
                EditarUsuarios controller = loader.getController();
                controller.setUsuario(usuarioSeleccionado);

                Stage stage = new Stage();
                stage.setTitle("Editar Usuario");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                // Recargar datos después de cerrar la ventana de edición
                recargarDatos();
            } else {
                mostrarAlerta("Error", "Selecciona un usuario para editar", Alert.AlertType.WARNING);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana de edición", Alert.AlertType.ERROR);
        }
    }

    private void abrirVerUsuario() {
        try {
            Usuario usuarioSeleccionado = tablaUsuario.getSelectionModel().getSelectedItem();
            if (usuarioSeleccionado != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("VerUsuarios.fxml"));
                Parent root = loader.load();

                //Pasar el usuario al controlador de ver
                VerUsuarios controller = loader.getController();
                controller.setUsuario(usuarioSeleccionado);

                Stage stage = new Stage();
                stage.setTitle("Ver Usuario");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } else {
                mostrarAlerta("Error", "Selecciona un usuario para ver", Alert.AlertType.WARNING);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana de visualización", Alert.AlertType.ERROR);
        }
    }

    private void cambiarEstadoUsuario() {
        Usuario usuarioSeleccionado = tablaUsuario.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            String nuevoEstado = usuarioSeleccionado.getEstado() ? "inactivo" : "activo";
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Cambiar Estado");
            confirmacion.setHeaderText("¿Estás seguro?");
            confirmacion.setContentText("El usuario cambiará a estado: " + nuevoEstado);
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                //Cambiar el estado
                usuarioSeleccionado.setEstado(!usuarioSeleccionado.getEstado());
                //Actualizar en la base de datos
                UsuarioDao dao = new UsuarioDao();
                if (dao.updateUsuario(usuarioSeleccionado)) {
                    recargarDatos();
                    mostrarAlerta("Éxito", "Estado cambiado correctamente", Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("Error", "No se pudo cambiar el estado", Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarAlerta("Error", "Selecciona un usuario para cambiar estado", Alert.AlertType.WARNING);
        }
    }

    private void recargarDatos() {
        UsuarioDao dao = new UsuarioDao();
        List<Usuario> datos = dao.readUsuarios();
        ObservableList<Usuario> datosObservables = FXCollections.observableArrayList(datos);
        tablaUsuario.setItems(datosObservables);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    void agregarUsuario(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegistrarUsuario.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Registrar Becarios");
            stage.initModality(Modality.APPLICATION_MODAL); //Espera a que la ventana se cierre
            stage.showAndWait();
            //Una vez cerrada la ventana de registro, actualizamos la tabla:
            UsuarioDao dao = new UsuarioDao();
            List<Usuario> lista = dao.readUsuarios();
            tablaUsuario.setItems(FXCollections.observableArrayList(lista));
            tablaUsuario.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}