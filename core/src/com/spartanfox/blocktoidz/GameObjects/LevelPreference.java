/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.GameObjects;

import java.util.Random;

/**
 *
 * @author Ben
 */
public class LevelPreference {
    public static enum Mode{Breaker,TimeTrial,Normal,Hard,Insane,Custom};
    public float startingTime;
    //name of the level
    public String name;
    //the image the level uses in level select
    public String image;
    //describing the level in generalised detail
    public String description;
    public String theme;
    public Mode mode;
    public long seed;
    public byte gridWidth,gridHeight;
    //how many triangles you can miss
    //to still complete a row
    public int gap;
    
    public int comboLimit;
    public int comboGive;
    
   // public byte speedIncrease;
    public int startingSpeed;
    public int maxSpeed;
    public float bigBlock;
    //this is what the score will be multiplied by which you can use to get more points the harder the level
    public int multiplier;
    public boolean random;
    public long levelRequired = -1;
    public String toUnlock = "Locked";
    public  int[][] level;
    public LevelPreference(){
      this("Default","Description");
    }
    public LevelPreference(String Name,String Description){
        this.name = Name;
        this.description = Description;
        this.theme = "";this.image = "Default";
        this.seed = new Random().nextLong();
        this.gridWidth = 6;
        this.gridHeight = 7;
        this.comboLimit = 1;
        this.gap = 1;
        this.mode = Mode.Hard;
        startingSpeed = 40;
        maxSpeed = 20;
        comboGive = 2;
        comboLimit = 1;
        multiplier = 4;
        bigBlock = 0.2f;
    }
    public LevelPreference mode(Mode diff){
        this.mode = diff;
        //when setting the mode give the preset meanings of the difficulties
        switch(mode){
            case Breaker:
                startingSpeed = 60;
                maxSpeed = 30;
                comboGive = 0;
                comboLimit = 0;
                multiplier = 1;
                bigBlock = 0;
                gap = 0;
                break;
            case TimeTrial:
                startingSpeed = 40;
                maxSpeed = 0;
                comboGive = 3;
                comboLimit = 7;
                multiplier = 3;
                bigBlock = 0;
                gap = 1;
                break;
            case Normal:
                startingSpeed = 60;
                maxSpeed = 25;
                comboGive = 3;
                comboLimit = 3;
                multiplier = 1;
                bigBlock = 0.5f;
                break;
            case Hard:
                startingSpeed = 50;
                maxSpeed = 25;
                comboGive = 3;
                comboLimit = 2;
                multiplier = 2;
                gap = 0;
                bigBlock = 0.5f;
                break;
            case Insane:
                startingSpeed = 40;
                maxSpeed = 25;
                comboGive = 0;
                comboLimit = 0;
                multiplier = 3;
                gap = 0;
                bigBlock = 0.6f;
                break;
        }
        return this;
    }
    public LevelPreference setlevelRequired(long levelRequired,String toUnlock){
        this.levelRequired = levelRequired;
        this.toUnlock = toUnlock;
        return this;
    }
    public LevelPreference Level(int[][] newLevel){
        gridWidth  = (byte)(newLevel[0].length);
        gridHeight = (byte)(newLevel   .length);
        level = newLevel;
        return this;
    }
    public LevelPreference setBigBlock(float frequency){
        bigBlock = frequency;
        return this;
    }
    public LevelPreference Level(boolean random){
        this.random = random;
        return this;
    }
    public LevelPreference Image(String img){
        this.image = img;
        return this;
    }
    public LevelPreference Theme(String theme){
        this.theme = theme+"/";
        return this;
    }
    
    public LevelPreference ComboLimit(int limit){comboLimit = (byte)limit;return this;}
    public LevelPreference Grid(int width, int height){
        this.gridWidth = (byte)width;
        this.gridHeight = (byte)height;
        
        return this;
    }
    public LevelPreference MultiplyScore(int multiplier){
        this.multiplier = multiplier;
        return this;
    }
    public LevelPreference Time(int minutes){
        startingTime = minutes*60;
        return this;
    }
}
