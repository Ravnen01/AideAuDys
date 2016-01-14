package iutbg.lpiem.aideauxdys.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import iutbg.lpiem.aideauxdys.Interface.Observeur;
import iutbg.lpiem.aideauxdys.Manager.FormaterManager;
import iutbg.lpiem.aideauxdys.Manager.PreferenceManager;
import iutbg.lpiem.aideauxdys.R;

/**
 * Created by Corentin on 14/01/2016.
 */
public class TextTokenFragment extends Fragment implements Observeur{
    private String recoText;
    private WebView webView;
    private Button btnEditer;
    private EditText edtTextEdition;
    private FormaterManager formaterManager;
    private View view;

    public TextTokenFragment() {



    }

    public String getRecoText() {
        return recoText;
    }

    public void setRecoText(String recoText) {
        this.recoText = recoText;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.text_token_fragment, container, false);
        webView = (WebView) view.findViewById(R.id.wvTextToken);
        btnEditer = (Button) view.findViewById(R.id.textToken_Button_editer);
        edtTextEdition = (EditText) view.findViewById(R.id.textToken_EdtText);
        formaterManager = new FormaterManager(view.getContext());
        edtTextEdition.setText(recoText);

        PreferenceManager preferenceManager=new PreferenceManager(view.getContext());
        if(preferenceManager.getIsCutSyllabe()) {
            webView.loadDataWithBaseURL("file:///android_asset/Fonts/", formaterManager.formatWithDecoupe(recoText), "text/html", "utf-8", null);
        }else{
            webView.loadDataWithBaseURL("file:///android_asset/Fonts/",formaterManager.formatWithPref(recoText), "text/html","utf-8",null);
        }


        btnEditer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.getVisibility() == View.GONE) {
                    btnEditer.setText(getString(R.string.textToken_editer));

                    webView.setVisibility(View.VISIBLE);
                    edtTextEdition.setVisibility(View.GONE);

                    recoText = edtTextEdition.getText().toString();
                    PreferenceManager preferenceManager=new PreferenceManager(view.getContext());
                    if(preferenceManager.getIsCutSyllabe()) {
                        webView.loadDataWithBaseURL("file:///android_asset/Fonts/", formaterManager.formatWithDecoupe(recoText), "text/html", "utf-8", null);
                    }else{
                        webView.loadDataWithBaseURL("file:///android_asset/Fonts/",formaterManager.formatWithPref(recoText), "text/html","utf-8",null);
                    }
                } else {
                    btnEditer.setText(getString(R.string.textToken_Save));

                    webView.setVisibility(View.GONE);
                    edtTextEdition.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    @Override
    public void update() {
        webView = (WebView) view.findViewById(R.id.wvTextToken);
        FormaterManager formaterManager=new FormaterManager(view.getContext());
        webView.loadDataWithBaseURL("file:///android_asset/Fonts/", formaterManager.formatWithPref(recoText), "text/html", "utf-8", null);
    }
}
