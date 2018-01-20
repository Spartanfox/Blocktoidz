/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.spartanfox.blocktoidz.Globals.LevelPreferences;
import static com.spartanfox.blocktoidz.Globals.LevelPreferences.preference;
import com.spartanfox.blocktoidz.Globals.Sounds;
import com.spartanfox.blocktoidz.Globals.Styles;
import com.spartanfox.blocktoidz.Globals.Textures;
import com.spartanfox.blocktoidz.Globals.Values;
import static com.spartanfox.blocktoidz.Globals.Values.AD_PADDING;
import com.spartanfox.blocktoidz.Main;
import java.util.Iterator;

/**
 *
 * @author Ben
 */
public class TutordialScreen implements Screen , GestureDetector.GestureListener , InputProcessor{

    Main main;
    SpriteBatch batch;
    Stage stage;
    //max 7 rows
    String[] howToPlay={
              "Blocks will stop in\n"
            + "the first block\n"
            + "they touch below.\n"
            + "Swiping down makes\n"
            + "the blocks fall faster"
            
            ,
        
             "Holding down makes the\n"
            + "block perform a phase and\n"
            + "pass through blocks that it\n"
            + "can fit in.",
              
        "Combining blocks of the\n"
       +"same colour will reward\n"
            + "you with tokens, so\n"
            +"will causing combos by\n"
            + "removing multiple rows"
            
            ,
        
        "Tokens give you a score\n"
            + "multiplier which will\n"
            + "drop faster the\n"
            + "higher it is "
            ,     
                "Combos are acheived\n"
            + "by breaking a full layer\n"
            + "below one or more layers\n"
            + "that are nearly full.\n"
            + "It will stop at the max combo\n"
            + "for that difficulty"
            ,     
    };
    int page = 0;
    Texture pageIcon;
    public TutordialScreen(Main main){
        this.main =  main;
        batch = new SpriteBatch();
    }
    public void LoadUI(){
        Gdx.app.log("Blocktoidz","Loading Tutordial screen");
        main.handler.showAds(true);
        /*if(level==null){
            level = preference;
        }*/
        batch = new SpriteBatch();
        
        //background = Textures.get("background");
        
        if(stage!=null)stage.dispose();
        stage = new Stage(main.view);
        
        Table table = new Table(Styles.getSkin());
        Table table2 = new Table(Styles.getSkin());
        
        table.setFillParent(true);
        table.setBackground(Textures.getDrawable("Background"));
        table.top();
        table.add(Styles.newImageButton("Title", "Title",null)).pad(10).padTop(AD_PADDING*4).colspan(5);
        Label levelName = Styles.newLabel("How to play");
        table.row();
        table.add(levelName).width(800).height(200).colspan(5);
        table.row().fill();
        if(Values.transitioning){
            if(pageIcon!=null){pageIcon.dispose();pageIcon = null;}
        }
        if(pageIcon == null)pageIcon = new Texture(Gdx.files.internal("data/help/"+(page+1)+".png"));
        
                Button image = Styles.newImageButton(Textures.toDrawable(pageIcon),null);
        
        if(!Values.transitioning&&Values.DEBUG_MODE){
            image.setTransform(true);
            image.setOrigin(300,200);
            ScaleToAction shrinkAction = new ScaleToAction(); 
            shrinkAction
                    //.setRotation(5);
                    .setScale(0.92f);
            shrinkAction.setDuration(10);
            shrinkAction.setInterpolation(Interpolation.exp5);
            ScaleToAction growAction = new ScaleToAction(); 
            growAction
                    //.setRotation(-5);
                    .setScale(1f);
            growAction.setDuration(10);
            growAction.setInterpolation(Interpolation.exp5);
            
            SequenceAction sequence = new SequenceAction(shrinkAction,growAction);
            RepeatAction repeatAction = new RepeatAction();
            repeatAction.setAction(sequence);
            repeatAction.setCount(RepeatAction.FOREVER);
            image.addAction(repeatAction);
        }
        if(pageIcon!=null)table2.add(image).pad(10).width(600).height(400);
        
        table.add(table2).colspan(5);
        table.row();
        table.add();
        table.row();
        table.add(Styles.newString(howToPlay[page])).pad(5).center().expandY().colspan(5);
        table.row();
                table.add(Styles.newButton("<<",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        firstPage();
                        
                    }
                }
                )).width(150).height(150).right();
                table.add(Styles.newButton("<",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        
                        previousPage();
                    }
                }
                )).width(150).height(150);
        
        table.add(Styles.newString((page+1)+"/"+howToPlay.length,1.5f));
        table.add(Styles.newButton(">",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        nextPage();
                    }
                }
                )).width(150).height(150);
                table.add(Styles.newButton(">>",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        lastPage();
                    }
                }
                )).width(150).height(150).left();
        table.row();
        table.add(Styles.newButton("Back",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("fart");
                        LevelPreferences.setLevel("Default");
                        main.setScreen(main.menu);
                    }
                }
                )).pad(30).width(600).height(150).colspan(5);
        
        
        stage.setDebugAll(Values.DEBUG_MODE);
        stage.addActor(table);
        //table.setFillParent(true);
        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(stage);
        multi.addProcessor(new GestureDetector(this));
        multi.addProcessor(this);
        if(!Values.transitioning)Gdx.input.setInputProcessor(multi);
        else Gdx.input.setInputProcessor(null);
    }
    
    public void firstPage(){
        if(!Values.transitioning){
            if(page!=0){
                page = 0;
                main.transition.setTransition(main.transitions.get(2),.2f);
                main.setScreen(this);
            }
        }
        
    }
    public void lastPage(){
        if(!Values.transitioning){
            if(page!=howToPlay.length-1){
                page = howToPlay.length-1;
                main.transition.setTransition(main.transitions.get(1),.2f);
                main.setScreen(this);
            }
            
        }
        
    }
        public void nextPage(){
        if(!Values.transitioning){
            Gdx.app.log("Blocktoidz","swiped left");
            this.page++;
            if(page>=howToPlay.length)page=howToPlay.length-1;
            else{
                main.transition.setTransition(main.transitions.get(1),.2f);
                main.setScreen(this);
            }
        }
        
    }
    public void previousPage(){
        if(!Values.transitioning){
            Gdx.app.log("Blocktoidz","swiped right");
            this.page--;
            if(page<0)page=0;  
            else{
                main.transition.setTransition(main.transitions.get(2),.2f);
                main.setScreen(this);
            }
        }
        
    }

    @Override
    public void show() {
        LoadUI();
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        LoadUI();    
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
    return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
            if(velocityX<0&&Math.abs(velocityX)>Math.abs(velocityY)){
                nextPage();
                return true;
            }
            if(velocityX>0&&Math.abs(velocityX)>Math.abs(velocityY)){
                previousPage();
                return true;
            }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {return false;
    }

    @Override
    public void pinchStop() {
    }

    @Override
    public boolean keyDown(int keycode) {return false;
    }

    @Override
    public boolean keyUp(int keycode) {return false;
    }

    @Override
    public boolean keyTyped(char character) {return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {return false;
    }

    @Override
    public boolean scrolled(int amount) {return false;
    }
    
}

