package by.balinasoft.faceanalyzer;

public class FaceProperties {

    private String confidence;
    private String name;
    private String value;

    public FaceProperties(String confidence, String name, String value) {
        this.confidence = confidence;
        this.name = name;
        this.value = value;
    }

    public String getConfidence() {
        return confidence;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
