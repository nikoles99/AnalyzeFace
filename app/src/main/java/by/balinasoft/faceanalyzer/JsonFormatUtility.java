package by.balinasoft.faceanalyzer;

import android.util.Xml;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonFormatUtility {

    public static final String SECRET = "171e8465-f548-401d-b63b-caf0dc28df5f";
    private static final String API_KEY = "api_key";
    public static final String API_SECRET = "api_secret";
    public static final String DETECTION_FLAGS = "detection_flags";
    public static final String IMAGE_FILE_DATA = "imagefile_data";
    public static final String ORIGINAL_FILENAME = "original_filename";
    public static final String API = "d45fd466-51e2-4701-8da8-04351c872236";
    public static final String FLAG = "27";
    private static final String IMAGE_UID = "img_uid";

    public static JSONObject toJson(byte[] photo, String namePhoto) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(API_KEY, API);
        jsonObject.put(API_SECRET, SECRET);
        jsonObject.put(DETECTION_FLAGS, FLAG);
        jsonObject.put(IMAGE_FILE_DATA, toJsonArray(photo));
        jsonObject.put(ORIGINAL_FILENAME, namePhoto);
        return jsonObject;
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

    public static JSONObject prepareJsonImageInfo(String imageUid) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(API_KEY, API);
        jsonObject.put(API_SECRET, SECRET);
        jsonObject.put(IMAGE_UID, imageUid);
        return jsonObject;
    }

    public static List<Face> toАppearance(JSONObject jsonObject) throws JSONException {
        List<Face> faces = new ArrayList<>();
        JSONArray tags = jsonObject.getJSONArray("faces");

        for (int i = 0; i < tags.length(); i++) {
            JSONObject tagArrays = tags.getJSONObject(i);
            JSONArray jsonArray = tagArrays.getJSONArray("tags");

            List<FaceProperties> faceProperties = new ArrayList<>();

            for (int j = 0; j < jsonArray.length(); j++) {
                String confidence = jsonArray.getJSONObject(j).getString("confidence");
                String name = jsonArray.getJSONObject(j).getString("name");
                String value = jsonArray.getJSONObject(j).getString("value");
                faceProperties.add(new FaceProperties(confidence, name, value));
            }
            faces.add(new Face(faceProperties));
        }
        return faces;
    }

    public static String getImgUid(InputStream xml) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(xml, null);

        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (xpp.getEventType()) {
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals("img_uid")) {
                        return xpp.nextText();
                    }
                    break;
            }
            xpp.next();
        }
        return null;
    }
}
