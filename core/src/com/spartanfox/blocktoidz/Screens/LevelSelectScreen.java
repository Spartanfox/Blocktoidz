/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.spartanfox.blocktoidz.GameObjects.LevelPreference;
import com.spartanfox.blocktoidz.GameObjects.LevelPreference.Mode;
import com.spartanfox.blocktoidz.Globals.*;
import com.spartanfox.blocktoidz.Globals.LevelPreferences;
import static com.spartanfox.blocktoidz.Globals.LevelPreferences.preference;
import static com.spartanfox.blocktoidz.Globals.Values.AD_PADDING;
import com.spartanfox.blocktoidz.Main;

/**
 *
 * @author Ben
 */
public class LevelSelectScreen implements Screen, GestureListener , InputProcessor{
    SpriteBatch batch;
    Stage stage;
    Main main;
    Texture levelIcon;
    Texture background;
    LevelPreference level;
    boolean locked;
    Mode[] modes;
    public LevelSelectScreen(Main main){
        this.main = main;
        LevelPreferences.preference = level = LevelPreferences.get("default");
    }
    @Override
    public void show() {
        loadUI();
    }
    //loading the level select screens UI
    public void loadUI(){
        Gdx.app.log("Blocktoidz","Loading Level select screen");
        main.handler.loadVideo();
        if(level==null){
            level = preference;
        }
        //if(modes != level.mode)this.previousLevel();
        batch = new SpriteBatch();
        
        background = Textures.get("background");
        locked = false;
        //if selected level is higher than current progress then level is locked
            Gdx.app.debug("Blocktoidz",level.levelRequired+" > "+Storage.getProgress(level.mode.toString()));
        if(level.levelRequired>Storage.getProgress(level.mode.toString())){
            Gdx.app.log("Blocktoidz","Level locked");
            locked = true;
        }
        if(stage!=null)stage.dispose();
        stage = new Stage(main.view);
        Table table = new Table(Styles.getSkin());
        Table table2 = new Table(Styles.getSkin());
        
        table.setFillParent(true);
        table.setBackground(Textures.getDrawable("Background"));
        table.top();
        table.add(Styles.newImageButton("Title", "Title",null)).padTop(AD_PADDING*4).colspan(2);
        Label levelName = Styles.newLabel(level.name);
        table.row();
        table.add(levelName).width(800).height(200).colspan(2);
        table.row().fill();
        if(levelIcon == null)levelIcon = new Texture(Gdx.files.internal("data/levels/"+level.image+".png"));
        if(Values.transitioning){
            if(levelIcon!=null){levelIcon.dispose();levelIcon = null;}
        }
        if(Values.transitioning&&levelIcon==null)levelIcon = new Texture(Gdx.files.internal("data/levels/"+level.image+".png"));
        table2.add(Styles.newButton("<",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        nextLevel();
                    }
                }
                )).pad(30).padBottom(20).width(150).height(150);
        Stack stack = new Stack();
        stack.addActorAt(0,Styles.newImageButton(new TextureRegionDrawable(new TextureRegion(levelIcon)),null));
       
        if(locked){
            stack.addActorAt(1,Styles.newImageButton("Locked","Locked",null));
        }else if(level.mode == Mode.TimeTrial){
            stack.addActorAt(1,Styles.newImageButton("Timer","Timer",null));
        }
        table2.add(stack).pad(10).width(400).height(400);
        table2.add(Styles.newButton(">",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        previousLevel();
                    }
                }
                )).pad(30).padBottom(20).width(150).height(150);
        table.add(table2).colspan(2);
        table.row();
        int pad = 40;
        table.add(Styles.newString("Mode: "+level.mode.toString(),0.8f,Align.right)).pad(5).padRight(pad).expandX().fillX();
        table.add(Styles.newString("Multiplier: "+level.multiplier,0.8f,Align.left)).pad(5).padLeft(pad).expandX().fillX();
        table.row();
        table.add(Styles.newString("Gap allowance: "+level.gap,0.8f,Align.right)).pad(5).padRight(pad).expandX().fillX();
        table.add(Styles.newString("Max combo: "+(level.comboLimit+1),0.8f,Align.left)).pad(5).padLeft(pad).expandX().fillX();
        table.row();
        if(locked)
            table.add(Styles.newString(level.toUnlock)).pad(15).center().colspan(2);
        else
            table.add(Styles.newString(level.description)).pad(15).center().colspan(2);
        table.row();
        if(level.levelRequired!=(Storage.getProgress(level.mode.toString())+1)||!main.handler.loaded()){
            table.add(Styles.newButton("Start Game",
                    new ClickListener(){
                        @Override
                        public void clicked(InputEvent event,float x, float y){
                            if(!locked){
                                if(!Values.transitioning){
                                    Sounds.play("blop");
                                    LevelPreferences.setLevel(level);
                                    main.setScreen(main.game);
                                }
                            }
                        }
                    }
                    )).pad(15).width(600).height(130).colspan(2);
            table.row();
            table.add(Styles.newButton("High Scores",
                    new ClickListener(){
                        @Override
                        public void clicked(InputEvent event,float x, float y){
                            if(!locked){
                                Sounds.play("blop");
                                LevelPreferences.previousLevel = level.name;
                                main.setScreen(main.highScores);
                            }
                        }
                    }
                    )).pad(10).width(600).height(130).colspan(2);

        }else{
            table.add(Styles.newString("or watch video to skip")).pad(15).width(600).height(130).colspan(2);
            table.row();
            table.add(Styles.newButton("Watch Video",
                    new ClickListener(){
                        @Override
                        public void clicked(InputEvent event,float x, float y){
                                Sounds.play("blop");
                                main.handler.showVideo();
                                loadUI();
                        }
                    }
                    )).pad(10).width(600).height(130).colspan(2);

        }
                table.row();
        table.add(Styles.newButton("Back",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("fart");
                        LevelPreferences.setLevel("Default");
                        main.setScreen(main.modeSelect);
                    }
                }
                )).pad(10).width(600).height(130).colspan(2);
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

    @Override
    public void render(float delta) {
        //batch.begin();
        //batch.draw(background,0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        //batch.end();
        stage.act();
        stage.draw();
    }
    public void setModes(Mode... modes){
        this.modes = modes;
        LevelPreferences.setLevel("Default");
        level = LevelPreferences.get("Default");
        while(!isMode(level.mode)){
            Gdx.app.debug("Blocktoidz","Finding");
            level = LevelPreferences.getRealive(1);
        };
        LevelPreferences.setLevel("Default");
    }
    public void nextLevel(){
        if(!Values.transitioning){
            do{
                Gdx.app.debug("Blocktoidz","swiped left");
                LevelPreferences.setLevel(level);
                level = LevelPreferences.getRealive(-1);
                LevelPreferences.setLevel("Default");
            }while(!isMode(level.mode));
            main.transition.setTransition(main.transitions.get(2),.2f);
            main.setScreen(this);    
        }
    }
    public void previousLevel(){
        if(!Values.transitioning){
            Gdx.app.debug("Blocktoidz","swiped right");
            do{
                LevelPreferences.setLevel(level);
                level = LevelPreferences.getRealive(1);
                LevelPreferences.setLevel("Default");
            }while(!isMode(level.mode));
            main.transition.setTransition(main.transitions.get(1),.2f);
            main.setScreen(this);
        }
    }
    public void unlockNext(){
        Storage.updateProgress("Breaker",Storage.getProgress("Breaker")+1);
    }
    public boolean isMode(Mode mode){
        for(Mode m : modes){
            if(mode == m)return true;
        }
        return false;
    }
    
    @Override
    public void resize(int width, int height) {
        loadUI();
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
                previousLevel();
                return true;
            }
            if(velocityX>0&&Math.abs(velocityX)>Math.abs(velocityY)){
                nextLevel();
                return true;
            }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {
    }
    @Override
public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                nextLevel();
                return true;
            case Input.Keys.RIGHT:
                previousLevel();
                return true;
            default:
                return false;
        }
    }
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
}