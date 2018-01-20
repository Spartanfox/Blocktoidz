/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.Globals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ben
 */
public abstract class Sounds {
    private static Music music;
    private static HashMap<String,Sound> sounds;
    public static float soundsVol,musicVol,masterVol;
    public static void create(){
        music = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/Music.mp3"));
        sounds = new HashMap();
        sounds.put("blop",Gdx.audio.newSound(Gdx.files.internal("data/Sounds/blop.wav")));
        sounds.put("fart",Gdx.audio.newSound(Gdx.files.internal("data/Sounds/lose.wav")));
        music.setLooping(true);
        float[] SoundSettings = Storage.getSounds();
        soundsVol = SoundSettings[2];
        musicVol = SoundSettings[1];
        masterVol = SoundSettings[0];
        music.setVolume(musicVol*masterVol);
    }
    public static void restart(){
        music.setPosition(0);
        music.play();
    }
    public static void pause(){
        music.pause();
    }
    public static void play(){
        music.play();
    }
    public static void play(String sound){
        if(!Values.transitioning)sounds.get(sound).play(soundsVol*masterVol);
    }
    public static void play(String sound,float pitch){
        if(!Values.transitioning)sounds.get(sound).setPitch(sounds.get(sound).play(soundsVol*masterVol),pitch);
    }
    public static void setVolume(float vol){
        music.setVolume(vol*masterVol);
        musicVol = vol;
    }
    public static void setSoundVolume(float vol){
        soundsVol = vol;
    }
    public static void save(){
        Storage.setSounds(masterVol, musicVol, soundsVol);
    }
}
