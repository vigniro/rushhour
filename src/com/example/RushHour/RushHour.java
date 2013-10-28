package com.example.RushHour;

/**
 * Created with IntelliJ IDEA.
 * User: GSUS
 * Date: 26.10.2013
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
import java.util.ArrayList;
import java.util.List;

public class RushHour {

    private final int EMPTY = -1;
    private final int COLUMNS = 6;
    private final int ROWS = 6;
    private int[][] m_board;

    public class Move {

        private int m_col;
        private int m_row;

        public Move( int col, int row ) {
            m_col = col;
            m_row = row;
        }

        public int getCol() {
            return m_col;
        }

        public int getRow() {
            return m_row;
        }

        public String toString() {
            return "(" + getCol() + ',' + getRow() + ')';
        }
    }

    public RushHour() {
        m_board = new int[COLUMNS][ROWS];
        reset();
    }

    public void reset() {
        for ( int col=0; col< COLUMNS; ++col ) {
            for ( int row=0; row< ROWS; ++row ) {
                m_board[col][row] = EMPTY;
            }
        }
    }

    public void set( String state ) {
        reset();
        int x = 0, o = 0;
        for ( int i=0, row= ROWS -1; row>=0; --row ) {
            for ( int col=0; col< COLUMNS; ++col, ++i ) {

            }
        }

    }

    public List<Move> getActions() {
        List<Move> actions = new ArrayList<Move>();
        for ( int col=0; col< COLUMNS; ++col ) {
            for ( int row=0; row< ROWS; ++row ) {
                if ( m_board[col][row] == EMPTY ) {
                    actions.add( new Move(col, row) );
                }
            }
        }
        return actions;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for ( int row= ROWS -1; row>=0; --row ) {
            for ( int col=0; col< COLUMNS; ++col ) {
                char ch = '.';
                switch ( m_board[col][row] ) {
                    case 0:
                        ch = 'x';
                        break;
                    case 1:
                        ch = 'o';
                        break;
                }
                sb.append( ch );
            }
            //sb.append( '\n');
        }
        return sb.toString();
    }

}