package iutbg.lpiem.aideauxdys.Manager;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import iutbg.lpiem.aideauxdys.DataBase.SettingDAO;
import iutbg.lpiem.aideauxdys.Model.Setting;

public class FormaterManager {
    private SettingDAO settingDAO;
    private final PreferenceManager preferenceManager;

    public FormaterManager(Context context) {
        settingDAO = new SettingDAO(context);
        preferenceManager = new PreferenceManager(context);
    }

    public String formatWithDecoupe(String text) {
        Decoupe decoupe;

        List<String> splitList = new ArrayList<>();
        text = text.replaceAll("[,.!?;:()]", " $0 ");
        Collections.addAll(splitList, text.split("((?<= )|(?= ))"));

        for (int i = 0; i < splitList.size(); i++) {
            String mot = splitList.get(i);
            if (mot.length() > 2) {
                decoupe = new Decoupe(mot);
                List<String> motSyllabe = decoupe.getSyllabe();
                if (motSyllabe.size() > 1) {
                    splitList.remove(i);
                    for (int j = motSyllabe.size()-1; j >= 0; j--) {
                        splitList.add(i, motSyllabe.get(j));
                    }
                }
            }
        }

        String[] listCouleur = {"#008000","#0000ff","#ff0000"};
        String finalText = "";
        for(int i = 0; i < splitList.size(); i++){
            finalText += "<span style=\"color: "+listCouleur[i%3]+";\">"+splitList.get(i)+"</span>";
        }

        return finalText;
    }

    public String formatWithPref(String text) {
        settingDAO.open();
        List<Setting> settingList = settingDAO.getAll();
        settingDAO.close();

        List<String> splitList = new ArrayList<>();
        splitList.add(text);

        // Pour chaque parametre
        for (Setting setting : settingList) {
            // On récupère le schema associé
            String schema = setting.getSchema();
            if (!schema.equals("")) {
                // Pour chaque partie de mon texte (découper en fonction des schemas)
                int size = splitList.size();
                for (int i = size - 1; i >= 0; i--) {
                    String part = splitList.get(i);
                    if (part != null && !part.matches("<(.*)span(.*)>")) {
                        // On le coupe en fonction du schema
                        List<String> newSplitList = split(part, schema);
                        // Si un schema est trouvé
                        if (newSplitList.size() > 1) {
                            // et on ajoute les balises de style entre les schemas trouvé
                            addBalise(newSplitList, setting);
                            // On ajoute la liste balisé dans la liste final
                            insertListIn(splitList, i, newSplitList);
                            // decremente de 3 car ajout des balises
                            i -= 1;
                        }
                    }
                }
            }
        }

        return listToString(splitList);
    }

    private String listToString(List<String> splitList) {
        String finalString = "";
        for (String part : splitList) {
            finalString += part;
        }
        return finalString;
    }

    private void insertListIn(List<String> oldList, int position, List<String> newList) {
        oldList.remove(position);
        for (int j = newList.size() - 1; j >= 0; j--) {
            oldList.add(position, newList.get(j));
        }
    }

    private List<String> split(String text, String schema) {
        List<String> myList = new ArrayList<>();
        Collections.addAll(myList, text.split("((?<=" + schema + ")|(?=" + schema + "))"));
        return myList;
    }

    private void addBalise(List<String> splitList, Setting setting) {
        String line, css = "color:"+String.format("#%06X", 0xFFFFFF & setting.getColor())+";";
        String font = preferenceManager.getFontName();
        String fontName = "";
        if (!font.trim().equals("")) {
            fontName = font.split("\\.")[0];
        }

        String style = "";
        if (setting.isItalic())
            style += "italic ";
        if (setting.isBold())
            style += "bold ";
        if (setting.isUnderline())
            css += "text-decoration: underline;";

        css += "font:" + style + setting.getSize() + "px '" + fontName + "' Arial;";

        for (int i = 0; i < splitList.size(); i++) {
            if (splitList.get(i).contains(setting.getSchema())) {
                line = "<span style=\""+css+"\">"+splitList.get(i)+"</span>";
                splitList.remove(i);
                splitList.add(i,line);
            }
        }
    }

    public String generateCSSfromPref(String html) {
        settingDAO.open();
        List<Setting> settingList = settingDAO.getAll();
        settingDAO.close();

        String css = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \n" +
                "   \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "    <head>\n" +
                "        <style type=\"text/css\">\n";

        // Global pref
        String font = preferenceManager.getFontName();
        String fontName = "";
        if (!font.trim().equals("")) {
            fontName = font.split("\\.")[0];
            css += "@font-face {\n" +
                    "    font-family: '" + fontName + "';\n" +
                    "    src: url('file:///android_asset/Fonts/" + font + "');\n" +
                    "}\n\n";
        }

        String colorText = String.format("#%06X", 0xFFFFFF & preferenceManager.getColor(PreferenceManager.PREFS_TEXT_COLOR));
        String colorBack = String.format("#%06X", 0xFFFFFF & preferenceManager.getColor(PreferenceManager.PREFS_BACK_COLOR));
        String globalCss = "color:"+colorText+";background-color:"+colorBack+";";

        String style = "";
        if (preferenceManager.isItalic())
            style += "italic ";
        if (preferenceManager.isBold())
            style += "bold ";
        if (preferenceManager.isUnderLine())
            globalCss += "text-decoration: underline;";

        globalCss += "font:" + style + preferenceManager.getSize() + "px '" + fontName + "' Arial;line-height: "+preferenceManager.getInterLigne()+";"+"letter-spacing: "+preferenceManager.getSpaceLetter()+"px;";

        /*String style = "";
        if (preferenceManager.isItalic())
            style += "italic ";
        if (preferenceManager.isBold())
            style += "bold ";
        if (preferenceManager.isUnderLine())
            css += "text-decoration: underline;\n";

        css += ".prefGlobal{\n";
        css += "font:" + style + preferenceManager.getSize() + "px '" + fontName + "' Arial;\n";
        css += "color: " + String.format("#%06X", 0xFFFFFF & preferenceManager.getColor(PreferenceManager.PREFS_TEXT_COLOR)) + "; \n";
        css += "background-color: " + String.format("#%06X", 0xFFFFFF & preferenceManager.getColor(PreferenceManager.PREFS_BACK_COLOR)) + "; \n" + "}\n\n";

        // Custom prefs
        for (Setting setting : settingList) {
            css += ".pref" + setting.getId() + "{\n";
            String color = String.format("#%06X", 0xFFFFFF & setting.getColor());
            css += "color: " + color + ";\n";

            style = "";
            if (setting.isItalic())
                style += "italic ";
            if (setting.isBold())
                style += "bold ";
            if (setting.isUnderline())
                css += "text-decoration: underline;\n";


            css += "font:" + style + setting.getSize() + "px '" + fontName + "' Arial;\n";
            css += "}\n\n";
        }*/

        css += "</style>\n</head>\n<body style=\""+globalCss+"\">\n";

        if (!html.isEmpty()) {
            css += html;
        }
        css += "    </body>\n" +
                "</html>";

        return css;
    }
}
