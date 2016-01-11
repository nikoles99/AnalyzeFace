package by.balinasoft.faceanalyzer.model;

import com.google.gson.annotations.SerializedName;

import by.balinasoft.faceanalyzer.constants.Constants;

public class UploadImageResponse {

    @SerializedName(Constants.IMAGE_UID)
    private String imageUid;

    @SerializedName(Constants.RESPONSE_CODE)
    private String responseCode;

    @SerializedName(Constants.RESPONSE_STATUS)
    private String responseStatus;

    public String getImageUid() {
        return imageUid;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getResponseStatus() {
        return responseStatus;
    }
}
