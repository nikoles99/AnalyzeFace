package by.balinasoft.faceanalyzer.loaders;

import com.google.gson.JsonObject;

import by.balinasoft.faceanalyzer.api.ApiFactory;
import by.balinasoft.faceanalyzer.service.MessageService;
import by.balinasoft.faceanalyzer.utils.ServerObserver;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

abstract public class FaceAnalyzerLoader<T> {

    private ServerObserver serverObserver;

    abstract protected Call<JsonObject> getRequestParams(MessageService service, T param);

    public void setServerObserver(ServerObserver serverObserver) {
        this.serverObserver = serverObserver;
    }

    public void makeRequest(T param) {
        MessageService service = ApiFactory.getMessageService();
        Call<JsonObject> call = getRequestParams(service, param);
        Callback<JsonObject> jsonObjectCallback = new Callback<JsonObject>() {
            @Override
            public void onResponse(Response<JsonObject> response, Retrofit retrofit) {
                handleResponse(response);
            }

            @Override
            public void onFailure(Throwable throwable) {
                serverObserver.failedExecute(throwable.getMessage());
            }
        };
        call.enqueue(jsonObjectCallback);
    }

    private void handleResponse(Response<JsonObject> response) {
        if (response.isSuccess()) {
            serverObserver.successExecute(response.body());
        } else {
            serverObserver.failedExecute(response.message());
        }
    }
}
