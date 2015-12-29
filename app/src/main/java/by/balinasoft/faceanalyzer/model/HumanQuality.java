package by.balinasoft.faceanalyzer.model;

import java.io.Serializable;

public class HumanQuality implements Serializable{

    private String type;

    private double accuracy;

    private String value;

    public HumanQuality(String type, String value, double accuracy) {
        this.type = type;
        this.accuracy = accuracy;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public String getValue() {
        return value;
    }
}
