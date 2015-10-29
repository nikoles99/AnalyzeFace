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

    public static final String TYPE_REQUEST = "POST";

    public static final int TIMEOUT_MILLIS = 10000;

    private String url;

    public ApiConnector(String url) {
        this.url = url;
    }

    public JSONObject makeJsonRequest(JSONObject requestJson) throws IOException {
        URL url = new URL(this.url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(TIMEOUT_MILLIS);
        connection.setConnectTimeout(TIMEOUT_MILLIS);
        connection.setRequestMethod(TYPE_REQUEST);
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    public JSONObject getResponse(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream, "UTF-8"));

        String inputStr;
        StringBuilder responseStrBuilder = new StringBuilder();

        while ((inputStr = bufferedReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }
        try {
            return new JSONObject(responseStrBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream makeXmlRequest(String xml) throws IOException {
        URL url = new URL(this.url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(TIMEOUT_MILLIS);
        connection.setConnectTimeout(TIMEOUT_MILLIS);
        connection.setRequestMethod(TYPE_REQUEST);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/xml;charset=utf-8");
        connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        connection.connect();
        OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
        outputStream.write(xml.getBytes());
        outputStream.flush();
        return connection.getInputStream();
    }
}