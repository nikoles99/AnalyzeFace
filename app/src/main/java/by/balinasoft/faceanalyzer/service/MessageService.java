package by.balinasoft.faceanalyzer.service;

import android.util.Xml;

import com.google.gson.JsonObject;

import org.w3c.dom.Document;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Url;


public interface MessageService {

    @POST
    Call<JsonObject> request(@Url String url, @Body JsonObject object);

    @POST
    Call<JsonObject> request(@Url String url, @Body Document s);

}
