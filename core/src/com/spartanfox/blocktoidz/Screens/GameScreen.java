/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.Screens;

import com.spartanfox.blocktoidz.Globals.LevelPreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;// ClickListener;
import com.badlogic.gdx.utils.Align;
import com.spartanfox.blocktoidz.GameObjects.Block;
import com.spartanfox.blocktoidz.GameObjects.LevelPreference;
import com.spartanfox.blocktoidz.Globals.*;
import static com.spartanfox.blocktoidz.Globals.LevelPreferences.preference;
import static com.spartanfox.blocktoidz.Globals.Values.*;
import com.spartanfox.blocktoidz.Levels.*;
import com.spartanfox.blocktoidz.Main;

/**
 *
 * @author Ben
 */
public class GameScreen implements Screen, GestureListener , InputProcessor{
    Stage stage;
    SpriteBatch batch;
    Label score;// = Styles.newLabel("     score: 00000000");
    Label time; //= Styles.newLabel ("     time : 12:14:16");
    Label rowsLabel;
    Label tokenLabel;
    public Main main;
    public short hintsCounter;
    Level level;
    //will be used in the future to make the games clock work
    float timer;
    int  i;
    
    public GameScreen(Main main){
       // Textures.load();
        this.main = main;
        
    }
    public void LoadUI(){
        
        Gdx.app.log("Blocktoidz","Loading Game screen");
        //Gdx.gl20.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()-100);
        
        i = 0;
        stage = new Stage(main.view);
        stage.getCamera().viewportWidth  = WIDTH ;
        stage.getCamera().viewportHeight = HEIGHT;
        
        
        batch = new SpriteBatch();
        Table topTable = new Table(Styles.getSkin());
        Table bottomTable = new Table(Styles.getSkin());
        Table table = new Table(Styles.getSkin());
        
        score = Styles.newLabel("     Score: 0",Align.left);
        time = Styles.newLabel ("     Time : 00:00:00",Align.left);
        rowsLabel  = Styles.newLabel("0",Align.center);
        tokenLabel = Styles.newLabel("0",Align.center);
        table.padTop(Values.AD_PADDING*2.65f);
        
        topTable.top().left().padTop(17);
        topTable.row().height(135).width(stage.getWidth()/1.4f);
        topTable.add(score).center().width(stage.getWidth()/1.8f).bottom();
        topTable.add(rowsLabel).width(stage.getWidth()/7.4f).bottom();
        

        
        topTable.row().height(135).width(stage.getWidth()/1.4f);
        topTable.add(time ).center().width(stage.getWidth()/1.8f);
        topTable.add(tokenLabel).width(stage.getWidth()/7.4f);
        

        bottomTable.row().fillX().height(stage.getWidth()/3.4f);
        bottomTable.add(Styles.newLeft(
            new ClickListener(){
                @Override
                public void clicked(InputEvent event,float x, float y){
                    if(hintsCounter==0){
                        Sounds.play("blop");
                        if(level!=null)level.Left(true);
                    }else hideHints();
                }
            }
        )).expandX().fill().padLeft(15).padRight(15);
        bottomTable.add(Styles.newRight (
            new ClickListener(){
                @Override
                public void clicked(InputEvent event,float x, float y){
                    if(hintsCounter==0){
                        Sounds.play("blop");
                        if(level!=null)level.Right(true);
                    }else hideHints();
                }
            }
        )).expandX().fill().padLeft(15).padRight(15);
        bottomTable.add(Styles.newRotate(
            new ClickListener(){
                @Override
                public void clicked(InputEvent event,float x, float y){
                    if(hintsCounter==0){
                        Sounds.play("blop");
                        if(level!=null)level.Rotate(true);
                    }else hideHints();
                }
            }
        )).expandX().fill().padLeft(15).padRight(15);
        
        table.setFillParent(true);
        table.row();
        table.add(topTable).top().center().fill();
        table.add(Styles.newImageButton("Label","Label",new ClickListener(){
                @Override
                public void clicked(InputEvent event,float x, float y){
                    if(hintsCounter==0){
                        if(level!=null)level.paused=true;
                        LoadUI();
                        Sounds.play("blop");
                        Sounds.pause();
                    }else hideHints();
                    
                }
            })).center().padTop(4).fill().width(WIDTH/3.1f).height(300);
        table.row();
        
        if(level!=null){
            //if paused is true display pause screen
            if(level.paused){
                if(hintsCounter<=1)main.handler.showAds(true);
                
                final int height = 150;
                //pause menu
                table.add(Styles.newLabel("Paused")).height(200).width(600).padTop(180).padBottom(80).expandY().colspan(3);
                table.row();
                table.add(Styles.newButton("Resume",new ClickListener(){
                @Override
                public void clicked(InputEvent event,float x, float y){
                    level.paused=false;
                    Sounds.play();
                    //level.unpause();
                    LoadUI();
                    Sounds.play("blop");
                }})).height(height).width(400).pad(70).expandY().colspan(3);
                table.row();
                table.add(Styles.newButton("Restart",new ClickListener(){
                @Override
                public void clicked(InputEvent event,float x, float y){
                    Sounds.play("fart");
                    Sounds.restart();
                    main.setScreen(main.game);
                }})).height(height).width(400).pad(70).expandY().colspan(3);
                table.row();
                table.add(Styles.newButton("View hints",new ClickListener(){
                @Override
                public void clicked(InputEvent event,float x, float y){
                    Sounds.play("blop");
                    //exploit how the pause and pause display
                    //are seperate to display the hints screen
                    if(level!=null)level.paused=false;
                    LoadUI();
                    level.paused=true;
                    hintsCounter = 100*60;
                    
                }})).height(height).width(400).pad(70).expandY().colspan(3);
                table.row();
                table.add(Styles.newButton("Exit",new ClickListener(){
                @Override
                public void clicked(InputEvent event,float x, float y){
                    Sounds.play("fart");
                    level.gameOver();
                }})).height(height).width(400).pad(70).padBottom(200).expandY().colspan(3);
                
            }else {
                table.add("").expandY().fillX();
            }
        }
        else{ 
            table.add("").expandY().fillX();
        }
        table.row();
        table.add(bottomTable).bottom().center().expandX().fillX().colspan(3);
        
        
        stage.addActor(table);
        
        stage.setDebugAll(DEBUG_MODE);
        
        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(stage);
        multi.addProcessor(this);
        multi.addProcessor(new GestureDetector(this));
        if(!Values.transitioning)Gdx.input.setInputProcessor(multi);
        else Gdx.input.setInputProcessor(null);  
    }

