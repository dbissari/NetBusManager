/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utbm.gl52.netbusmanager.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.CoordinateLine;
import com.sothawo.mapjfx.Extent;
import com.sothawo.mapjfx.MapLabel;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.XYZParam;
import com.sothawo.mapjfx.event.MapLabelEvent;
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;
import com.sothawo.mapjfx.offline.OfflineCache;
import fr.utbm.gl52.netbusmanager.FxmlResource;
import fr.utbm.gl52.netbusmanager.MainApp;
import fr.utbm.gl52.netbusmanager.dao.StopDao;
import fr.utbm.gl52.netbusmanager.dao.StopTimeDao;
import fr.utbm.gl52.netbusmanager.dao.TripDao;
import fr.utbm.gl52.netbusmanager.entity.Stop;
import fr.utbm.gl52.netbusmanager.entity.StopTime;
import fr.utbm.gl52.netbusmanager.entity.Trip;
import fr.utbm.gl52.netbusmanager.util.ValidatorUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javax.validation.ConstraintViolation;

/**
 * FXML Controller class
 *
 * @author bright
 */
public class MapController implements Initializable {

    private final StopDao stopDao = MainApp.getStopDao();
    private final TripDao tripDao = MainApp.getTripDao();
    private final StopTimeDao stopTimeDao = MainApp.getStopTimeDao();

    private Stop stopToCreate = null;

    private final HashMap<Marker, Stop> stopMarkers = new HashMap<>();
    private final List<CoordinateLine> tripLines = new ArrayList<>();

    private static final XYZParam xyzParam = new XYZParam()
            .withUrl("https://{s}.tile.openstreetmap.fr/osmfr/{z}/{x}/{y}.png")
            .withAttributions("donn&eacute;es &copy; <a href=\"//osm.org/copyright\">OpenStreetMap</a>/ODbL - rendu <a href=\"//openstreetmap.fr\">OSM France</a>");

    @FXML
    private MapView mapView;
    @FXML
    private ListView tripListView;
    @FXML
    private TreeView stopTimeTreeView;
    @FXML
    private Label mapInfosLabel;
    @FXML
    private VBox createStopForm;
    @FXML
    private Label editorInformationLabel;
    @FXML
    private TextField stopNameTextField;
    @FXML
    private TextField stopLatitudeTextField;
    @FXML
    private TextField stopLongitudeTextField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.mapView.setAnimationDuration(500);
        this.mapView.setXYZParam(xyzParam);

        List<Trip> trips = this.tripDao.getAll();

        this.tripListView.setCellFactory((param) -> {
            return new TripCell();
        });
        this.tripListView.setItems(FXCollections.observableArrayList(trips));

