package com.example.RushHour.Views;

/**
 * Created with IntelliJ IDEA.
 * User: GSUS
 * Date: 26.10.2013
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.example.RushHour.GameObjects.Block;
import com.example.RushHour.GameObjects.BlockType;
import com.example.RushHour.OnMoveEventHandler;
import com.example.RushHour.GameObjects.Puzzle;

import java.util.ArrayList;


public class BoardView extends View {

    private int m_cellWidth = 0;
    private int m_cellHeight = 0;
    private int COLUMNS = 6;
    private int ROWS = 6;
    private char[][] m_board = new char[COLUMNS][ROWS];

    // private boolean[][] m_boolBoard = new boolean[COLUMNS][ROWS];
    private boolean[][] m_boolBoard = {
        {false,false,false,false,false,false},
        {false,false,false,false,false,false},
        {false,false,false,false,false,false},
        {false,false,false,false,false,false},
        {false,false,false,false,false,false},
        {false,false,false,false,false,false},
    };

    private Paint m_paint = new Paint();
    Paint mPaint = new Paint();
    private OnMoveEventHandler m_moveHandler = null;

    ArrayList<Block> blocks;
    Block m_movingBlock = null;
    int legalBackwards;
    int legalForward;
    ShapeDrawable m_shape = new ShapeDrawable( new RectShape() );
    Rect m_rect = new Rect();

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_paint.setColor( Color.WHITE );
        m_paint.setStyle( Paint.Style.STROKE );
    }

    public void setBoard( Puzzle puzzle, int width, int height )
    {
        blocks = new ArrayList<Block>();
        int cellWidth = width / COLUMNS;
        int cellHeight = height / ROWS;

        for(Block b: puzzle.blocks)
        {
            initBoolBoard(b);
            // System.out.println(b.left + ", " + b.top);
            if(b.orientation.equalsIgnoreCase("H")){
                b.setRect(b.left * cellWidth, b.top * cellHeight,
                        b.size * cellWidth + b.left * cellWidth,b.top * cellHeight + cellHeight);
                b.setColor(b.type == BlockType.PLAYER ? Color.RED : Color.BLUE);
                blocks.add(b);
            }
           else{
                b.setRect(b.left * cellWidth, b.top * cellHeight, b.left * cellWidth + cellWidth,
                        b.size * cellHeight + b.top * cellHeight);
                b.setColor(b.type == BlockType.PLAYER ? Color.RED : Color.BLUE);
                blocks.add(b);
            }
        }
        //Add goal stripe to the block array
        Block goal = new Block(BlockType.GOAL, 6 * m_cellWidth-10, 2 * m_cellHeight, 6 * m_cellWidth, 3 * m_cellHeight);
        goal.setColor(Color.RED);
        blocks.add(goal);

        printBoolBoard(m_boolBoard);
        invalidate();
    }

    public void initBoolBoard(Block b) {
        // System.out.println("X: " + b.left + " , Y: " + b.top + " , Orientation: " + b.orientation + " , Size: " + b.size);
        m_boolBoard[b.left][b.top] = true;
        if (b.orientation.equalsIgnoreCase("V")) {
            m_boolBoard[b.left][b.top+1] = true;
            if (b.size == 3) {
                m_boolBoard[b.left][b.top+2] = true;
            }
        }
        else { // orientation of block is horizontal
            m_boolBoard[b.left+1][b.top] = true;
            if (b.size == 3) {
                m_boolBoard[b.left+2][b.top] = true;
            }
        }
    }

    public void printBoolBoard(boolean[][] boolBoard) {

        for(int i=0; i<COLUMNS; i++) {
            String tmp = "";
            for(int j=0; j<ROWS; j++) {
                tmp += boolBoard[j][i] + ", ";
            }
            System.out.println(tmp);
        }
    }

    //public void isMoveValid()


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        m_cellWidth = xNew / COLUMNS;
        m_cellHeight = yNew / ROWS;
    }

    // @Override
    public void onDraw( Canvas canvas )
    {
        drawGrid(canvas);
        for ( Block b : blocks) {
            mPaint.setColor( b.getColor() );
            canvas.drawRect( b.getRect(), mPaint );
        }
    }

    private void drawGrid(Canvas canvas){
        for ( int r= ROWS -1; r>=0; --r ) {
            for ( int c=0; c< COLUMNS; ++c ) {
                m_rect.set( c * m_cellWidth,
                            r * m_cellHeight,
                            c * m_cellWidth + m_cellWidth,
                            r * m_cellHeight + m_cellHeight );
                canvas.drawRect( m_rect, m_paint );
                m_rect.inset( (int)(m_rect.width() * 0.1), (int)(m_rect.height() * 0.1) );
                m_shape.setBounds( m_rect );

            }
        }
    }

    @Override
    public boolean onTouchEvent( MotionEvent event ) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        System.out.println("x: " + x + " y: " + y);

        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                m_movingBlock = findBlock(x, y);
                scanLegalMoves();
                break;
            case MotionEvent.ACTION_UP:
                if ( m_movingBlock != null ) {
                    updateBoolBoard();
                    m_movingBlock = null;
                    // emit an custom event ....
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if ( m_movingBlock != null ) {
                    x = Math.min( x, getWidth() - m_movingBlock.getRect().width() );
                    if(m_movingBlock.orientation.equalsIgnoreCase("H")){
                        //if(m_movingBlock.getRect().left >= 0 && m_movingBlock.getRect().width() <= getWidth())
                            m_movingBlock.getRect().offsetTo( x, m_movingBlock.getRect().top );
                    }else{
                        m_movingBlock.getRect().offsetTo(m_movingBlock.getRect().left, y);
                        invalidate();
                    }
                    invalidate();
                    break;
                 }
        }
        /*
        if ( event.getAction() == MotionEvent.ACTION_DOWN ) {
            if ( m_moveHandler != null ) {
                m_moveHandler.onMove( xToCol(x), yToRow(y) );
            }
        }      */
        return true;
    }

    public void setMoveEventHandler( OnMoveEventHandler handler ) {
        m_moveHandler = handler;
    }

    private Block findBlock(int x, int y) {
        for ( Block b : blocks) {
            if ( b.getRect().contains( x, y ) ) {
                return b;
            }
        }
        return null;
    }

    private void updateBoolBoard() {

    }

    private void scanLegalMoves() {
        // Init
        //legalBackwards=-1;
        //legalForward=-1;

        // Orientation of moving block is horizontal
        if(m_movingBlock.orientation.equalsIgnoreCase("H")) {
            boolean blockOnLeft=false, blockOnRight=false;

            // Find the next block, if any, on the lefthand side of the moving block
            for (int i=m_movingBlock.left-1; i>=0; i--) {
                if (m_boolBoard[i][m_movingBlock.top]) {
                    legalBackwards = i+1;
                    blockOnLeft = true;
                    break;
                }
            }

            if (!blockOnLeft) {
                legalBackwards = 0;
            }

            // Find the next block, if any, on the righthand side of the moving block
            for (int i=m_movingBlock.left+m_movingBlock.size; i<COLUMNS; i++) {
                if (m_boolBoard[i][m_movingBlock.top]) {
                    legalForward = i-1;
                    blockOnRight = true;
                    break;
                }
            }

            if (!blockOnRight) {
                legalForward = COLUMNS-1;
            }
            System.out.println(legalBackwards + " " + legalForward);
        }

        // Orientation of moving block is vertical
        else {
            boolean blockAbove=false, blockBelow=false;

            // Find the next block, if any, on the lefthand side of the moving block
            for (int i=m_movingBlock.top-1; i>=0; i--) {
                if (m_boolBoard[m_movingBlock.left][i]) {
                    legalBackwards = i+1;
                    blockAbove = true;
                    break;
                }
            }

            if (!blockAbove) {
                legalBackwards = 0;
            }

            // Find the next block, if any, on the righthand side of the moving block
            for (int i=m_movingBlock.top+m_movingBlock.size; i<ROWS; i++) {
                if (m_boolBoard[m_movingBlock.left][i]) {
                    legalForward = i-1;
                    blockBelow = true;
                    break;
                }
            }

            if (!blockBelow) {
                legalForward = ROWS-1;
            }
            System.out.println(legalBackwards + " " + legalForward);

        }

    }

    private int xToCol( int x ) {
        return x / m_cellWidth;
    }

    private int yToRow( int y ) {
        return y / m_cellHeight;
    }

}

