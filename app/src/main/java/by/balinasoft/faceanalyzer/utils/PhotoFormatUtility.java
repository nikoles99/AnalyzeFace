package by.balinasoft.faceanalyzer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;

import by.balinasoft.faceanalyzer.constants.Constants;

public class PhotoFormatUtility {

    private static final int VALUE = 100;

    public static String bitmapToString(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, VALUE, stream);
        byte[] b = stream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap stringToBitmap(String image_base64) {
        byte[] decodedByte = Base64.decode(image_base64, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}