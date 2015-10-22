package by.balinasoft.faceanalyzer;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ServerRequest {


    private String Url;



    public static void makeRequest() {
        int[] d ={81,109,70,122,90,83,65,50,78,67,66,84,100,72,74,108,89,87,48,61};
        try {
            Gson gson = new Gson();
            gson.toJson(d);
           String da = gson.toJson(d);
            URL url = new URL("http://www.betafaceapi.com/service_json.svc/UploadNewImage_Url");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("api_key", "d45fd466-51e2-4701-8da8-04351c872236");
            jsonObject.put("api_secret", "171e8465-f548-401d-b63b-caf0dc28df5f");
            jsonObject.put("detection_flags", "27");
            jsonObject.put("image_url", "http://www.betafaceapi.com/api_examples/sample1.jpg");
            jsonObject.put("original_filename", "sample1.jpg");
            String message = jsonObject.toString();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /*milliseconds*/);
            conn.setConnectTimeout(15000 /* milliseconds */);
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

            InputStream is = conn.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            new JSONObject(responseStrBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*
    private JSON doRequest(List<NameValuePair> nameValuePairs) throws IOException {
        HttpPost httpPost = new HttpPost(Url);
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);
        int httpStatus = response.getStatusLine().getStatusCode();

        if (httpStatus == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } else {
            throw new IOException(String.format("Http status unsuccessful %d expected '" +
                    HttpStatus.SC_OK + "'", httpStatus));
        }
    }*/

}
