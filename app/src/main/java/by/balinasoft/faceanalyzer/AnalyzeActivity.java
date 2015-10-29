package by.balinasoft.faceanalyzer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

public class AnalyzeActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAMERA = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    public static final String TYPE = "image/*";
    public static final String MAKE_PHOTO = "Please select image or make photo on camera";
    public static final String INVALID_IMAGE = "Invalid Image";

    private FaceAdapter faceAdapter;

    private ProgressBar progressBar;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera(view);
            }
        });

        ListView listView = (ListView) findViewById(R.id.listView);
        faceAdapter = new FaceAdapter(this, new ArrayList<Face>());
        listView.setAdapter(faceAdapter);

        imageView = (ImageView) findViewById(R.id.photo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void openCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAMERA);
        // Snackbar.make(view, "Open camera", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(TYPE);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.galery:
                openGallery();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bitmap image = null;

            switch (requestCode) {
                case REQUEST_IMAGE_CAMERA:
                    image = getImageFromCamera(data);
                    break;
                case REQUEST_IMAGE_GALLERY:
                    image = getImageFromGallery(data);
                    break;
            }
            imageView.setImageBitmap(image);
            analyze(image);
        }
    }

    private void analyze(Bitmap image) {
        if (image != null) {
            new PhotoExecutor().execute(image);
        } else {
            showMessage(MAKE_PHOTO);
        }
    }

    private Bitmap getImageFromGallery(Intent data) {
        Bitmap imageBitmap;
        try {
            Uri selectedImage = data.getData();
            imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
        } catch (IOException e) {
            showMessage(INVALID_IMAGE);
            throw new IllegalStateException(INVALID_IMAGE);
        }
        return imageBitmap;
    }

    private Bitmap getImageFromCamera(Intent data) {
        Bitmap imageBitmap;
        Bundle extras = data.getExtras();
        imageBitmap = (Bitmap) extras.get("data");
        return imageBitmap;
    }

    private void showMessage(String message) {
        Toast.makeText(progressBar.getContext().getApplicationContext(),
                message, Toast.LENGTH_SHORT).show();
    }

    public class PhotoExecutor extends AsyncTask<Bitmap, Void, JSONObject> {

        private static final String URL_REQUEST_UID = "http://www.betafaceapi.com/service.svc/" +
                "UploadNewImage_File";
        private static final String URL_REQUEST_IMG_INFO = "http://betafaceapi.com/service_json." +
                "svc/GetImageInfo";

        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Bitmap... bitmaps) {
            try {
                for (Bitmap bitmap : bitmaps) {
                    String imgUid = getPhotoUid(bitmap);
                    return getPhotoInfo(imgUid);
                }
            } catch (IOException e) {
                exception = e;
            }
            return null;
        }

        private JSONObject getPhotoInfo(String imgUid) throws IOException {
            JSONObject jsonObject = PhotoFormatUtility.prepareJsonImageInfo(imgUid);
            return new ApiConnector(URL_REQUEST_IMG_INFO).makeJsonRequest(jsonObject);
        }

        private String getPhotoUid(Bitmap bitmap) throws IOException {
            String base64Photo = PhotoFormatUtility.bitmapToString(bitmap);
            String xmlRequest = PhotoFormatUtility.toXml(base64Photo);
            InputStream response = new ApiConnector(URL_REQUEST_UID).makeXmlRequest(xmlRequest);
            return PhotoFormatUtility.parse(response);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            if (exception != null || jsonObject == null) {
                showMessage(exception.getMessage());
                return;
            }
            List<Face> list = PhotoFormatUtility.parse(jsonObject);
            faceAdapter.update(list);
            progressBar.setVisibility(View.GONE);
        }
    }
}