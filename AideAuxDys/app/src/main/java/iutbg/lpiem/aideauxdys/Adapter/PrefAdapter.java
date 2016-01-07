package iutbg.lpiem.aideauxdys.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import iutbg.lpiem.aideauxdys.DataBase.SettingDAO;
import iutbg.lpiem.aideauxdys.Model.Setting;
import iutbg.lpiem.aideauxdys.R;

public class PrefAdapter extends BaseAdapter {
    private List<Setting> settingList = new ArrayList<Setting>();
    private SettingDAO settingDAO;
    private Context mContext;

    public PrefAdapter(Context mContext) {
        this.mContext = mContext;
        settingDAO = new SettingDAO(mContext);
        actualiser();
    }

    @Override
    public int getCount() {
        return settingList.size();
    }

    @Override
    public Setting getItem(int position) {
        return settingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.pref_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imgDelete = (ImageView) convertView.findViewById(R.id.item_imgVw_delete);
            viewHolder.imgTxtColor = (ImageView) convertView.findViewById(R.id.item_imgVw_txtColor);
            viewHolder.edtSchema = (EditText) convertView.findViewById(R.id.item_edtTxt_schema);
            viewHolder.edtSize = (EditText) convertView.findViewById(R.id.item_edtTxt_size);
            viewHolder.btnBold = (Button) convertView.findViewById(R.id.item_bttn_bold);
            viewHolder.btnItalic = (Button) convertView.findViewById(R.id.item_bttn_italic);
            viewHolder.btnUnderline = (Button) convertView.findViewById(R.id.item_bttn_underline);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        initListener(position, viewHolder);

        return  convertView;
    }

    private void initListener(final int position, ViewHolder viewHolder) {

        viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingDAO.open();
                settingDAO.remove(getItem(position).getId());
                settingDAO.close();
                settingList.remove(position);
            }
        });

        viewHolder.edtSchema.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Setting setting = settingList.get(position);
                setting.setSchema(s.toString());
                settingDAO.open();
                settingDAO.update(setting);
                settingDAO.close();
            }
        });

        viewHolder.edtSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Setting setting = settingList.get(position);
                setting.setSize(Integer.parseInt(s.toString()));
                settingDAO.open();
                settingDAO.update(setting);
                settingDAO.close();
            }
        });

        View.OnClickListener clickStyle = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setting setting = settingList.get(position);

                switch (v.getId()){
                    case R.id.item_bttn_bold :
                        boolean isBold = !setting.isBold();
                        setButtonStyle(v, isBold);
                        setting.setBold(isBold);
                        break;
                    case R.id.item_bttn_italic :
                        boolean isItalic = !setting.isItalic();
                        setButtonStyle(v, isItalic);
                        setting.setItalic(isItalic);
                        break;
                    case R.id.item_bttn_underline :
                        boolean isUnderline = !setting.isUnderline();
                        setButtonStyle(v, isUnderline);
                        setting.setUnderline(isUnderline);
                        break;
                }

                settingDAO.open();
                settingDAO.update(setting);
                settingDAO.close();
            }
        };
        viewHolder.btnBold.setOnClickListener(clickStyle);
        viewHolder.btnItalic.setOnClickListener(clickStyle);
        viewHolder.btnUnderline.setOnClickListener(clickStyle);
    }

    private void setButtonStyle(View v, boolean isClicked) {
        if(isClicked)
            v.setBackgroundColor(mContext.getResources().getColor(R.color.colorButtonClick));
        else
            v.setBackgroundColor(mContext.getResources().getColor(R.color.colorButton));
    }

    public void addSetting() {
        Setting setting = new Setting(0, "", false, false, false, 16, "", "#000");

        settingDAO.open();
        settingDAO.add(setting);
        settingList.add(setting);
        settingDAO.close();

        notifyDataSetChanged();
    }

    private class ViewHolder{
        public ImageView imgDelete, imgTxtColor;
        public EditText edtSchema, edtSize;
        public Button btnBold, btnItalic, btnUnderline;
    }

    public void actualiser(){
        settingDAO.open();
        settingList = settingDAO.getAll();
        settingDAO.close();
        notifyDataSetChanged();
    }
}
