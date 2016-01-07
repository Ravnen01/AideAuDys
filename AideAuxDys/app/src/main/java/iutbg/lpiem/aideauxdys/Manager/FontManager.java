package iutbg.lpiem.aideauxdys.Manager;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

import java.io.IOException;

public class FontManager {
    private Context context;

    public FontManager(Context context) {
        this.context = context;
    }

    public String[] getFontList(){
        AssetManager assetManager = context.getAssets();
        try {
            return assetManager.list("Fonts");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setTypeFace(TextView textView, String fontName){
        Typeface font = Typeface.createFromAsset(context.getAssets(), "Fonts/"+fontName);
        textView.setTypeface(font);
    }
}
