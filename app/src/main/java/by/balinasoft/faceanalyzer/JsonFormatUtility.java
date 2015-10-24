package by.balinasoft.faceanalyzer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonFormatUtility {

    public static final String SECRET = "171e8465-f548-401d-b63b-caf0dc28df5f";
    private static final String API_KEY = "api_key";
    public static final String API_SECRET = "api_secret";
    public static final String DETECTION_FLAGS = "detection_flags";
    public static final String IMAGE_FILE_DATA = "imagefile_data";
    public static final String ORIGINAL_FILENAME = "original_filename";
    public static final String API = "d45fd466-51e2-4701-8da8-04351c872236";
    public static final String FLAG = "27";

    public static JSONObject toJson(byte[] photo, String namePhoto) throws JSONException {
        byte[] bytes = new byte[]{81, 109, 70, 122, 90, 83, 65, 50, 78, 67, 66, 84, 100, 72, 74, 108, 89, 87, 48, 61};
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(API_KEY, API);
        jsonObject.put(API_SECRET, SECRET);
        jsonObject.put(DETECTION_FLAGS, FLAG);
        jsonObject.put(IMAGE_FILE_DATA, toJsonArray(photo));
        jsonObject.put(ORIGINAL_FILENAME, namePhoto);
        return jsonObject;
    }

    private static JSONArray toJsonArray(byte[] param) {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < param.length; i++) {
            jsonArray.put((int) param[i]);
        }
        return jsonArray;
    }
}
