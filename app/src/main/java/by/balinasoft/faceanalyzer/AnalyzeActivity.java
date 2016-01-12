package by.balinasoft.faceanalyzer;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.loaders.FaceAnalyzerLoader;
import by.balinasoft.faceanalyzer.loaders.GetImageInfoLoader;
import by.balinasoft.faceanalyzer.loaders.UploadImageLoader;
import by.balinasoft.faceanalyzer.model.Face;
import by.balinasoft.faceanalyzer.model.FaceProperties;
import by.balinasoft.faceanalyzer.model.GetImageInfoRequest;
import by.balinasoft.faceanalyzer.model.GetImageInfoResponse;
import by.balinasoft.faceanalyzer.model.UploadImageRequest;
import by.balinasoft.faceanalyzer.model.UploadImageResponse;
import by.balinasoft.faceanalyzer.utils.PhotoFormatUtility;
import by.balinasoft.faceanalyzer.utils.ServerObserver;

public class AnalyzeActivity extends AppCompatActivity
        implements ServerObserver<JsonObject, String> {

    private static final int REQUEST_IMAGE_CAMERA = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final int REQUEST_FACE_QUALITIES = 3;

    private static final String TYPE = "image/*";
    private static final String MAKE_PHOTO = "Please select image or make photo on camera";
    private static final String INVALID_IMAGE = "Invalid Image";
    private static final String DATA = "data";
    public static final String ANALYZE_TYPE = "extended";
    public static final String RESPONSE_OK = "ok";
    public static final String REQUEST_IS_IN_THE_QUEUE = "Request is in the queue";

    private ImageView makePhoto;
    private ImageView openGallery;
    private ProgressBar progressBar;
    private RelativeLayout loadingLayout;

    private Bitmap photo;

    private JsonObject mappingTable;
    private JsonObject language;

    private Button retry;
    private Button cancel;

    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        language = ((FaceAnalyzerApplication) getApplicationContext()).getAppLanguage();
        mappingTable = ((FaceAnalyzerApplication) getApplicationContext()).getMappingTable();
        String title = language.get(Constants.ANALYSIS).getAsString();
        getSupportActionBar().setTitle(title);

        String makePhotoTitle = language.get(Constants.TAKE_PHOTO).getAsString();
        ((TextView) findViewById(R.id.makePhotoTitle)).setText(makePhotoTitle);

        String loadFromGalleryTitle = language.get(Constants.CHOOSE_FROM_GALLERY).getAsString();
        ((TextView) findViewById(R.id.loadFromGalleryTitle)).setText(loadFromGalleryTitle);

        loadingLayout = (RelativeLayout) findViewById(R.id.loadingLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        message = (TextView) findViewById(R.id.message);

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setText(language.get(Constants.CANCEL).getAsString());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        retry = (Button) findViewById(R.id.retry);
        retry.setText(language.get(Constants.RETRY).getAsString());
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                analyze(photo, ANALYZE_TYPE);
            }
        });

        makePhoto = (ImageView) findViewById(R.id.makePhoto);
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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(TYPE);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    private void setLoadingLayoutVisibility(boolean enable) {
        loadingLayout.setVisibility(enable ? View.GONE : View.VISIBLE);
        message.setVisibility(enable ? View.GONE : View.VISIBLE);
        makePhoto.setEnabled(enable);
        openGallery.setEnabled(enable);
    }

    private void setProgressFormVisibility(boolean enable) {
        progressBar.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
        retry.setVisibility(enable ? View.GONE : View.VISIBLE);
        cancel.setVisibility(enable ? View.GONE : View.VISIBLE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAMERA:
                    photo = getImageFromCamera(data);
                    analyze(photo, ANALYZE_TYPE);
                    break;
                case REQUEST_IMAGE_GALLERY:
                    photo = getImageFromGallery(data);
                    analyze(photo, ANALYZE_TYPE);
                    break;
                case REQUEST_FACE_QUALITIES:
                    handleResponse(data);
                    break;
            }
        }
    }

    private void handleResponse(Intent data) {
        switch (data.getStringExtra(AnalyzeResultActivity.GET_PHOTO_MODE)) {
            case Constants.MAKE_PHOTO:
                openCamera();
                break;
            case Constants.LOAD_FROM_GALLERY:
                openGallery();
                break;
        }
    }

    private void analyze(Bitmap image, String flag) {
        if (image != null) {
            getPhotoUid(image, flag);
            setLoadingLayoutVisibility(false);
            setProgressFormVisibility(true);
            message.setText(language.get(Constants.WAIT).getAsString() + "\n" +
                    language.get(Constants.PHOTO_LOADING).getAsString());
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

    private void getPhotoUid(Bitmap bitmap, String flag) {
        String base64Photo = PhotoFormatUtility.bitmapToString(bitmap);
        UploadImageRequest request = new UploadImageRequest();
        request.setImageBase64(base64Photo);
        request.setDetectionFlag(flag);
        FaceAnalyzerLoader<UploadImageRequest> analyzerLoader = new UploadImageLoader();
        analyzerLoader.setServerObserver(this);
        analyzerLoader.makeRequest(request);
    }

    private void getFaceInfo(String photoUid) {
        FaceAnalyzerLoader<GetImageInfoRequest> analyzerLoader = new GetImageInfoLoader();
        analyzerLoader.setServerObserver(this);
        GetImageInfoRequest request = new GetImageInfoRequest();
        request.setImageUid(photoUid);
        analyzerLoader.makeRequest(request);
    }

    private void showHumanQuality(List<Face> faceList) {
        Intent intent = new Intent(AnalyzeActivity.this, AnalyzeResultActivity.class);
        intent.putExtra(AnalyzeResultActivity.FACE_LIST, (Serializable) faceList);
        String filePath = PhotoFormatUtility.savePhotoInStorage(photo, this);
        intent.putExtra(AnalyzeResultActivity.PHOTO, filePath);
        startActivityForResult(intent, REQUEST_FACE_QUALITIES);
    }

    private List<Face> localFaceListAnalyze(List<Face> serverFaceList) {
        message.setText(language.get(Constants.ANALYSING).getAsString());
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

    private void updateStatisticAnalyzes(String typeAnalyze) {
        FaceAnalyzerApplication application = ((FaceAnalyzerApplication) getApplicationContext());
        int countAnalyzes = application.getStatisticValue(typeAnalyze);
        application.saveStatisticValue(typeAnalyze, ++countAnalyzes);

        if (!typeAnalyze.equals(Constants.NUM_OF_INITIATED_ANALYSIS)) {
            updateStatisticAnalyzes(Constants.NUM_OF_INITIATED_ANALYSIS);
        }
    }

    private void createProperty(String typeHumanQuality, Face localFace,
                                FaceProperties serverFaceProperty, JsonObject valueProperty) {
        if (valueProperty != null) {
            JsonArray humanQualities = valueProperty.getAsJsonArray(typeHumanQuality);

            for (JsonElement quality : humanQualities) {
                FaceProperties localFaceProperty = new FaceProperties(serverFaceProperty.
                        getConfidence(), typeHumanQuality, quality.getAsString());
                localFace.addProperty(localFaceProperty);
            }
        }
    }

    private void handleResponse(JsonObject jsonObject) {
        String status = jsonObject.get(Constants.RESPONSE).getAsString();

        switch (status) {
            case RESPONSE_OK:
                if (jsonObject.get(Constants.FACES) != null) {
                    GetImageInfoResponse getImageInfoResponse = new Gson().
                            fromJson(jsonObject, new TypeToken<GetImageInfoResponse>() {
                            }.getType());
                    if (!getImageInfoResponse.getFaces().isEmpty()) {
                        List<Face> faces = localFaceListAnalyze(getImageInfoResponse.getFaces());
                        setLoadingLayoutVisibility(true);
                        updateStatisticAnalyzes(Constants.SUCCESSFULLY_ANALYSES);
                        showHumanQuality(faces);
                    } else {
                        updateStatisticAnalyzes(Constants.FAILED_ANALYSES);
                        showMessage(language.get(Constants.NO_FACE_WERE_FOUND).getAsString());
                        setLoadingLayoutVisibility(true);
                    }
                } else {
                    UploadImageResponse uploadImageResponse = new Gson().
                            fromJson(jsonObject, new TypeToken<UploadImageResponse>() {
                            }.getType());
                    getFaceInfo(uploadImageResponse.getImageUid());
                }
                break;
            case REQUEST_IS_IN_THE_QUEUE:
                // showMessage(REQUEST_IS_IN_THE_QUEUE);
                getFaceInfo(jsonObject.get(Constants.UID).getAsString());
                break;
            default:
                showMessage(status);
                break;
        }
    }

    @Override
    public void successExecute(JsonObject jsonObject) {
        if (jsonObject != null) {
            handleResponse(jsonObject);
        } else {
            message.setText(language.get(Constants.NO_FACE_WERE_FOUND).getAsString());
            updateStatisticAnalyzes(Constants.FAILED_ANALYSES);
            setProgressFormVisibility(false);
        }
    }

    @Override
    public void failedExecute(String errorMessage) {
        updateStatisticAnalyzes(Constants.FAILED_ANALYSES);
        message.setText(language.get(Constants.NO_CONNECTION).getAsString());
        setProgressFormVisibility(false);
    }
}