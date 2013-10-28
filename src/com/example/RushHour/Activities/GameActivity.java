package com.example.RushHour.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import com.example.RushHour.GameObjects.Puzzle;
import com.example.RushHour.R;
import com.example.RushHour.RushHour;
import com.example.RushHour.Views.BoardView;
import com.example.RushHour.XMLParser;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: GSUS
 * Date: 27.10.2013
 * Time: 22:24
 * To change this template use File | Settings | File Templates.
 */
public class GameActivity extends Activity {

    BoardView boardView;
    RushHour rushHour;
    ArrayList<Puzzle> puzzles;
    private int currPuzzle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

         /*
        rushHour = new RushHour();
        if ( savedInstanceState != null ) {
            String state = savedInstanceState.getString( "stateRushHour" );
            rushHour.set( state );
        }
            */
        currPuzzle = 0;
        boardView = (BoardView) findViewById( R.id.boardview );
        ViewTreeObserver vto = boardView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                XMLParser parser = new XMLParser();
                puzzles = parser.parsePuzzleFile();//(ArrayList<Puzzle>)i.getSerializableExtra("puzzles");
                boardView.setBoard(puzzles.get(currPuzzle), boardView.getWidth(), boardView.getHeight());
            }

        });

    }

    public void buttonReset(View view){
        boardView.setBoard(puzzles.get(currPuzzle), boardView.getWidth(), boardView.getHeight());
    }

    public void buttonSkip(View view){
        currPuzzle++;
        boardView.setBoard(puzzles.get(currPuzzle), boardView.getWidth(), boardView.getHeight());
    }

    @Override
    public void onSaveInstanceState( Bundle savedInstanceState ) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("stateRushHour", rushHour.toString());
    }

}
