/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utbm.gl52.netbusmanager;

/**
 *
 * @author bright
 */
public enum FxmlResource {
    
    MAIN_MENU("MainMenu"),
    STOP_EDITOR("StopEditor"),
    LINE_EDITOR("RouteAndTripEditor"),
    LINE_LIST("TripList"),
    MAP("Map"),
    SCHEDULE_EDITOR("ScheduleEditor");
    
    private final String fileName;
    
    private FxmlResource(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }
}
