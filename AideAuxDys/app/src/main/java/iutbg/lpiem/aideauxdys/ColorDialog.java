package iutbg.lpiem.aideauxdys;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.danielnilsson9.colorpickerview.view.ColorPanelView;
import com.github.danielnilsson9.colorpickerview.view.ColorPickerView;

import iutbg.lpiem.aideauxdys.Manager.PreferenceManager;

public class ColorDialog extends Dialog implements View.OnClickListener, ColorPickerView.OnColorChangedListener {
    private ColorPickerView mColorPickerView;
    private ColorPanelView mOldColorPanelView;
    private ColorPanelView mNewColorPanelView;
    private PreferenceManager preferenceManager;
    private OnColorPickListener callback;
    private Button mOkButton;
    private Button mCancelButton;
    private ImageView imgColor;
    private String prefColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        init();
    }

    /**
     *
     * @param context
     * @param prefColor Choisir entre PreferenceManager.PREFS_TEXT_COLOR et PreferenceManager.PREFS_BACK_COLOR
     */
    public ColorDialog(Context context, String prefColor) {
        super(context);
        this.prefColor = prefColor;

        if(context instanceof OnColorPickListener)
            callback = (OnColorPickListener)context;
    }

    public ColorDialog(Context context, ImageView imgColor) {
        super(context);
        this.imgColor = imgColor;

        if(context instanceof OnColorPickListener)
            callback = (OnColorPickListener)context;
    }

    private void init() {
        preferenceManager = new PreferenceManager(getContext());
        int initialColor = 0xFFFFFFFF;
        if(prefColor != null)
            initialColor = preferenceManager.getColor(prefColor);

        mColorPickerView = (ColorPickerView) findViewById(R.id.colorpickerview__color_picker_view);
        mOldColorPanelView = (ColorPanelView) findViewById(R.id.colorpickerview__color_panel_old);
        mNewColorPanelView = (ColorPanelView) findViewById(R.id.colorpickerview__color_panel_new);

        mOkButton = (Button) findViewById(R.id.okButton);
        mCancelButton = (Button) findViewById(R.id.cancelButton);


        ((LinearLayout) mOldColorPanelView.getParent()).setPadding(
                mColorPickerView.getPaddingLeft(), 0,
                mColorPickerView.getPaddingRight(), 0);

        mColorPickerView.setOnColorChangedListener(this);
        mColorPickerView.setColor(initialColor, true);
        mOldColorPanelView.setColor(initialColor);

        mOkButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
    }

    @Override
    public void onColorChanged(int newColor) {
        mNewColorPanelView.setColor(mColorPickerView.getColor());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okButton:
                int color = mColorPickerView.getColor();

                // Change color of view
                if(imgColor != null)
                    imgColor.setBackgroundColor(color);

                // Notify activity
                if(callback != null) {
                    if(prefColor != null)
                        callback.onColorPickSuccess(prefColor, color);
                    else if(imgColor.getTag() != null)
                        callback.onColorPickSuccess((int)imgColor.getTag(), color);
                }

                break;
            case R.id.cancelButton:
                break;
        }
        dismiss();
    }

    public interface OnColorPickListener{
        void onColorPickSuccess(String colorType, int color);
        void onColorPickSuccess(int position, int color);
    }
}
