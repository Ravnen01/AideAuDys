package iutbg.lpiem.aideauxdys;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ProgressBar;

import iutbg.lpiem.aideauxdys.Task.PhotoTokenAssync;

public class TextTokenActivity extends AppCompatActivity {
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/AideAuxDysOCR/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_token);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(10);
        PhotoTokenAssync photoTokenAssync=new PhotoTokenAssync(getApplicationContext(),(WebView)findViewById(R.id.wvTextToken),progressBar);
        photoTokenAssync.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
}