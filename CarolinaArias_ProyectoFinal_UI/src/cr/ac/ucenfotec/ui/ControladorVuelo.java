package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.entidades.Aeropuerto;
import cr.ac.ucenfotec.entidades.Vuelo;
import cr.ac.ucenfotec.logica.Gestor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.time.LocalTime;

/**
 * @author Carolina Arias
 * @version 1.0
 * @since 24/11/2022
 *
 * Esta clase se encarga de gestionar el formulario FXML de Vuelos
 */
public class ControladorVuelo {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public TextField numVueloText;
    public TextField horaSalidaText;
    public TextField horaLlegadaText;
    public ComboBox<String> estadoCB;
    @FXML
    public RadioButton salidaRadio;
    @FXML
    public RadioButton llegadaRadio;
    @FXML
    public TextField cantAsientosText;
    public TextField precioText;
    public TextField impuestoText;
    public ComboBox<Aeropuerto> aeropuertoOrigenCB;
    public ComboBox<Aeropuerto> aeropuertoDestinoCB;
    @FXML
    TableView<Vuelo> listaVuelos;
    @FXML
    TableColumn<Vuelo,Integer> tNumVuelo;
    @FXML
    TableColumn<Vuelo, LocalTime> tHoraSalida;
    @FXML
    TableColumn<Vuelo,LocalTime> tHoraLlegada;
    @FXML
    TableColumn<Vuelo, String> tEstado;
    @FXML
    TableColumn<Vuelo, String> tTipo;
    @FXML
    TableColumn<Vuelo,Integer> tCantAsientos;
    @FXML
    TableColumn<Vuelo,Double> tPrecio;
    @FXML
    TableColumn<Vuelo, Double> tImpuesto;
    @FXML
    public ObservableList<Vuelo> observableVuelos;
    @FXML
    public ObservableList<Aeropuerto> observableAeropuertos;

