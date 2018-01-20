/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spartanfox.blocktoidz.Globals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import static com.spartanfox.blocktoidz.Globals.LevelPreferences.preference;
import java.util.ArrayList;

/**
 *
 * @author Ben
 */
public abstract class Textures {
    private static ArrayList<TextureHandler> textures = new ArrayList();
    private static Texture error;
    Textures(){
        
    }
    
    
    public static void load(){
        //dispose all the unused textures during a theme change to avoid memory leaks
        for(TextureHandler t : textures){
            if(t.texture!=null)t.texture.dispose();
        }
        textures.clear();
        
        //if the textures are corrupt or just arent there then the error TextureHandler will
        //be a placeholder which is generated programatically so it is always there
        int size = 64;
        Pixmap pixmap = new Pixmap(size,size, Format.RGBA8888 );
        pixmap.setColor(1f,0f,0f,1f);
        pixmap.fillCircle(size/2,size/2,size/2);
        pixmap.setColor(0f,0f,0f,1f);
        pixmap.fillCircle(size/2,size/2,(size/2)-2);
        pixmap.setColor(1f,0f,0f,1f);
        int offset = size/6;
        int length = (size+size)/3;
        pixmap.drawLine(offset,offset,offset+length,offset+length);
        pixmap.drawLine(offset+length,offset,offset,offset+length);
        error = new Texture(pixmap);
        pixmap.dispose();
        //things that get rendered the most get put at the top so theyre the first in the list
        textures.add(new TextureHandler("Block" ,"Block.png",true));
        textures.add(new TextureHandler("Block1","Block1.png",true));
        textures.add(new TextureHandler("Block2","Block2.png",true));
        textures.add(new TextureHandler("Block3","Block3.png",true));
        textures.add(new TextureHandler("Block4","Block4.png",true));
        textures.add(new TextureHandler("GameBackground","GameBackground.png",true));
        
        textures.add(new TextureHandler("Hints","Hints.png",false));
        textures.add(new TextureHandler("Left"  ,"ButtonLeft.png",true));
        textures.add(new TextureHandler("Right" ,"ButtonRight.png",true));
        textures.add(new TextureHandler("Rotate","ButtonRotate.png",true));
        textures.add(new TextureHandler("Pause" ,"ButtonPause.png",true));
        textures.add(new TextureHandler("Label" ,"TextBox.png",true));
        
        textures.add(new TextureHandler("Locked","levels/Locked.png",false));
        textures.add(new TextureHandler("Timer","levels/Clock.png",false));
        
        textures.add(new TextureHandler("PauseSelected","SelectedPause.png",true));
        textures.add(new TextureHandler("LeftSelected","SelectedLeft.png",true));
        textures.add(new TextureHandler("RightSelected","SelectedRight.png",true));
        textures.add(new TextureHandler("RotateSelected","SelectedRotate.png",true));
        
        textures.add(new TextureHandler("Background","Background.png",false));
        textures.add(new TextureHandler("Title","MainMenuTitle.png",false));

    }
    
    
    //only use in non gameplay areas if the game requires textures use Texture.get()
    //to retreive the TextureHandler and put it into a reference to avoid needless performance drops
    public static Texture get(String name){
        for(TextureHandler t : textures){
            if(t.getName().toLowerCase().equals(name.toLowerCase())){
                return t.getTexture();
            }
        }
        return error;
    }
    public static TextureRegionDrawable toDrawable(Texture img){
                    return new TextureRegionDrawable(new TextureRegion(img));

        }
    public static TextureRegionDrawable getDrawable(String name){
        for(TextureHandler t : textures){
            if(t.getName().toLowerCase().equals(name.toLowerCase())){
                return new TextureRegionDrawable(new TextureRegion(t.getTexture()));
            }
        }
        Gdx.app.error("Blocktoidz",name+ " failed to load");
        return new TextureRegionDrawable(new TextureRegion(error));
    }
    public static boolean unload(String name){
         for(TextureHandler t : textures){
            if(t.getName().toLowerCase().equals(name.toLowerCase())){
                t.texture.dispose();
                t.texture = null;
                return true;
            }
        }
        return false;
    }
    public static boolean unloadAll(){
         for(TextureHandler t : textures){
                if(t.texture!=null){
                   t.texture.dispose();
                   t.texture = null;
                }
                return true;
        }
        return false;
    }
    public static void reloadAll(){
        //check if the theme has changed
        for(TextureHandler t : textures){
            if(t.themed){
                if(!t.theme.equals(preference.theme)){
                    Gdx.app.debug("Blocktoidz","reloading textures");
                    unloadAll();
                    load();
                    break;
                }
            }
        }
    }
    private static class TextureHandler{
        private Texture texture;
        private String name;
        private String location;
        private boolean themed;
        private String theme;
        TextureHandler(String name, String location,boolean themed){
            this.location = location;
            this.name = name;
            this.themed = themed;
            if(themed) theme = preference.theme;
        }
        public Texture getTexture(){
            if(texture == null){
                try{
                    if(themed)
                        this.texture = new Texture(Gdx.files.internal("data/"+preference.theme+location));
                        
                    else
                        this.texture = new Texture(Gdx.files.internal("data/"+location));
                    
                //this.texture.setFilter(TextureFilter.Nearest, TextureFilter.MipMapNearestNearest);
                }catch(Exception e){
                    texture = error;
                }
            }
            return texture;
        }
        public String getName(){
            return name;
        }
    }
}
                              