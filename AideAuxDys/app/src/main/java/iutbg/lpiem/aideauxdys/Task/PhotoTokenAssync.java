package iutbg.lpiem.aideauxdys.Task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;

import iutbg.lpiem.aideauxdys.Manager.FormaterManager;

/**
 * Created by Corentin on 06/01/2016.
 */
public class PhotoTokenAssync extends AsyncTask<Integer, Integer,String> {
    private static final String TAG = "PhotoTokenAssync.java";
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/AideAuxDysOCR/";
    public static final String lang = "fra";
    private Context context;
    private String path;
    private WebView wvPhotoToken;
    private ProgressBar progressBar;
    private OnFinishListener callback;

    public PhotoTokenAssync(Context context,WebView wvPhotoToken,ProgressBar progressBar) {
        this.context = context;
        this.wvPhotoToken=wvPhotoToken;
        this.progressBar=progressBar;
        this.path= DATA_PATH + "/ocr.jpg";

        if(context instanceof OnFinishListener)
            callback = (OnFinishListener)context;
    }

    @Override
    protected String doInBackground(Integer... params) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        try {
            ExifInterface exif = new ExifInterface(path);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            Log.v(TAG, "Orient: " + exifOrientation);

            int rotate = 0;

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            Log.v(TAG, "Rotation: " + rotate);

            if (rotate != 0) {

                // Getting width & height of the given image.
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                // Rotating Bitmap
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
            }

            // Convert to ARGB_8888, required by tess
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        } catch (IOException e) {
            Log.e(TAG, "Couldn't correct orientation: " + e.toString());
        }


        // _image.setImageBitmap( bitmap );

        Log.v(TAG, "Before baseApi");

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(DATA_PATH, lang);
        baseApi.setImage(bitmap);

        String recognizedText = baseApi.getUTF8Text();

        baseApi.end();

        // You now have the text in recognizedText var, you can do anything with it.
        // We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
        // so that garbage doesn't make it to the display.

        Log.v(TAG, "OCRED TEXT: " + recognizedText);

        if ( recognizedText.length() != 0 ) {
            // Envoi le texte brute au callback
            if(callback != null)
                callback.onFinishSuccess(recognizedText);

            FormaterManager formaterManager = new FormaterManager(context);
            return formaterManager.formatWithPref(recognizedText);
        }

        // Cycle done.

        return null;
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        progressBar.setVisibility(View.INVISIBLE);
        wvPhotoToken.loadDataWithBaseURL("file:///android_asset/Fonts/",o, "text/html","utf-8",null);
    }

    public interface OnFinishListener{
        void onFinishSuccess(String data);
    }
}
