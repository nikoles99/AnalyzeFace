package by.balinasoft.faceanalyzer;

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

public class ApiConnector {

    private String url;

    public ApiConnector(String url) {
        this.url = url;
    }

    public JSONObject makeRequest(JSONObject requestJson) throws IOException, JSONException {
        URL url = new URL(this.url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        connection.connect();
        OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
        outputStream.write(requestJson.toString().getBytes());
        outputStream.flush();
        return getResponse(connection);
    }

    public JSONObject getResponse(HttpURLConnection connection) throws IOException, JSONException {
        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream, "UTF-8"));

        String inputStr;
        StringBuilder responseStrBuilder = new StringBuilder();

        while ((inputStr = bufferedReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }
        return new JSONObject(responseStrBuilder.toString());
    }
}
