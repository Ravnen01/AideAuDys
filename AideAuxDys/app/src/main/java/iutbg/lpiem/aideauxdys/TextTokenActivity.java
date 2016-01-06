package iutbg.lpiem.aideauxdys;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import iutbg.lpiem.aideauxdys.Task.PhotoTokenAssync;

public class TextTokenActivity extends AppCompatActivity {
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/AideAuxDysOCR/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_token);
        PhotoTokenAssync photoTokenAssync=new PhotoTokenAssync(getApplicationContext());
        photoTokenAssync.execute();
    }


}
