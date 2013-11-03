package com.example.RushHour.DAO;

/*
     Created by vidirr on 11/1/13.
 */


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


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
    private String[] LevelsFinishedColumns = {DbHelper.COLUMN_ID};
    private String[] CurrentPositionColumns = {DbHelper.COLUMN_CURRENT_LEVEL, DbHelper.COLUMN_POSITON};

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
            Adds an entry to the LEVELS_FINISHED table so that we can keep track of the levels that
            the player has finished.

            Accepts an integer levelID as a parameter.
     */
    public void markFinishedLevel(int levelID) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_ID, levelID);

        //No idea why we need null here.
        System.out.println("Level ID: " + levelID);
        try {
            long insertId = db.insert(DbHelper.TABLE_LEVELS_FINISHED, null, values);
        }
        catch (SQLException e) {
            System.out.println("SQL EXCEPTION!");
            System.out.println(e);
        }

        /* From tutorial. Probably deletable - but keeping it here if needed for future reference.
        Cursor cursor = database.query(DbHelper.TABLE_LEVELS_FINISHED,
                allColumns, DbHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        cursor.close();
        return newComment;
        */
    }

    /*
            Removes the entry for a level with the given ID from the LEVELS_FINISHED table.
            Usable for example if the user wants to forget a finished level, or if we want to allow the user to reset the game.
     */
    public void deleteFinishedLevel(int levelID) {
        System.out.println("Comment deleted with id: " + levelID);

        db.delete(DbHelper.TABLE_LEVELS_FINISHED, DbHelper.COLUMN_ID
                + " = " + levelID + 1, null);
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

}