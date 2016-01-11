package by.balinasoft.faceanalyzer.loaders;

import com.google.gson.JsonObject;

import by.balinasoft.faceanalyzer.service.MessageService;
import retrofit.Call;

public class GetImageInfoLoader extends FaceAnalyzerLoader<JsonObject>{

    private static final String URL = "GetImageInfo";

    @Override
    protected Call<JsonObject> getRequestParams(MessageService service, JsonObject param) {
        return service.request(URL, param);
    }
}
