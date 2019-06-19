package fr.utbm.gl52.netbusmanager.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.time.DateUtils;

/**
 * @author bright
 *
 */
@Entity
public class StopTime implements Serializable {

    public interface Schedule {}
    public interface RouteTrip {}
    
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false, referencedColumnName = "id")
    @NotNull(groups = {RouteTrip.class}, message = "Un itinéraire doit être renseigné")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "stop_id", nullable = false, referencedColumnName = "id")
    @NotNull(groups = {RouteTrip.class}, message = "Un arrêt doit être renseigné")
    private Stop stop;

    @Column(nullable = false)
    @NotNull(groups = {RouteTrip.class})
    private Integer stopSequence;

    @Column(nullable = true)
    @NotNull(groups = {Schedule.class}, message = "Le temps depuis le premier arrêt doit être renseigné")
    @Min(value = 0, groups = {Schedule.class}, message = "Le temps depuis le premier arrêt doit être supérieur ou égal à 0")
    private Integer timeFromFirstStop;

    public StopTime() {

    }

    /**
     * @param trip the trip
     * @param stop the stop
     * @param stopSequence the stopSequence
     */
    public StopTime(Trip trip, Stop stop, Integer stopSequence) {
        this.trip = trip;
        this.stop = stop;
        this.stopSequence = stopSequence;
        // Default time from first stop
        this.timeFromFirstStop = stopSequence * 2;
    }
    
    public List<Date> getDayStopTimes() throws ParseException {
        List<Date> dayStopTimes = new ArrayList<>();
        
        Date startTime = this.trip.getStartTime() == null ? TIME_FORMAT.parse("00:00") : this.trip.getStartTime();
        Date endTime = this.trip.getEndTime() == null ? TIME_FORMAT.parse("23:59") : this.trip.getEndTime();
        
        Date timeToAdd = this.timeFromFirstStop == null ? null : DateUtils.addMinutes(startTime, this.timeFromFirstStop);
        
        while (timeToAdd != null && timeToAdd.getTime() >= startTime.getTime() && timeToAdd.getTime() <= endTime.getTime()) {
            dayStopTimes.add(timeToAdd);
            Date newTime = DateUtils.addMinutes(timeToAdd, this.trip.getFrequency());
            if (!DateUtils.isSameDay(timeToAdd, newTime))
                timeToAdd = null;
            else
                timeToAdd = newTime;
        }
        
        return dayStopTimes;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the trip
     */
    public Trip getTrip() {
        return this.trip;
    }

    /**
     * @param trip the trip to set
     */
    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    /**
     * @return the stop
     */
    public Stop getStop() {
        return this.stop;
    }

    /**
     * @param stop the stop to set
     */
    public void setStop(Stop stop) {
        this.stop = stop;
    }

    /**
     * @return the stopSequence
     */
    public Integer getStopSequence() {
        return this.stopSequence;
    }

    /**
     * @param stopSequence the stopSequence to set
     */
    public void setStopSequence(Integer stopSequence) {
        this.stopSequence = stopSequence;
    }

    /**
     * @return the timeFromFirstStop
     */
    public Integer getTimeFromFirstStop() {
        return this.timeFromFirstStop;
    }

    /**
     * @param timeFromFirstStop the timeFromFirstStop to set
     */
    public void setTimeFromFirstStop(Integer timeFromFirstStop) {
        this.timeFromFirstStop = timeFromFirstStop;
    }

    @Override
    public String toString() {
        return trip +
                (timeFromFirstStop == null ? "" : "; Temps depuis le premier arrêt : " + timeFromFirstStop)
                ;
    }

}
