package by.balinasoft.faceanalyzer.model;

import java.io.Serializable;

public class HumanQuality implements Serializable{

    private double accuracy;

    private String value;

    public HumanQuality(String value, double accuracy) {
        this.accuracy = accuracy;
        this.value = value;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public String getValue() {
        return value;
    }
}
