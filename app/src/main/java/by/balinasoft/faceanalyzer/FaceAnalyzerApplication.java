package by.balinasoft.faceanalyzer;

import android.app.Application;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import by.balinasoft.faceanalyzer.constants.Constants;

public class FaceAnalyzerApplication extends Application {

    private static final String WORDS_TRANSLATION_FILE = "words-translation.json";

    @Override
    public void onCreate() {
        super.onCreate();
        setApplicationLanguage();
    }

    private void setApplicationLanguage() {
        String deviceLanguage = Locale.getDefault().getDisplayLanguage();

        JsonObject jsonObject = loadJsonFile(WORDS_TRANSLATION_FILE);
/*
        switch (deviceLanguage) {
            case Constants.RUSSIAN_LANGUAGE:
                languageFileName = RUSSIAN_TABLE_FILE;
                break;
            default:
                languageFileName = ENGLISH_TABLE_FILE;
                break;
        }*/
        String d = jsonObject.toString();
    }


    private JsonObject loadJsonFile(String fileName) {
        JsonObject jsonObject = new JsonObject();

        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new String(buffer, "UTF-8"));
            jsonObject = jsonElement.getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }
}
