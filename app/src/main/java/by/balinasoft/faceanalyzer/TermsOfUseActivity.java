package by.balinasoft.faceanalyzer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonObject;

import by.balinasoft.faceanalyzer.constants.Constants;

public class TermsOfUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_of_use);

        JsonObject language = FaceAnalyzerApplication.getAppLanguage();

        String title = language.get(Constants.EULA).getAsString();
        getSupportActionBar().setTitle(title);
    }
}