        // add listener for mapView initialization state
        this.mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            // if map is initilazed
            if (newValue) {
                // The list of initialized stops to create an extent and center map on it
                List<Coordinate> stopsCoordinates = new ArrayList<>();

                // Add stop markers
                this.stopDao.getAll().forEach(stop -> {
                    stopsCoordinates.add(this.addStopToMap(stop));
                });
                Extent allStopsExtent = Extent.forCoordinates(stopsCoordinates);
                this.mapView.setExtent(allStopsExtent);

                // Add trip lines
                trips.forEach(trip -> {
                    List<Coordinate> lineCoordinates = new ArrayList<>();
                    this.stopTimeDao.getAllStopsByTrip(trip).forEach(stop -> {
                        lineCoordinates.add(new Coordinate(stop.getLatitude(), stop.getLongitude()));
                    });
                    CoordinateLine line = new CoordinateLine(lineCoordinates)
                            .setVisible(true)
                            .setColor(Color.valueOf(trip.getRoute().getColor()))
                            .setWidth(5);
                    this.mapView.addCoordinateLine(line);
                    this.tripLines.add(line);
                });

            }
        });

        // On marker clicked, if a stop is as associated we show its time table
        this.mapView.addEventHandler(MarkerEvent.MARKER_CLICKED, event -> {
            this.createStopForm.setVisible(false);
            Stop stop = this.stopMarkers.get(event.getMarker());
            // if the marker match with a stop
            if (stop != null) {
                TreeItem<String> stopRootNode = new TreeItem<>(stop.toString());
                stopRootNode.setExpanded(true);
                this.stopTimeDao.getAllByStop(stop).forEach(stopTime -> {
                    TreeItem<String> tripNode = new TreeItem<>(stopTime.getTrip().toString());
                    try {
                        stopTime.getDayStopTimes().forEach(time -> {
                            TreeItem<String> timeLeaf = new TreeItem<>(StopTime.TIME_FORMAT.format(time));
                            tripNode.getChildren().add(timeLeaf);
                        });
                    } catch (ParseException ex) {
                        System.err.println("Time parsing failed." + ex);
                        throw new ExceptionInInitializerError(ex);
                    }
                    stopRootNode.getChildren().add(tripNode);
                });
                this.stopTimeTreeView.setRoot(stopRootNode);
            }
            event.consume();
        });
        // On label clicked, if attached to a marker we trigger the marker click event
        this.mapView.addEventHandler(MapLabelEvent.MAPLABEL_CLICKED, event -> {
            if (event.getMapLabel().getMarker().isPresent()) {
                this.mapView.fireEvent(new MarkerEvent(MarkerEvent.MARKER_CLICKED, event.getMapLabel().getMarker().get()));
            }
        });

        // On map right clicked web try to create a new stop
        mapView.addEventHandler(MapViewEvent.MAP_RIGHTCLICKED, event -> {
            this.stopToCreate = new Stop("", event.getCoordinate().getLatitude(), event.getCoordinate().getLongitude());
            this.createStopForm.setVisible(true);
            this.stopLatitudeTextField.setText(this.stopToCreate.getLatitude().toString());
            this.stopLongitudeTextField.setText(this.stopToCreate.getLongitude().toString());
            event.consume();
        });

        mapInfosLabel.textProperty().bind(Bindings.format("Centre : %s | Zoom : %.0f", this.mapView.centerProperty(), this.mapView.zoomProperty()));

        this.initOfflineCache();
        this.mapView.initialize();

    }

    @FXML
    public void saveStop() {
        this.editorInformationLabel.setVisible(false);
        this.editorInformationLabel.setText("");
        
        if (this.stopToCreate != null) {
            this.stopToCreate.setName(this.stopNameTextField.getText());
            Set<ConstraintViolation<Stop>> violations = ValidatorUtil.getValidator().validate(this.stopToCreate);
            if (violations.isEmpty()) {
                this.stopDao.save(this.stopToCreate);
                this.addStopToMap(stopToCreate);
                this.stopToCreate = null;
                this.stopNameTextField.clear();
                this.createStopForm.setVisible(false);
            } else {
                violations.forEach(violation -> {
                    this.editorInformationLabel.setText(this.editorInformationLabel.getText() + " | " + violation.getMessage());
                });
                this.editorInformationLabel.setVisible(true);
            }
        }
    }

    private Coordinate addStopToMap(Stop stop) {
        Coordinate coordinate = new Coordinate(stop.getLatitude(), stop.getLongitude());
        Marker marker = Marker.createProvided(Marker.Provided.GREEN).setPosition(coordinate)
                .setVisible(true);
        MapLabel label = new MapLabel(stop.getName())
                .setPosition(coordinate)
                .setVisible(true);
        marker.attachLabel(label);
        this.mapView.addMarker(marker).addLabel(label);
        this.stopMarkers.put(marker, stop);
        
        return coordinate;
    }

    private void initOfflineCache() {
        final OfflineCache offlineCache = this.mapView.getOfflineCache();
        final String cacheDir = System.getProperty("java.io.tmpdir") + "/mapjfx-cache";
        System.out.println("using dir for cache: " + cacheDir);
        try {
            Files.createDirectories(Paths.get(cacheDir));
            offlineCache.setCacheDirectory(cacheDir);
            offlineCache.setActive(true);
            offlineCache.setNoCacheFilters(Collections.singletonList(".*\\.sothawo\\.com/.*"));
        } catch (IOException e) {
            System.err.println("could not activate offline cache" + e);
        }
    }

    @FXML
    public void goBackToMainScreen() throws IOException {
        MainApp.switchScene(FxmlResource.MAIN_MENU.getFileName());
    }

    /**
     * Used to display color behind trip in list view
     */
    static class TripCell extends ListCell<Trip> {

        @Override
        public void updateItem(Trip item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {
                setText(item.toString());
                Rectangle rect = new Rectangle(20, 20);
                rect.setFill(Color.valueOf(item.getRoute().getColor()));
                setGraphic(rect);
            }
        }
    }

}
