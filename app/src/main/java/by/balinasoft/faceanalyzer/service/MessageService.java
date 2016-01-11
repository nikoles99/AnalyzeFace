package by.balinasoft.faceanalyzer.service;

import com.google.gson.JsonObject;

import by.balinasoft.faceanalyzer.model.GetImageInfoRequest;
import by.balinasoft.faceanalyzer.model.UploadImageRequest;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Url;


public interface MessageService {

    @POST
    Call<JsonObject> uploadImageRequest(@Url String url, @Body UploadImageRequest request);

    @POST
    Call<JsonObject> getImageInfoRequest(@Url String url, @Body GetImageInfoRequest request);
}
