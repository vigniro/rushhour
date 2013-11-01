package com.example.RushHour;

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

    public static final String TABLE_CURRENT_POSITION = "current_position";
    public static final String COLUMN_CURRENT_LEVEL = "_level_id";
    public static final String COLUMN_POSITON = "_position";


    private static final String DATABASE_NAME = "RushHour.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            //Table that simply stores the ID's of levels that the player has finished.
            + TABLE_LEVELS_FINISHED + "(" + COLUMN_ID
            + " integer unique);"
            //Table to store the current layout of the playing board, contains level ID and a string representation of the board.
            + "create table " + TABLE_CURRENT_POSITION + "(" + COLUMN_CURRENT_LEVEL + " integer unique, "
            + COLUMN_POSITON + "text not null);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS_FINISHED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENT_POSITION);
        onCreate(db);
    }

}