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
     * @param fileName the fxml file to load
     * @throws IOException
     */
    public static void switchScene(String fileName) throws IOException {
        Parent root = FXMLLoader.load(MainApp.class.getResource("/fxml/" + fileName + ".fxml"));
        MainApp.stage.setScene(new Scene(root));
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

    /**
     * @return the stop dao
     */
    public static StopDao getStopDao() {
        return stopDao;
    }

    /**
     * @return the route dao
     */
    public static RouteDao getRouteDao() {
        return routeDao;
    }

    /**
     * @return the stop time dao
     */
    public static StopTimeDao getStopTimeDao() {
        return stopTimeDao;
    }

    /**
     * @return the trip dao
     */
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
