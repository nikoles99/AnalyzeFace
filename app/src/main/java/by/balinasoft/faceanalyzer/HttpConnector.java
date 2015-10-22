package by.balinasoft.faceanalyzer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnector {

    public static JSONObject makeRequest() throws IOException, JSONException {
        byte[] bArr = new byte[]{81, 109, 70, 122, 90, 83, 65, 50, 78, 67, 66, 84, 100, 72, 74, 108, 89, 87, 48, 61};
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("api_key", "d45fd466-51e2-4701-8da8-04351c872236");
        jsonObject.put("api_secret", "171e8465-f548-401d-b63b-caf0dc28df5f");
        jsonObject.put("detection_flags", "27");

        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<bArr.length;i++) {
            jsonArray.put((int)bArr[i]);
        }
        jsonObject.put("imagefile_data", jsonArray);
        //jsonObject.put("image_url", "http://www.betafaceapi.com/api_examples/sample1.jpg");
        jsonObject.put("original_filename", "sample1.jpg");
        String message = jsonObject.toString();

        URL url = new URL("http://www.betafaceapi.com/service_json.svc/UploadNewImage_File");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setFixedLengthStreamingMode(message.getBytes().length);
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        conn.connect();
        OutputStream os = new BufferedOutputStream(conn.getOutputStream());
        os.write(message.getBytes());
        os.flush();

        InputStream inputStream = conn.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = bufferedReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }
        return new JSONObject(responseStrBuilder.toString());
    }

}
