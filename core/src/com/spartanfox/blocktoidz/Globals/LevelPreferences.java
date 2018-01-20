/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.Globals;
import static com.spartanfox.blocktoidz.GameObjects.Block.top;
import static com.spartanfox.blocktoidz.GameObjects.Block.bottom;
import static com.spartanfox.blocktoidz.GameObjects.Block.left;
import static com.spartanfox.blocktoidz.GameObjects.Block.right;
import static com.spartanfox.blocktoidz.GameObjects.Block.solid;
import com.spartanfox.blocktoidz.GameObjects.HighScore;
import com.spartanfox.blocktoidz.GameObjects.LevelPreference;
import com.spartanfox.blocktoidz.GameObjects.LevelPreference.Mode;
import java.util.ArrayList;
import java.util.HashMap;
import static com.spartanfox.blocktoidz.GameObjects.Block.R;
import static com.spartanfox.blocktoidz.GameObjects.Block.G;
import static com.spartanfox.blocktoidz.GameObjects.Block.B;
import java.util.Calendar;
import java.util.Date;
/**
 *
 * @author Ben
 */
public abstract class LevelPreferences {
    //public static HashMap<String,LevelPreference> preferenceMode;
    public static ArrayList<LevelPreference> preferences;
    public static LevelPreference preference;
    public static HighScore previousScore;
    public static String previousLevel;
    public static void load(){
        int w = R|G|B;
        
        preferences = new ArrayList();
        
        
        //kind of terrible way of creating the levels but it works 
        preferences.add(new LevelPreference("Default","The default blocktoidz\nexperience").mode(Mode.Normal).Image("Default").Theme(""));
        preferences.add(new LevelPreference("Hard Difficulty","Faster speeds and no gaps\n").mode(Mode.Hard).Image("Wood").Theme("wood"));
        
        preferences.add(new LevelPreference("Insane Difficulty","No combos and no gaps\nvery difficult").mode(Mode.Insane).Image("Metal").Theme("metal"));
        preferences.add(new LevelPreference("Small Grid","This gamemode requires fast\nmovements and alot of skill").mode(Mode.Hard).MultiplyScore(5).Image("Small").Grid(4,5).setBigBlock(0.0001f));
        
        preferences.add(new LevelPreference("Large Grid","Much larger grid space\nthan the normal game").Image("Large").mode(Mode.Normal).Grid(12,14).ComboLimit(10).setBigBlock(1));
        preferences.add(new LevelPreference("Big Block Free","Same as default level\nbut no big blocks").mode(Mode.Normal).Image("Default").Theme("").setBigBlock(0));        

        preferences.add(new LevelPreference("1 Min time trial","Get as many points as possible\nbefore the 1 minute is up").mode(Mode.TimeTrial).MultiplyScore(2).Grid(4,5).Image("Small").Time(1));
        preferences.add(new LevelPreference("3 Min time trial","Get as many points as possible\nbefore the 3 minutes is up").mode(Mode.TimeTrial).MultiplyScore(5).Image("Default").Time(3));
        preferences.add(new LevelPreference("5 Min time trial","Get as many points as possible\nbefore the 5 minutes is up").mode(Mode.TimeTrial).MultiplyScore(4).Image("Default").Time(5));
        int level = 0;
        if(new Date().getMonth() == 11){
            preferences.add(new LevelPreference("Christmas tree","Christmas cheer and\nlots of beer").mode(Mode.Breaker).Image("ChristmasTree").Theme("").Level(new int[][]{
                        { 0, 0, 0, 0, 0, 0},
                        { 0, 0, right|R|G, left|R|G, 0, 0},
                        { 0, 0, bottom|right|G, bottom|left|G, 0, 0},
                        { 0, bottom|right|G, solid|R, solid|G, bottom|left|G, 0},
                        { 0, bottom|right|G, solid|G, solid|G, bottom|left|G, 0},
                        { bottom|right|G, solid|B, solid|G, solid|R|B, solid|G, bottom|left|G},
                        { 0, 0, solid, solid, 0, 0},
                    }
            ).setlevelRequired(level,"Complete the "+preferences.get(preferences.size()-1).name+"\nlevel to unlock this")); 

        }
        preferences.add(new LevelPreference("Pyramid","Destroy the pyramid!\n").mode(Mode.Breaker).Image("Pyramid").Theme("").Level(new int[][]{
                    { 0, 0, 0, 0, 0, 0},
                    { 0, 0, 0, 0, 0, 0},
                    { 0, 0, 0, 0, 0, 0},
                    { 0, 0, 0, 0, 0, 0},
                    { 0, 0, bottom|right|R|G,bottom|left|R|G, 0, 0},
                    {0,bottom|right|R|G,solid|R|G,solid|R|G,bottom|left|R|G,0},
                    {bottom|right|R|G,solid|R|G,top|left|R|G,top|right|R|G,solid|R|G,bottom|left|R|G},
                }
        ).setlevelRequired(level,"Complete the "+preferences.get(preferences.size()-1).name+"\nlevel to unlock this")); 
        level++;
        preferences.add(new LevelPreference("House","Break the\nhouse down").mode(Mode.Breaker).Image("House").Theme("wood").Level(new int[][]{
                    { 0, 0, 0, 0, 0, 0},
                    { 0, 0, 0, 0, 0, 0},
                    { 0, 0, bottom|right,bottom|left,left|bottom|right, 0},
                    { 0, bottom|right,solid|R|G|B,solid|R|G|B,solid, 0},
                    {bottom|right,solid|R|G|B,solid|R|G|B,solid|R|G|B,solid|R|G|B,left|bottom},
                    {0,solid|R|G|B,solid|R,solid|R|G|B,solid|R|G|B,0},
                    {0,solid|R|G|B,solid|R,solid|R|G|B,solid|R|G|B,0},
                }
        ).setlevelRequired(level,"Complete the "+preferences.get(preferences.size()-1).name+"\nlevel to unlock this")); 
        level++;
        preferences.add(new LevelPreference("Hover Tank","WE MUST FIGHT\nTHE REBELLION!!").mode(Mode.Breaker).Image("Tank").Theme("").Level(new int[][]{
                { 0, 0, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0},
                { 0, 0, right|G, bottom|R|G, 0, 0},
                { 0, bottom|right|G, solid|G, solid|G, left|right,   left},
                { 0,        solid|G, solid|G, solid|G,    solid|G,      0},
                { 0,        top|B|G, top|B|G, top|B|G,    top|B|G,      0},
            }
        ).setlevelRequired(level,"Complete the "+preferences.get(preferences.size()-1).name+"\nlevel to unlock this")); 
        level++;
        preferences.add(new LevelPreference("Rocket Ship","No blast off\nfor you").mode(Mode.Breaker).Image("Rocket").Theme("metal").Level(
                new int[][]{
                    { 0, 0, 0, 0, 0, 0},
                    { 0, 0, 0, 0, 0, 0},
                    { 0, 0,12, 9, 0, 0},
                    { 0,20,11,14,17, 0},
                    { 0, 0,15,15, 0, 0},
                    { 0,25,14,11,28, 0},
                    {28,31,15,15,31,25},
                }
        ).setlevelRequired(level,"Complete the "+preferences.get(preferences.size()-1).name+"\nlevel to unlock this")); 
                level++;
        preferences.add(new LevelPreference("Dragon","This dragons very\nhard to beat").mode(Mode.Breaker).Image("Dragon").Theme("").Level(new int[][]{
                    { 0, 0, 0, 0, 0, 0},
                    { 0, 0, 0, 0, 0, 0},
                    { 0, 0, 0,  bottom|R|G, bottom|R|G,0},
                    {bottom|right|R|G|B,solid|R|G|B,left|bottom|R|G|B,bottom|right|top|R|G|B,solid|R|G|B,left|bottom|R|G|B},
                    { top|right|R|G,left|top|right|R|G,solid|R|G|B,left|top|bottom|R|G|B, 0, 0},
                    { bottom|R|G,bottom|right|R|G|B,solid|R|G|B,left|R|G|B,left|R|G|B, 0},
                    { top|right|R|G|B,left|top|R|G|B,top|right|bottom|R|G|B,left|bottom|R|G|B,0,0},
                }
        ).setlevelRequired(level,"Complete the "+preferences.get(preferences.size()-1).name+"\nlevel to unlock this")); 
                level++;   
                preferences.add(new LevelPreference("Palm Tree","Time for a\nvacation!").mode(Mode.Breaker).Image("Tree").Theme("wood").Level(new int[][]{
                    { 0, 0, 0, 0, 0, 0},
                    { 0, G|bottom, 0,0, G|bottom, 0},
                    { G|bottom|right,G|solid,G|solid-top,G|solid-top,G|solid,G|bottom|left},
                    { G|bottom|right,G|left|top, w|solid-left,w|solid-right,G|top|right,G|bottom|left},
                    { 0, 0, w|solid-left,w|solid-right, 0, 0},
                    { 0, 0, w|solid-left,w|solid-right, 0, 0},
                    { 0,w|right|bottom, w|solid,w|solid,w|left|bottom, 0},
                }
        ).setlevelRequired(level,"Complete the "+preferences.get(preferences.size()-1).name+"\nlevel to unlock this")); 
                level++;  
        preferences.add(new LevelPreference("More levels","").mode(Mode.Breaker  ).Image("Locked").Theme("metal").setlevelRequired(100,"More levels to come!\n")); 
        preferences.add(new LevelPreference("More levels","").mode(Mode.TimeTrial).Image("Locked").Theme("metal").setlevelRequired(100,"More levels to come!\n")); 

        /*
        new byte[][]{
                    {111, 97,000,105, 97,000,000},
                    {000,104,104,000,104,000,000},
                    {000,000,000,000,000,000,000},
                    {000,111, 98,000, 97,102,000},
                    {000,000,109, 99,000,000,000},
                    {108,000,000,106,101,106,000},
                   
                }
        
        new byte[][]{
                    {1 ,12,13,19,25,0,0},
                    {2 ,11,14,20,26,0,0},
                    {3 ,10,15,21,27,0,0},
                    {4 ,9 ,16,22,28,0,0},
                    {5 ,8 ,17,23,29,0,0},
                    {6 ,7 ,18,24,30,0,0},
                }
        
        */
        preference = preferences.get(0);
    }
    public static LevelPreference get(String name){
        for(LevelPreference p : preferences ){
            if(p.name.toLowerCase().equals(name.toLowerCase())){return p;}
        }
        return preferences.get(0);
    }
    
    //getting the levels relative to the current or selected level
    public static LevelPreference getRealive(int Relative){
        if(preferences.indexOf(preference)+Relative<0){
            return preference = preferences.get(preferences.size()-1);
        }else{
            return preference = preferences.get(Math.abs(preferences.indexOf(preference)+Relative)%preferences.size());
        }
    }    
    public static LevelPreference getRealive(LevelPreference currentLevel,int Relative){
        if(preferences.indexOf(currentLevel)+Relative<0){
            return preferences.get(preferences.size()-1);
        }else{
            return preferences.get(Math.abs(preferences.indexOf(currentLevel)+Relative)%preferences.size());
        }
    }
    public static void setLevel(String name){
        preference = get(name);
    }
    //probably shouldnt be used since the levels are already contained within the list
    //but will allow for custom levels to be used that arent within the list
    public static void setLevel(LevelPreference Preference){
        preference = Preference;
    }
}
