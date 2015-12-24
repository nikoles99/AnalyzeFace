package by.balinasoft.faceanalyzer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import by.balinasoft.faceanalyzer.constants.Constants;

public class Face implements Serializable {

    @SerializedName(Constants.FACE_PROPERTIES)
    private List<FaceProperties> faceProperties;

    public List<FaceProperties> getFaceProperties() {
        return faceProperties;
    }

    public void setFaceProperties(List<FaceProperties> faceProperties) {
        this.faceProperties = faceProperties;
    }

}
