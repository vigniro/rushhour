package com.example.RushHour;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    BoardView m_bv;
    //TextView m_tv;
    Button m_button;
    Button m_buttonReset;
    Spinner m_spinner;

    RushHour m_ttt;
    Random  m_random = new Random();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //m_tv = (TextView) findViewById( R.id.textview );
        m_bv = (BoardView) findViewById( R.id.boardview );
        m_bv.setMoveEventHandler( new OnMoveEventHandler() {
            @Override
            public void onMove(int col, int row) {
                String actionStr = "(" + col + "," + row + ")";
                RushHour.Move move = m_ttt.strToMove( actionStr );
                if ( move != null ) {
                    m_ttt.makeMove( move );
                    updateDisplay();
                }
            }
        });
        m_button = (Button) findViewById( R.id.button );
        m_spinner = (Spinner) findViewById( R.id.spinner );
        m_buttonReset = (Button) findViewById( R.id.buttonReset );
        m_buttonReset.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_ttt.reset();
                updateDisplay();
            }
        });

        m_ttt = new RushHour();
        if ( savedInstanceState != null ) {
            String state = savedInstanceState.getString( "stateTTT" );
            m_ttt.set( state );
        }
        updateDisplay();
    }


    public void updateDisplay()
    {
        ArrayList<String> actionsStr = new ArrayList<String>();
        List<RushHour.Move> actions = m_ttt.getActions();
        for ( RushHour.Move action : actions ) {
            actionsStr.add( action.toString() );
        }

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>( this, android.R.layout.simple_spinner_dropdown_item, actionsStr );

        m_spinner.setAdapter( arrayAdapter );
        //m_tv.setText( m_ttt.toString() );
        m_bv.setBoard( m_ttt.toString() );
        if ( m_ttt.isGameOver() ) {
            Toast.makeText( getApplicationContext(), "Game over!", Toast.LENGTH_LONG ).show();
        }

    }


    public void buttonPlay( View view )
    {
        /*
        Button button = (Button) view;
        Log.d("***** RushHour", "Button clicked");
        Object action = m_spinner.getSelectedItem();
        if ( action != null ) {
            RushHour.Move move = m_ttt.strToMove( action.toString() );
            if ( move != null ) {
                m_ttt.makeMove( move );
                updateDisplay();
            }
        }
        */
        List<RushHour.Move> actions = m_ttt.getActions();
        if ( actions.size() > 0 ) {
            RushHour.Move move = actions.get( m_random.nextInt( actions.size() ) );
            m_ttt.makeMove( move );
            updateDisplay();
        }


    }

    @Override
    public void onSaveInstanceState( Bundle savedInstanceState ) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString( "stateTTT", m_ttt.toString() );
    }

}