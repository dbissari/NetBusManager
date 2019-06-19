/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utbm.gl52.netbusmanager.controller;

import fr.utbm.gl52.netbusmanager.FxmlResource;
import fr.utbm.gl52.netbusmanager.MainApp;
import fr.utbm.gl52.netbusmanager.dao.TripDao;
import fr.utbm.gl52.netbusmanager.entity.Trip;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author bright
 */
public class TripListController implements Initializable {

    private final TripDao tripDao = new TripDao();

    @FXML
    private TableView dataTableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn routeColumn = new TableColumn("Ligne");
        routeColumn.setPrefWidth(100);
        routeColumn.setCellValueFactory(new PropertyValueFactory<>("route"));

        TableColumn directionColumn = new TableColumn("Direction");
        directionColumn.setPrefWidth(170);
        directionColumn.setCellValueFactory(new PropertyValueFactory<>("direction"));

        TableColumn startTimeColumn = new TableColumn("Début service");
        startTimeColumn.setPrefWidth(120);
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        startTimeColumn.setCellFactory(column -> {
            return new TableCell<Trip, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty)
                        setText(null);
                    else
                        setText(Trip.TIME_FORMAT.format(item));
                }
            };
        });

        TableColumn endTimeColumn = new TableColumn("Fin service");
        endTimeColumn.setPrefWidth(120);
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        endTimeColumn.setCellFactory(column -> {
            return new TableCell<Trip, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty)
                        setText(null);
                    else
                        setText(Trip.TIME_FORMAT.format(item));
                }
            };
        });

        this.dataTableView.setItems(FXCollections.observableArrayList(this.tripDao.getAll()));

        this.dataTableView.getColumns().addAll(routeColumn, directionColumn, startTimeColumn, endTimeColumn);
    }

    @FXML
    public void goBackToMainScreen() throws IOException {
        MainApp.switchScene(FxmlResource.MAIN_MENU.getFileName());
    }

    @FXML
    public void goToTripEditorScreen() throws IOException {
        MainApp.switchScene(FxmlResource.TRIP_EDITOR.getFileName());
    }

}
