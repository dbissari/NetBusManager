package fr.utbm.gl52.netbusmanager.controller;

import fr.utbm.gl52.netbusmanager.FxmlResource;
import fr.utbm.gl52.netbusmanager.MainApp;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainMenuController {
    
    @FXML
    private void mapShow(ActionEvent event) throws IOException {
        MainApp.switchScene(FxmlResource.MAP.getFileName());
    }
    
    @FXML
    private void stopEditorShow(ActionEvent event) throws IOException {
        MainApp.switchScene(FxmlResource.STOP_EDITOR.getFileName());
    }
    
    @FXML
    private void routeEditorShow(ActionEvent event) throws IOException {
        MainApp.switchScene(FxmlResource.ROUTE_EDITOR.getFileName());
    }
    
    @FXML
    private void tripEditorShow(ActionEvent event) throws IOException {
        MainApp.switchScene(FxmlResource.TRIP_EDITOR.getFileName());
    }
    
    @FXML
    private void scheduleEditorShow(ActionEvent event) throws IOException {
        MainApp.switchScene(FxmlResource.SCHEDULE_EDITOR.getFileName());
    }
    
}
