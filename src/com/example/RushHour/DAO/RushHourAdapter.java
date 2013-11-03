package com.example.RushHour.DAO;

/*
     Created by vidirr on 11/1/13.
 */


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


/*
        A wrapper class for all DB activity within the RushHour app.
        Could be extended to use entity models of the resources that we're working with,
        but it seems like an overkill since we're only working with the ID's and a
        string representation of the playing board at a given time.
 */


public class RushHourAdapter {
    // Database fields
    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private Context context;

    /*
            String array representation of the available columns of each table.
            This seems to be needed for querying the tables, and it's more verbose if we simply
            query on all columns (can be thought of as SELECT * FROM TABLE).
     */
    private String[] LevelsFinishedColumns = DbHelper.TableLevelsFinishedCols;
    private String[] CurrentLevelColumns = DbHelper.TableCurrentLevelCols;

    public RushHourAdapter(Context c) {
        context = c;
    }

    public RushHourAdapter openToRead() {
        dbHelper = new DbHelper( context );
        db = dbHelper.getReadableDatabase();
        return this;
    }

    public RushHourAdapter openToWrite() {
        dbHelper = new DbHelper( context  );
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        String dbpath = "/data/data/com.example.RushHour/databases/RushHour.db";
        try {
            checkDB = SQLiteDatabase.openDatabase(dbpath, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null ? true : false;
    }

    public void close() {
        db.close();
    }

    public long markLevelAsFinished(int levelID) {
        String[] cols = DbHelper.TableLevelsFinishedCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put(cols[0], ((Integer)levelID).toString() );
        openToWrite();
        long value = db.insert(DbHelper.TABLE_LEVELS_FINISHED, null, contentValues);
        close();
        return value;
    }

    /*
            Removes the entry for a level with the given ID from the LEVELS_FINISHED table.
            Usable for example if the user wants to forget a finished level, or if we want to allow the user to reset the game.
     */
    public void deleteFinishedLevel(int levelID) {
        System.out.println("Comment deleted with id: " + levelID);

        db.delete(DbHelper.TABLE_LEVELS_FINISHED, DbHelper.COLUMN_ID
                + " = " + levelID, null);
    }

    /*
            Returns an ArrayList of intgers that contain the ID's of the levels that the player has finished.
     */
    public List<Integer> getFinishedLevels() {
        List<Integer> levels = new ArrayList<Integer>();

        openToRead();
        Cursor cursor = db.query(DbHelper.TABLE_LEVELS_FINISHED,
                LevelsFinishedColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int finishedLevel = (int)cursor.getLong(0);
            levels.add(finishedLevel);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        close();
        return levels;
    }

    public int getCurrentLevel() {

        openToRead();

        String[] bla = {"*"};
        Cursor cursor = db.query(DbHelper.TABLE_CURRENT_LEVEL,
                bla, null, null, null, null, null);
        //Set to -1 for debugging purposes.
        int currentLevel = -1;
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            currentLevel = (int)cursor.getLong(1);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        System.out.println("CURRENT LEVEL ID: " + currentLevel);
        return currentLevel;
    }


    public void setCurrentLevel(int levelID) {
        openToWrite();

        String[] cols = DbHelper.TableCurrentLevelCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put(cols[0], 1);
        contentValues.put(cols[1], levelID);

        db.execSQL("DELETE FROM " + dbHelper.TABLE_CURRENT_LEVEL + " WHERE _id=1");
        db.insert(DbHelper.TABLE_CURRENT_LEVEL, null, contentValues);
        close();
    }


    public void reinitDatabase() {
        openToWrite();
        dbHelper.reinitDatabase(db);
        close();
    }


}