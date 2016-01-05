package by.balinasoft.faceanalyzer.service;

import com.google.gson.JsonObject;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Url;


public interface MessageService {

    @POST
    Call<JsonObject> request(@Url String url, @Body JsonObject object);
}
