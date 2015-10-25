package by.balinasoft.faceanalyzer;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
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
                InputStream inputStream = new ApiConnector("http://www.betafaceapi.com/service.svc/UploadNewImage_File").makeRequestXml(JsonFormatUtility.toXml(base64));
                String imgUid = JsonFormatUtility.getImgUid(inputStream);
                return new ApiConnector("http://betafaceapi.com/service_json.svc/GetImageInfo").makeRequest(JsonFormatUtility.prepareJsonImageInfo(imgUid));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
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
