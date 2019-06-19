package fr.utbm.gl52.netbusmanager.simulator;

import com.sothawo.mapjfx.Coordinate;
import java.util.List;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author bright
 */
public class Bus {

    private final Property<Coordinate> coordinateProperty;
    private final List<Coordinate> trip;
    private Integer cursor;
    private Integer timeBeforeDeparture;
    private final SimpleBooleanProperty visibleProperty;

    public Bus(List<Coordinate> trip, Integer timeBeforeDeparture) {
        this.trip = trip;
        this.cursor = 0;
        this.coordinateProperty = new SimpleObjectProperty<>();
        this.coordinateProperty.setValue(this.trip.get(this.cursor));
        this.timeBeforeDeparture = timeBeforeDeparture;
        this.visibleProperty = new SimpleBooleanProperty();
        this.visibleProperty.setValue(timeBeforeDeparture == 0);
    }

    public void moveToNextStep() {
        if (this.timeBeforeDeparture == 0) {
            // if not visible and not at the end of trip set visible
            if (!this.visibleProperty.getValue() && this.cursor < this.trip.size()-1) {
                this.visibleProperty.setValue(Boolean.TRUE);
            }
            // if visible and at the end of trip set not visible
            if (this.visibleProperty.getValue() && this.cursor >= this.trip.size()-1) {
                this.visibleProperty.setValue(Boolean.FALSE);
            }
            // if not at the end of trip move to next
            if (this.cursor < this.trip.size() - 1) {
                this.cursor++;
                this.coordinateProperty.setValue(this.trip.get(this.cursor));
            }
        } else {
            this.timeBeforeDeparture--;
        }
    }

    public Property<Coordinate> coordinateProperty() {
        return this.coordinateProperty;
    }
    
    public SimpleBooleanProperty visibleProperty() {
        return this.visibleProperty;
    }
}
