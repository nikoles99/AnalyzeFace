package by.balinasoft.faceanalyzer;

import android.app.Application;

import com.google.gson.JsonObject;

import java.util.Locale;

import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.utils.JsonFileReader;

public class FaceAnalyzerApplication extends Application {

    private static final String WORDS_TRANSLATION_FILE = "words-translation.json";
    private static final String ENGLISH = "EN";
    private static final String RUSSIAN = "RU";

    private static JsonObject appLanguage;

    @Override
    public void onCreate() {
        super.onCreate();
        setApplicationLanguage();
    }

    public static JsonObject getAppLanguage() {
        return appLanguage;
    }

    private void setApplicationLanguage() {
        String deviceLanguage = Locale.getDefault().getDisplayLanguage();
        JsonObject jsonObject = JsonFileReader.
                loadJsonFile(getApplicationContext(), WORDS_TRANSLATION_FILE);

        switch (deviceLanguage) {
            case Constants.RUSSIAN_LANGUAGE:
                appLanguage = jsonObject.getAsJsonObject(RUSSIAN);
                break;
            default:
                appLanguage = jsonObject.getAsJsonObject(ENGLISH);
                break;
        }
    }
}
