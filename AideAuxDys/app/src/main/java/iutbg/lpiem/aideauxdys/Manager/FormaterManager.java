package iutbg.lpiem.aideauxdys.Manager;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import iutbg.lpiem.aideauxdys.DataBase.SettingDAO;
import iutbg.lpiem.aideauxdys.Model.Setting;

public class FormaterManager {
    private SettingDAO settingDAO;

    public FormaterManager(Context context) {
        settingDAO = new SettingDAO(context);
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
            // Pour chaque partie de mon texte (découper en fonction des schemas)
            int size = splitList.size();
            for (int i = size-1; i >= 0; i--) {
                if(!splitList.get(i).matches("<(.*)div(.*)>")) {
                    // On le coupe en fonction du schema
                    List<String> newSplitList = split(splitList.get(i), schema);
                    // Si un schema est trouvé
                    if (newSplitList.size() > 1) {
                        // et on ajoute les balises de style entre les schemas trouvé
                        addBalise(newSplitList, setting);
                        // On ajoute la liste balisé dans la liste final
                        insertListIn(splitList, i, newSplitList);
                        // decremente de 3 car ajout des balises
                        i-=1;
                    }
                }
            }
        }

        return ListToString(splitList);
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
                splitList.add(i + 1, "<\\div>");
                splitList.add(i, "<div class=\"pref" + setting.getId() + "\">");
                i++;
            }
        }
    }
}
