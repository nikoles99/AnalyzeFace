package by.balinasoft.faceanalyzer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.model.Face;
import by.balinasoft.faceanalyzer.model.FaceProperties;
import by.balinasoft.faceanalyzer.model.HumanQuality;
import by.balinasoft.faceanalyzer.utils.FileReader;

public class AnalyzeResultActivity extends AppCompatActivity {

    public static final String LIST_FACES = AppCompatActivity.class + "Face";

    public static final String PHOTO = AppCompatActivity.class + "Photo";

    private static final String ENGLISH_TABLE_FILE = "mapping-table-en.json";
    private static final String RUSSIAN_TABLE_FILE = "mapping-table-ru.json";

    private Bitmap bitmap;
    private ListView listView;
    private ImageView imageView;
    private ArrayAdapter<String> arrayAdapter;
    private List<Face> faceList;
    private JsonObject mappingTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_result);

        JsonObject language = FaceAnalyzerApplication.getAppLanguage();

        String title = language.get(Constants.ANALYSES_RESULT).getAsString();
        getSupportActionBar().setTitle(title);

        String tableFile = FaceAnalyzerApplication.getMappingTableFile();
        mappingTable = FileReader.loadJsonFile(this, tableFile);

        faceList = (List<Face>) getIntent().getSerializableExtra(LIST_FACES);


        List<HumanQuality> humanQualityList = analyzeFace();

        listView = (ListView) findViewById(R.id.listView);

        imageView = (ImageView) findViewById(R.id.photo);
        bitmap = getIntent().getParcelableExtra(PHOTO);
        imageView.setImageBitmap(bitmap);
        setApplicationLanguage();

    }

    private List<HumanQuality> analyzeFace() {
        for (Face face : faceList) {
            return analyzeFaceProperties(face);
        }
        return null;
    }

    private List<HumanQuality> analyzeFaceProperties(Face face) {
        List<HumanQuality> humanQualityList = new ArrayList<>();

        for (FaceProperties faceProperties : face.getFaceProperties()) {
            JsonObject name = mappingTable.getAsJsonObject(faceProperties.getName());
            if (name != null) {
                JsonObject valueQuality = name.getAsJsonObject(faceProperties.getValue());
                JsonArray character = valueQuality.getAsJsonArray("X");
                JsonArray relationsWithPeople = valueQuality.getAsJsonArray("O");
                for (JsonElement element : character) {
                    humanQualityList.add(new HumanQuality("X", element.getAsString(), faceProperties.getConfidence()));
                }
                for (JsonElement element : relationsWithPeople) {
                    humanQualityList.add(new HumanQuality("O", element.getAsString(), faceProperties.getConfidence()));
                }
            }

        }
        return  humanQualityList;
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
