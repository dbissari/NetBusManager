/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utbm.gl52.netbusmanager.simulator;

import com.sothawo.mapjfx.Coordinate;
import java.util.List;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author bright
 */
public class Bus {
    private final Property<Coordinate> coordinateProperty;
    private final List<Coordinate> trip;
    private Integer cursor;

    public Bus(List<Coordinate> trip) {
        this.trip = trip;
        this.cursor = 0;
        this.coordinateProperty = new SimpleObjectProperty<>();
        this.coordinateProperty.setValue(this.trip.get(this.cursor));
    }
    
    public void moveToNextStep() {
        this.cursor++;
        if (this.cursor == this.trip.size())
            this.cursor = 0;
        this.coordinateProperty.setValue(this.trip.get(this.cursor));
    }
    
    public Property<Coordinate> coordinateProperty() {
        return this.coordinateProperty;
    }
}
