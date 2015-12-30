package by.balinasoft.faceanalyzer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.google.gson.JsonObject;


import java.util.List;

import by.balinasoft.faceanalyzer.adapters.FaceAdapter;
import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.model.Face;

public class AnalyzeResultActivity extends AppCompatActivity {

    public static final String FACE_LIST = AppCompatActivity.class + "faceList";

    public static final String PHOTO = AppCompatActivity.class + "Photo";


    private Bitmap bitmap;
    private ExpandableListView listView;
    private ImageView imageView;
    private FaceAdapter faceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_result);

        JsonObject language = ((FaceAnalyzerApplication) getApplicationContext()).getAppLanguage();

        String title = language.get(Constants.ANALYSES_RESULT).getAsString();
        getSupportActionBar().setTitle(title);

        List<Face> faceList = (List<Face>) getIntent().getSerializableExtra(FACE_LIST);

        listView = (ExpandableListView) findViewById(R.id.listView);
        faceAdapter = new FaceAdapter(this, faceList);
        listView.setAdapter(faceAdapter);
        imageView = (ImageView) findViewById(R.id.photo);
        bitmap = getIntent().getParcelableExtra(PHOTO);
        imageView.setImageBitmap(bitmap);
    }
}
