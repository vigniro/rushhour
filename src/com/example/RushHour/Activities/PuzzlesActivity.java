package com.example.RushHour.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.RushHour.DAO.RushHourAdapter;
import com.example.RushHour.GameObjects.Puzzle;
import com.example.RushHour.R;
import com.example.RushHour.XMLParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: GSUS
 * Date: 27.10.2013
 * Time: 22:12
 * To change this template use File | Settings | File Templates.
 */
public class PuzzlesActivity extends Activity
        implements AdapterView.OnItemClickListener {

    ListView listView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzles);

        //Populate the view with a list of puzzles
        XMLParser parser = new XMLParser();
        ArrayList<Puzzle> puzzles = parser.parsePuzzleFile();

        RushHourAdapter db = new RushHourAdapter(this);
        List<Integer> levels = db.getFinishedLevels();
        if(levels != null || levels.size() == 0){
            for(Integer lvl: levels){
                puzzles.get(lvl).completed = true;
            }
        }
        listView = (ListView) findViewById(R.id.puzzleList);
        listView.setOnItemClickListener(this);

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, puzzles);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        CharSequence curr = ((TextView)view).getText();
        String c = curr.toString();
        Character s = c.charAt(7);
        Intent intent = new Intent( this, GameActivity.class );
        Bundle bundle = new Bundle();
        bundle.putInt("currPuzzle", Integer.parseInt(s.toString())-1);
        intent.putExtras(bundle);
        startActivity( intent );

        finish();
        //Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
        //        Toast.LENGTH_SHORT).show();
    }
}
