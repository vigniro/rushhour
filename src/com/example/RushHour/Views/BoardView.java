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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.RushHour.GameObjects.Block;
import com.example.RushHour.GameObjects.BlockType;
import com.example.RushHour.OnGameWonEventHandler;
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

    private boolean[][] m_boolBoard = new boolean[COLUMNS][ROWS];

    private Paint m_paint = new Paint();
    Paint blockPaint;
    Paint goalPaint;
    Paint playerPaint;
    private OnGameWonEventHandler winHandler = null;
    ArrayList<Block> originalBlocks;
    ArrayList<Block> blocks;
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

    public void setWinHandler(OnGameWonEventHandler handler)
    {
        this.winHandler = handler;
    }

    public void configurePaint(){
        /*
        Resources res = getResources();
        BitmapShader shader;
        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;

        bmm = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.brick3256, options));
        shader = new BitmapShader(bmm, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                     */
        blockPaint = new Paint();
        blockPaint.setAntiAlias(true);
        blockPaint.setStyle(Paint.Style.FILL);
        //blockPaint.setShader(shader);
        blockPaint.setColor(Color.BLUE);

        //bm = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.playerbrick, options));
        //shader = new BitmapShader(bm, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        playerPaint = new Paint();
        playerPaint.setAntiAlias(true);
        playerPaint.setStyle(Paint.Style.FILL);
        playerPaint.setColor(Color.RED);

        //playerPaint.setShader(shader);

        goalPaint = new Paint();
        goalPaint.setColor(Color.DKGRAY);
        goalPaint.setStyle(Paint.Style.FILL);

    }

    public void setBoard( Puzzle puzzle, int width, int height )
    {
        blocks = new ArrayList<Block>();
        int cellWidth = width / COLUMNS;
        int cellHeight = height / ROWS;
        initBoolBoard();

        for(Block b: puzzle.blocks)
        {
            updateBoolBoard(b,true);
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

        invalidate();
    }

    public void initBoolBoard() {
        m_boolBoard = new boolean[COLUMNS][ROWS];
        for (int i=0; i<COLUMNS; i++) {
            for (int j=0; j<ROWS; j++) {
                m_boolBoard[i][j] = false;
            }
        }
    }

    public void updateBoolBoard(Block b, boolean state) {
        m_boolBoard[b.left][b.top] = state;
        if (b.orientation.equalsIgnoreCase("V")) {
            m_boolBoard[b.left][b.top+1] = state;
            if (b.size == 3) {
                m_boolBoard[b.left][b.top+2] = state;
            }
        }
        else { // orientation of block is horizontal
            m_boolBoard[b.left+1][b.top] = state;
            if (b.size == 3) {
                m_boolBoard[b.left+2][b.top] = state;
            }
        }
    }

    public void printBoolBoard(boolean[][] boolBoard) {
        System.out.println("----------------------------");
        for(int i=0; i<COLUMNS; i++) {
            String tmp = "";
            for(int j=0; j<ROWS; j++) {
                tmp += boolBoard[j][i] + ", ";
            }
            System.out.println(tmp);
        }

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

        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                m_movingBlock = findBlock(x, y);
                if (m_movingBlock != null && m_movingBlock.type != BlockType.GOAL) {
                    scanLegalMoves();
                    if(m_movingBlock.orientation.equalsIgnoreCase("H")){
                        deltaX = x-m_movingBlock.left*m_cellWidth;
                    }
                    else {
                        deltaY = y-m_movingBlock.top*m_cellHeight;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if ( m_movingBlock != null && m_movingBlock.type != BlockType.GOAL) {
                    updateBoolBoard(m_movingBlock, false); // Remove the block from the boolBoard before updating
                    updateMovedBlock(m_movingBlock);       // Update the block
                    updateBoolBoard(m_movingBlock, true);  // Place the new position of the block into boolBoard
                    m_movingBlock = null;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if ( m_movingBlock != null && m_movingBlock.type != BlockType.GOAL) {

                    if(m_movingBlock.orientation.equalsIgnoreCase("H")){
                        dx = x-deltaX;

                        if(checkIfSolved(dx))
                        {
                            m_movingBlock = null;
                        }

                        if(m_movingBlock != null){
                            if (legalBackwards*m_cellWidth <= dx && dx+m_movingBlock.getRect().width() <= (legalForward+1)*m_cellWidth) {
                                distX = dx - m_movingBlock.left*m_cellWidth;
                                m_movingBlock.getRect().offsetTo( dx, m_movingBlock.getRect().top );
                            }
                            else if (legalBackwards*m_cellWidth > dx) {
                                m_movingBlock.getRect().offsetTo( legalBackwards*m_cellWidth, m_movingBlock.getRect().top );
                            }
                            else {
                                m_movingBlock.getRect().offsetTo( (legalForward+1)*m_cellWidth-m_movingBlock.getRect().width(), m_movingBlock.getRect().top );
                            }
                        }
                    }else{
                        dy = y-deltaY;

                        if (legalBackwards*m_cellHeight <= dy && dy+m_movingBlock.getRect().height() <= (legalForward+1)*m_cellHeight) {
                            distY = dy - m_movingBlock.top*m_cellHeight;
                            m_movingBlock.getRect().offsetTo( m_movingBlock.getRect().left, dy );
                        }
                        else if (legalBackwards*m_cellHeight > dy) {
                            m_movingBlock.getRect().offsetTo(m_movingBlock.getRect().left, legalBackwards*m_cellHeight);
                        }
                        else {
                            m_movingBlock.getRect().offsetTo(m_movingBlock.getRect().left, (legalForward+1)*m_cellHeight-m_movingBlock.getRect().height());
                        }
                    }
                    invalidate();
                    break;
                 }
        }

        return true;
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

        int rest=0, cellsMoved=0;
        boolean leftMove = false, upMove = false;

        if(movedBlock.orientation.equalsIgnoreCase("H")) {
            // Block moved to the left
            if(distX < 0) {
                leftMove = true;
            }

            cellsMoved = Math.abs(distX) / m_cellWidth;
            rest = Math.abs(distX) % m_cellWidth;

            // If the moving block is more than 25% inside the next cell, we shift it there
            if (rest > m_cellWidth/4) {
                cellsMoved++;
            }

            if (leftMove && m_movingBlock.left - cellsMoved < legalBackwards ) {
                cellsMoved = 0;
            }
            else if (!leftMove && m_movingBlock.left + (m_movingBlock.size-1) + cellsMoved > legalForward ) {
                cellsMoved = 0;
            }

            blocks.get(currBlockIndex).updateHorizontalRect(cellsMoved, leftMove, m_cellWidth);
            invalidate();
        }
        else {
            // Block moved to the right
            if(distY < 0) {
                upMove = true;
            }

            cellsMoved = Math.abs(distY) / m_cellHeight;
            rest = Math.abs(distY) % m_cellHeight;

            // If the moving block is more than 25% inside the next cell, we shift it there
            if (rest > m_cellHeight/4) {
                cellsMoved++;
            }

            if (upMove && m_movingBlock.top - cellsMoved < legalBackwards ) {
                cellsMoved = 0;
            }
            else if (!upMove && m_movingBlock.top + (m_movingBlock.size-1) + cellsMoved > legalForward ) {
                cellsMoved = 0;
            }

            blocks.get(currBlockIndex).updateVerticalRect(cellsMoved, upMove, m_cellHeight);
            invalidate();
        }
    }

    private boolean checkIfSolved(int x) {
        if (x+m_movingBlock.getRect().width() > goalBlock.getRect().left && m_movingBlock.type == BlockType.PLAYER && legalForward == 5) {
            System.out.println("You won!");
            if(winHandler != null){
                winHandler.win();
            }
            return true;
        }
        return false;
    }

    private void scanLegalMoves() {

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
        }

    }

}

