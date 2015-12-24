package by.balinasoft.faceanalyzer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import by.balinasoft.faceanalyzer.constants.Constants;

public class FaceProperties implements Serializable {

    @SerializedName(Constants.FACE_PROPERTIES_CONFIDENCE)
    private double confidence;

    @SerializedName(Constants.FACE_PROPERTIES_NAME)
    private String name;

    @SerializedName(Constants.FACE_PROPERTIES_VALUE)
    private String value;

    public FaceProperties(double confidence, String name, String value) {
        this.confidence = confidence;
        this.name = name;
        this.value = value;
    }

    public double getConfidence() {
        return confidence;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
