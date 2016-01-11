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
                    if (!splitList.get(i).matches("<(.*)span(.*)>")) {
                        // On le coupe en fonction du schema
                        List<String> newSplitList = split(splitList.get(i), schema);
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

        String html = ListToString(splitList);

        html = generateCSSfromPref(html, settingList);

        return html;
    }

    private String ListToString(List<String> splitList) {
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
        for (int i = 0; i < splitList.size(); i++) {
            if (splitList.get(i).contains(setting.getSchema())) {
                splitList.add(i + 1, "</span>");
                splitList.add(i, "<span class=\"pref" + setting.getId() + "\">");
                i++;
            }
        }
    }

    private String generateCSSfromPref(String html, List<Setting> settingList) {
        String css = "<html><header><style>";

        // Global pref
        String fontName = preferenceManager.getFontName();
        css += "@font-face {\n" +
                "    font-family: '" + fontName + "';\n" +
                "    src: url('/" + fontName + "');\n" +
                "}\n\n";

        String style = "";
        if (preferenceManager.isItalic())
            style += "italic ";
        if (preferenceManager.isBold())
            style += "bold ";
        if (preferenceManager.isUnderLine())
            css += "text-decoration: underline;\n";

        css += ".prefGlobal{\n";
        css += "font:" + style + preferenceManager.getSize() + "px " + fontName + " Arial;\n";
        css += "color: "+String.format("#%06X", 0xFFFFFF & preferenceManager.getColor(PreferenceManager.PREFS_TEXT_COLOR))+"; \n";
        css += "background-color: "+String.format("#%06X", 0xFFFFFF & preferenceManager.getColor(PreferenceManager.PREFS_BACK_COLOR))+"; \n" + "}\n\n";

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


            css += "font:" + style + setting.getSize() + "px " + fontName + " Arial;\n";
            css += "}\n\n";
        }

        css += "</style></header><body><span class=\"prefGlobal\">";

        if (!html.isEmpty()) {
            css += html;
        }
        css += "</span></body></html>";

        return css;
    }
}
