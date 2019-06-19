package fr.utbm.gl52.netbusmanager.controller;

import fr.utbm.gl52.netbusmanager.FxmlResource;
import fr.utbm.gl52.netbusmanager.MainApp;
import fr.utbm.gl52.netbusmanager.dao.StopTimeDao;
import fr.utbm.gl52.netbusmanager.dao.TripDao;
import fr.utbm.gl52.netbusmanager.entity.Stop;
import fr.utbm.gl52.netbusmanager.entity.StopTime;
import fr.utbm.gl52.netbusmanager.entity.Trip;
import fr.utbm.gl52.netbusmanager.util.ValidatorUtil;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javax.validation.ConstraintViolation;

/**
 * FXML Controller class
 *
 * @author bright
 */
public class ScheduleEditorController implements Initializable {
    
    private final TripDao tripDao = MainApp.getTripDao();
    private final StopTimeDao stopTimeDao = MainApp.getStopTimeDao();
    
    private StopTime stopTimeToEdit;
    
    @FXML
    private ListView tripListView;
    @FXML
    private ListView stopListView;
    @FXML
    private TextField timeFromFirstStopTextField;
    @FXML
    private Label editorInformationLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tripListView.setItems(FXCollections.observableArrayList(this.tripDao.getAll()));
        
        this.tripListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.stopListView.setItems(FXCollections.observableArrayList(this.stopTimeDao.getAllStopsByTrip((Trip) newValue)));
        });
        
        this.stopListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                this.stopTimeToEdit = this.stopTimeDao.getByStopAndTrip((Stop) newValue, (Trip) this.tripListView.getSelectionModel().getSelectedItem());
            }
            catch(Throwable ex) {
                this.stopTimeToEdit = null;
            }
            
            this.updateEditorFields();
        });
    }
    
    private void updateEditorFields() {
        if (this.stopTimeToEdit != null) {
            this.timeFromFirstStopTextField.setText(this.stopTimeToEdit.getTimeFromFirstStop() == null ? "" : this.stopTimeToEdit.getTimeFromFirstStop().toString());
        }
        else {
            this.timeFromFirstStopTextField.clear();
        }
    }
    
    @FXML
    public void saveStopTime() throws ParseException {
        this.editorInformationLabel.setVisible(false);
        this.editorInformationLabel.setText("");
        
        if (this.stopTimeToEdit != null) {
            if (!this.timeFromFirstStopTextField.getText().isEmpty())
                this.stopTimeToEdit.setTimeFromFirstStop(Integer.parseInt(this.timeFromFirstStopTextField.getText()));
            
            Set<ConstraintViolation<StopTime>> violations = ValidatorUtil.getValidator().validate(this.stopTimeToEdit, StopTime.Schedule.class);
            if (violations.isEmpty()) {
                this.stopTimeDao.update(stopTimeToEdit);
                this.editorInformationLabel.setText("Modifications enregistrées avec succès");
                this.editorInformationLabel.setVisible(true);
            }
            else {
                violations.forEach(violation -> {
                    this.editorInformationLabel.setText(this.editorInformationLabel.getText() + " | " + violation.getMessage());
                });
                this.editorInformationLabel.setVisible(true);
            }
        }
        else {
            this.editorInformationLabel.setText("Veuillez choisir un itinéraire et un arrêt");
            this.editorInformationLabel.setVisible(true);
        }
    }

    @FXML
    public void goBackToMainScreen() throws IOException {
        MainApp.switchScene(FxmlResource.MAIN_MENU.getFileName());
    }
    
}
