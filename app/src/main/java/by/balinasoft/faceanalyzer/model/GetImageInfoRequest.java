package by.balinasoft.faceanalyzer.model;

import com.google.gson.annotations.SerializedName;

import by.balinasoft.faceanalyzer.constants.Constants;

public class GetImageInfoRequest {

    @SerializedName(Constants.API_KEY)
    private String apiKey = Constants.API;

    @SerializedName(Constants.API_SECRET)
    private String secret = Constants.SECRET;

    @SerializedName(Constants.IMAGE_UID)
    private String imageUid;

    public void setImageUid(String imageUid) {
        this.imageUid = imageUid;
    }
}
