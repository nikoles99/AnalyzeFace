package by.balinasoft.faceanalyzer.api;

import android.support.annotation.NonNull;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.service.MessageService;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class ApiFactory {

    private static final int CONNECT_TIMEOUT = 60000;
    private static final int WRITE_TIMEOUT = 60000;
    private static final int TIMEOUT = 60000;

    private static final OkHttpClient CLIENT = new OkHttpClient();

    static {
        CLIENT.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        CLIENT.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        CLIENT.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);
    }

    @NonNull
    public static MessageService getMessageService() {
        return getRetrofit().create(MessageService.class);
    }

    @NonNull
    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(CLIENT)
                .build();
    }
}