package by.balinasoft.faceanalyzer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.model.Face;

public class AnalyzeResultActivity extends AppCompatActivity {

    public static final String LIST_FACES = AppCompatActivity.class + "Face";

    public static final String PHOTO = AppCompatActivity.class + "Photo";

    private static final String ENGLISH_TABLE_FILE = "mapping-table-en.json";
    private static final String RUSSIAN_TABLE_FILE = "mapping-table-ru.json";

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
        setApplicationLanguage();

    }

    private void setApplicationLanguage() {
        String deviceLanguage = Locale.getDefault().getDisplayLanguage();
        String languageFileName;

        switch (deviceLanguage) {
            case Constants.RUSSIAN_LANGUAGE:
                languageFileName = RUSSIAN_TABLE_FILE;
                break;
            default:
                languageFileName = ENGLISH_TABLE_FILE;
                break;
        }
        String json = loadJSONFromAsset(languageFileName);
        JSONObject jsonObject;
        try {
             jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String d = json.substring(0);
    }



    private String loadJSONFromAsset(String fileName) {
        String json = null;

        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;

    }
}
