package iutbg.lpiem.aideauxdys.Manager;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    public static final String PREFS_TEXT_COLOR = "textColor";
    public static final String PREFS_BACK_COLOR = "backColor";
    private static final String PREFS_TEXT_SIZE = "textSize";
    private static final String PREFS_GRAS = "gras";
    private static final String PREFS_UNDERLINE = "souligne";
    private static final String PREFS_ITALIC = "italique";
    private static final String PREFS_TEXT_SYLLABE = "syllabe";
    private static final String PREFS_TEXT_FONT = "textFont";
    private static final String PREFS_INTER_LINE = "interligne";
    private static final String PREFS_SPACE_LETTER = "spaceLetter";
    private static final String PREFS_NAME = "SETTING";
    private SharedPreferences preferences;
    private Context context;

    public PreferenceManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREFS_NAME, 0);
    }

    /**
     * @param colorType Choisir entre PreferenceManager.PREFS_TEXT_COLOR et PreferenceManager.PREFS_BACK_COLOR
     * @param color
     */
    public void saveColor(String colorType, int color){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(colorType, color);
        editor.apply();
    }

    /**
     * @param colorType Choisir entre PreferenceManager.PREFS_TEXT_COLOR et PreferenceManager.PREFS_BACK_COLOR
     */
    public int getColor(String colorType){
        int defaultColor;
        if(colorType.equals(PREFS_BACK_COLOR))
            defaultColor = 0xFFFFFFFF;
        else
            defaultColor = 0xFF000000;

        return preferences.getInt(colorType, defaultColor);
    }

    /**
     * @param size
     */
    public void saveTextSize(int size){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREFS_TEXT_SIZE, size);
        editor.apply();
    }

    /**
     * @return textSize
     */
    public int getSize(){
        return preferences.getInt(PREFS_TEXT_SIZE, 20);
    }

    /**
     * @param size
     */
    public void saveInterLigne(int size){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREFS_INTER_LINE, size);
        editor.apply();
    }

    /**
     * @return textSize
     */
    public int getInterLigne(){
        return preferences.getInt(PREFS_INTER_LINE, 2);
    }

    /**
     * @param size
     */
    public void saveSpaceLetter(int size){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREFS_SPACE_LETTER, size);
        editor.apply();
    }

    /**
     * @return textSize
     */
    public int getSpaceLetter(){
        return preferences.getInt(PREFS_SPACE_LETTER, 1);
    }

    /**
     * @param isBold
     */
    public void saveisBold(boolean isBold){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREFS_GRAS, isBold);
        editor.apply();
    }

    /**
     * @return isBold
     */
    public boolean isBold(){
        return preferences.getBoolean(PREFS_GRAS, false);
    }

    /**
     * @param isItalic
     */
    public void saveIsItalic(boolean isItalic){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREFS_ITALIC, isItalic);
        editor.apply();
    }

    /**
     * @return isItalic
     */
    public boolean isItalic(){
        return preferences.getBoolean(PREFS_ITALIC, false);
    }

    /**
     * @param isUnderLine
     */
    public void saveIsUnderLine(boolean isUnderLine){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREFS_UNDERLINE, isUnderLine);
        editor.apply();
    }

    /**
     * @return isUnderLine
     */
    public boolean isUnderLine(){
        return preferences.getBoolean(PREFS_UNDERLINE, false);
    }

    /**
     * @param font
     */
    public void saveFontName(String font){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFS_TEXT_FONT, font);
        editor.apply();
    }

    /**
     * @return fontName
     */
    public String getFontName(){
        return preferences.getString(PREFS_TEXT_FONT, "OpenDyslexic-Regular.otf");
    }

    /**
     * @param isCutsyllabe
     */
    public void saveIsCutSyllabe(Boolean isCutsyllabe){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREFS_TEXT_SYLLABE, isCutsyllabe);
        editor.apply();
    }

    /**
     * @return isCutsyllabe
     */
    public Boolean isCutSyllabe(){
        return preferences.getBoolean(PREFS_TEXT_SYLLABE, false);
    }
}
