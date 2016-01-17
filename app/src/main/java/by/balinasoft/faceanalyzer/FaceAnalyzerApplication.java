package by.balinasoft.faceanalyzer;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.FacebookSdk;
import com.google.gson.JsonObject;
import com.vk.sdk.VKSdk;

import java.util.Locale;

import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.utils.FileReader;

public class FaceAnalyzerApplication extends Application {

    private SharedPreferences sharedPreferences;

    private static final String WORDS_TRANSLATION_FILE = "words-translation.json";
    private static final String ENGLISH = "EN";
    private static final String RUSSIAN = "RU";

    private static String eulaFile;

    private static JsonObject appLanguage;
    private static JsonObject mappingTable;

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(FaceAnalyzerApplication.this);
        FacebookSdk.sdkInitialize(FaceAnalyzerApplication.this);
        setApplicationLanguage();
        sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(FaceAnalyzerApplication.this);
    }

    private void setApplicationLanguage() {
        String deviceLanguage = Locale.getDefault().getDisplayLanguage();
        String mappingTableFile;
        JsonObject jsonObject = FileReader.
                loadJsonFile(getApplicationContext(), WORDS_TRANSLATION_FILE);

        switch (deviceLanguage) {
            case Constants.RUSSIAN_LANGUAGE:
                appLanguage = jsonObject.getAsJsonObject(RUSSIAN);
                eulaFile = Constants.RUSSIAN_TERMS_OF_USE;
                mappingTableFile = Constants.RUSSIAN_MAPPING_TABLE;
                break;
            default:
                appLanguage = jsonObject.getAsJsonObject(ENGLISH);
                eulaFile = Constants.ENGLISH_TERMS_OF_USE;
                mappingTableFile = Constants.ENGLISH_MAPPING_TABLE;
                break;
        }
        mappingTable = FileReader.loadJsonFile(getApplicationContext(), mappingTableFile);
    }


    public JsonObject getAppLanguage() {
        return appLanguage;
    }

    public JsonObject getMappingTable() {
        return mappingTable;
    }

    public String getEulaFile() {
        return eulaFile;
    }

    public void saveStatisticValue(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getStatisticValue(String key) {
        int defaultValue = 0;
        return sharedPreferences.getInt(key, defaultValue);
    }
}
