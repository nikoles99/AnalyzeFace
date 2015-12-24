package by.balinasoft.faceanalyzer.loaders;

import android.util.Xml;

import com.google.gson.JsonObject;

import by.balinasoft.faceanalyzer.service.MessageService;
import retrofit.Call;

public class PhotoUidLoader extends FaceAnalyzerLoader<String> {

    private static final String URL = "service.svc/UploadNewImage_File";

    @Override
    protected Call<JsonObject> getRequestParams(MessageService service, String param) {
        return service.request(URL, param);
    }
}
