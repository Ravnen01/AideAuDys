package iutbg.lpiem.aideauxdys;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ProgressBar;

import iutbg.lpiem.aideauxdys.Task.PhotoTokenAssync;

public class TextTokenActivity extends AppCompatActivity {
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/AideAuxDysOCR/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_token);
        ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(10);
        PhotoTokenAssync photoTokenAssync=new PhotoTokenAssync(getApplicationContext(),(WebView)findViewById(R.id.wvTextToken),progressBar);
        photoTokenAssync.execute();
    }


}
