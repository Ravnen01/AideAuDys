package iutbg.lpiem.aideauxdys.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


import iutbg.lpiem.aideauxdys.DataBase.SettingDAO;
import iutbg.lpiem.aideauxdys.Interface.Observeur;
import iutbg.lpiem.aideauxdys.Manager.FormaterManager;
import iutbg.lpiem.aideauxdys.Model.Setting;
import iutbg.lpiem.aideauxdys.R;

/**
 * Created by Corentin on 13/01/2016.
 */
public class ChoixPrefAdapteur extends BaseAdapter implements iutbg.lpiem.aideauxdys.Interface.Observable{
    private List<Setting>listSetting;
    private Context context;
    private ArrayList<Observeur> listObserveur=new ArrayList<>();

    public ChoixPrefAdapteur(Context context) {
        this.context=context;
        SettingDAO settingDAO=new SettingDAO(this.context);
        settingDAO.open();
        listSetting= settingDAO.getAll();
        settingDAO.close();
    }

    @Override
    public int getCount() {
        return listSetting.size();
    }

    @Override
    public Object getItem(int position) {
        return listSetting.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout layout;
        //if it the first creation
        if(convertView == null) {
            layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.choix_pref_adapter, null);
        } else {
            // reuse an existing view so we need to re-set all parameters
            layout = (RelativeLayout) convertView;
        }
        final Setting setting=listSetting.get(position);
        String schema=setting.getSchema();
        FormaterManager formaterManager=new FormaterManager(context);
        schema=formaterManager.formatWithPref(schema);
        WebView webView=(WebView)layout.findViewById(R.id.wvChoixPref);
        webView.loadDataWithBaseURL("file:///android_asset/Fonts/", schema, "text/html", "utf-8", null);

        Switch switchDrower=(Switch)layout.findViewById(R.id.switchChoixPref);
        switchDrower.setChecked(setting.isEnabled());
        switchDrower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingDAO settingDAO=new SettingDAO(context);
                settingDAO.open();
                setting.setEnabled(isChecked);
                settingDAO.update(setting);
                settingDAO.close();
                notif();
            }
        });
        return layout;
    }

    @Override
    public void addObserveur(Observeur observeur) {
        listObserveur.add(observeur);
    }

    @Override
    public void notif() {
        for(int i=0;i<listObserveur.size();i++){
            listObserveur.get(i).update();
        }
    }
}
