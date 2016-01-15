package iutbg.lpiem.aideauxdys;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class MainActivity extends AppCompatActivity {
    public static final String PACKAGE_NAME = "iutbg.lpiem.aideauxdys";
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/AideAuxDysOCR/";
    public static final String lang = "fra";
    private static final String TAG = "MainActivity.java";
    protected static final String PHOTO_TAKEN = "photo_taken";
    protected String path;
    protected boolean taken;
    private static final int SELECT_PHOTO=12;
    private static final int CAPTURE_PHOTO=11;
    private LinearLayout layoutLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        layoutLoading = (LinearLayout) findViewById(R.id.main_layout_loading);

        ImageView imgSetting = (ImageView)findViewById(R.id.ivOption);
        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        ImageView imgImportImage=(ImageView)findViewById(R.id.ivGalerieImage);
        imgImportImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutLoading.setVisibility(View.VISIBLE);
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

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

        if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".traineddata");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + " traineddata");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
            }
        }
        path = DATA_PATH + "/ocr.jpg";

        ImageView ivPhoto=(ImageView)findViewById(R.id.ivPhoto);
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraActivity();
            }
        });
        final EditText etImport=(EditText)findViewById(R.id.etImport);
        Button bImport=(Button)findViewById(R.id.bTextImport);
        bImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),TextTokenActivity.class);
                Bundle b=new Bundle();
                b.putString("recoString",etImport.getText().toString());
                i.putExtras(b);
                startActivity(i);
            }
        });

    }

    protected void startCameraActivity() {
        File file = new File(path);
        Uri outputFileUri = Uri.fromFile(file);

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(intent, CAPTURE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.i(TAG, "resultCode: " + resultCode);
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode==RESULT_OK){
                    final Uri imageUri = data.getData();
                    final InputStream imageStream;
                    FileOutputStream out = null;
                    try {
                        imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);


                        File file=new File(path);
                        out = new FileOutputStream(file);
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance

                        starActivityResizePhoto();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }else{
                    Log.v(TAG,"User cancelled");
                }
                break;
            case CAPTURE_PHOTO:
                if (resultCode == RESULT_OK) {
                    starActivityResizePhoto();
                } else {
                    Log.v(TAG, "User cancelled");
                }
                break;
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(MainActivity.PHOTO_TAKEN, taken);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onRestoreInstanceState()");
        if (savedInstanceState.getBoolean(MainActivity.PHOTO_TAKEN)) {
            starActivityResizePhoto();
        }
    }

    protected void starActivityResizePhoto(){
        Intent intent=new Intent(this,CropActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        layoutLoading.setVisibility(View.GONE);
    }


}
