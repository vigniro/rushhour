package com.example.RushHour;

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
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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
        mShapes.add(new MyShape(new Rect(0, 0, 100, 300), Color.RED, Orientation.VERTICAL));
        mShapes.add( new MyShape( new Rect( 200, 300, 300, 350), Color.BLUE, Orientation.HORIZONTAL ) );
    }

    public void setBoard( String string )
    {
        for ( int idx=0, r= ROWS-1; r>=0; --r ) {
            for ( int c=0; c < COLUMNS; ++c, ++idx ) {
                m_board[c][r] = string.charAt( idx );
            }
        }
        invalidate();
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
        for ( MyShape shape : mShapes ) {
            mPaint.setColor( shape.color );
            canvas.drawRect( shape.rect, mPaint );
        }

        for ( int r= ROWS -1; r>=0; --r ) {
            for ( int c=0; c< COLUMNS; ++c ) {
                m_rect.set( c * m_cellWidth, r * m_cellHeight,
                        c * m_cellWidth + m_cellWidth, r * m_cellHeight + m_cellHeight );
                canvas.drawRect( m_rect, m_paint );
                m_rect.inset( (int)(m_rect.width() * 0.1), (int)(m_rect.height() * 0.1) );
                m_shape.setBounds( m_rect );
                /*
                switch ( m_board[c][r] ) {
                    case 'x':
                        m_shape.getPaint().setColor( Color.RED );
                        m_shape.draw( canvas );
                        break;
                    case 'o':
                        m_shape.getPaint().setColor( Color.BLUE );
                        m_shape.draw( canvas );
                        break;
                    default:
                        break;
                }   */
            }
        }
    }

    private int xToCol( int x ) {
        return x / m_cellWidth;
    }

    private int yToRow( int y ) {
        return y / m_cellHeight;
    }

    @Override
    public boolean onTouchEvent( MotionEvent event ) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        int originX = x;
        int originY = y;
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
                    mMovingShape.rect.
                    x = Math.min( x, getWidth() - mMovingShape.rect.width() );
                    if(mMovingShape.orientation == Orientation.HORIZONTAL){
                        mMovingShape.rect.offsetTo( originX, y );
                    }
                    else{
                        mMovingShape.rect.offsetTo( x, originY );
                    }
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

        MyShape( Rect r, int c, Orientation o ) {
            rect = r;
            color = c;
            orientation = o;
        }
        Rect rect;
        int  color;
        Orientation orientation;
    }

    private enum Orientation {
        HORIZONTAL,
        VERTICAL
    }
}

