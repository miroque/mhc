package ru.shirokuma.mhc.model;

import javafx.beans.property.*;
import ru.shirokuma.mhc.util.LocalDateTimeAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by panov on 11.03.2015.
 */
public class Pressure {
    private final IntegerProperty sbp;
    private final IntegerProperty dbp;
    private final IntegerProperty pulse;
    private final ObjectProperty<LocalDateTime> timePoint;
    private Integer hashCode;

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
        this.timePoint = new SimpleObjectProperty<>(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Pressure)) {
            return false;
        }
        final Pressure p = (Pressure) obj;
        if (sbp.get() == p.sbp.get() && dbp.get() == p.dbp.get() && pulse.get() == p.pulse.get() &&
                Objects.equals(timePoint.get(), p.timePoint.get())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (hashCode == null) {
            hashCode = Objects.hash(sbp.get(), dbp.get(), pulse.get(), timePoint.get());
        }
        return hashCode;
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

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
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
