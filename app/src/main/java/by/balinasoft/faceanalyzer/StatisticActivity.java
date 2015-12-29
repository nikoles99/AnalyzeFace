package by.balinasoft.faceanalyzer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.JsonObject;

import by.balinasoft.faceanalyzer.constants.Constants;

public class StatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        JsonObject language = ((FaceAnalyzerApplication)getApplicationContext()).getAppLanguage();

        String title = language.get(Constants.STATS).getAsString();
        getSupportActionBar().setTitle(title);

        String initiatedAnalyses = language.get(Constants.NUM_OF_INITIATED_ANALYSIS).getAsString();
        ((TextView) findViewById(R.id.startAnalyzes)).setText(initiatedAnalyses);

        String successfullyAnalyses = language.get(Constants.SUCCESSFULLY_ANALYSES).getAsString();
        ((TextView) findViewById(R.id.successfullyAnalyses)).setText(successfullyAnalyses);

        String failedAnalyses = language.get(Constants.FAILED_ANALYSES).getAsString();
        ((TextView) findViewById(R.id.failedAnalyses)).setText(failedAnalyses);
    }
}
