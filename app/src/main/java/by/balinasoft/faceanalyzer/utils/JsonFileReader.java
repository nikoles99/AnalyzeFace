package by.balinasoft.faceanalyzer.utils;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;

public class JsonFileReader {

    private static final String ENCODING = "UTF-8";

    public static JsonObject loadJsonFile(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return stringToJson(new String(buffer, ENCODING));
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Invalid Task format %s", fileName), e);
        }
    }

    private static JsonObject stringToJson(String json) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(json);
        return jsonElement.getAsJsonObject();
    }
}
