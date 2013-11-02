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
        parseBlockSetup(setup);
    }

    private void parseBlockSetup(String setup){
        String set[] = setup.split(",");
        for(int i = 0; i < set.length; i++){
            String s = set[i].replace("(", "").replace(")", "");
            String blockConfig[] = s.trim().split(" ");
            blocks.add(
                    new Block(
                        i == 0, //If this is the first iteration mark the block as the player block.
                        blockConfig[0],
                        Integer.valueOf(blockConfig[1]),
                        Integer.valueOf(blockConfig[2]),
                        Integer.valueOf(blockConfig[3])
                    )
            );
        }
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Puzzle " + id + " (Level " + level + ")");
        return sb.toString();
    }
}