package by.balinasoft.faceanalyzer;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.loaders.FaceAnalyzerLoader;
import by.balinasoft.faceanalyzer.loaders.PhotoInfoLoader;
import by.balinasoft.faceanalyzer.model.Face;
import by.balinasoft.faceanalyzer.model.FaceProperties;
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
    public static final String ANALYZE_TYPE = "extended";
    public static final String RESPONSE_OK = "ok";

    private Bitmap image;
    private JsonObject mappingTable;
    private JsonObject language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        language = ((FaceAnalyzerApplication) getApplicationContext()).getAppLanguage();
        mappingTable = ((FaceAnalyzerApplication) getApplicationContext()).getMappingTable();
        String title = language.get(Constants.ANALYSIS).getAsString();
        getSupportActionBar().setTitle(title);

        ImageView makePhoto = (ImageView)
                findViewById(R.id.makePhoto);
        makePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        ImageView openGallery = (ImageView) findViewById(R.id.loadFromGallery);
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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
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
            analyze(image, ANALYZE_TYPE);
        }
    }

    private void analyze(Bitmap image, String flag) {
        if (image != null) {
            getFaceInfo("c1546a5d-0976-4988-a557-3bfded74182e");
            //new PhotoExecutor(flag).execute(image);
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

/*    private void getPhotoUid(Bitmap bitmap, String flag) {
        String base64Photo = PhotoFormatUtility.bitmapToString(bitmap);
        String jsonObject = PhotoFormatUtility.toXml(base64Photo, flag);
        FaceAnalyzerLoader<JsonObject> analyzerLoader = new PhotoUidLoader();
        analyzerLoader.setServerObserver(this);
        analyzerLoader.makeRequest(jsonObject);
    }*/

    private void getFaceInfo(String photoUid) {
        FaceAnalyzerLoader<JsonObject> analyzerLoader = new PhotoInfoLoader();
        analyzerLoader.setServerObserver(this);
        JsonObject jsonObject = PhotoFormatUtility.prepareJsonImageInfo(photoUid);
        analyzerLoader.makeRequest(jsonObject);
    }

    @Override
    public void successExecute(JsonObject jsonObject) {
        if (jsonObject != null) {
            handleResponse(jsonObject);
        } else {
            showMessage(language.get(Constants.NO_FACE_WERE_FOUND).getAsString());
        }
    }

    @Override
    public void failedExecute(String errorMessage) {
        showMessage(errorMessage);
    }


    private void handleResponse(JsonObject jsonObject) {
        String status = jsonObject.get(Constants.RESPONSE).getAsString();

        if (status.equals(RESPONSE_OK)) {
            JsonArray faces = jsonObject.getAsJsonArray(Constants.FACES);
            List<Face> serverFaceList = new Gson().fromJson(faces, new TypeToken<List<Face>>() {
            }.getType());
            showHumanQuality((Serializable) localFaceListAnalyze(serverFaceList));
        } else {
            showMessage(status);
        }
    }

    private void showHumanQuality(Serializable faceList) {
        Intent intent = new Intent(AnalyzeActivity.this, AnalyzeResultActivity.class);
        intent.putExtra(AnalyzeResultActivity.FACE_LIST, faceList);
        intent.putExtra(AnalyzeResultActivity.PHOTO, image);
        startActivity(intent);
    }

    private List<Face> localFaceListAnalyze(List<Face> serverFaceList) {
        List<Face> localFaceList = new ArrayList<>();

        for (Face serverFace : serverFaceList) {
            localFaceList.add(localFaceAnalyze(serverFace));
        }
        return localFaceList;
    }

    private Face localFaceAnalyze(Face serverFace) {
        Face localFace = new Face();

        for (FaceProperties serverFaceProperty : serverFace.getFaceProperties()) {
            localAnalyzeProperty(localFace, serverFaceProperty);
        }
        return localFace;
    }

    private void localAnalyzeProperty(Face localFace, FaceProperties serverFaceProperty) {
        JsonObject nameProperty = mappingTable.getAsJsonObject(serverFaceProperty.getName());

        if (nameProperty != null) {
            JsonObject valueProperty = nameProperty.getAsJsonObject(serverFaceProperty.getValue());

            createProperty(Constants.CHARACTER, localFace, serverFaceProperty, valueProperty);
            createProperty(Constants.RALATIONSHIP, localFace, serverFaceProperty, valueProperty);
        }
    }

    private void createProperty(String typeHumanQuality, Face localFace,
                                FaceProperties serverFaceProperty, JsonObject valueProperty) {
        JsonArray humanQualities = valueProperty.getAsJsonArray(typeHumanQuality);

        for (JsonElement quality : humanQualities) {
            FaceProperties localFaceProperty = new FaceProperties(serverFaceProperty.
                    getConfidence(), typeHumanQuality, quality.getAsString());
            localFace.addProperty(localFaceProperty);
        }
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
            byte[] bytes = PhotoFormatUtility.base64Decode(base64Photo);
            String xmlRequest = PhotoFormatUtility.toXml(base64Photo, flag);
            InputStream response = new ApiConnector(URL_REQUEST_UID).makeXmlRequest(xmlRequest);
            return PhotoFormatUtility.parse(response);
        }
    }
}