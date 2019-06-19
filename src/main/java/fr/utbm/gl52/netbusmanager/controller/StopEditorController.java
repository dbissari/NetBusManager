package fr.utbm.gl52.netbusmanager.controller;

import fr.utbm.gl52.netbusmanager.FxmlResource;
import fr.utbm.gl52.netbusmanager.MainApp;
import fr.utbm.gl52.netbusmanager.dao.StopDao;
import fr.utbm.gl52.netbusmanager.entity.Stop;
import fr.utbm.gl52.netbusmanager.util.ValidatorUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javax.validation.ConstraintViolation;
import org.apache.commons.io.FileUtils;

/**
 * FXML Controller class
 *
 * @author bright
 */
public class StopEditorController implements Initializable {

    private final StopDao stopDao = MainApp.getStopDao();

    private Stop stopToEdit;

    @FXML
    private GridPane editStopGridPane;
    @FXML
    private TextField stopNameTextField;
    @FXML
    private TextField longitudeTextField;
    @FXML
    private TextField latitudeTextField;
    @FXML
    private Button addStopButton;
    @FXML
    private Label editorInformationLabel;
    @FXML
    private TableView dataTableView;
    @FXML
    private Pane listStopPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn nameColumn = new TableColumn("Nom");
        nameColumn.setPrefWidth(150);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn longitudeColumn = new TableColumn("Longitude");
        longitudeColumn.setPrefWidth(100);
        longitudeColumn.setCellValueFactory(new PropertyValueFactory<>("longitude"));

        TableColumn latitudeColumn = new TableColumn("Latitude");
        latitudeColumn.setPrefWidth(100);
        latitudeColumn.setCellValueFactory(new PropertyValueFactory<>("latitude"));

        this.refreshDataTableView();

        this.dataTableView.getColumns().addAll(nameColumn, longitudeColumn, latitudeColumn);
    }
    
    private void refreshDataTableView() {
        this.dataTableView.setItems(FXCollections.observableArrayList(this.stopDao.getAll()));
        this.dataTableView.refresh();
    }

    @FXML
    public void goBackToMainScreen() throws IOException {
        MainApp.switchScene(FxmlResource.MAIN_MENU.getFileName());
    }

    @FXML
    public void toggleStopEditorForm(ActionEvent e) {

        if (!this.editStopGridPane.isVisible()) {
            if (this.listStopPane.isVisible()) {
                this.listStopPane.setVisible(false);
            }
            this.editStopGridPane.setVisible(true);
            
            if (this.stopToEdit == null)
                this.clearStopEditorFields();
            else
                this.fillStopEditorFields();
        
            if (this.editorInformationLabel.isVisible())
                this.editorInformationLabel.setVisible(false);
        }
        else {
            this.editStopGridPane.setVisible(false);
            this.listStopPane.setVisible(true);
        }
    }

    private void clearStopEditorFields() {
        this.latitudeTextField.clear();
        this.longitudeTextField.clear();
        this.stopNameTextField.clear();
    }

    private void fillStopEditorFields() {
        this.latitudeTextField.setText(this.stopToEdit.getLatitude().toString());
        this.longitudeTextField.setText(this.stopToEdit.getLongitude().toString());
        this.stopNameTextField.setText(this.stopToEdit.getName());
    }

    @FXML
    public void saveStop(ActionEvent e) throws IOException {
        this.editorInformationLabel.setVisible(false);
        this.editorInformationLabel.setText("");
        
        Double latitude = this.latitudeTextField.getText().isEmpty() ? null : Double.parseDouble(this.latitudeTextField.getText());
        Double longitude = this.longitudeTextField.getText().isEmpty() ? null : Double.parseDouble(this.longitudeTextField.getText());
        Stop newStop = new Stop(this.stopNameTextField.getText(), latitude, longitude);
        
        Set<ConstraintViolation<Stop>> violations = ValidatorUtil.getValidator().validate(newStop);
        if (violations.isEmpty()) {
            if (this.stopToEdit == null) {
                this.stopDao.save(newStop);
            }
            else {
                newStop.setId(this.stopToEdit.getId());
                this.stopDao.update(newStop);
                this.stopToEdit = null;
            }

            this.addStopButton.fire();
            this.refreshDataTableView();
        } 
        else {
            violations.forEach(violation -> {
                this.editorInformationLabel.setText(this.editorInformationLabel.getText() + " | " + violation.getMessage());
            });
            this.editorInformationLabel.setVisible(true);
        }
    }

    @FXML
    public void editSelectedStop(ActionEvent e) {
        this.stopToEdit = (Stop) this.dataTableView.getSelectionModel().getSelectedItem();
        if (this.stopToEdit != null) {
            this.addStopButton.fire();
        }
    }

    @FXML
    private void loadStopsFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir le fichier d'arrêts à charger");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers d'arrêts", "*.stops.txt"));
        File file = fileChooser.showOpenDialog(MainApp.getPrimaryStage());

        if (file != null) {
            try {
                List<String> lines = FileUtils.readLines(file, "UTF-8");

                for (String line : lines) {
                    String[] lineData = line.split(",");
                    this.stopDao.save(new Stop(lineData[0], Double.parseDouble(lineData[1]), Double.parseDouble(lineData[2])));
                }
                this.refreshDataTableView();
            } catch (IOException ex) {
                System.err.println("Could not load file. " + ex);
            }
        }
    }
}
