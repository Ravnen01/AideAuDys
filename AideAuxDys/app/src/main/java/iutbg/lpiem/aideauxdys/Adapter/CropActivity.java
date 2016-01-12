package iutbg.lpiem.aideauxdys.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.edmodo.cropper.CropImageView;

import java.io.IOException;

import iutbg.lpiem.aideauxdys.R;

public class CropActivity extends AppCompatActivity {
    private CropImageView cropPhoto;
    private static final String TAG = "PhotoTokenAssync.java";
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/AideAuxDysOCR/";
    private String path;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        this.path= DATA_PATH + "/ocr.jpg";

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        bitmap = BitmapFactory.decodeFile(path, options);

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

        cropPhoto = (CropImageView)findViewById(R.id.CropImageView_crop);
        cropPhoto.setImageBitmap(bitmap);

        ImageButton ibRotateLeft=(ImageButton)findViewById(R.id.ibRotateLeft);
        ImageButton ibRotateRight=(ImageButton)findViewById(R.id.ibRotateRight);
        ImageButton ibValide=(ImageButton)findViewById(R.id.ibValidImage);

        ibRotateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix matrix = new Matrix();

                matrix.postRotate(-90);

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);

                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
                bitmap=rotatedBitmap;
                cropPhoto.setImageBitmap(bitmap);
            }
        });

        ibRotateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix matrix = new Matrix();

                matrix.postRotate(90);

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);

                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
                bitmap=rotatedBitmap;
                cropPhoto.setImageBitmap(bitmap);
            }
        });

        ibValide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
