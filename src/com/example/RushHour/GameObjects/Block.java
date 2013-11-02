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
    public boolean isPlayerBlock;
    public int left;
    public int top;
    public int size;
    public String orientation;
    private int color;
    private Rect rect;

    public Block(boolean isPlayerBlock, String orientation, int left, int top, int size){
        this.isPlayerBlock = isPlayerBlock;
        this.orientation = orientation;
        this.left = left;
        this.top = top;
        this.size = size;
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
          this.rect.inset(10,10);
    }

    public Rect getRect()
    {
        return this.rect;
    }
}
