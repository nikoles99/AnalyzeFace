package by.balinasoft.faceanalyzer;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import by.balinasoft.faceanalyzer.utils.FileReader;

public class TermsOfUseActivity extends Activity {

    private static final String TYPE = "text/html; charset=UTF-8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_of_use);

        String eula = ((FaceAnalyzerApplication)getApplicationContext()).getEula();

        String text = FileReader.loadFile(getApplicationContext(), eula);
        WebView webView = (WebView)findViewById(R.id.termsOfUse);
        webView.loadData(text, TYPE, null);
    }
}
