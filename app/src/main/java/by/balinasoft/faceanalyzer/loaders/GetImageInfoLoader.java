package by.balinasoft.faceanalyzer.loaders;

import com.google.gson.JsonObject;

import by.balinasoft.faceanalyzer.model.GetImageInfoRequest;
import by.balinasoft.faceanalyzer.service.MessageService;
import retrofit.Call;

public class GetImageInfoLoader extends FaceAnalyzerLoader<GetImageInfoRequest>{

    private static final String URL = "GetImageInfo";

    @Override
    protected Call<JsonObject> getRequestParams(MessageService service, GetImageInfoRequest param) {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return service.getImageInfoRequest(URL, param);
    }
}
