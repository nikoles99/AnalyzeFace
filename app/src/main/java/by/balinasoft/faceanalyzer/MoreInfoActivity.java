package by.balinasoft.faceanalyzer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.List;

import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.model.Face;
import by.balinasoft.faceanalyzer.model.FaceProperties;

public class MoreInfoActivity extends AppCompatActivity {

    public static final String Face = AppCompatActivity.class + "Face";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        JsonObject language = FaceAnalyzerApplication.getAppLanguage();

        textView = (TextView) findViewById(R.id.textView);

        by.balinasoft.faceanalyzer.model.Face face = (Face)getIntent().getSerializableExtra(Face);
        print(face.getFaceProperties());
    }

    private void print(List<FaceProperties> propertiesList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (FaceProperties faceProperties : propertiesList) {
            double confidence = faceProperties.getConfidence();
            confidence *= 100;
            stringBuilder.append(faceProperties.getName() + "     " + faceProperties.getValue() +
                    "     " + confidence + "% \n");
        }
        textView.setText(stringBuilder);
    }
}
