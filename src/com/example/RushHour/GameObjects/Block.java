package com.example.RushHour.GameObjects;

import android.graphics.Rect;

/**
 * Created with IntelliJ IDEA.
 * User: GSUS
 * Date: 28.10.2013
 * Time: 01:09
 * To change this template use File | Settings | File Templates.
 */
public class Block {
    public int color;
    public int left;
    public int top;
    public int size;
    public String orientation;
    private Rect rect;
    public BlockType type;

    public Block(BlockType type, String orientation, int left, int top, int size){
        this.type = type;
        this.orientation = orientation;
        this.left = left;
        this.top = top;
        this.size = size;
    }

    public Block(BlockType type, int left, int top, int right, int bottom){
        this.type = type;
        setRect(left, top, right, bottom);
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public int getColor(){
        return this.color;
    }

    public void setRect(int left, int top, int right, int bottom)
    {
        this.rect = new Rect(left, top, right, bottom );
        if(type == BlockType.NORMAL || type == BlockType.PLAYER){
            this.rect.inset(10,10);
        }
    }

    public void updateHorizontalRect(int cellsMoved, boolean leftMove, int cellWidth)
    {
        if(0 < this.left && leftMove){
            this.left -= cellsMoved;
        }
        else if (this.left < 6 && !leftMove) {
            this.left += cellsMoved;
        }
        this.rect.left = this.left*cellWidth;
        this.rect.right = this.rect.left+this.size*cellWidth;
    }

    public void updateVerticalRect(int cellsMoved, boolean upMove, int cellHeight)
    {
        if(0 < this.top && upMove){
            this.top -= cellsMoved;
        }
        else if (this.top < 6 && !upMove) {
            this.top += cellsMoved;
        }

        this.rect.top = this.top*cellHeight;
        this.rect.bottom = this.rect.top+this.size*cellHeight;
    }

    public Rect getRect()
    {
        return this.rect;
    }
}
