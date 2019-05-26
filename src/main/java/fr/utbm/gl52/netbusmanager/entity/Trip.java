/**
 *
 */
package fr.utbm.gl52.netbusmanager.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author dbissari
 *
 */
@Entity
public class Trip implements Serializable {
    
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false, referencedColumnName = "id")
    @NotNull(message = "Une ligne doit être renseignée")
    private Route route;

    @Column(nullable = false)
    @NotBlank(message = "La direction ne peut être vide")
    private String direction;

    @Column(nullable = false)
    @NotBlank(message = "La couleur ne peut être vide")
    private String color;

    @Column(nullable = true)
    @Temporal(TemporalType.TIME)
    private Date startTime;

    @Column(nullable = true)
    @Temporal(TemporalType.TIME)
    private Date endTime;

    public Trip() {

    }

    /**
     * @param route the route
     * @param direction the direction
     * @param startTime the start time
     * @param endTime the end time
     * @param color the color
     */
    public Trip(Route route, String direction, Date startTime, Date endTime, String color) {
        this.route = route;
        this.direction = direction;
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = color;
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
     * @return the route
     */
    public Route getRoute() {
        return this.route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     * @return the direction
     */
    public String getDirection() {
        return this.direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return the start time
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the end time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return this.color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return route + "; Direction : " + direction;
    }

}
