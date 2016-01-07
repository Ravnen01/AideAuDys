package iutbg.lpiem.aideauxdys.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import iutbg.lpiem.aideauxdys.Manager.FontManager;

public class SpinnerAdapter extends ArrayAdapter {
    private FontManager fontManager;

    public SpinnerAdapter(Context context, String[] objects) {
        super(context, android.R.layout.simple_spinner_item, objects);
        fontManager = new FontManager(context);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        fontManager.setTypeFace(v, v.getText().toString());
        return v;
    }
}
