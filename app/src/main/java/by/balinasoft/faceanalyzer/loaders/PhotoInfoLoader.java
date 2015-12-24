package by.balinasoft.faceanalyzer.loaders;

import com.google.gson.JsonObject;

import by.balinasoft.faceanalyzer.service.MessageService;
import retrofit.Call;

public class PhotoInfoLoader extends FaceAnalyzerLoader<JsonObject>{

    private static final String URL = "service_json.svc/GetImageInfo";

    @Override
    protected Call<JsonObject> getRequestParams(MessageService service, JsonObject param) {
        return service.request(URL, param);
    }
}
