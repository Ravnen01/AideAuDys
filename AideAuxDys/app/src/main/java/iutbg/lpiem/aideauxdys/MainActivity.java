package iutbg.lpiem.aideauxdys;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static final String PACKAGE_NAME = "iutbg.lpiem.aideauxdys";
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/AideAuxDysOCR/";
    public static final String lang = "fra";
    private static final String TAG = "MainActivity.java";
    protected static final String PHOTO_TAKEN = "photo_taken";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }

        }

    }
}
