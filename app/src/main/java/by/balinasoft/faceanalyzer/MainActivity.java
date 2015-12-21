package by.balinasoft.faceanalyzer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    private boolean isEvaluate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView analyze = (ImageView) findViewById(R.id.analyzeButton);
        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnalyzeActivity.class));
            }
        });

        ImageView termsOfUse = (ImageView) findViewById(R.id.termsOfUse);
        termsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TermsOfUseActivity.class));
            }
        });

        ImageView statistic = (ImageView) findViewById(R.id.statistic);
        statistic.setOnClickListener(new View.OnClickListener() {
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
