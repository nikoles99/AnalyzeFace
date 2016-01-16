package by.balinasoft.faceanalyzer.utils;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;

public class FileReader {

    private static final String ENCODING = "UTF-8";

    public static String loadFile(Context context, String fileName) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer, ENCODING);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Invalid String format %s", fileName), e);
        }
    }

    public static JsonObject loadJsonFile(Context context, String fileName) {
        String jsonString = loadFile(context, fileName);
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonString);
        return jsonElement.getAsJsonObject();
    }
}
