package by.balinasoft.faceanalyzer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import by.balinasoft.faceanalyzer.constants.Constants;

public class Face implements Serializable {

    @SerializedName(Constants.FACE_PROPERTIES)
    private List<FaceProperties> faceProperties;

    private List<FaceProperties> qualitiesCharacter = new ArrayList<>();
    private List<FaceProperties> qualitiesRelationship = new ArrayList<>();

    public List<FaceProperties> getFaceProperties() {
        return faceProperties;
    }

    public void addProperty(FaceProperties property) {
        if (faceProperties == null) {
            faceProperties = new ArrayList<>();
        } else {
            faceProperties.add(property);
        }
    }

    public List<FaceProperties> getQualitiesList(String quality) {
        switch (quality) {
            case Constants.CHARACTER:
                return getFaceProperties(quality, qualitiesCharacter);
            case Constants.RALATIONSHIP:
                return getFaceProperties(quality, qualitiesRelationship);
            default:
                return null;
        }
    }

    private List<FaceProperties> getFaceProperties(String quality, List<FaceProperties> list) {
        if (list.isEmpty()) {
            for (FaceProperties property : faceProperties) {
                if (property.getName().equals(quality)) {
                    list.add(property);
                }
            }
        }
        return list;
    }
}
