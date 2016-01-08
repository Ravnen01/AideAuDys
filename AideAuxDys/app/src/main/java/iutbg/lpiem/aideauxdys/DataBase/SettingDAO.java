package iutbg.lpiem.aideauxdys.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import iutbg.lpiem.aideauxdys.Model.Setting;

public class SettingDAO extends DAOBase{
    public static final String TABLE_NAME = "Setting";
    public static final String KEY = "id";
    public static final String SCHEMA = "schema";
    public static final String GRAS = "gras";
    public static final String ITALIQUE = "italique";
    public static final String SOULIGNER = "souligner";
    public static final String TAILLE = "taille";
    public static final String POLICE = "police";
    public static final String COULEUR = "couleur";

    public static final String SETTING_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SCHEMA + " TEXT, " +
                    GRAS + " INTEGER, " +
                    ITALIQUE + " INTEGER, " +
                    SOULIGNER + " INTEGER, " +
                    TAILLE + " INTEGER, " +
                    POLICE + " TEXT, " +
                    COULEUR + " INTEGER);";

    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public SettingDAO(Context pContext) {
        super(pContext);
    }

    public void add(Setting s) {
        ContentValues value = new ContentValues();
        value.put(SCHEMA,s.getSchema());
        value.put(GRAS, (s.isBold())?1:0);
        value.put(ITALIQUE,(s.isItalic())?1:0);
        value.put(SOULIGNER,(s.isUnderline())?1:0);
        value.put(TAILLE, s.getSize());
        value.put(POLICE, s.getFont());
        value.put(COULEUR, s.getColor());
        mDb.insert(TABLE_NAME, null, value);
    }

    public void remove(long id) {
        mDb.delete(TABLE_NAME, KEY + " = ?", new String[]{String.valueOf(id)});
    }

    public void update(Setting s) {
        ContentValues value = new ContentValues();
        if(s != null) {
            value.put(SCHEMA, s.getSchema());
            value.put(GRAS, (s.isBold()) ? 1 : 0);
            value.put(ITALIQUE, (s.isItalic()) ? 1 : 0);
            value.put(SOULIGNER, (s.isUnderline()) ? 1 : 0);
            value.put(TAILLE, s.getSize());
            value.put(POLICE, s.getFont());
            value.put(COULEUR, s.getColor());
            mDb.update(TABLE_NAME, value, KEY + " = ?", new String[]{String.valueOf(s.getId())});
        }
    }

    public List<Setting> getAll() {
        Cursor c = mDb.rawQuery("select * from " + TABLE_NAME, null);
        List<Setting> settingList = new ArrayList<>();
        while (c.moveToNext()){
            settingList.add(new Setting(c.getLong(0),
                    c.getString(1),
                    (c.getInt(2) == 1),
                    (c.getInt(3) == 1),
                    (c.getInt(4) == 1),
                    c.getInt(5),
                    c.getString(6),
                    c.getInt(7)));
        }
        c.close();
        return settingList;
    }
}

