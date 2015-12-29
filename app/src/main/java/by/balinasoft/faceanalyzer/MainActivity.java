package by.balinasoft.faceanalyzer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import by.balinasoft.faceanalyzer.constants.Constants;


public class MainActivity extends AppCompatActivity {

    private boolean isEvaluate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JsonObject language = ((FaceAnalyzerApplication)getApplicationContext()).getAppLanguage();

        String title = language.get(Constants.ANALYSIS).getAsString();
        getSupportActionBar().setTitle(title);

        String analysis = language.get(Constants.ANALYSIS).getAsString();
        ((TextView)findViewById(R.id.analyzeTextView)).setText(analysis);

        String statistic = language.get(Constants.STATS).getAsString();
        ((TextView)findViewById(R.id.statisticTextView)).setText(statistic);

        String termsOfUse = language.get(Constants.EULA).getAsString();
        ((TextView)findViewById(R.id.termOfUseTextView)).setText(termsOfUse);

        String share = language.get(Constants.SHARE).getAsString();
        ((TextView)findViewById(R.id.shareTextView)).setText(share);

        ImageView analyze = (ImageView) findViewById(R.id.analyzeButton);
        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnalyzeActivity.class));
            }
        });

        ImageView termsOfUseImageView = (ImageView) findViewById(R.id.termsOfUse);
        termsOfUseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TermsOfUseActivity.class));
            }
        });

        ImageView statisticImageView = (ImageView) findViewById(R.id.statistic);
        statisticImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StatisticActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (isEvaluate) {
            super.onBackPressed();
        } else {
            evaluateApplication();
        }
    }

    private void evaluateApplication() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Please Evaluate Application");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=ru.lxx.soltroll"));
                startActivity(intent);
                isEvaluate = true;

            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isEvaluate = true;
            }
        });
        dialog.setNeutralButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        dialog.show();
    }
}
