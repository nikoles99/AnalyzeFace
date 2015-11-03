package by.balinasoft.faceanalyzer;

import java.io.Serializable;
import java.util.List;

public class Face implements Serializable {

    private List<FaceProperties> classifiersProperties;

    private List<FaceProperties> extendedProperties;

    public Face() {
    }

    public List<FaceProperties> getClassifiersProperties() {
        return classifiersProperties;
    }

    public List<FaceProperties> getExtendedProperties() {
        return extendedProperties;
    }

    public void setClassifiersProperties(List<FaceProperties> classifiersProperties) {
        this.classifiersProperties = classifiersProperties;
    }

    public void setExtendedProperties(List<FaceProperties> extendedProperties) {
        this.extendedProperties = extendedProperties;
    }
}
