package iutbg.lpiem.aideauxdys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ProgressBar;

import iutbg.lpiem.aideauxdys.Manager.TextReader;
import iutbg.lpiem.aideauxdys.Task.PhotoTokenAssync;

public class TextTokenActivity extends AppCompatActivity implements PhotoTokenAssync.OnFinishListener {
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/AideAuxDysOCR/";
    private TextReader textReader;
    private String recoText = "";
    private MenuItem itemPlay;
    private MenuItem itemPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_token);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        textReader = new TextReader(this);
        final WebView webView = (WebView) findViewById(R.id.wvTextToken);
        ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar);

        progressBar.setMax(10);
        PhotoTokenAssync photoTokenAssync=new PhotoTokenAssync(this, webView,progressBar);
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
            case R.id.action_play:
                readText();
                return true;
            case R.id.action_pause:
                pauseReader();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void pauseReader() {
        if (textReader.isSpeaking()) {
            textReader.stopSpeaking();
            itemPause.setVisible(false);
            itemPlay.setVisible(true);
        }
    }

    private void readText() {
        if (textReader.isInit() && !recoText.equals("")) {
            textReader.say(recoText);
            itemPlay.setVisible(false);
            itemPause.setVisible(true);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        itemPlay = menu.findItem(R.id.action_play);
        itemPause = menu.findItem(R.id.action_pause);
        itemPause.setVisible(false);
        return true;
    }

    @Override
    public void onFinishSuccess(String data) {
        recoText = data;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseReader();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textReader.destroy();
    }
}