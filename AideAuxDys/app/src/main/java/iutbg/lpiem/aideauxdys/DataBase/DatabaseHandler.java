package iutbg.lpiem.aideauxdys.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String SETTING_KEY = "id";
    public static final String SETTING_SCHEMA = "schema";
    public static final String SETTING_GRAS = "gras";
    public static final String SETTING_ITALIQUE = "italique";
    public static final String SETTING_SOULIGNER = "souligner";
    public static final String SETTING_TAILLE = "taille";
    public static final String SETTING_COULEUR = "couleur";
    public static final String SETTING_ENABLED = "active";

    public static final String SETTING_TABLE_NAME = "Setting";
    public static final String SETTING_TABLE_CREATE =
            "CREATE TABLE " + SETTING_TABLE_NAME + " (" +
                    SETTING_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SETTING_SCHEMA + " TEXT, " +
                    SETTING_GRAS + " INTEGER, " +
                    SETTING_ITALIQUE + " INTEGER, " +
                    SETTING_SOULIGNER + " INTEGER, " +
                    SETTING_TAILLE + " INTEGER, " +
                    SETTING_COULEUR + " INTEGER," +
                    SETTING_ENABLED + " INTEGER);";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SETTING_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

