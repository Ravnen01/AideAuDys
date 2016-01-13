package iutbg.lpiem.aideauxdys;

import android.content.Intent;
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

import com.itextpdf.text.Document;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;

import iutbg.lpiem.aideauxdys.Manager.FormaterManager;
import iutbg.lpiem.aideauxdys.Manager.TextReader;

public class TextTokenActivity extends AppCompatActivity{
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/AideAuxDysOCR/";
    private TextReader textReader;
    private String recoText = "";
    private MenuItem itemPlay;
    private MenuItem itemPause;
    private WebView webView;
    private Button btnEditer;
    private EditText edtTextEdition;
    private FormaterManager formaterManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_token);
        recoText=getIntent().getStringExtra("recoString");
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        textReader = new TextReader(this);
        webView = (WebView) findViewById(R.id.wvTextToken);
        btnEditer = (Button) findViewById(R.id.textToken_Button_editer);
        edtTextEdition = (EditText) findViewById(R.id.textToken_EdtText);

        formaterManager = new FormaterManager(getApplicationContext());
        webView.loadDataWithBaseURL("file:///android_asset/Fonts/", formaterManager.formatWithDecoupe(recoText), "text/html","utf-8",null);
        edtTextEdition.setText(recoText);

        btnEditer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webView.getVisibility() == View.GONE) {
                    btnEditer.setText(getString(R.string.textToken_editer));

                    webView.setVisibility(View.VISIBLE);
                    edtTextEdition.setVisibility(View.GONE);

                    recoText = edtTextEdition.getText().toString();
                    webView.loadDataWithBaseURL("file:///android_asset/Fonts/", formaterManager.formatWithDecoupe(recoText), "text/html","utf-8",null);
                }else {
                    btnEditer.setText(getString(R.string.textToken_Save));

                    webView.setVisibility(View.GONE);
                    edtTextEdition.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_play:
                readText();
                return true;
            case R.id.action_pause:
                pauseReader();
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
            itemPause.setVisible(false);
            itemPlay.setVisible(true);
        }
    }

    private void readText() {
        if (textReader.isInit() && !recoText.equals("")) {
            textReader.say(recoText);
            itemPlay.setVisible(false);
            itemPause.setVisible(true);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        itemPlay = menu.findItem(R.id.action_play);
        itemPause = menu.findItem(R.id.action_pause);
        itemPause.setVisible(false);

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseReader();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textReader.destroy();
    }

    private void createPDF(){
        try{
            OutputStream file = new FileOutputStream(new File(Environment.getExternalStorageDirectory().toString() + "/AideAuxDysOCR/"+"test.pdf"));
            Document document = new Document();
            PdfWriter.getInstance(document, file);
            document.open();
            HTMLWorker htmlWorker = new HTMLWorker(document);
            FormaterManager formaterManager=new FormaterManager(getApplicationContext());
            String str = formaterManager.formatWithDecoupe(recoText);
            htmlWorker.parse(new StringReader(str));
            document.close();
            file.close();
            Intent i=new Intent(Intent.ACTION_SEND);
            i.setType("application/pdf");
            i.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/AideAuxDysOCR/"+"test.pdf")));
            startActivity(Intent.createChooser(i,getString(R.string.shareFile)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}