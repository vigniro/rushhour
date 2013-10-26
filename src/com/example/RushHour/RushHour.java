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
    private final int PLAYER_X = 0;
    private final int PLAYER_O = 1;
    private final int COLUMNS = 6;
    private final int ROWS = 6;
    private int m_toMove;
    private int[][] m_board;

    class Move {

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
        m_toMove = PLAYER_X;
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
                switch ( state.charAt(i) ) {
                    case 'x':
                        m_board[col][row] = PLAYER_X;
                        ++x;
                        break;
                    case 'o':
                        m_board[col][row] = PLAYER_O;
                        ++o;
                        break;
                }
            }
        }
        m_toMove = ( x > o ) ? PLAYER_O : PLAYER_X;
    }

    public List<Move> getActions() {
        List<Move> actions = new ArrayList<Move>();
        if ( !isGameOver() ) {
            for ( int col=0; col< COLUMNS; ++col ) {
                for ( int row=0; row< ROWS; ++row ) {
                    if ( m_board[col][row] == EMPTY ) {
                        actions.add( new Move(col, row) );
                    }
                }
            }
        }
        return actions;
    }

    public void makeMove( Move move ) {
        m_board[move.getCol()][move.getRow()] = m_toMove;
        m_toMove = (m_toMove == PLAYER_X) ? PLAYER_O : PLAYER_X;
    }

    public void undoMove( Move move ) {
        m_board[move.getCol()][move.getRow()] = EMPTY;
        m_toMove = (m_toMove == PLAYER_X) ? PLAYER_O : PLAYER_X;
    }

    public int getPlayerToMove() {
        return m_toMove;
    }

    public boolean isGameOver() {

        boolean hasEmpty = false;
        for ( int col=0; col< COLUMNS && !hasEmpty; ++col ) {
            for ( int row=0; row< ROWS && !hasEmpty; ++row ) {
                if ( m_board[col][row] == EMPTY ) {
                    hasEmpty = true;
                }
            }
        }
        if ( !hasEmpty ) {
            return true;
        }
        int notToMove = (m_toMove == PLAYER_X) ? PLAYER_O : PLAYER_X;
        for ( int row=0; row< ROWS; ++row ) {
            if ( hasInARow(ROWS, notToMove, 0, row, +1, 0 ) ) {
                return true;
            }
        }
        for ( int col=0; col< COLUMNS; ++col ) {
            if ( hasInARow(COLUMNS, notToMove, col, 0, 0, +1 ) ) {
                return true;
            }
        }
        if ( hasInARow(COLUMNS, notToMove, 0, 0, +1, +1 ) ) {
            return true;
        }
        if ( hasInARow(ROWS, notToMove, 0, ROWS -1, +1, -1 ) ) {
            return true;
        }
        return false;
    }

    public Move strToMove( String moveStr ) {
        List<Move> actions = getActions();
        for ( Move m : actions ) {
            if ( moveStr.equals( m.toString() ) ) {
                return m;
            }
        }
        return null;
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

    private boolean hasInARow( int inARow, int player, int colStart, int rowStart, int colOffset, int rowOffset ) {
        int count = 0;
        for ( int col=colStart, row=rowStart; col>=0 && col< COLUMNS && row>=0 && row< ROWS; col+=colOffset, row+=rowOffset ) {
            if ( m_board[col][row] == player ) {
                ++count;
            }
            else {
                count = 0;
            }
            if ( count >= inARow ) {
                return true;
            }
        }
        return false;
    }
}