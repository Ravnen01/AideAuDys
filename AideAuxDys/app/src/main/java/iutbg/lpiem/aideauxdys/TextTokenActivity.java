package iutbg.lpiem.aideauxdys;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.List;

import iutbg.lpiem.aideauxdys.Adapter.ChoixPrefAdapteur;

import iutbg.lpiem.aideauxdys.DataBase.SettingDAO;

import iutbg.lpiem.aideauxdys.Fragment.TextTokenFragment;

import iutbg.lpiem.aideauxdys.Interface.Observeur;
import iutbg.lpiem.aideauxdys.Manager.FormaterManager;
import iutbg.lpiem.aideauxdys.Manager.PreferenceManager;
import iutbg.lpiem.aideauxdys.Manager.TextReader;
import iutbg.lpiem.aideauxdys.Model.Setting;


public class TextTokenActivity extends AppCompatActivity{

    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/AideAuxDysOCR/";
    private TextReader textReader;
    private String recoText = "";
    private MenuItem itemPlayPause;
    private Drawable iconPlay, iconPause;
    private TextTokenFragment fragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_token);
        recoText = getIntent().getStringExtra("recoString");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        fragment=new TextTokenFragment();
        fragment.setRecoText(recoText);
        ListView listView=(ListView)findViewById(R.id.lvChoixPref);
        ChoixPrefAdapteur adpteur=new ChoixPrefAdapteur(getApplicationContext());
        adpteur.addObserveur(fragment);

        listView.setAdapter(adpteur);


        textReader = new TextReader(this);
        getFragmentManager().beginTransaction().replace(R.id.flMainActivity, fragment).commit();

        iconPause = getResources().getDrawable(android.R.drawable.ic_media_pause);
        iconPlay = getResources().getDrawable(android.R.drawable.ic_media_play);


        

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_playPause:
                if (itemPlayPause.getIcon().equals(iconPlay))
                    readText();
                else
                    pauseReader();
                return true;
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_partage:
                createPDF();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void pauseReader() {
        if (textReader.isSpeaking()) {
            textReader.stopSpeaking();
        }
        switchButtonSpeak();
    }

    private void readText() {
        if (textReader.isInit() && !recoText.equals("")) {
            textReader.say(fragment.getRecoText());
            switchButtonSpeak();
        }
    }

    private void switchButtonSpeak() {
        if (itemPlayPause.getIcon().equals(iconPause)) {
            itemPlayPause.setIcon(iconPlay);
        } else {
            itemPlayPause.setIcon(iconPause);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        itemPlayPause = menu.findItem(R.id.action_playPause);

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textReader.destroy();
    }

    private void createPDF() {
        FormaterManager formaterManager = new FormaterManager(getApplicationContext());
        try {
            OutputStream file = new FileOutputStream(new File(DATA_PATH + "test.pdf"));
            Document document = new Document();
            PdfWriter.getInstance(document, file);
            document.open();
            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.setStyleSheet(generateStyleSheet());
            String str = "<html><body>"+formaterManager.formatWithPref(recoText)+"</body></html>";
            htmlWorker.parse(new StringReader(str));
            document.close();
            file.close();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("application/pdf");
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(DATA_PATH + "test.pdf")));
            startActivity(Intent.createChooser(i, getString(R.string.shareFile)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StyleSheet generateStyleSheet() {
        SettingDAO settingDAO = new SettingDAO(this);
        settingDAO.open();
        List<Setting> settingList = settingDAO.getAll();
        settingDAO.close();
        PreferenceManager preferenceManager = new PreferenceManager(this);
        String fontName = preferenceManager.getFontName();
        FontFactory.register("assets/Fonts/" + fontName, fontName.split("\\.")[0]);

        StyleSheet css = new StyleSheet();

        css.loadTagStyle("body", "face", fontName.split("\\.")[0]);
        css.loadTagStyle("body", "size", preferenceManager.getSize() + "px");
        css.loadTagStyle("body", "color", String.format("#%06X", 0xFFFFFF & preferenceManager.getColor(PreferenceManager.PREFS_TEXT_COLOR)));
        css.loadTagStyle("body", "background-color", String.format("#%06X", 0xFFFFFF & preferenceManager.getColor(PreferenceManager.PREFS_BACK_COLOR)));

        return css;
    }




    @Override
    protected void onRestart() {
        super.onRestart();

        ListView listView=(ListView)findViewById(R.id.lvChoixPref);
        ChoixPrefAdapteur adpteur=new ChoixPrefAdapteur(getApplicationContext());
        recoText=fragment.getRecoText();
        fragment=new TextTokenFragment();
        fragment.setRecoText(recoText);

        adpteur.addObserveur(fragment);
        listView.setAdapter(adpteur);

        getFragmentManager().beginTransaction().replace(R.id.flMainActivity, fragment).commit();

    }
}