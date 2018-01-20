/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.Levels;

import com.badlogic.gdx.Gdx;
import com.spartanfox.blocktoidz.Globals.Cal;
import com.spartanfox.blocktoidz.Globals.LevelPreferences;
import static com.spartanfox.blocktoidz.Globals.LevelPreferences.preference;
import com.spartanfox.blocktoidz.Globals.Storage;
import com.spartanfox.blocktoidz.Screens.GameScreen;

/**
 *
 * @author Ben
 */

public class BreakerLevel extends Level{
    byte[][] startingLevel;
    public BreakerLevel(GameScreen main, float x, float y, float width, float height) {
        super(main, x, y, width, height);
        startingLevel = new byte[blocksX][blocksY];
        if(preference.level!=null){
            for (int Bx = 0; Bx < blockArray.length; Bx++) {
                for (int By = 0; By < blockArray[0].length; By++) {
                    startingLevel[Bx][(blocksY-1)-By] = (byte) preference.level[By][Bx];
                }
            }
        }
        score = 1000000;
    }
    @Override
    public void render(){
        super.render();
        super.drawLevel(startingLevel);
        if(this.placed>0)if(!paused&&!gameOver)score-=Cal.timeScale(10);
    }
    @Override
    public void giveScore(long score){
    
    }
    @Override
    public void gameOver(){
        score = 0;
        super.gameOver();
    }
    @Override
    public void updateTimer(){
        if(this.placed>0)super.updateTimer();
    }
    @Override
    public void deleteRow(byte[][] a, int v) {
        super.deleteRow(startingLevel, v);
        super.deleteRow(a,v);
        rows--;
        boolean complete = true;
        for(byte[] column : startingLevel){
            for(byte b : column){
                if(b!=0)complete = false;
            }
        }
        if(complete){
            Storage.updateProgress(preference.mode.toString(),preference.levelRequired+1);
            super.gameOver("Level complete!");
        }
        
    }
    
}
