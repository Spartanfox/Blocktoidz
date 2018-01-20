/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.GameObjects;

import static com.spartanfox.blocktoidz.Globals.Values.MAX_CHARACTERS;

/**
 *
 * @author Ben
 */

//container for the highscores
public final class HighScore {
    String name;
    String time;
    long score;

    static String[] prefix ={"","Lil","Tater","Fluffy","Plurple","Bashie","Purty"};
    static String[] postfix={"","Tot","Nurple","Dragon","Dingo","Pop","Flap"};

    public HighScore(String name, String time, long score){
        if(name.replaceAll(" ","").isEmpty()){
            //name = generateName();
            name = "Enter name";
        }
        if(name.length()>MAX_CHARACTERS)name = name.substring(0,MAX_CHARACTERS);

        this.name = name;
        this.time = time;
        this.score = score;
        
    }
    public String generateName(){
        String name = prefix[(int)(Math.random()*prefix.length)]+postfix[(int)(Math.random()*postfix.length)];
        if(name.length()>MAX_CHARACTERS||name.length()==0) name = generateName();
        return name;
    }
    
    public long getScore(){
        return score;
    }
    
    public String getName(){
        return name;
    }
    
    public String getTime(){
        return time;
    }
    
    public HighScore setName(String name){
        if(name.length()>MAX_CHARACTERS)name = name.substring(0,10);
        this.name = name;
        return this;
    }
    
    @Override
    public String toString(){
        return name+" "+time+" "+score;
    }
    
    
}
