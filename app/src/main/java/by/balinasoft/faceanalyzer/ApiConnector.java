package by.balinasoft.faceanalyzer;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiConnector {

    public static final String TYPE_REQUEST = "POST";

    public static final int TIMEOUT_MILLIS = 100000;

    private String url;

    public ApiConnector(String url) {
        this.url = url;
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

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return connection.getInputStream();
        } else {
            throw new IOException("Error code: " + connection.getResponseCode());
        }
    }
}
