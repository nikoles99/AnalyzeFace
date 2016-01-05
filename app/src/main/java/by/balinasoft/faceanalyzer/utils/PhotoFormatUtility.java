package by.balinasoft.faceanalyzer.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;

import by.balinasoft.faceanalyzer.constants.Constants;

public class PhotoFormatUtility {

    private static final String NAME = "sample1.jpg";

    private static final int VALUE = 100;

    public static JsonObject prepareJsonImageUid(String photo, String FLAG) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constants.API_KEY, Constants.API);
        jsonObject.addProperty(Constants.API_SECRET, Constants.SECRET);
        jsonObject.addProperty(Constants.DETECTION_FLAGS, FLAG);
        jsonObject.addProperty(Constants.IMAGE_BASE64, photo);
        jsonObject.addProperty(Constants.ORIGINAL_FILENAME, NAME);
        return jsonObject;
    }

    public static JsonObject prepareJsonImageInfo(String imageUid) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constants.API_KEY, Constants.API);
        jsonObject.addProperty(Constants.API_SECRET, Constants.SECRET);
        jsonObject.addProperty(Constants.IMAGE_UID, imageUid);
        return jsonObject;
    }

    public static String bitmapToString(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, VALUE, stream);
        byte[] b = stream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}