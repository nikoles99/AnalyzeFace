package by.balinasoft.faceanalyzer.loaders;

import com.google.gson.JsonObject;

import by.balinasoft.faceanalyzer.model.UploadImageRequest;
import by.balinasoft.faceanalyzer.service.MessageService;
import retrofit.Call;

public class UploadImageLoader extends FaceAnalyzerLoader<UploadImageRequest> {

    private static final String URL = "UploadImage";

    @Override
    protected Call<JsonObject> getRequestParams(MessageService service, UploadImageRequest param) {
        return service.uploadImageRequest(URL, param);
    }
}
