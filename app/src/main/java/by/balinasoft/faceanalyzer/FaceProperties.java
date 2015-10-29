package by.balinasoft.faceanalyzer;

public class FaceProperties {

    private double confidence;

    private String name;

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
