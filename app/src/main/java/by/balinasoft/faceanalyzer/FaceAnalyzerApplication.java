package by.balinasoft.faceanalyzer;

import android.app.Application;

import com.google.gson.JsonObject;

import java.util.Locale;

import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.utils.FileReader;

public class FaceAnalyzerApplication extends Application {

    private static final String WORDS_TRANSLATION_FILE = "words-translation.json";
    private static final String ENGLISH = "EN";
    private static final String RUSSIAN = "RU";

    private static String mappingTableFile;
    private static String eula;

    private static JsonObject appLanguage;

    @Override
    public void onCreate() {
        super.onCreate();
        setApplicationLanguage();
    }

    private void setApplicationLanguage() {
        String deviceLanguage = Locale.getDefault().getDisplayLanguage();
        JsonObject jsonObject =  FileReader.
                loadJsonFile(getApplicationContext(), WORDS_TRANSLATION_FILE);

        switch (deviceLanguage) {
            case Constants.RUSSIAN_LANGUAGE:
                appLanguage = jsonObject.getAsJsonObject(RUSSIAN);
                eula = Constants.RUSSIAN_TERMS_OF_USE;
                mappingTableFile = Constants.RUSSIAN_MAPPING_TABLE;
                break;
            default:
                appLanguage = jsonObject.getAsJsonObject(ENGLISH);
                eula = Constants.ENGLISH_TERMS_OF_USE;
                mappingTableFile = Constants.ENGLISH_MAPPING_TABLE;
                break;
        }
    }


    public static JsonObject getAppLanguage() {
        return appLanguage;
    }

    public static String getMappingTableFile() {
        return mappingTableFile;
    }

    public static String getEula() {
        return eula;
    }
}
