/**
 *
 */
package fr.utbm.gl52.netbusmanager.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

/**
 * @author dbissari
 *
 */
@Entity
public class Route implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "L'identidiant ne peut être vide")
    private String identifier;

    @Column(nullable = false)
    @NotBlank(message = "La couleur ne peut être vide")
    private String color;

    public Route() {

    }

    /**
     * @param identifier the identifier
     * @param color the color
     */
    public Route(String identifier, String color) {
        this.identifier = identifier;
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
     * @return the identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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
        return "Ligne : " + identifier;
    }

}
