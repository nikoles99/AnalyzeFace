package by.balinasoft.faceanalyzer;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.loaders.FaceAnalyzerLoader;
import by.balinasoft.faceanalyzer.loaders.PhotoInfoLoader;
import by.balinasoft.faceanalyzer.loaders.PhotoUidLoader;
import by.balinasoft.faceanalyzer.model.Face;
import by.balinasoft.faceanalyzer.utils.PhotoFormatUtility;
import by.balinasoft.faceanalyzer.utils.ServerObserver;

public class AnalyzeActivity extends AppCompatActivity
        implements ServerObserver<JsonObject, String> {

    private static final int REQUEST_IMAGE_CAMERA = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    private static final String TYPE = "image/*";
    private static final String MAKE_PHOTO = "Please select image or make photo on camera";
    private static final String INVALID_IMAGE = "Invalid Image";
    private static final String DATA = "data";

    private ImageView openGallery;

    private Bitmap image;

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
            analyze(image, "extended");
        }
    }

    private void analyze(Bitmap image, String flag) {
        if (image != null) {
            new PhotoExecutor(flag).execute(image);
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

    private void getPhotoUid(Bitmap bitmap, String flag) {
        String base64Photo = PhotoFormatUtility.bitmapToString(bitmap);
        String xmlRequest = PhotoFormatUtility.toXml(base64Photo, flag);

        FaceAnalyzerLoader<String> analyzerLoader = new PhotoUidLoader();
        analyzerLoader.setServerObserver(this);
        analyzerLoader.makeRequest(xmlRequest);
    }

    private void getFaceInfo(String photoUid) {
        FaceAnalyzerLoader<JsonObject> analyzerLoader = new PhotoInfoLoader();
        analyzerLoader.setServerObserver(this);
        JsonObject jsonObject = PhotoFormatUtility.prepareJsonImageInfo(photoUid);
        analyzerLoader.makeRequest(jsonObject);
    }

    @Override
    public void successExecute(JsonObject jsonObject) {
        if (jsonObject != null) {
            String responseOk = jsonObject.get("string_response").getAsString();
            if (responseOk.equals("ok")) {
                JsonArray faces = jsonObject.getAsJsonArray(Constants.FACES);
                List<Face> faceList = new Gson().fromJson(faces, new TypeToken<List<Face>>() {
                }.getType());
                Intent intent = new Intent(AnalyzeActivity.this, AnalyzeResultActivity.class);
                intent.putExtra(AnalyzeResultActivity.LIST_FACES, (Serializable) faceList);
                intent.putExtra(AnalyzeResultActivity.PHOTO, image);
                startActivity(intent);
            }
            else {
                showMessage("error responseOk");
            }
        } else {
            showMessage("error");
        }
    }

    @Override
    public void failedExecute(String errorMessage) {
        showMessage(errorMessage);
    }


    public class PhotoExecutor extends AsyncTask<Bitmap, Void, String> {

        private static final String URL_REQUEST_UID = "http://www.betafaceapi.com/service.svc/" +
                "UploadNewImage_File";

        private Exception exception;

        private String FLAG;

        public PhotoExecutor(String FLAG) {
            this.FLAG = FLAG;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            try {
                for (Bitmap bitmap : bitmaps) {
                    return getPhotoUid(bitmap, FLAG);
                }
            } catch (IOException e) {
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String uid) {
            super.onPostExecute(uid);

            if (exception == null || !uid.isEmpty()) {
                getFaceInfo(uid);
            } else {
                showMessage(exception.getMessage());
            }
        }

        private String getPhotoUid(Bitmap bitmap, String flag) throws IOException {
            String base64Photo = PhotoFormatUtility.bitmapToString(bitmap);
            String xmlRequest = PhotoFormatUtility.toXml(base64Photo, flag);
            InputStream response = new ApiConnector(URL_REQUEST_UID).makeXmlRequest(xmlRequest);
            return PhotoFormatUtility.parse(response);
        }
    }
}