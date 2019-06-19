package fr.utbm.gl52.netbusmanager.controller;

import fr.utbm.gl52.netbusmanager.FxmlResource;
import fr.utbm.gl52.netbusmanager.MainApp;
import java.io.IOException;
import javafx.fxml.FXML;

/**
 * FXML Controller class
 *
 * @author bright
 */
public class MainMenuController {

    @FXML
    private void mapShow() throws IOException {
        MainApp.switchScene(FxmlResource.MAP.getFileName());
    }

    @FXML
    private void stopEditorShow() throws IOException {
        MainApp.switchScene(FxmlResource.STOP_EDITOR.getFileName());
    }

    @FXML
    private void routeEditorShow() throws IOException {
        MainApp.switchScene(FxmlResource.ROUTE_EDITOR.getFileName());
    }

    @FXML
    private void tripEditorShow() throws IOException {
        MainApp.switchScene(FxmlResource.TRIP_EDITOR.getFileName());
    }

    @FXML
    private void scheduleEditorShow() throws IOException {
        MainApp.switchScene(FxmlResource.SCHEDULE_EDITOR.getFileName());
    }

}
