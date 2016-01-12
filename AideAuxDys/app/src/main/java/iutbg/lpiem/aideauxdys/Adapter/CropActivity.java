package iutbg.lpiem.aideauxdys.Adapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.edmodo.cropper.CropImageView;

import iutbg.lpiem.aideauxdys.R;

public class CropActivity extends AppCompatActivity {
    private CropImageView cropPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        cropPhoto = (CropImageView)findViewById(R.id.CropImageView_crop);
        //cropImageView.setImageBitmap(photo);
    }
}
