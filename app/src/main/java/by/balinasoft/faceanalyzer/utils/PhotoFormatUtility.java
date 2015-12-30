package by.balinasoft.faceanalyzer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Xml;

import com.google.gson.JsonObject;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import by.balinasoft.faceanalyzer.model.FaceProperties;
import by.balinasoft.faceanalyzer.model.Face;

public class PhotoFormatUtility {

    private static final String SECRET = "171e8465-f548-401d-b63b-caf0dc28df5f";

    private static final String API_KEY = "api_key";

    private static final String API_SECRET = "api_secret";

    private static final String DETECTION_FLAGS = "detection_flags";

    private static final String IMAGE_FILE_DATA = "imagefile_data";

    private static final String ORIGINAL_FILENAME = "original_filename";

    private static final String API = "d45fd466-51e2-4701-8da8-04351c872236";

    private static final String FLAG = "extended";

    private static final String IMAGE_UID = "img_uid";

    private static final String TAG_TAGS = "tags";

    private static final String TAG_CONFIDENCE = "confidence";

    private static final String TAG_NAME = "name";

    private static final String TAG_VALUE = "value";

    private static final String TAG_FACES = "faces";

    private static final String STRING_RESPONSE = "string_response";

    private static final String OK = "ok";

    private static final String CODES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" +
            "0123456789+/=";

    private static final int CLASSIFIERS = 0;

    private static final int EXTENDED = 1;


    public static String toXml(String photo, String FLAG) {
        return "<ImageRequestBinary xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "<api_key>" + API + "</api_key>\n" +
                "<api_secret>" + SECRET + "</api_secret>\n" +
                "<detection_flags>" + FLAG + "</detection_flags>\n" +
                "<imagefile_data>\n" + photo + "</imagefile_data>\n" +
                "<original_filename>sample1.jpg</original_filename>\n" +
                "</ImageRequestBinary>";
    }

    public static JsonObject toJson(String photo, String FLAG) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(API_KEY, API);
        jsonObject.addProperty(API_SECRET, SECRET);
        jsonObject.addProperty(DETECTION_FLAGS, FLAG);
        jsonObject.addProperty(IMAGE_FILE_DATA, photo);
        jsonObject.addProperty(ORIGINAL_FILENAME, "sample1.jpg");
        return jsonObject;
    }

    private static final String codes = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    public static byte[] base64Decode(String input)    {
        if (input.length() % 4 != 0)    {
            throw new IllegalArgumentException("Invalid base64 input");
        }
        byte decoded[] = new byte[((input.length() * 3) / 4) - (input.indexOf('=') > 0 ? (input.length() - input.indexOf('=')) : 0)];
        char[] inChars = input.toCharArray();
        int j = 0;
        int b[] = new int[4];
        for (int i = 0; i < inChars.length; i += 4)     {
            // This could be made faster (but more complicated) by precomputing these index locations
            b[0] = codes.indexOf(inChars[i]);
            b[1] = codes.indexOf(inChars[i + 1]);
            b[2] = codes.indexOf(inChars[i + 2]);
            b[3] = codes.indexOf(inChars[i + 3]);
            decoded[j++] = (byte) ((b[0] << 2) | (b[1] >> 4));
            if (b[2] < 64)      {
                decoded[j++] = (byte) ((b[1] << 4) | (b[2] >> 2));
                if (b[3] < 64)  {
                    decoded[j++] = (byte) ((b[2] << 6) | b[3]);
                }
            }
        }

        return decoded;
    }

    public static JsonObject prepareJsonImageInfo(String imageUid) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(API_KEY, API);
        jsonObject.addProperty(API_SECRET, SECRET);
        jsonObject.addProperty(IMAGE_UID, imageUid);
        return jsonObject;
    }


    public static String parse(InputStream xml) throws IOException {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(xml, null);
            return getImgUid(xpp);
        } catch (XmlPullParserException e) {
            throw new IllegalArgumentException(String.format("Invalid InputStream format %s",
                    xml), e);
        }
    }

    private static String getImgUid(XmlPullParser xpp) throws XmlPullParserException, IOException {
        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (xpp.getEventType()) {
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals(IMAGE_UID)) {
                        return xpp.nextText();
                    }
                    break;
            }
            xpp.next();
        }
        throw new XmlPullParserException("Invalid IMG_UID");
    }


    public static String bitmapToString(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] b = stream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}