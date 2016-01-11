package by.balinasoft.faceanalyzer.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import by.balinasoft.faceanalyzer.constants.Constants;

public class GetImageInfoResponse {

    @SerializedName(Constants.CHECKSUM)
    private String checksum;

    @SerializedName(Constants.FACES)
    private List<Face> faces = new ArrayList<>();

    @SerializedName(Constants.RESPONSE_CODE)
    private String responseCode;

    @SerializedName(Constants.RESPONSE_STATUS)
    private String responseStatus;

    @SerializedName(Constants.UID)
    private String uid;

    public List<Face> getFaces() {
        return faces;
    }

}
