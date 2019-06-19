package fr.utbm.gl52.netbusmanager.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author bright
 *
 */
@Entity
public class Stop implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "Le nom ne peut être vide")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "La latitude ne peut être vide")
    @Min(value = 0, message = "Une latitude est supérieure à 0")
    @Max(value = 90, message = "Une latitude est inférieure à 90")
    private Double latitude;

    @Column(nullable = false)
    @NotNull(message = "La longitude ne peut être vide")
    @Min(value = -180, message = "Une longitude est supérieure à 180")
    @Max(value = 180, message = "Une longitude est inférieure à 0")
    private Double longitude;

    public Stop() {

    }

    /**
     * @param name the name
     * @param latitude the latitude
     * @param longitude the longitude
     */
    public Stop(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the latitude
     */
    public Double getLatitude() {
        return this.latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public Double getLongitude() {
        return this.longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Arrêt : " + name;
    }

}
