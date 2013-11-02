package com.example.RushHour.Views;

/**
 * Created with IntelliJ IDEA.
 * User: GSUS
 * Date: 26.10.2013
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.example.RushHour.GameObjects.Block;
import com.example.RushHour.GameObjects.BlockType;
import com.example.RushHour.OnMoveEventHandler;
import com.example.RushHour.GameObjects.Puzzle;
import com.example.RushHour.R;

import java.util.ArrayList;


public class BoardView extends View {

    private int m_cellWidth = 0;
    private int m_cellHeight = 0;
    private int COLUMNS = 6;
    private int ROWS = 6;
    private int radius = 25;
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
    Paint blockPaint;
    Paint goalPaint;
    Paint playerPaint;
    private OnMoveEventHandler m_moveHandler = null;
    ArrayList<Block> blocks;
    Bitmap bmm;
    Block goalBlock;
    Block m_movingBlock = null;
    int legalBackwards;
    int legalForward;
    int deltaX, deltaY;
    int dx, dy;
    int distX, distY;
    int currBlockIndex;
    Rect m_rect = new Rect();
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_paint.setColor(Color.rgb(244, 222, 163));
        m_paint.setStyle( Paint.Style.FILL );
        configurePaint();

    }

    public void configurePaint(){

        Resources res = getResources();
        BitmapShader shader;
        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;

        bmm = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.brick3256, options));
        shader = new BitmapShader(bmm, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        blockPaint = new Paint();
        blockPaint.setAntiAlias(true);
        blockPaint.setStyle(Paint.Style.FILL);
        blockPaint.setShader(shader);

        bm = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.playerbrick, options));
        shader = new BitmapShader(bm, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        playerPaint = new Paint();
        playerPaint.setAntiAlias(true);
        blockPaint.setStyle(Paint.Style.FILL);
        playerPaint.setShader(shader);

        //bm = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.brick1));
        //shader = new BitmapShader(bm, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        goalPaint = new Paint();
        //goalPaint.setAntiAlias(true);
        //goalPaint.setShader(shader);
        goalPaint.setColor(Color.DKGRAY);
        blockPaint.setStyle(Paint.Style.FILL);

        //RectF rect = new RectF(0.0f, 0.0f,     width, height);

        // rect contains the bounds of the shape
        // radius is the radius in pixels of the rounded corners
        // paint contains the shader that will texture the shape
        //canvas.drawRoundRect(rect, radius, radius, blockPaint);
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
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
                blocks.add(b);
            }
           else{
                b.setRect(b.left * cellWidth, b.top * cellHeight, b.left * cellWidth + cellWidth,
                        b.size * cellHeight + b.top * cellHeight);
                blocks.add(b);
            }
        }
        //Add goal stripe to the block array
        this.goalBlock = new Block(BlockType.GOAL, 6 * m_cellWidth-10, 2 * m_cellHeight, 6 * m_cellWidth, 3 * m_cellHeight);
        blocks.add(goalBlock);
        // System.out.println(goalBlock.left);
        // System.out.println(6 * m_cellWidth-10);

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
            RectF rectF = new RectF(b.getRect());
            if(b.type == BlockType.NORMAL)
                canvas.drawRoundRect(rectF, radius, radius, blockPaint);
                //canvas.drawBitmap(bmm, null, rectF, null);
            else if(b.type == BlockType.PLAYER)
                canvas.drawRoundRect(rectF, radius, radius, playerPaint);
            else
                canvas.drawRect( b.getRect(), goalPaint );
        }
    }

    private void drawGrid(Canvas canvas){

        for ( int r= ROWS -1; r>=0; --r ) {
            for ( int c=0; c< COLUMNS; ++c ) {
                m_rect.set( c * m_cellWidth,
                            r * m_cellHeight,
                            c * m_cellWidth + m_cellWidth,
                            r * m_cellHeight + m_cellHeight );
                m_rect.inset(1,1);
                canvas.drawRect( m_rect, m_paint );

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
                if(m_movingBlock.orientation.equalsIgnoreCase("H")){
                    deltaX = x-m_movingBlock.left*m_cellWidth;
                }
                else {
                    deltaY = y-m_movingBlock.top*m_cellHeight;
                }
                break;
            case MotionEvent.ACTION_UP:
                if ( m_movingBlock != null ) {
                    updateMovedBlock(m_movingBlock);
                    updateBoolBoard();
                    m_movingBlock = null;
                    // emit an custom event ....
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if ( m_movingBlock != null ) {

                    if(m_movingBlock.orientation.equalsIgnoreCase("H")){
                        dx = x-deltaX;

                        //System.out.println(m_movingBlock.left + " " + m_cellWidth + " " + m_movingBlock.getRect().width() + " x: " + x);
                        if (puzzleWon(dx))
                        {
                            System.out.println("You won!");
                        }


                        if (legalBackwards*m_cellWidth <= dx && dx+m_movingBlock.getRect().width() <= (legalForward+1)*m_cellWidth) {
                            //System.out.println("hurray!");
                            distX = Math.abs(dx - m_movingBlock.left*m_cellWidth);
                            System.out.println("distX: " + distX);
                            m_movingBlock.getRect().offsetTo( dx, m_movingBlock.getRect().top );
                        }
                        else if (legalBackwards*m_cellWidth > dx) {
                            m_movingBlock.getRect().offsetTo( legalBackwards*m_cellWidth, m_movingBlock.getRect().top );
                        }
                        else {
                            m_movingBlock.getRect().offsetTo( (legalForward+1)*m_cellWidth-m_movingBlock.getRect().width(), m_movingBlock.getRect().top );
                        }
                        updateMovedBlock(m_movingBlock);
                    }else{
                        dy = y-deltaY;

                        if (legalBackwards*m_cellHeight <= dy && dy+m_movingBlock.getRect().height() <= (legalForward+1)*m_cellHeight) {
                            System.out.println("hurray!");
                            m_movingBlock.getRect().offsetTo( m_movingBlock.getRect().left, dy );
                        }
                        else if (legalBackwards*m_cellHeight > dy) {
                            m_movingBlock.getRect().offsetTo(m_movingBlock.getRect().left, legalBackwards*m_cellHeight);
                        }
                        else {
                            m_movingBlock.getRect().offsetTo(m_movingBlock.getRect().left, (legalForward+1)*m_cellHeight-m_movingBlock.getRect().height());
                        }
                        updateMovedBlock(m_movingBlock);
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
                currBlockIndex = blocks.indexOf(b);
                return b;

            }
        }
        return null;
    }


    private void updateMovedBlock(Block movedBlock) {
         blocks.get(currBlockIndex).updateRect(movedBlock.getRect());
    }

    private void updateBoolBoard() {

    }

    private boolean puzzleWon (int x) {
        if (x+m_movingBlock.getRect().width() > 470) {
            return true;
        }
        return false;
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

