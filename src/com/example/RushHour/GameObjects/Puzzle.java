package com.example.RushHour.GameObjects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: GSUS
 * Date: 28.10.2013
 * Time: 01:03
 * To change this template use File | Settings | File Templates.
 */
public class Puzzle implements Serializable{
    private int id;
    private int level;
    public ArrayList<Block> blocks;

    public Puzzle(int id, int level, String setup){
        this.id = id;
        this.level = level;
        blocks = new ArrayList<Block>();
        parseSetup(setup);
    }

    private void parseSetup(String setup){
        String set[] = setup.split(",");
        for(String s: set){
            s = s.replace("(", "");
            s= s.replace(")", "");
            String blockConfig[] = s.trim().split(" ");
            blocks.add(new Block(blockConfig[0],
                    Integer.valueOf(blockConfig[1]),
                    Integer.valueOf(blockConfig[2]),
                    Integer.valueOf(blockConfig[3])));
        }

    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Puzzle " + id);
        return sb.toString();
    }
}