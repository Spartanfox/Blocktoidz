/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.Globals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.spartanfox.blocktoidz.GameObjects.HighScore;
import com.spartanfox.blocktoidz.GameObjects.LevelPreference;
import com.spartanfox.blocktoidz.GameObjects.LevelPreference.Mode;
import static com.spartanfox.blocktoidz.Globals.LevelPreferences.preferences;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 * @author Ben
 */

public abstract class Storage {
    public static Preferences scores;
    public static Preferences settings;
    public static Preferences progress;
    //need to redo the highscores to suit multiple level types and difficulties
    
    public static void load(){
        scores = Gdx.app.getPreferences("HighScores");
        settings = Gdx.app.getPreferences("Settings");
        progress = Gdx.app.getPreferences("Progress");
        //this code only runs once when you first start the game 
        if(!settings.getBoolean("first")){
            reset();
            
            setSounds(1f,0.4f,0.5f);
            settings.putBoolean("hints", true);
            settings.putBoolean("preview", true);
            settings.putBoolean("controls", false);
            settings.flush();
            
        }
       
        Values.hints   = settings.getBoolean("hints");
        Values.preview = settings.getBoolean("preview");
        Values.control = settings.getBoolean("controls") ? Values.control.Complex : Values.control.Simple;
    }
    public static void reset(){
            clearAllProgress();
        ClearAllHighScores();
            settings.putBoolean("first",true);
            SaveHighScore("Default",new HighScore("Spartanfox","20:00:00",200000));
            SaveHighScore("Default",new HighScore("Gold","15:00:00",150000));
            SaveHighScore("Default",new HighScore("Silver","10:00:00",100000));
            SaveHighScore("Default",new HighScore("Bronze","05:00:00",50000));
            settings.flush();
    }
    public static HighScore[] getHighScores(String levelName){
        HighScore[] scoresArray = new HighScore[10];
        scores = Gdx.app.getPreferences("HighScores"+levelName);
        for (int i = 0; i < scoresArray.length; i++) {
            scoresArray[i] = new HighScore(scores.getString(i+"Name"),scores.getString(i+"Time"),scores.getLong(i+"Score"));
            if(scores.getString(i+"Time").length() == 0){
                ClearHighScores(levelName);
                scoresArray = getHighScores(levelName);
                break;
            }
            Gdx.app.debug("Blocktoidz","Loaded: "+scoresArray[i].toString());
        }
        return scoresArray;
    }
    public static boolean isHighScore(String levelName, long score){
        HighScore[] highScores = getHighScores(levelName);
        if(score >= highScores[0].getScore())return true;
        return false;
    }
    public static void SaveHighScore(String levelName, HighScore newScore){
        
        //if the new score is empty then dont attempt to save it and instead just go back
        if(newScore == null){return;}
        
        Gdx.app.debug("Blocktoidz","Saving high score:"+newScore.getScore());
        //convert the high scores list to make it easier to insert the new highscore
        LinkedList<HighScore> highScores = new LinkedList(Arrays.asList(getHighScores(levelName)));
        
        for(HighScore s : highScores){
            //if the new score is bigger than the current high score then insert it above 
            if(s.getScore()<newScore.getScore()){
                Gdx.app.debug("Blocktoidz","Inserting "+newScore.toString());
                highScores.add(highScores.indexOf(s),newScore);
                break;
            }
        }
        //save these high scores as the new high scores
        SaveHighScores(levelName, highScores.toArray(new HighScore[highScores.size()]));
    }   
    public static void ClearAllHighScores(){
        for(LevelPreference p : preferences){
            ClearHighScores(p.name);
        }
    }
    public static void ClearHighScores(String levelName){
        HighScore[] scoresArray = new HighScore[10];
        for (int i = 0; i < scoresArray.length; i++) {
            
            scoresArray[i] = new HighScore("___________","00:00:00",0);
        }
        
        SaveHighScores(levelName,scoresArray);
    }
    public static void SaveHint(boolean value){
        settings.putBoolean("hints",value);
        settings.flush();
    }
    public static void SavePreview(boolean value){
        settings.putBoolean("preview",value);
        settings.flush();
    }
    public static void saveControls(boolean value){
        settings.putBoolean("controls",value);
        settings.flush();
    }
    public static void SaveHighScores(String levelName, HighScore[] highScores){
        Gdx.app.log("Blocktoidz","Saving highscores");
        scores = Gdx.app.getPreferences("HighScores"+levelName);
        for (int i = 0; i < highScores.length; i++) {
            Gdx.app.log("Blocktoidz","Saving: "+highScores[i].toString());
            scores.putLong  (i+"Score",highScores[i].getScore());
            scores.putString(i+"Time",highScores[i].getTime()  );
            scores.putString(i+"Name",highScores[i].getName().replaceAll("\n"," "));
        }
        scores.flush();
    }
    public static void setSounds(float master,float music,float sound){
        settings.putFloat("MasterSound",master);
        settings.putFloat("Music",music);
        settings.putFloat("Sound",sound);
        settings.flush();
    }
    public static void setTheme(String theme){
        settings.putString("Theme", theme);
    }
    public static float[] getSounds(){
        return new float[]{settings.getFloat("MasterSound"),settings.getFloat("Music"),settings.getFloat("Sound")};
        
    }
    public static long getProgress(String mode){
        return progress.getLong(mode);
    }
    public static void updateProgress(String mode,long completed){
        long current = progress.getLong(mode);
        if(completed > current){
            progress.putLong(mode,completed);
            progress.flush();
        }
    }
    public static void clearAllProgress(){
        for(Mode m : Mode.values()){
            progress.putLong(m.toString(),0);
        }
        progress.flush();
    }
}
