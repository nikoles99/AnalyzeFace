package by.balinasoft.faceanalyzer;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MoreInfoActivity extends AppCompatActivity {

    public static final String Face = AppCompatActivity.class + "Face";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        textView = (TextView) findViewById(R.id.textView);

        Face face = (Face)getIntent().getSerializableExtra(Face);
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
