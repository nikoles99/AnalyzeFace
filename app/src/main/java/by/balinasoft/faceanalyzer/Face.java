package by.balinasoft.faceanalyzer;

import java.io.Serializable;
import java.util.List;

public class Face implements Serializable {

    private List<FaceProperties> faceProperties;

    public Face() {
    }

    public List<FaceProperties> getFaceProperties() {
        return faceProperties;
    }

    public void setFaceProperties(List<FaceProperties> faceProperties) {
        this.faceProperties = faceProperties;
    }

}
