/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utbm.gl52.netbusmanager.simulator;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 *
 * @author bright
 */
public class Simulator {
    
    // number of seconds for buses moving next step
    public static final Integer FRAME_REFRESH_SECONDS = 1;
    
    // number of seconds matches one saved minute
    public static final Integer FRAME_REFRESH_SCALE = 60;
    
    private final Timeline refreshTimeline;
    
    private List<Bus> buses = new ArrayList<>();

    public Simulator() {
        this.refreshTimeline = new Timeline();
        this.refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        this.refreshTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(FRAME_REFRESH_SECONDS), (ActionEvent) -> {
            this.buses.forEach(bus -> {
                bus.moveToNextStep();
            });
        }));
    }
    
    public void addBus(Bus bus) {
        this.buses.add(bus);
    }
    
    public void run() {
        this.refreshTimeline.play();
    }
}
