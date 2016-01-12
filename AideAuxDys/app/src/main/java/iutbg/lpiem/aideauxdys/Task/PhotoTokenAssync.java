package iutbg.lpiem.aideauxdys.Task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.googlecode.tesseract.android.TessBaseAPI;

import iutbg.lpiem.aideauxdys.TextTokenActivity;

/**
 * Created by Corentin on 06/01/2016.
 */
public class PhotoTokenAssync extends AsyncTask<Integer, Integer,String> {
    private static final String TAG = "PhotoTokenAssync.java";
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/AideAuxDysOCR/";
    public static final String lang = "fra";
    private Activity activity;
    private ProgressBar progressBar;
    private OnFinishListener callback;
    private Bitmap bitmap;

    public PhotoTokenAssync(Activity activity,ProgressBar progressBar,Bitmap bitmap) {
        this.activity = activity;
        this.bitmap=bitmap;
        this.progressBar=progressBar;


        if(this.activity instanceof OnFinishListener)
            callback = (OnFinishListener) this.activity;
    }

    @Override
    protected String doInBackground(Integer... params) {


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

            return recognizedText;
        }

        // Cycle done.

        return null;
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        progressBar.setVisibility(View.INVISIBLE);


        Intent i=new Intent(activity, TextTokenActivity.class);
        Bundle b=new Bundle();
        b.putString("recoString",o);
        i.putExtras(b);
        // Envoi le texte brute au callback
        if (callback != null)
            callback.onFinishSuccess(o);
        activity.startActivity(i);
        activity.finish();


    }

    public interface OnFinishListener{
        void onFinishSuccess(String data);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar.setVisibility(View.VISIBLE);
    }
}
