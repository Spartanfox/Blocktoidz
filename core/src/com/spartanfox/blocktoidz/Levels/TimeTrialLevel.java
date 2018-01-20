/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.Levels;

import com.badlogic.gdx.Gdx;
import static com.spartanfox.blocktoidz.Globals.LevelPreferences.preference;
import com.spartanfox.blocktoidz.Screens.GameScreen;

/**
 *
 * @author Ben
 */
public class TimeTrialLevel extends Level{
    float startingTime = 3*60;
    public TimeTrialLevel(GameScreen main, float x, float y, float width, float height) {
        super(main, x, y, width, height);
        timer = preference.startingTime;
    }
    @Override
    public void updateTimer(){
        if(placed>0)timer-=Gdx.graphics.getDeltaTime();
        if(timer<=0){
            timer =  preference.startingTime;
            super.gameOver("Times up!");
        }
    }
    @Override
    public void gameOver(String message){
        timer = preference.startingTime-timer;
        super.gameOver(message);
    }
}
