package by.balinasoft.faceanalyzer;

import java.util.List;

public class Face {

    private List<FaceProperties> faceProperties;

    public Face(List<FaceProperties> faceProperties) {
        this.faceProperties = faceProperties;
    }

    public List<FaceProperties> getFaceProperties() {
        return faceProperties;
    }
}
