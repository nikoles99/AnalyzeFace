package by.balinasoft.faceanalyzer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;

import java.util.List;

import by.balinasoft.faceanalyzer.adapters.FaceAdapter;
import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.model.Face;
import by.balinasoft.faceanalyzer.utils.FaceAnalyzerObserver;
import by.balinasoft.faceanalyzer.utils.PhotoFormatUtility;

public class AnalyzeResultActivity extends AppCompatActivity implements FaceAnalyzerObserver {

    public static final String FACE_LIST = AppCompatActivity.class + "faceList";
    public static final String PHOTO = AppCompatActivity.class + "Photo";
    public static final String GET_PHOTO_MODE = "getPhotoMode";

    private LinearLayout photoInfoLayout;

    private GetPhotoFragment getPhotoFragment;

    private boolean isGetPhotoFragmentShowing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_result);

        JsonObject language = ((FaceAnalyzerApplication) getApplicationContext()).getAppLanguage();

        String title = language.get(Constants.ANALYSES_RESULT).getAsString();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        photoInfoLayout = (LinearLayout) findViewById(R.id.photoInfoLayout);

        List<Face> faceList = (List<Face>) getIntent().getSerializableExtra(FACE_LIST);
        createFacesListView(faceList);

        ImageView imageView = (ImageView) findViewById(R.id.photo);
        String photoPath = getIntent().getStringExtra(PHOTO);
        Bitmap photo = PhotoFormatUtility.loadImageFromStorage(photoPath);
        imageView.setImageBitmap(photo);

        getPhotoFragment = new GetPhotoFragment();
        getPhotoFragment.setObserver(this);
        setGetPhotoFragmentVisibility(isGetPhotoFragmentShowing);
    }

    private void createFacesListView(List<Face> faceList) {
        for (Face face : faceList) {
            ExpandableListView listView = new ExpandableListView(this);
            FaceAdapter faceAdapter = new FaceAdapter(this, face);
            listView.setAdapter(faceAdapter);
            photoInfoLayout.addView(listView);
        }
    }

    private void setGetPhotoFragmentVisibility(boolean isGetPhotoFragmentShowing) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (isGetPhotoFragmentShowing) {
            fragmentTransaction.remove(getPhotoFragment);
        } else {
            fragmentTransaction.replace(R.id.getPhotoFragment, getPhotoFragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                isGetPhotoFragmentShowing = !isGetPhotoFragmentShowing;
                setGetPhotoFragmentVisibility(isGetPhotoFragmentShowing);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void notifyListener(String flag) {
        Intent intent = new Intent();
        intent.putExtra(GET_PHOTO_MODE, flag);
        setResult(RESULT_OK, intent);
        finish();
    }
}
