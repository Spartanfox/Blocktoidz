/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.Globals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import static com.spartanfox.blocktoidz.Globals.LevelPreferences.preference;
import static com.spartanfox.blocktoidz.Globals.Values.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ben
 */
public abstract class Styles {
    private static Skin skin;
    private static HashMap<String,BitmapFont> fonts;
    private static boolean textures = false;
    public static void create(){
        //can be called to recreate the UIs skin for different themes
        if(skin!=null)skin.dispose();
        skin = new Skin(Gdx.files.internal("data/"+preference.theme+"skins/glassy-ui.json"));    
    }
    private static void setFont(String name,BitmapFont font){
        fonts.put(name, font);
    }
    public static BitmapFont getFont(String name){
        /*if(fonts.containsKey(name)){
            return fonts.get(name);
        }*/
        return new BitmapFont();
    }
    public static Skin getSkin(){
        if(skin !=null){
            return skin;
        }
        return new Skin();
    }
    
    //all of these methods are used in the LoadUI methods to construct the user interface
    //in a neater and easier to manage way than creating new object the same way over and over
    public static TextButton newButton(String name,String type,EventListener listener){
        TextButton newButton = new TextButton(name, skin,type);
        newButton.addListener(listener);
        return newButton;
    }
    public static TextButton newButton(String name,EventListener listener){
        TextButton newButton = new TextButton(name, skin);
        newButton.addListener(listener);
        return newButton;
    }
    public static TextButton newButton(String name){
        TextButton newButton = new TextButton(name, skin);        
        return newButton;
    }
    //for simplisity reasons i gave the games buttons their own methods to make the
    //LoadUI a bit more readable 
    public static ImageButton newLeft(EventListener listener){
        ImageButtonStyle style = new ImageButtonStyle();
        style.up = Textures.getDrawable("Left");
        style.down = Textures.getDrawable("LeftSelected");
        ImageButton newButton = new ImageButton(style);
        newButton.addListener(listener);
        return newButton;
    }
    public static ImageButton newRight(EventListener listener){
        ImageButtonStyle style = new ImageButtonStyle();
        style.up = Textures.getDrawable("Right");
        style.down = Textures.getDrawable("RightSelected");
        ImageButton newButton = new ImageButton(style);
        newButton.addListener(listener);
        return newButton;
    }
    public static ImageButton newRotate(EventListener listener){
        ImageButtonStyle style = new ImageButtonStyle();
        style.up = Textures.getDrawable("Rotate");
        style.down = Textures.getDrawable("RotateSelected");
        ImageButton newButton = new ImageButton(style);
        newButton.addListener(listener);
        return newButton;
    }
    public static ImageButton newImageButton(String up, String down,EventListener listener){
        ImageButtonStyle style = new ImageButtonStyle();
        style.up = Textures.getDrawable(up);
        style.down = Textures.getDrawable(down);
        ImageButton newButton = new ImageButton(style);
        if(listener!=null)newButton.addListener(listener);
        return newButton;
    }
    public static ImageButton newImageButton(Drawable img,EventListener listener){
        ImageButtonStyle style = new ImageButtonStyle();
        style.up = img;
        style.down = img;
        ImageButton newButton = new ImageButton(style);
        if(listener!=null)newButton.addListener(listener);
        return newButton;
    }
    public static CheckBox newCheck(String name,EventListener listener){
        CheckBox newCheck = new CheckBox(name, skin);
        newCheck.addListener(listener);
        return newCheck;
    }
    public static Slider newSlider(EventListener listener){
        Slider newSlider = new Slider(0f,1f,0.01f,false,skin);
        newSlider.setValue(1f);
        newSlider.addListener(listener);
        return newSlider;
    }
    /*public static ScrollPane newScrollPane(){
        ScrollPane panel = new ScrollPane(skin);
        return newCheck;
    }*/
    public static TextArea newTextBox(String input, float scale, EventListener event){
        TextArea newText = new TextArea("",skin);
        newText.setMessageText(input);
        newText.setMaxLength(MAX_CHARACTERS);
        newText.setPrefRows(1);
        BitmapFont newFont = skin.getFont("font-big");
        newFont.getData().setScale(scale);
        newText.getStyle().font = newFont;
        newText.addListener(event);
        return newText;
    }
   //can be used to display text instead of newString if you want it to have a border
    public static Label newLabel(String title,int alignment){
        LabelStyle style = new LabelStyle();
        style.background = Textures.getDrawable("Label");
        style.font = skin.getFont("font-big");
        Label newLabel = new Label(title,style);
        newLabel.setAlignment(alignment);
        return newLabel;
    }
    public static Label newLabel(String title){
        LabelStyle style = new LabelStyle();
        style.background = Textures.getDrawable("Label");
        style.font = skin.getFont("font-big");
        Label newLabel = new Label(title,style);
        newLabel.setAlignment(Align.center);
        return newLabel;
    }
    public static Label newString(String title){
        LabelStyle style = new LabelStyle();
        style.font = skin.getFont("font-big");
        Label newLabel = new Label(title,style);
        newLabel.setAlignment(Align.center,Align.center);
        return newLabel;
    }
    public static Label newString(String title,float scale){
        LabelStyle style = new LabelStyle();
        style.font = skin.getFont("font-big");
        Label newLabel = new Label(title,style);
        newLabel.setFontScale(scale);
        return newLabel;
    }
    public static Label newString(String title,float scale,int align){
        LabelStyle style = new LabelStyle();
        style.font = skin.getFont("font-big");
        Label newLabel = new Label(title,style);
        newLabel.setAlignment(align);
        newLabel.setFontScale(scale);
        return newLabel;
    }
    
}
