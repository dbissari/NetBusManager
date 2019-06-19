package fr.utbm.gl52.netbusmanager.controller;

import fr.utbm.gl52.netbusmanager.FxmlResource;
import fr.utbm.gl52.netbusmanager.MainApp;
import fr.utbm.gl52.netbusmanager.dao.RouteDao;
import fr.utbm.gl52.netbusmanager.entity.Route;
import fr.utbm.gl52.netbusmanager.util.ValidatorUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javax.validation.ConstraintViolation;

/**
 * FXML Controller class
 *
 * @author bright
 */
public class RouteEditorController implements Initializable {
    
    private final RouteDao routeDao = MainApp.getRouteDao();
    private Route routeToEdit = null;
    
    @FXML
    private TextField identifierTextField;
    @FXML
    private ColorPicker routeColorPicker;
    @FXML
    private ListView routesListView;
    @FXML
    private Label editorInformationLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.refreshListView();
    }    
    
    @FXML
    public void saveRoute() {
        this.editorInformationLabel.setVisible(false);
        this.editorInformationLabel.setText("");
        
        Route newRoute = new Route(this.identifierTextField.getText(), this.routeColorPicker.getValue().toString());
            
            Set<ConstraintViolation<Route>> violations = ValidatorUtil.getValidator().validate(newRoute);
            if (violations.isEmpty()) {
                if (this.routeToEdit == null) {
                    this.routeDao.save(newRoute);
                }
                else {
                    newRoute.setId(this.routeToEdit.getId());
                    this.routeDao.update(newRoute);
                    this.routeToEdit = null;
                }
                this.clearEditorFields();
                this.refreshListView();
            } 
            else {
                violations.forEach(violation -> {
                    this.editorInformationLabel.setText(this.editorInformationLabel.getText() + " | " + violation.getMessage());
                });
                this.editorInformationLabel.setVisible(true);
            }
    }
    
    @FXML
    public void editSelectedRoute() {
        this.routeToEdit = (Route) this.routesListView.getSelectionModel().getSelectedItem();
        if (this.routeToEdit != null) {
            this.identifierTextField.setText(this.routeToEdit.getIdentifier());
            this.routeColorPicker.setValue(Color.valueOf(this.routeToEdit.getColor()));
            this.editorInformationLabel.setVisible(false);
            this.editorInformationLabel.setText("");
        }
    }
    
    private void refreshListView() {
        this.routesListView.setItems(FXCollections.observableArrayList(this.routeDao.getAll()));
        this.routesListView.refresh();
    }
    
    private void clearEditorFields() {
        this.identifierTextField.clear();
        this.routeColorPicker.setValue(Color.WHITE);
    }
    
    @FXML
    public void goBackToMainScreen() throws IOException {
        MainApp.switchScene(FxmlResource.MAIN_MENU.getFileName());
    }
    
    @FXML
    public void goToTripEditorScreen() throws IOException {
        MainApp.switchScene(FxmlResource.TRIP_EDITOR.getFileName());
    }
    
    @FXML
    public void goToTripListScreen() throws IOException {
        MainApp.switchScene(FxmlResource.TRIP_LIST.getFileName());
    }
}
