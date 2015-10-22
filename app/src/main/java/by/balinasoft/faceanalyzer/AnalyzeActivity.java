package by.balinasoft.faceanalyzer;


import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AnalyzeActivity extends AppCompatActivity {

    final int TYPE_PHOTO = 1;

    final int REQUEST_CODE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.camera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri(TYPE_PHOTO));
                startActivityForResult(intent, REQUEST_CODE_PHOTO);
                break;
            case R.id.galery:
                new PhotoAnalyzer().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
