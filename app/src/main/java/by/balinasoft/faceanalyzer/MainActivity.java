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
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.JsonObject;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import by.balinasoft.faceanalyzer.constants.Constants;


public class MainActivity extends AppCompatActivity {

    private static final String VK_METHOD = "wall.post";
    private static final String PARAM_ADD_ON_WALL = "wall";
    private static final String NO = "No";
    private static final String LATER = "Later";
    private static final String YES = "Yes";
    private static final String MARKET_URI = "market://details?id=ru.lxx.soltroll";
    private static final String APP_DESCRIPTION = "Приложение для определения характера человека," +
            " по фотографии лица.";
    private static final String APP_URL = "https://play.google.com/store/apps/" +
            "details?id=com.betaface.betaface&hl=ru";

    private boolean isEvaluate = false;

    private JsonObject language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        language = ((FaceAnalyzerApplication) getApplicationContext()).getAppLanguage();

        String title = language.get(Constants.ANALYSIS).getAsString();
        getSupportActionBar().setTitle(title);

        String analysis = language.get(Constants.ANALYSIS).getAsString();
        ((TextView) findViewById(R.id.analyzeTextView)).setText(analysis);

        String statistic = language.get(Constants.STATS).getAsString();
        ((TextView) findViewById(R.id.statisticTextView)).setText(statistic);

        String termsOfUse = language.get(Constants.EULA).getAsString();
        ((TextView) findViewById(R.id.termOfUseTextView)).setText(termsOfUse);

        String share = language.get(Constants.SHARE).getAsString();
        ((TextView) findViewById(R.id.shareTextView)).setText(share);

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

        ImageView shareVk = (ImageView) findViewById(R.id.vkontakte);
        shareVk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareVk();
            }
        });

        ImageView shareFacebook = (ImageView) findViewById(R.id.facebook);
        shareFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFacebook();
            }
        });
    }

    private void shareVk() {
        VKSdk.login(MainActivity.this, PARAM_ADD_ON_WALL);
    }

    private void shareFacebook() {
        ShareDialog shareDialog = new ShareDialog(MainActivity.this);

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(Constants.APP_TITLE)
                    .setContentDescription(APP_DESCRIPTION)
                    .setContentUrl(Uri.parse(APP_URL))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    private VKParameters getVkRequestParameters(VKAccessToken res) {
        VKParameters params = new VKParameters();
        params.put(VKApiConst.OWNER_ID, res.userId);
        params.put(VKApiConst.MESSAGE, APP_DESCRIPTION);
        params.put(VKApiConst.ATTACHMENTS, APP_URL);
        return params;
    }

    private void makeVkRequest(String method, VKParameters params) {
        VKRequest shareAppInfo = new VKRequest(method, params);
        shareAppInfo.executeWithListener(new VKRequest.VKRequestListener() {

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                showMessage(language.get(Constants.SHARE_INFO_MESSAGE).getAsString());
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                showMessage(error.errorMessage);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {

            @Override
            public void onResult(VKAccessToken res) {
                VKParameters params = getVkRequestParameters(res);
                makeVkRequest(VK_METHOD, params);
            }

            @Override
            public void onError(VKError error) {
                showMessage(error.errorMessage);
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showMessage(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (isEvaluate) {
            super.onBackPressed();
        } else {
            showEvaluateDialog();
        }
    }

    private void showEvaluateDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(language.get(Constants.EVALUATE_APP).getAsString());
        dialog.setPositiveButton(YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                evaluateApplication();
            }
        });
        dialog.setNegativeButton(NO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isEvaluate = true;
            }
        });
        dialog.setNeutralButton(LATER, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    private void evaluateApplication() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(MARKET_URI));
        startActivity(intent);
        isEvaluate = true;
    }
}
