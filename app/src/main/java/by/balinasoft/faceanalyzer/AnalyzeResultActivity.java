package by.balinasoft.faceanalyzer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;


import java.util.List;

import by.balinasoft.faceanalyzer.adapters.FaceAdapter;
import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.model.Face;

public class AnalyzeResultActivity extends AppCompatActivity {

    public static final String FACE_LIST = AppCompatActivity.class + "faceList";

    public static final String PHOTO = AppCompatActivity.class + "Photo";

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_result);

        JsonObject language = ((FaceAnalyzerApplication) getApplicationContext()).getAppLanguage();

        String title = language.get(Constants.ANALYSES_RESULT).getAsString();
        getSupportActionBar().setTitle(title);

        List<Face> faceList = (List<Face>) getIntent().getSerializableExtra(FACE_LIST);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        createFacesListView(faceList);

        ImageView imageView = (ImageView) findViewById(R.id.photo);
        Bitmap bitmap = getIntent().getParcelableExtra(PHOTO);
        imageView.setImageBitmap(bitmap);
    }

    private void createFacesListView(List<Face> faceList) {
        for (Face face : faceList) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ExpandableListView listView = new ExpandableListView(this);
            FaceAdapter faceAdapter = new FaceAdapter(this, face);
            listView.setAdapter(faceAdapter);
            linearLayout.addView(listView, layoutParams);
        }
    }
}
