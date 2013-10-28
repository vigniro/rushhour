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
    public int left;
    public int top;
    public int size;
    public String orientation;
    private Rect rect;

    public Block(String orientation, int left, int top, int size){
        this.orientation = orientation;
        this.left = left;
        this.top = top;
        this.size = size;
    }

    private void setRect(int left, int top, int right, int bottom)
    {
          this.rect = new Rect(left, top, right, bottom );
    }

    public Rect getRect()
    {
        return this.rect;
    }
}