    public void hideHints(){
        if(hintsCounter>30){
            hintsCounter =30;
        }
    }
    
    @Override
    public void show() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        Gdx.app.debug("Blocktoidz",width+" : "+height);
        
        if(level==null&&!Values.transitioning){
            Gdx.app.log("Blocktoidz","Transition finished starting game");
            
                    
            switch(preference.mode){
                case TimeTrial:
                    level = new TimeTrialLevel(this,0,height/5.95f,width,((float)height)/1.62f);
                    break;
                case Breaker:
                    level = new BreakerLevel(this,0,height/5.95f,width,((float)height)/1.62f);
                    break;
                default:
                    level = new Level(this,0,height/5.95f,width,((float)height)/1.62f);
                    break;  
            }
        }
        LoadUI();
        if(hints&&!Values.transitioning){
            hintsCounter = 20*60;
            level.paused = true;
        }
        
    }

    @Override
    public void render(float delta) {
       
        batch.begin();
            batch.draw(Textures.get("GameBackground"),0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/1.085f);
        batch.end();
        stage.act();
        stage.draw();
        
        if(level!=null){
            batch.begin();
            //adds colour to the tokens and score when theyre going up high  
            int maxScore = 2500*preference.multiplier;
            int maxTokens = 20;
            if(level.addScore>maxScore/2){
                score.getStyle().fontColor = new Color(Cal.map(level.addScore,maxTokens/2,maxTokens,1,.8f),Cal.map(level.addScore,maxScore/2,maxScore,1,.2f),0,1);
            }else{
                score.getStyle().fontColor = new Color(1,1,Cal.map(level.addScore,0,maxScore/2,1,0),1);
            }
            if(level.tokens>maxTokens/2){
               tokenLabel.getStyle().fontColor = new Color(Cal.map(level.tokens,maxTokens/2,maxTokens,1,.8f),Cal.map(level.tokens,maxTokens/2,maxTokens,1,0),0,1);
            }else{
               tokenLabel.getStyle().fontColor = new Color(1,1,Cal.map(level.tokens,0,maxTokens/2,1,0),1);
            }
            level.updateScore();
            score.setText("     Score: "+((long)level.score));
            int minutes = (int)(level.timer/60);
            int seconds = (int)((level.timer)%60);
            int milli = (int)((level.timer*100)%100);
            time.setText ("     Time : "+((minutes>=10)? minutes: ("0"+minutes))+":"+((seconds>=10)? seconds: ("0"+seconds))+":"+((milli>=10)? milli: ("0"+milli)));
            if(!DEBUG_MODE)rowsLabel .setText(""+level.rows);
            else rowsLabel .setText(""+Gdx.graphics.getFramesPerSecond());
            tokenLabel.setText(""+level.tokens);

            if(!level.paused&&level.preview!=null){
                level.preview.drawPreview(batch,
                        (Gdx.graphics.getWidth()/1.29f),
                        (Gdx.graphics.getHeight()/1.24f),
                        ((Gdx.graphics.getWidth())+(Gdx.graphics.getHeight())/2)/13,//width
                        ((Gdx.graphics.getWidth())+(Gdx.graphics.getHeight())/2)/13 //height
                        );
            }
            batch.end();


            //only render if game is not paused
            if(!level.paused)
                level.render();
            if(hintsCounter>0){
                batch.begin();
                //start fade out sequence when counter is less than 30
                if(hintsCounter<30){
                    batch.setColor(1,1,1,Cal.map(hintsCounter,0f,30f,0f,1f));
                }
                batch.draw(Textures.get("Hints"),0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/1.085f);
                batch.setColor(Color.WHITE);
                batch.end();
                hintsCounter--;
                if(hintsCounter==0){
                    level.paused = false;
                    Sounds.play();
                    LoadUI();
                }
            }
        }
        
        //i++;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        if(level!=null)level.paused = true;
        LoadUI();
        hintsCounter = 0;
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        stage.dispose();
        stage =null;
        score = null;
        time = null;
        level = null;
        
        
    }

    @Override
    public void dispose() {

    }
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if(level!=null)level.touch = true;
        return false;
    }
    @Override
    public boolean tap(float x, float y, int count, int button) {
        hideHints();
        //level.tapped();
        return false;
    }
    @Override
    public boolean longPress(float x, float y) {
        return false;
    }
    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if(level!=null&&!level.paused){
            if(velocityY>0&&Math.abs(velocityY)>Math.abs(velocityX)){
                    level.Down();
                    return true;
            }
            if(velocityY<0&&Math.abs(velocityY)>Math.abs(velocityX)){
                    level.Rotate(true);
                    return true;
            }
            if(velocityX<0&&Math.abs(velocityX)>Math.abs(velocityY)){
                    level.Left(true);
                    return true;
            }
            if(velocityX>0&&Math.abs(velocityX)>Math.abs(velocityY)){
                    level.Right(true);
                    return true;
            }
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
        if(keycode == Keys.ESCAPE || keycode == Keys.W){level.paused^=true;LoadUI();return true;}
        switch (keycode) {
            
            case Keys.UP:
            case Keys.SPACE:
                if(level!=null)level.Rotate(true);
                return true;
            
            case Keys.DOWN:
            case Keys.S:
                if(level!=null)level.Down();
                return true;
            
            case Keys.LEFT:
            case Keys.A:
                if(level!=null)level.Left(true);
                return true;
            
            case Keys.RIGHT:
            case Keys.D:
                if(level!=null)level.Right(true);
                return true;
            
            case Keys.E:
                Gdx.app.log("Blocktoidz","Speeding up");
                if(level!=null)level.touch = true;
                return true;
            
            case Keys.PLUS:
                if(level!=null)level.tokens++;
                return true;
            
            case Keys.MINUS:
                if(level!=null)level.tokens--;
                return true;
            default:
                if(hintsCounter!=0){
                    level.paused=false;
                    hintsCounter = 1;
                }
                LoadUI();
                return true;
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.E:
                Gdx.app.log("Blocktoidz","Slowing down");
                if(level!=null)level.touch = false;
                return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("Blocktoidz","Speeding up block");
        
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("Blocktoidz","Slowing down");
        if(level!=null)level.touch = false;
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
