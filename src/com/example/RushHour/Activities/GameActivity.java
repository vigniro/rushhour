package com.example.RushHour.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import android.widget.LinearLayout;
import com.example.RushHour.DAO.RushHourAdapter;
import com.example.RushHour.GameObjects.Puzzle;
import com.example.RushHour.OnGameWonEventHandler;
import com.example.RushHour.R;
import com.example.RushHour.RushHour;
import com.example.RushHour.Views.BoardView;
import com.example.RushHour.XMLParser;

import java.util.ArrayList;
import java.util.List;

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
    RushHourAdapter db;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game);
        db = new RushHourAdapter(this);

        //currPuzzle = 0;
        //DEBUG THIS BITCH!!!!!!!!!!!!!!!!!!!!
        currPuzzle = db.getCurrentLevel();
        //if(currPuzzle == -1)
        //   currPuzzle = 0;


        if(this.getIntent().hasExtra("currPuzzle"))
        {
            Bundle parameters = this.getIntent().getExtras();
            Integer puzzleListChoice = parameters.getInt("currPuzzle");
            if(puzzleListChoice != null)
            {
                currPuzzle = puzzleListChoice;
            }
        }

        boardView = (BoardView) findViewById( R.id.boardview );
        boardView.setWinHandler(new OnGameWonEventHandler(){
            public void win(){
                puzzleSolved();
            }
        });

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

    private void puzzleSolved(){
        try{
            db.markLevelAsFinished(currPuzzle);
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

        List<Integer> levels = db.getFinishedLevels();

        for(Integer level : levels) {
            System.out.println("Level finished: " + level);
        }
        nextPuzzle();
    }

    public void buttonWin(View view)
    {
        try{
            db.markLevelAsFinished(currPuzzle);
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

        List<Integer> levels = db.getFinishedLevels();

        for(Integer level : levels) {
            System.out.println("Level finished: " + level);
        }


        nextPuzzle();
    }

    public void buttonReset(View view){
        boardView.setBoard(puzzles.get(currPuzzle), boardView.getWidth(), boardView.getHeight());
    }

    public void buttonSkip(View view){
            nextPuzzle();
    }

    private void nextPuzzle(){
        if(currPuzzle < puzzles.size())
            currPuzzle++;
        boardView.setBoard(puzzles.get(currPuzzle), boardView.getWidth(), boardView.getHeight());
    }

    @Override
    public void onSaveInstanceState( Bundle savedInstanceState ) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("stateRushHour", rushHour.toString());
    }

}
