package com.example.RushHour.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by vidirr on 11/1/13.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String TABLE_LEVELS_FINISHED = "levels_finished";
    public static final String COLUMN_ID = "_id";

    public static final String TABLE_CURRENT_LEVEL = "current_level";


    public static final String[] TableLevelsFinishedCols = {COLUMN_ID};
    public static final String[] TableCurrentLevelCols = {COLUMN_ID};

    private static final String DATABASE_NAME = "RushHour.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            //Table that simply stores the ID's of levels that the player has finished.
            + TABLE_LEVELS_FINISHED + "(" + COLUMN_ID + " integer unique);"
            //Table to store the current level we're on. Should only hold 1 record at all times.
            + "create table " + TABLE_CURRENT_LEVEL + "(" + COLUMN_ID + " integer unique);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        //Initialize the current_level table with the ID of the first level - since if we're creating the DB that means that the player is on level 0.
        //Levels are 0-based indexed.
        database.execSQL("INSERT INTO " + TABLE_CURRENT_LEVEL + " VALUES(1, 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS_FINISHED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENT_LEVEL);
        onCreate(db);
    }

    public void reinitDatabase(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS_FINISHED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENT_LEVEL);
        onCreate(db);
    }

}