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

    private static final String FLAG = "27";

    private static final String IMAGE_UID = "img_uid";

    private static final String CODES = "ABCDEFGHIJKLMNOPQRSTUVWXYZab" +
            "cdefghijklmnopqrstuvwxyz0123456789+/=";

    public static JSONObject toJson(byte[] photo) {
        JSONObject jsonObject = new JSONObject();
        try {
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

    public static String toXml(String photo) {
        return "<ImageRequestBinary xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "<api_key>" + API + "</api_key>\n" +
                "<api_secret>" + SECRET + "</api_secret>\n" +
                "<detection_flags>" + FLAG + "</detection_flags>\n" +
                "<imagefile_data>\n" + photo + "</imagefile_data>\n" +
                "<original_filename>sample1.jpg</original_filename>\n" +
                "</ImageRequestBinary>";
    }

    private static JSONArray toJsonArray(byte[] param) {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < param.length; i++) {
            jsonArray.put((int) param[i]);
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
            throw new IllegalArgumentException(String.format("Invalid String format %s", imageUid), e);
        }
    }

    public static List<FaceProperties> parse(JSONObject jsonObject) {
        try {
            List<Face> facesList = new ArrayList<>();
            JSONArray faces = jsonObject.getJSONArray("faces");

            for (int i = 0; i < faces.length(); i++) {
                JSONObject face = faces.getJSONObject(i);
                JSONArray tags = face.getJSONArray("tags");
                List<FaceProperties> faceProperties = getFaceProperties(tags);
                facesList.add(new Face(faceProperties));
            }
            return facesList.get(0).getFaceProperties();
        } catch (JSONException e) {
            throw new IllegalArgumentException(String.format("Invalid JSONObject format %s",
                    jsonObject), e);
        }
    }

    private static List<FaceProperties> getFaceProperties(JSONArray tags) throws JSONException {
        List<FaceProperties> faceProperties = new ArrayList<>();

        for (int j = 0; j < tags.length(); j++) {
            String confidence = tags.getJSONObject(j).getString("confidence");
            String name = tags.getJSONObject(j).getString("name");
            String value = tags.getJSONObject(j).getString("value");
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
            throw new IllegalArgumentException(String.format("Invalid InputStream format %s", xml), e);
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
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap stringToBitmap(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}