package com.example.roman.interaction;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to manipulate and send requests to the SQL database
 */
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
     * Converts latin name to common name.
     * @param latin latin to be convered
     * @return common name if available, else an empty string
     */
    public String getCommon(String latin) {
        // get common name where latin matches the parameter
        Cursor cursor = database.rawQuery("SELECT common FROM latintocommon WHERE latin = '"+latin+"'", null);

        // if cursor is empty, return an empty string
        if (cursor.getCount() == 0) {
            return "";
        }
        // get first common name
        cursor.moveToFirst();
        String commonName = cursor.getString(0);
        cursor.close();

        // strip string from language prefix and trailing whitespace
        String common = StringUtils.strip(commonName, "@en").trim();
        return " (" + common + ")";
    }

    /**
     * Retrieves all available species from the database to be used for autocomplete function.
     * @return string array of all taxa
     */
    public String[] getAllTaxa() {
        // select all taxa from table
        Cursor cursor = database.rawQuery("SELECT * FROM autocomplete", null);

        // check if cursor isn't empty
        if (cursor.getCount() > 0) {

            // create new string array
            String[] taxa = new String[cursor.getCount()];

            // put all species into array
            int i = 0;
            while (cursor.moveToNext()) {
                taxa[i] = cursor.getString(cursor.getColumnIndex("taxa"));
                i++;
            }
            cursor.close();

            return taxa;
        }
        // if cursor is empty, return empty string
        else {
            cursor.close();
            return new String[] {};
        }
    }

    /**
     * Retrieves all URLs connected to a species
     * @param latin latin to search for urls
     * @return array of urls
     */
    public String[] getAllUrls(String latin) {
        // trim the latin for use in database
        String latinTrimmed = latin.replaceAll("\\(.*?\\)","").trim();

        // select urls that fit the latin parameter
        Cursor cursor = database.rawQuery("SELECT * FROM urlMap WHERE taxa = '"+latinTrimmed+"'", null);

        // check if cursor isnt empty
        if (cursor.getCount() >0) {

            // make new string array and fill with urls
            String[] urls = new String[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                urls[i] = cursor.getString(cursor.getColumnIndex("url"));
                i++;
            }
            cursor.close();

            return urls;
        }
        // if cursor is empty, return an empty string array
        else {
            cursor.close();
            return new String [] {};
        }
    }

}