    /**
     * Metodo para registrar un vuelo
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void registrar (ActionEvent actionEvent) {
        if(numVueloText.getText().isEmpty() || horaSalidaText.getText().isEmpty() || horaLlegadaText.getText().isEmpty() || estadoCB.getValue() == null || obtenerTipoVuelo().equals("N") || cantAsientosText.getText().isEmpty() || precioText.getText().isEmpty() || impuestoText.getText().isEmpty() || aeropuertoOrigenCB.getValue() == null || aeropuertoDestinoCB.getValue() == null)
        {
            showAlert(Alert.AlertType.ERROR,"Hay campos obligatorios sin llenar","Hay campos obligatorios sin llenar.\nPor favor llene todos los campos\nobligatorios.");
        } else {
            Gestor gestor = new Gestor();
            Vuelo vuelo = obtenerVuelo();
            String mensaje = gestor.insertarVuelo(vuelo);
            if(mensaje.equals("El vuelo fue registrado con éxito."))
            {
                showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                resetearValores();
            } else {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            }
        }
    }

    /**
     * Metodo para obtener los valores de un vuelo en los TextField
     * @param mouseEvent es de tipo MouseEvent representa algun tipo de accion realizada por el mouse
     */
    public void dobleClick(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
            Vuelo vuelo = (Vuelo) listaVuelos.getSelectionModel().getSelectedItem();
            numVueloText.setText(String.valueOf(vuelo.getNumeroVuelo()));
            horaSalidaText.setText(String.valueOf(vuelo.getHoraSalida()));
            horaLlegadaText.setText(String.valueOf(vuelo.getHoraLlegada()));
            estadoCB.getSelectionModel().select(vuelo.getEstado());
            cantAsientosText.setText(String.valueOf(vuelo.getCantAsientosDiponibles()));
            precioText.setText(String.valueOf(vuelo.getPrecioAsientos()));
            impuestoText.setText(String.valueOf(vuelo.getMontoImpuesto()));
            aeropuertoOrigenCB.getSelectionModel().select(vuelo.getAeropuertoOrigen());
            aeropuertoDestinoCB.getSelectionModel().select(vuelo.getAeropuertoDestino());
            if(vuelo.getTipoVuelo().equals("Salida")) {
                salidaRadio.setSelected(true);
            } else {
                if(vuelo.getTipoVuelo().equals("Llegada")) {
                    llegadaRadio.setSelected(true);
                }
            }
        }
    }

    /**
     * Metodo para actualizar un vuelo
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void actualizarVuelo (ActionEvent actionEvent) {
        if(numVueloText.getText().isEmpty() || horaSalidaText.getText().isEmpty() || horaLlegadaText.getText().isEmpty() || estadoCB.getValue() == null || obtenerTipoVuelo().equals("N") || cantAsientosText.getText().isEmpty() || precioText.getText().isEmpty() || impuestoText.getText().isEmpty() || aeropuertoOrigenCB.getValue() == null || aeropuertoDestinoCB.getValue() == null)
        {
            showAlert(Alert.AlertType.ERROR,"Hay campos obligatorios sin llenar","Hay campos obligatorios sin llenar.\nPor favor llene todos los campos\nobligatorios.");
        } else {
            Gestor gestor = new Gestor();
            Vuelo vuelo = obtenerVuelo();
            String mensaje = gestor.actualizarVuelo(vuelo);
            if(mensaje.equals("El vuelo fue actualizado con éxito."))
            {
                showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                cargarListaVuelos();
            } else {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            }
        }
    }

    /**
     * Metodo para eliminar un vuelo
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void eliminarVuelo (ActionEvent actionEvent) {
        if(listaVuelos.getSelectionModel().getSelectedItem() == null)
        {
            showAlert(Alert.AlertType.ERROR,"No ha seleccionado ningún vuelo.","No ha seleccionado ningún vuelo.\nPor favor seleccione un vuelo para eliminar.");
        } else {
            Gestor gestor = new Gestor();
            Vuelo vuelo = (Vuelo) listaVuelos.getSelectionModel().getSelectedItem();
            String mensaje = gestor.eliminarVuelo(vuelo);
            if(mensaje.equals("El vuelo fue eliminado con éxito."))
            {
                showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                cargarListaVuelos();
            } else {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            }
        }
    }

    /**
     * Metodo para obtener los datos de un vuelo de los TextField
     */
    public Vuelo obtenerVuelo() {
        int numVuelo = Integer.parseInt(numVueloText.getText());
        LocalTime horaSalida = LocalTime.parse(horaSalidaText.getText());
        LocalTime horaLlegada = LocalTime.parse(horaLlegadaText.getText());
        String estado = estadoCB.getValue();
        String tipoVuelo = obtenerTipoVuelo();
        int cantAsientosDisponibles = Integer.parseInt(cantAsientosText.getText());
        Double precioAsientos = Double.parseDouble(precioText.getText());
        Double montoImpuesto = Double.parseDouble(impuestoText.getText());
        Aeropuerto aeropuertoOrigen = (Aeropuerto) aeropuertoOrigenCB.getValue();
        Aeropuerto aeropuertoDestino = (Aeropuerto) aeropuertoDestinoCB.getValue();

        Vuelo vuelo = new Vuelo(numVuelo, horaSalida, horaLlegada, estado, tipoVuelo, cantAsientosDisponibles, precioAsientos, montoImpuesto, aeropuertoOrigen, aeropuertoDestino);

        return vuelo;
    }

    /**
     * Metodo para obtener el tipo de vuelo de los RadioButton
     */
    public String obtenerTipoVuelo(){
        String tipoVuelo = "";
        if (salidaRadio.isSelected()) {
            tipoVuelo = "Salida";
        } else {
            if (llegadaRadio.isSelected()) {
                tipoVuelo = "Llegada";
            } else {
                    if (!salidaRadio.isSelected() && !llegadaRadio.isSelected()) {
                        tipoVuelo = "N";
                    }
                }
        }
        return tipoVuelo;
    }

    /**
     * Metodo para resetear los valores dem los TextField
     */
    public void resetearValores() {
        cargarListaVuelos();
        numVueloText.setText("");
        horaSalidaText.setText("");
        horaLlegadaText.setText("");
        estadoCB.getSelectionModel().clearSelection();
        salidaRadio.setSelected(false);
        llegadaRadio.setSelected(false);
        cantAsientosText.setText("");
        precioText.setText("");
        impuestoText.setText("");
        aeropuertoOrigenCB.getSelectionModel().clearSelection();
        aeropuertoDestinoCB.getSelectionModel().clearSelection();
    }

    /**
     * Metodo para actualizar el TableView de los vuelos
     */
    public void cargarListaVuelos(){
        Gestor gestor = new Gestor();
        listaVuelos.getItems().clear();
        observableVuelos = FXCollections.observableArrayList();
        gestor.listarVuelos().forEach(vuelo -> observableVuelos.addAll(vuelo));
        tNumVuelo.setCellValueFactory(new PropertyValueFactory<Vuelo,Integer>("numeroVuelo"));
        tHoraSalida.setCellValueFactory(new PropertyValueFactory<Vuelo,LocalTime>("horaSalida"));
        tHoraLlegada.setCellValueFactory(new PropertyValueFactory<Vuelo,LocalTime>("horaLlegada"));
        tEstado.setCellValueFactory(new PropertyValueFactory<Vuelo, String>("estado"));
        tTipo.setCellValueFactory(new PropertyValueFactory<Vuelo, String>("tipoVuelo"));
        tCantAsientos.setCellValueFactory(new PropertyValueFactory<Vuelo,Integer>("cantAsientosDiponibles"));
        tPrecio.setCellValueFactory(new PropertyValueFactory<Vuelo,Double>("precioAsientos"));
        tImpuesto.setCellValueFactory(new PropertyValueFactory<Vuelo,Double>("montoImpuesto"));
        listaVuelos.setItems(observableVuelos);

        //ComboBox
        observableAeropuertos = FXCollections.observableArrayList(gestor.listarAeropuertos());
        aeropuertoOrigenCB.setItems(observableAeropuertos);
        aeropuertoDestinoCB.setItems(observableAeropuertos);
        estadoCB.setItems(FXCollections.observableArrayList("A tiempo", "Atrasado", "Llegó", "Cancelado", "Registrado", "En sala"));
        Callback<ListView<Aeropuerto>, ListCell<Aeropuerto>> cellFactory = new Callback<>() {

            @Override
            public ListCell<Aeropuerto> call(ListView<Aeropuerto> l) {
                return new ListCell<>() {

                    @Override
                    protected void updateItem(Aeropuerto item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getNombre()+ " (" + item.getCodigo() + ")");
                        }
                    }
                };
            }
        };

        aeropuertoOrigenCB.setButtonCell(cellFactory.call(null));
        aeropuertoOrigenCB.setCellFactory(cellFactory);
        aeropuertoDestinoCB.setButtonCell(cellFactory.call(null));
        aeropuertoDestinoCB.setCellFactory(cellFactory);
    }

    /**
     * Metodo para inicializar el ObservableList y el TableView
     */
    @FXML
    public void initialize()
    {
        try {
            cargarListaVuelos();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Metodo para mostrar una alerta al usuario
     * @param alertType es de tipo Alert.AlertType y corresponde al tipo de alerta
     * @param title es tipo String y corresponde al título de la alerta
     * @param message  es de tipo String y corresponde al mensaje de la alerta
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * Metodo para ir a la pantalla de inicio
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void inicio (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Inicio.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo para ir a la pantalla de administradores
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void administradores (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Administrador.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo para ir a la pantalla de aeropuertos
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void aeropuertos (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Aeropuerto.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo para ir a la pantalla de ubicaciones
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void ubicaciones (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Ubicacion.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo para salir de la aplicacion
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void salir(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }
}