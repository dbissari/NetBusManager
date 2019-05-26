/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utbm.gl52.netbusmanager.controller;

import fr.utbm.gl52.netbusmanager.FxmlResource;
import fr.utbm.gl52.netbusmanager.MainApp;
import fr.utbm.gl52.netbusmanager.dao.RouteDao;
import fr.utbm.gl52.netbusmanager.dao.StopDao;
import fr.utbm.gl52.netbusmanager.dao.StopTimeDao;
import fr.utbm.gl52.netbusmanager.dao.TripDao;
import fr.utbm.gl52.netbusmanager.entity.Route;
import fr.utbm.gl52.netbusmanager.entity.Stop;
import fr.utbm.gl52.netbusmanager.entity.StopTime;
import fr.utbm.gl52.netbusmanager.entity.Trip;
import fr.utbm.gl52.netbusmanager.util.ValidatorUtil;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import javax.validation.ConstraintViolation;

/**
 *
 * @author bright
 */
public class RouteAndTripEditorController implements Initializable {
    
    private final RouteDao routeDao = MainApp.getRouteDao();
    private final TripDao tripDao = MainApp.getTripDao();
    private final StopTimeDao stopTimeDao = MainApp.getStopTimeDao();
    private final StopDao stopDao = MainApp.getStopDao();
    
    @FXML
    ComboBox routeComboBox;
    @FXML
    TextField directionTextField;
    @FXML
    TextField startTimeTextField;
    @FXML
    TextField endTimeTextField;
    @FXML
    ListView allStopsListView;
    @FXML
    ListView addedStopsListView;
    @FXML
    private Label editorInformationLabel;
    @FXML
    private Button existingLinesButton;
    @FXML
    private ColorPicker tripColorPicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.routeComboBox.setItems(FXCollections.observableArrayList(this.routeDao.getAll()));
        this.resetListViews();
    }
    
    private void resetListViews() {
        this.allStopsListView.setItems(FXCollections.observableArrayList(this.stopDao.getAll()));
        this.allStopsListView.refresh();
        
        this.addedStopsListView.setItems(FXCollections.observableArrayList());
        this.addedStopsListView.refresh();
    }
    
    @FXML
    public void goBackToMainScreen() throws IOException {
        MainApp.switchScene(FxmlResource.MAIN_MENU.getFileName());
    }
    
    @FXML
    public void existingLinesShow() throws IOException {
        MainApp.switchScene(FxmlResource.LINE_LIST.getFileName());
    }
    
    @FXML
    public void addNewRoute(ActionEvent e) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ajout d'une ligne");
        dialog.setHeaderText(dialog.getTitle());
        dialog.setContentText("Identifiant/Description :");
        Optional<String> result = dialog.showAndWait();
        
        this.editorInformationLabel.setVisible(false);
        this.editorInformationLabel.setText("");
        
        if (result.isPresent()){
            Route newRoute = new Route(result.get());
            
            Set<ConstraintViolation<Route>> violations = ValidatorUtil.getValidator().validate(newRoute);
            if (violations.isEmpty()) {
                this.routeDao.save(newRoute);
                this.routeComboBox.setItems(FXCollections.observableArrayList(this.routeDao.getAll()));
            } 
            else {
                violations.forEach(violation -> {
                    this.editorInformationLabel.setText(this.editorInformationLabel.getText() + " | " + violation.getMessage());
                });
                this.editorInformationLabel.setVisible(true);
            }
        }
    }
    
    @FXML
    public void addSelectedStop() {
        this.addedStopsListView.getItems().addAll(this.allStopsListView.getSelectionModel().getSelectedItems());
        this.addedStopsListView.refresh();
        
        this.allStopsListView.getItems().removeAll(this.allStopsListView.getSelectionModel().getSelectedItems());
        this.allStopsListView.refresh();
    }
    
    @FXML
    public void removeSelectedStop() {
        this.allStopsListView.getItems().addAll(this.addedStopsListView.getSelectionModel().getSelectedItems());
        this.allStopsListView.refresh();
        
        this.addedStopsListView.getItems().removeAll(this.addedStopsListView.getSelectionModel().getSelectedItems());
        this.addedStopsListView.refresh();
    }
    
    @FXML
    public void submitTrip(ActionEvent e) throws ParseException {
        this.editorInformationLabel.setVisible(false);
        this.editorInformationLabel.setText("");
        
        Date startTime = this.startTimeTextField.getText() == null || this.startTimeTextField.getText().isEmpty() ?
            null : Trip.TIME_FORMAT.parse(this.startTimeTextField.getText());
        Date endTime = this.endTimeTextField.getText() == null || this.endTimeTextField.getText().isEmpty() ?
            null : Trip.TIME_FORMAT.parse(this.endTimeTextField.getText());
        Trip newTrip = new Trip((Route) this.routeComboBox.getSelectionModel().getSelectedItem(), this.directionTextField.getText(), startTime, endTime, this.tripColorPicker.getValue().toString());
        
        Set<ConstraintViolation<Trip>> violations = ValidatorUtil.getValidator().validate(newTrip);
        if (violations.isEmpty() && this.addedStopsListView.getItems().size() >= 2) {
            this.tripDao.save(newTrip);
            for (int i = 0; i < this.addedStopsListView.getItems().size(); i++) {
                this.stopTimeDao.save(new StopTime(newTrip, (Stop) this.addedStopsListView.getItems().get(i), i));
            }
            this.clearEditorFields();
            this.existingLinesButton.fire();
        } 
        else {
            violations.forEach(violation -> {
                this.editorInformationLabel.setText(this.editorInformationLabel.getText() + " | " + violation.getMessage());
            });
            if (this.addedStopsListView.getItems().size() < 2)
                this.editorInformationLabel.setText(this.editorInformationLabel.getText() + " | " + "Veuillez ajouter au moins 2 arrêts");
            this.editorInformationLabel.setVisible(true);
        }
    }
    
    private void clearEditorFields() {
        this.routeComboBox.getSelectionModel().select(null);
        this.directionTextField.clear();
        this.startTimeTextField.clear();
        this.endTimeTextField.clear();
        this.resetListViews();
    }
        
}
