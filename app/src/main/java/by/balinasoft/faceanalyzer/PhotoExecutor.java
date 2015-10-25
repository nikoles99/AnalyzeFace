package by.balinasoft.faceanalyzer;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class PhotoExecutor extends AsyncTask<Bitmap, Void, JSONObject> {

    private FaceAdapter faceAdapter;
    private ProgressBar progressBar;

    public PhotoExecutor(FaceAdapter faceAdapter, ProgressBar progressBar) {
        this.faceAdapter = faceAdapter;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected JSONObject doInBackground(Bitmap... bitmaps) {
        try {
            for (Bitmap bitmap : bitmaps) {
                String base64 = PhotoFormatUtility.bitmatToString(bitmap);
                byte[] photo =base64.getBytes();
                JSONObject jsonObject = new ApiConnector("http://www.betafaceapi.com/service_json.svc/UploadNewImage_File").makeRequest(JsonFormatUtility.toJson(photo, "sample1.jpg"));
                String imgUid = (String) jsonObject.get("img_uid");
                //imgUid = "ec43984f-0e81-4a85-866f-31f605d1f3be";
                return new ApiConnector("http://betafaceapi.com/service_json.svc/GetImageInfo").makeRequest(JsonFormatUtility.prepareJsonImageInfo(imgUid));
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
        try {
            List<Face> list = JsonFormatUtility.toАppearance(jsonObject);
            faceAdapter.update(list);
            progressBar.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
