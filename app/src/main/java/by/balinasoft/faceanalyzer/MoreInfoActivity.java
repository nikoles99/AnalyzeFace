package by.balinasoft.faceanalyzer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MoreInfoActivity extends AppCompatActivity {

    public static final String FACE = AppCompatActivity.class + "Face";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        TextView textView = (TextView) findViewById(R.id.textView);

        Face face = (Face) getIntent().getSerializableExtra(FACE);

        print(textView, face);
    }

    private void print(TextView textView, Face face) {
        StringBuilder stringBuilder = new StringBuilder();

        for (FaceProperties faceProperties : face.getExtendedProperties()) {
            double confidence = faceProperties.getConfidence();
            confidence *= 100;
            stringBuilder.append(faceProperties.getName() + " " + faceProperties.getValue() + " " + confidence + "% \n");
        }
        textView.setText(stringBuilder);
    }
}
