package main.java.ru.shirokuma.mhc.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;

/**
 * Created by panov on 11.03.2015.
 */
public class Pressure {
    private final IntegerProperty sbp;
    private final IntegerProperty dbp;
    private final IntegerProperty pulse;
    private final ObjectProperty<LocalDateTime> timePoint;

    /**
     * Default constructor.
     */
    public Pressure() {
        this(120, 80, 60);
    }

    /**
     * Constructor with some initial data.
     *
     * @param upper
     * @param bottom
     * @param pulse
     */
    public Pressure(int upper, int bottom, int pulse) {
        this.sbp = new SimpleIntegerProperty(upper);
        this.dbp = new SimpleIntegerProperty(bottom);
        this.pulse = new SimpleIntegerProperty(pulse);

        // Some initial dummy data, just for convenient testing.
        this.timePoint = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.now());
    }

    public int getSbp() {
        return sbp.get();
    }

    public IntegerProperty sbpProperty() {
        return sbp;
    }

    public void setSbp(int sbp) {
        this.sbp.set(sbp);
    }

    public int getDbp() {
        return dbp.get();
    }

    public IntegerProperty dbpProperty() {
        return dbp;
    }

    public void setDbp(int dbp) {
        this.dbp.set(dbp);
    }

    public int getPulse() {
        return pulse.get();
    }

    public IntegerProperty pulseProperty() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse.set(pulse);
    }

    public LocalDateTime getTimePoint() {
        return timePoint.get();
    }

    public ObjectProperty<LocalDateTime> timePointProperty() {
        return timePoint;
    }

    public void setTimePoint(LocalDateTime timePoint) {
        this.timePoint.set(timePoint);
    }
}
