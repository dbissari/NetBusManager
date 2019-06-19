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
    private final Integer frameRefreshSeconds;
    
    // number of seconds matches one saved minute
    private final Integer frameRefreshScale;
    
    private final Timeline refreshTimeline;
    
    private List<Bus> buses = new ArrayList<>();

    public Simulator(Integer frameRefreshSeconds, Integer frameRefreshScale) {
        this.frameRefreshSeconds = frameRefreshSeconds;
        this.frameRefreshScale = frameRefreshScale;
        this.refreshTimeline = new Timeline();
        this.refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        this.refreshTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(frameRefreshSeconds), (ActionEvent) -> {
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
    
    /**
     * From minutes it returns number of steps to create
     * @param minutes the minutes
     * @return the number of steps which correspond to the minutes passed
     */
    public Integer getStepsCountFromMinutes(Integer minutes) {
        return minutes * this.frameRefreshScale / this.frameRefreshSeconds;
    }
}
