package fr.utbm.gl52.netbusmanager;

import fr.utbm.gl52.netbusmanager.dao.RouteDao;
import fr.utbm.gl52.netbusmanager.dao.StopDao;
import fr.utbm.gl52.netbusmanager.dao.StopTimeDao;
import fr.utbm.gl52.netbusmanager.dao.TripDao;
import fr.utbm.gl52.netbusmanager.util.EntityManagerUtil;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage stage;
    
    private final static StopDao stopDao;
    private final static RouteDao routeDao;
    private final static StopTimeDao stopTimeDao;
    private final static TripDao tripDao;
    
    static {
        stopDao = new StopDao();
        routeDao = new RouteDao();
        stopTimeDao = new StopTimeDao();
        tripDao = new TripDao();
    }

    /**
     * Switches to another window
     * 
     * @param fileName the window fxml file
     * @throws IOException
     */
    public static void switchScene(String fileName) throws IOException {
        Parent root = FXMLLoader.load(MainApp.class.getResource("/fxml/" + fileName + ".fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/css/style.css");
        MainApp.stage.setScene(scene);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MainApp.stage = stage;

        MainApp.switchScene(FxmlResource.MAIN_MENU.getFileName());

        stage.setTitle("NetBusManager");
        stage.show();

        // close emf on app closing
        stage.setOnCloseRequest(event -> {
            EntityManagerUtil.closeEntityManagerFactory();
        });
    }

    public static Stage getPrimaryStage() {
        return stage;
    }
    
    public static StopDao getStopDao() {
        return stopDao;
    }

    public static RouteDao getRouteDao() {
        return routeDao;
    }

    public static StopTimeDao getStopTimeDao() {
        return stopTimeDao;
    }

    public static TripDao getTripDao() {
        return tripDao;
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
