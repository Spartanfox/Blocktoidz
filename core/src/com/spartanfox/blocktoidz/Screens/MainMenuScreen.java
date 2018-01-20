 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.*;
import com.spartanfox.blocktoidz.Globals.Sounds;
import com.spartanfox.blocktoidz.Globals.Storage;
import com.spartanfox.blocktoidz.Globals.Styles;
import com.spartanfox.blocktoidz.Globals.Textures;
import com.spartanfox.blocktoidz.Globals.Values;
import static com.spartanfox.blocktoidz.Globals.Values.AD_PADDING;
import com.spartanfox.blocktoidz.Main;

/**
 *
 * @author Ben
 */
public class MainMenuScreen implements Screen{
    Stage stage;
    Main main;
    SpriteBatch batch;
    public MainMenuScreen(Main main){
        this.main = main;
        batch = new SpriteBatch();
    }    

    @Override
    public void show() {
        LoadUI();
        //Run once code goes here
    }
    private void LoadUI(){
        Gdx.app.log("Blocktoidz","Loading Main menu screen");
        main.handler.showAds(true);
        if(stage!=null)stage.dispose();
        stage = new Stage(main.view);
        
        Table table = new Table(Styles.getSkin());
        table.setFillParent(true);
        table.setBackground(Textures.getDrawable("Background"));
        
        table.top();
        table.add(Styles.newImageButton("Title", "Title",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        main.handler.showVideo();
                    }
                }
                )).pad(30).padTop(AD_PADDING*4).colspan(2);
        table.row();
        //table.add(Styles.newString("Expires in "+((Values.DEADLINE-TimeUtils.millis())/1000/60/60/24)+" days",1));
        table.add("").height(0);
        table.row();
        table.add(Styles.newButton("Start",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        main.setScreen(main.modeSelect);
                    }
                }
                )).pad(20).width(600).height(250).colspan(2);
        table.row();
        table.add(Styles.newButton("How to play",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        main.setScreen(main.tutordials);
                    }
                }
                )).pad(20).width(600).height(250).colspan(2);;
        table.row();
        table.add(Styles.newButton("Settings",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        main.setScreen(main.settings);
                    }
                }
                )).pad(20).width(600).height(250).colspan(2);;
        table.row();
        table.add(
                Styles.newButton("Close",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("fart");
                        Gdx.app.exit();
                    }
                }
                )
        ).pad(20).width(600).height(250).colspan(2);;
        
        table.row();
        
        table.add(Styles.newString("Version "+Values.FULLVERSION,.7f)).padTop(150).left().padLeft(70);
        table.add(Styles.newString("Spartanfox productions"     ,.7f)).padTop(150).padRight(60).right();
        stage.addActor(table);
        
        stage.setDebugAll(Values.DEBUG_MODE);
        //table.setFillParent(true);
        if(!Values.transitioning)Gdx.input.setInputProcessor(stage);
        else Gdx.input.setInputProcessor(null);
    }
    @Override
    public void render(float delta) {
       // batch.begin();
         //   batch.draw(Textures.get("Background"),0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
       // batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        LoadUI();
        //stage.getCamera().viewportWidth = width;
        //stage.getCamera().viewportHeight = height;
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
       
    }

    @Override
    public void hide() {
        stage.dispose();
        stage = null;
    }

    @Override
    public void dispose() {
        //stage.dispose();
        //stage = null;
    }
    
}
