package ru.shirokuma.mhc.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by shirokuma on 04.04.2015.
 */
@XmlRootElement(name = "pressure-data")
public class PressureListWrapper {
    private List<Pressure> pressureList;

    @XmlElement(name = "pressure")
    public List<Pressure> getPressureList() {
        return pressureList;
    }

    public void setPressureList(List<Pressure> pressureList) {
        this.pressureList = pressureList;
    }
}
