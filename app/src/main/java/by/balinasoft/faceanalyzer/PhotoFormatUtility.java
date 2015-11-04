package by.balinasoft.faceanalyzer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    public static JSONObject toJson(byte[] photo) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(API_SECRET, SECRET);
            jsonObject.put(DETECTION_FLAGS, FLAG);
            jsonObject.put(IMAGE_FILE_DATA, toJsonArray(photo));
            jsonObject.put(ORIGINAL_FILENAME, ORIGINAL_FILENAME);
            jsonObject.put(API_KEY, API);
            return jsonObject;
        } catch (JSONException e) {
            throw new IllegalArgumentException(String.format("Invalid byte[] format %s", photo), e);
        }
    }

    private static JSONArray toJsonArray(byte[] params) {
        JSONArray jsonArray = new JSONArray();

        for (byte param : params) {
            jsonArray.put((int) param);
        }
        return jsonArray;
    }

    public static JSONObject prepareJsonImageInfo(String imageUid) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(API_KEY, API);
            jsonObject.put(API_SECRET, SECRET);
            jsonObject.put(IMAGE_UID, imageUid);
            return jsonObject;
        } catch (JSONException e) {
            throw new IllegalArgumentException(String.format("Invalid String format %s",
                    imageUid), e);
        }
    }

    public static List<Face> parse(JSONObject jsonObject) {
        try {
            JSONArray faceProperties = jsonObject.getJSONArray(TAG_FACES);
            return getFace(faceProperties);
        } catch (JSONException e) {
            throw new IllegalArgumentException(String.format("Invalid JSONArray format %s",
                    jsonObject), e);
        }
    }

    private static List<Face> getFace(JSONArray jsonProperties) throws JSONException {
        List<Face> facesList = new ArrayList<>();

        for (int i = 0; i < jsonProperties.length(); i++) {
            Face face = new Face();

            List<FaceProperties> faceProperties = getFaceProperties(jsonProperties.
                    getJSONObject(i));
            face.setFaceProperties(faceProperties);

            facesList.add(face);
        }
        return facesList;
    }

    private static List<FaceProperties> getFaceProperties(JSONObject face) throws JSONException {
        JSONArray tags = face.getJSONArray(TAG_TAGS);
        List<FaceProperties> faceProperties = new ArrayList<>();

        for (int j = 0; j < tags.length(); j++) {
            String confidence = tags.getJSONObject(j).getString(TAG_CONFIDENCE);
            String name = tags.getJSONObject(j).getString(TAG_NAME);
            String value = tags.getJSONObject(j).getString(TAG_VALUE);
            faceProperties.add(new FaceProperties(Double.parseDouble(confidence), name, value));
        }
        return faceProperties;
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


    public static String base64Encode(byte[] in) {
        StringBuffer out = new StringBuffer((in.length * 4) / 3);
        int b;
        for (int i = 0; i < in.length; i += 3) {
            b = (in[i] & 0xFC) >> 2;
            out.append(CODES.charAt(b));
            b = (in[i] & 0x03) << 4;
            if (i + 1 < in.length) {
                b |= (in[i + 1] & 0xF0) >> 4;
                out.append(CODES.charAt(b));
                b = (in[i + 1] & 0x0F) << 2;
                if (i + 2 < in.length) {
                    b |= (in[i + 2] & 0xC0) >> 6;
                    out.append(CODES.charAt(b));
                    b = in[i + 2] & 0x3F;
                    out.append(CODES.charAt(b));
                } else {
                    out.append(CODES.charAt(b));
                    out.append('=');
                }
            } else {
                out.append(CODES.charAt(b));
                out.append("==");
            }
        }
        return out.toString();
    }

    public static String bitmapToString(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] b = stream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap stringToBitmap(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static boolean checkJson(JSONObject jsonObject) throws IOException {
        try {
            String message = jsonObject.getString(STRING_RESPONSE);
            if (message.equals(OK)) {
                return true;
            } else {
                throw new IOException(message + ", please try again");
            }
        } catch (JSONException e) {
            throw new IllegalArgumentException(String.format("Invalid " +
                    "JSONObject format %s", jsonObject), e);
        }
    }
}