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
import com.example.RushHour.OnMoveEventHandler;
import com.example.RushHour.GameObjects.Puzzle;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: yngvi
 * Date: 22.10.2013
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 */
public class BoardView extends View {

    private int m_cellWidth = 0;
    private int m_cellHeight = 0;
    private int COLUMNS = 6;
    private int ROWS = 6;
    private char[][] m_board = new char[COLUMNS][ROWS];
    private Paint m_paint = new Paint();
    Paint mPaint = new Paint();
    private OnMoveEventHandler m_moveHandler = null;

    ArrayList<MyShape> mShapes = new ArrayList<MyShape>();
    MyShape mMovingShape = null;
    ShapeDrawable m_shape = new ShapeDrawable( new RectShape() );
    Rect m_rect = new Rect();

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_paint.setColor( Color.WHITE );
        m_paint.setStyle( Paint.Style.STROKE );
    }

    public void setBoard( Puzzle puzzle, int width, int height )
    {
        int cellWidth = width / COLUMNS;
        int cellHeight = height / ROWS;
        for(Block b: puzzle.blocks)
        {
            if(b.orientation.equalsIgnoreCase("H")){
                mShapes.add(new MyShape(
                        new Rect(
                            b.left * cellWidth,
                            b.top * cellHeight,
                            b.size * cellWidth + cellWidth,
                            b.top * cellHeight + cellHeight),
                            Color.RED
                        ));

            }
           else{
                mShapes.add(new MyShape(
                        new Rect(
                                b.left * cellWidth,
                                b.top * cellHeight,
                                b.left * cellWidth + cellWidth,
                                b.size * cellHeight + cellHeight),
                                Color.BLUE
                ));

            }
        }
        //invalidate();
    }


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

    public void onDraw( Canvas canvas )
    {
        drawGrid(canvas);
        for ( MyShape shape : mShapes ) {
            mPaint.setColor( shape.color );
            canvas.drawRect( shape.rect, mPaint );
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
        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                mMovingShape = findShape( x, y );
                break;
            case MotionEvent.ACTION_UP:
                if ( mMovingShape != null ) {
                    mMovingShape = null;
                    // emit an custom event ....
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if ( mMovingShape != null ) {
                    x = Math.min( x, getWidth() - mMovingShape.rect.width() );
                    mMovingShape.rect.offsetTo( x, y );
                    invalidate();
                }
                break;
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

    private MyShape findShape( int x, int y ) {
        for ( MyShape shape : mShapes ) {
            if ( shape.rect.contains( x, y ) ) {
                return shape;
            }
        }
        return null;
    }

    private class MyShape {

        MyShape( Rect r, int c) {
            rect = r;
            color = c;
        }
        Rect rect;
        int  color;
    }
}

