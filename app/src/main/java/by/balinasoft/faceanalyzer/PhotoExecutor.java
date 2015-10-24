package by.balinasoft.faceanalyzer;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PhotoExecutor extends AsyncTask<Bitmap, Void, JSONObject> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(Bitmap... bitmaps) {
        try {
            for (Bitmap bitmap : bitmaps) {
                String base64 = PhotoFormatUtility.bitmatToString(bitmap);
                byte[] photo =base64.getBytes();
                // = PhotoFormatUtility.base64Decode(base64);
                return new ApiConnector("http://www.betafaceapi.com/service_json.svc/UploadNewImage_File").makeRequest(JsonFormatUtility.toJson(photo,"sample1.jpg"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

    }
}
