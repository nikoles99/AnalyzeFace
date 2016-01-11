package by.balinasoft.faceanalyzer.model;

import com.google.gson.annotations.SerializedName;

import by.balinasoft.faceanalyzer.constants.Constants;

public class UploadImageRequest {

    @SerializedName(Constants.API_KEY)
    private String apiKey = Constants.API;

    @SerializedName(Constants.API_SECRET)
    private String secret = Constants.SECRET;

    @SerializedName(Constants.DETECTION_FLAGS)
    private String detectionFlag;

    @SerializedName(Constants.IMAGE_BASE64)
    private String imageBase64;

    @SerializedName(Constants.ORIGINAL_FILENAME)
    private String fileName = "sample1.jpg";

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public void setDetectionFlag(String detectionFlag) {
        this.detectionFlag = detectionFlag;
    }
}
