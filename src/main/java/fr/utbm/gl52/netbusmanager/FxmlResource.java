package fr.utbm.gl52.netbusmanager;

/**
 * Enum created to hold project's FXML files to ease the referencing
 *
 * @author bright
 */
public enum FxmlResource {
    
    MAIN_MENU("MainMenu"),
    STOP_EDITOR("StopEditor"),
    TRIP_EDITOR("TripEditor"),
    TRIP_LIST("TripList"),
    MAP("Map"),
    SCHEDULE_EDITOR("ScheduleEditor"),
    ROUTE_EDITOR("RouteEditor");
    
    private final String fileName;
    
    private FxmlResource(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }
}
