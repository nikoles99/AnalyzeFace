package by.balinasoft.faceanalyzer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import by.balinasoft.faceanalyzer.model.Face;

public class AnalyzeResultActivity extends AppCompatActivity {

    public static final String LIST_FACES = AppCompatActivity.class + "Face";

    public static final String PHOTO = AppCompatActivity.class + "Photo";

    private Bitmap bitmap;
    private ListView listView;
    private ImageView imageView;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_result);

        List<Face> faceList = (List<Face>) getIntent().getSerializableExtra(LIST_FACES);

        String[] strings = new String[]{"sdfsdfsf", "sdfsdfsdf", "sdfsdfsddf", "sdfsdfdsdf", "sdfsdfdsdf"};
        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strings);

        imageView = (ImageView) findViewById(R.id.imageView);
        bitmap = getIntent().getParcelableExtra(PHOTO);
//        imageView.setImageBitmap(bitmap);

    }
}
