/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.Globals;

import com.badlogic.gdx.Gdx;

/**
 *
 * @author Ben
 */

//will be used for complicated calculations that need to be preformed
//within the game
public abstract class Cal {
    
    public static float range(float x, float x2){
        return  Math.abs(x-x2);
    }
    public static double map(double x, double in_min, double in_max, double out_min, double out_max){
          return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
    public static float map(float x, float in_min, float in_max, float out_min, float out_max){
        if(x<in_min)x=in_min;
        if(x>in_max)x=in_max;
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
    public static float timeScale(float input){
        return (input*60)*Gdx.graphics.getDeltaTime();
    }
    
}
