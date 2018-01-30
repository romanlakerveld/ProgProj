package com.example.roman.interaction;

/**
 * Created by roman on 21/01/2018.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private  SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public String getCommon(String latin) {
        Cursor cursor = database.rawQuery("SELECT common FROM latintocommon WHERE latin = '"+latin+"'", null);
        if (cursor.getCount() == 0) {
            return "";
        }
        cursor.moveToFirst();
        String latinName = cursor.getString(0);
        cursor.close();
        String common = StringUtils.strip(latinName, "@en").trim();
        return " (" + common + ")";
    }

    public String[] getAllTaxa() {
        Cursor cursor = database.rawQuery("SELECT * FROM autocomplete", null);

        if (cursor.getCount() > 0) {
            String[] taxa = new String[cursor.getCount()];
            int i = 0;

            while (cursor.moveToNext()) {
                taxa[i] = cursor.getString(cursor.getColumnIndex("taxa"));
                i++;
            }
            cursor.close();
            return taxa;
        }
        else {
            cursor.close();
            return new String[] {};
        }
    }

    public String[] getAllUrls(String latin) {

        String latinTrimmed = latin.replaceAll("\\(.*?\\)","").trim();
        Cursor cursor = database.rawQuery("SELECT * FROM urlMap WHERE taxa = '"+latinTrimmed+"'", null);

        if (cursor.getCount() >0) {
            String[] urls = new String[cursor.getCount()];
            int i = 0;

            while (cursor.moveToNext()) {
                urls[i] = cursor.getString(cursor.getColumnIndex("url"));
                i++;
            }
            cursor.close();
            return urls;
        }
        else {
            cursor.close();
            return new String [] {};
        }
    }

}
