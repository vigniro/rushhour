package com.example.RushHour.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.RushHour.GameObjects.Puzzle;
import com.example.RushHour.R;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: GSUS
 * Date: 27.10.2013
 * Time: 22:02
 * To change this template use File | Settings | File Templates.
 */
public class MenuActivity extends Activity {
    public ArrayList<Puzzle> puzzles;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        //XMLParser parser = new XMLParser();
        //puzzles = parser.parsePuzzleFile();
    }

    public void buttonPlay( View view ) {
        Intent intent = new Intent( this, GameActivity.class );
        startActivity( intent );
    }

    public void buttonPuzzles( View view ) {
        Intent intent = new Intent( this, PuzzlesActivity.class );
        //intent.putExtra("puzzles", puzzles);
        startActivity(intent);
    }

    public void buttonOptions( View view )  {
        Intent intent = new Intent( this, OptionsActivity.class );
        startActivity( intent );
    }

    public void buttonAbout( View view ) {
        Intent intent = new Intent( this, AboutActivity.class );
        startActivity( intent );
    }

    @Override
    public void onSaveInstanceState( Bundle savedInstanceState ) {
        super.onSaveInstanceState(savedInstanceState);
    }
}
