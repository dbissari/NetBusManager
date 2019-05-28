/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utbm.gl52.netbusmanager.util;

import com.sothawo.mapjfx.Coordinate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bright
 */
public class CoordinatesUtil {

    /**
     * Splits a line between 2 coordinates into the specified number of points
     * @param a the departure coordinates
     * @param b the arrival coordinates
     * @param count the number of coordinates between the coordinates to get
     * @return the line coordinates including departure and arrival
     */
    public static List<Coordinate> splitLine(Coordinate a, Coordinate b, Integer count) {

        count = count + 1;

        Double d = Math.sqrt((a.getLatitude() - b.getLatitude()) * (a.getLatitude() - b.getLatitude()) + (a.getLongitude() - b.getLongitude()) * (a.getLongitude() - b.getLongitude())) / count;
        Double fi = Math.atan2(b.getLongitude() - a.getLongitude(), b.getLatitude() - a.getLatitude());

        List<Coordinate> points = new ArrayList<>(count + 1);

        for (int i = 0; i <= count; ++i) {
            points.add(new Coordinate(a.getLatitude() + i * d * Math.cos(fi), a.getLongitude() + i * d * Math.sin(fi)));
        }

        return points;
    }
}
