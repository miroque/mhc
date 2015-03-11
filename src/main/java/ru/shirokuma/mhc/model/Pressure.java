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
}
