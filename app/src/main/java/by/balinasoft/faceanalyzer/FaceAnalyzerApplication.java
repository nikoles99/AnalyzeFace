package by.balinasoft.faceanalyzer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.JsonObject;

import java.util.Locale;

import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.utils.FileReader;

public class FaceAnalyzerApplication extends Application {

    private SharedPreferences sharedPreferences;

    private static final String WORDS_TRANSLATION_FILE = "words-translation.json";
    private static final String ENGLISH = "EN";
    private static final String RUSSIAN = "RU";

    private static String eula;

    private static JsonObject appLanguage;
    private static JsonObject mappingTable;
    private int defaultValue = 0;

    @Override
    public void onCreate() {
        super.onCreate();
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
                eula = Constants.RUSSIAN_TERMS_OF_USE;
                mappingTableFile = Constants.RUSSIAN_MAPPING_TABLE;
                break;
            default:
                appLanguage = jsonObject.getAsJsonObject(ENGLISH);
                eula = Constants.ENGLISH_TERMS_OF_USE;
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

    public String getEula() {
        return eula;
    }

    public void saveStatisticValue(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getStatisticValue(String key) {
        return sharedPreferences.getInt(key, defaultValue);
    }
}
