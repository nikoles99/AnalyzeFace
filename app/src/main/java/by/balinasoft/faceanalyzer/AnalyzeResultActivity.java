package by.balinasoft.faceanalyzer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.JsonObject;


import java.util.List;

import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.model.HumanQuality;

public class AnalyzeResultActivity extends AppCompatActivity {

    public static final String HUMAN_QUALITY_LIST = AppCompatActivity.class + "humanQuality";

    public static final String PHOTO = AppCompatActivity.class + "Photo";


    private Bitmap bitmap;
    private ListView listView;
    private ImageView imageView;
    private ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_result);

        JsonObject language = ((FaceAnalyzerApplication) getApplicationContext()).getAppLanguage();

        String title = language.get(Constants.ANALYSES_RESULT).getAsString();
        getSupportActionBar().setTitle(title);

        List<HumanQuality> humanQualities = (List<HumanQuality>) getIntent().
                getSerializableExtra(HUMAN_QUALITY_LIST);

        listView = (ListView) findViewById(R.id.listView);

        imageView = (ImageView) findViewById(R.id.photo);
        bitmap = getIntent().getParcelableExtra(PHOTO);
        imageView.setImageBitmap(bitmap);

    }

}
