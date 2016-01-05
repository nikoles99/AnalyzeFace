package by.balinasoft.faceanalyzer.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;

public class PhotoFormatUtility {

    private static final String SECRET = "171e8465-f548-401d-b63b-caf0dc28df5f";

    private static final String API_KEY = "api_key";

    private static final String API_SECRET = "api_secret";

    private static final String DETECTION_FLAGS = "detection_flags";

    private static final String ORIGINAL_FILENAME = "original_filename";

    private static final String API = "d45fd466-51e2-4701-8da8-04351c872236";

    private static final String IMAGE_UID = "img_uid";

    private static final String IMAGE_BASE64 = "image_base64";


    public static JsonObject prepareJsonImageUid(String photo, String FLAG) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(API_KEY, API);
        jsonObject.addProperty(API_SECRET, SECRET);
        jsonObject.addProperty(DETECTION_FLAGS, FLAG);
        jsonObject.addProperty(IMAGE_BASE64, photo);
        jsonObject.addProperty(ORIGINAL_FILENAME, "sample1.jpg");
        return jsonObject;
    }

    public static JsonObject prepareJsonImageInfo(String imageUid) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(API_KEY, API);
        jsonObject.addProperty(API_SECRET, SECRET);
        jsonObject.addProperty(IMAGE_UID, imageUid);
        return jsonObject;
    }

    public static String bitmapToString(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] b = stream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}