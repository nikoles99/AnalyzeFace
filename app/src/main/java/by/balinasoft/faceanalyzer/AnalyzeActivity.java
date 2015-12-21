package by.balinasoft.faceanalyzer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONObject;

public class AnalyzeActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAMERA = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    private static final String TYPE = "image/*";
    private static final String MAKE_PHOTO = "Please select image or make photo on camera";
    private static final String INVALID_IMAGE = "Invalid Image";
    private static final String DATA = "data";

    private FaceAdapter faceAdapter;

    private ProgressBar progressBar;

    private ImageView openGallery;

    private Bitmap image;

    private int facePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        ImageView makePhoto = (ImageView)
                findViewById(R.id.makePhoto);
        makePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        openGallery = (ImageView) findViewById(R.id.loadFromGallery);
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

      /*  ListView listView = (ListView) findViewById(R.id.listView);
        faceAdapter = new FaceAdapter(this, new ArrayList<Face>());
        listView.setAdapter(faceAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                facePosition = position;
                analyze(image, "extended");
            }
        });*/
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAMERA);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(TYPE);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAMERA:
                    image = getImageFromCamera(data);
                    break;
                case REQUEST_IMAGE_GALLERY:
                    image = getImageFromGallery(data);
                    break;
            }
            openGallery.setImageBitmap(image);
            analyze(image, "");
        }
    }

    private void analyze(Bitmap image, String FLAG) {
        if (image != null) {
            new PhotoExecutor(FLAG).execute(image);
        } else {
            showMessage(MAKE_PHOTO);
        }
    }

    private Bitmap getImageFromGallery(Intent data) {
        try {
            Uri selectedImage = data.getData();
            return MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
        } catch (IOException e) {
            showMessage(INVALID_IMAGE);
            throw new IllegalStateException(INVALID_IMAGE);
        }
    }

    private Bitmap getImageFromCamera(Intent data) {
        Bundle extras = data.getExtras();
        return (Bitmap) extras.get(DATA);
    }

    private void showMessage(String message) {
        Snackbar.make(openGallery, message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    public class PhotoExecutor extends AsyncTask<Bitmap, Void, JSONObject> {

        private static final String URL_REQUEST_UID = "http://www.betafaceapi.com/service.svc/" +
                "UploadNewImage_File";
        private static final String URL_REQUEST_IMG_INFO = "http://betafaceapi.com/service_json." +
                "svc/GetImageInfo";

        private Exception exception;

        private String FLAG;

        public PhotoExecutor(String FLAG) {
            this.FLAG = FLAG;
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
                    return getFaceInfo(bitmap, FLAG);
                }
            } catch (IOException e) {
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            progressBar.setVisibility(View.GONE);

            if (exception == null || jsonObject != null) {
                List<Face> list = PhotoFormatUtility.parse(jsonObject);

                if (FLAG.isEmpty()) {
                    faceAdapter.update(list);
                    checkFaces(list);
                } else {
                    Intent intent = new Intent(AnalyzeActivity.this, MoreInfoActivity.class);
                    intent.putExtra(MoreInfoActivity.Face, list.get(facePosition));
                    startActivity(intent);
                }
            } else {
                showMessage(exception.getMessage());
            }
        }

        private JSONObject getFaceInfo(Bitmap bitmap, String flag) throws IOException {
            String photoUid = getPhotoUid(bitmap, flag);
            final JSONObject face = getPhotoInfo(photoUid);
            PhotoFormatUtility.checkJson(face);
            return face;
        }

        private JSONObject getPhotoInfo(String imgUid) throws IOException {
            JSONObject jsonObject = PhotoFormatUtility.prepareJsonImageInfo(imgUid);
            return new ApiConnector(URL_REQUEST_IMG_INFO).makeJsonRequest(jsonObject);
        }

        private String getPhotoUid(Bitmap bitmap, String flag) throws IOException {
            String base64Photo = PhotoFormatUtility.bitmapToString(bitmap);
            String xmlRequest = PhotoFormatUtility.toXml(base64Photo, flag);
            InputStream response = new ApiConnector(URL_REQUEST_UID).makeXmlRequest(xmlRequest);
            return PhotoFormatUtility.parse(response);
        }

        private void checkFaces(List<Face> list) {
            if (list.isEmpty()) {
                showMessage("Photo doesn't contain faces");
            }
        }
    }
}