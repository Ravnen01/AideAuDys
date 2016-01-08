package iutbg.lpiem.aideauxdys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import iutbg.lpiem.aideauxdys.Adapter.PrefAdapter;
import iutbg.lpiem.aideauxdys.Adapter.SpinnerAdapter;
import iutbg.lpiem.aideauxdys.DataBase.SettingDAO;
import iutbg.lpiem.aideauxdys.Manager.FontManager;
import iutbg.lpiem.aideauxdys.Manager.PreferenceManager;
import iutbg.lpiem.aideauxdys.Model.Setting;

public class SettingActivity extends AppCompatActivity implements ColorDialog.OnColorPickListener {
    private PreferenceManager preferenceManager;
    private ListView lstVwSetting;
    private ImageView imgTxtColor;
    private ImageView imgBackColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Initialisation
        preferenceManager = new PreferenceManager(this);
        FontManager fontManager = new FontManager(this);
        lstVwSetting = (ListView) findViewById(R.id.setting_lstVw_pref);
        imgTxtColor = (ImageView) findViewById(R.id.setting_imgVw_txtColor);
        imgBackColor = (ImageView) findViewById(R.id.setting_imgVw_backColor);
        EditText edtTxtSize = (EditText) findViewById(R.id.setting_edtTxt_txtSize);
        Button btnBold = (Button) findViewById(R.id.setting_bttn_bold);
        Button btnItalic = (Button) findViewById(R.id.setting_bttn_italic);
        Button btnUnderLine = (Button) findViewById(R.id.setting_bttn_underline);
        Button btnAdd = (Button) findViewById(R.id.setting_bttn_add);
        Switch switchSyllabe = (Switch) findViewById(R.id.setting_switch_syllabe);
        Spinner spinnerFonts = (Spinner) findViewById(R.id.setting_spinner_fonts);

        String[] fontList = fontManager.getFontList();
        String fontName = preferenceManager.getFontName();
        // On intervertit la premiere police avec celle choisie
        for (int i = 0; i < fontList.length; i++) {
            if (fontList[i].equals(fontName)) {
                String tmps = fontList[0];
                fontList[0] = fontName;
                fontList[i] = tmps;
            }
        }
        SpinnerAdapter spinnerArrayAdapter = new SpinnerAdapter(this, fontList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFonts.setAdapter(spinnerArrayAdapter);
        spinnerFonts.setPrompt(preferenceManager.getFontName());

        setButtonStyle(btnBold, preferenceManager.getIsBold());
        setButtonStyle(btnItalic, preferenceManager.getIsItalic());
        setButtonStyle(btnUnderLine, preferenceManager.getIsUnderLine());

        edtTxtSize.setText(String.valueOf(preferenceManager.getTextSize()));
        switchSyllabe.setChecked(preferenceManager.getIsCutSyllabe());
        lstVwSetting.setAdapter(new PrefAdapter(this));

        imgTxtColor.setBackgroundColor(preferenceManager.getColor(PreferenceManager.PREFS_TEXT_COLOR));
        imgBackColor.setBackgroundColor(preferenceManager.getColor(PreferenceManager.PREFS_BACK_COLOR));

        // Listener
        spinnerFonts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view != null) {
                    String font = ((TextView) view).getText().toString();
                    preferenceManager.saveFontName(font);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PrefAdapter) lstVwSetting.getAdapter()).addSetting();
            }
        });
        switchSyllabe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                preferenceManager.saveIsCutSyllabe(isChecked);
            }
        });
        imgTxtColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayColorPicker(PreferenceManager.PREFS_TEXT_COLOR);
            }
        });
        imgBackColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayColorPicker(PreferenceManager.PREFS_BACK_COLOR);
            }
        });
        edtTxtSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Si le champ n'est pas vide
                if (!s.toString().equals("")) {
                    int size = Integer.parseInt(s.toString());
                    preferenceManager.saveTextSize(size);
                }
            }
        });
        View.OnClickListener clickStyle = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.setting_bttn_bold:
                        boolean isBold = !preferenceManager.getIsBold();
                        preferenceManager.saveisBold(isBold);
                        setButtonStyle(v, isBold);
                        break;
                    case R.id.setting_bttn_italic:
                        boolean isItalic = !preferenceManager.getIsItalic();
                        preferenceManager.saveIsItalic(isItalic);
                        setButtonStyle(v, isItalic);
                        break;
                    case R.id.setting_bttn_underline:
                        boolean isUnderLine = !preferenceManager.getIsUnderLine();
                        preferenceManager.saveIsUnderLine(isUnderLine);
                        setButtonStyle(v, isUnderLine);
                        break;
                }
            }
        };
        btnBold.setOnClickListener(clickStyle);
        btnItalic.setOnClickListener(clickStyle);
        btnUnderLine.setOnClickListener(clickStyle);
    }

    private void setButtonStyle(View v, boolean isClicked) {
        if (isClicked)
            v.setBackgroundColor(getResources().getColor(R.color.colorButtonClick));
        else
            v.setBackgroundColor(getResources().getColor(R.color.colorButton));
    }

    private void displayColorPicker(String colorType) {
        ColorDialog dialog = new ColorDialog(this, colorType);
        dialog.show();
    }

    @Override
    public void onColorPickSuccess(String colorType, int color) {
        if(colorType != null) {
            if (colorType.equals(PreferenceManager.PREFS_BACK_COLOR)) {
                imgBackColor.setBackgroundColor(color);
            } else if (colorType.equals(PreferenceManager.PREFS_TEXT_COLOR)) {
                imgTxtColor.setBackgroundColor(color);
            }
            preferenceManager.saveColor(colorType, color);
        }
    }

    @Override
    public void onColorPickSuccess(int position, int color) {
        // Cas des pickers des preferences custom
        ((PrefAdapter)lstVwSetting.getAdapter()).setColorPickerValue(color, position);
    }
}
