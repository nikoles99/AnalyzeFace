package by.balinasoft.faceanalyzer.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import by.balinasoft.faceanalyzer.constants.Constants;

public class PhotoFormatUtility {

    private static final int QUALITY_VALUE = 100;

    private static final String IMAGE_FILE_NAME = "Face.jpg";

    public static String bitmapToString(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, QUALITY_VALUE, stream);
        byte[] b = stream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String savePhotoInStorage(Bitmap bitmapImage, Context context) {
        try {
            ContextWrapper contextWrapper = new ContextWrapper(context.getApplicationContext());
            File directory = contextWrapper.getDir(Constants.APP_TITLE, Context.MODE_PRIVATE);
            File image = new File(directory, IMAGE_FILE_NAME);
            FileOutputStream outputStream = new FileOutputStream(image);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, QUALITY_VALUE, outputStream);
            outputStream.close();
            return directory.getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Bitmap loadImageFromStorage(String path) {
        try {
            File file = new File(path, IMAGE_FILE_NAME);
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}