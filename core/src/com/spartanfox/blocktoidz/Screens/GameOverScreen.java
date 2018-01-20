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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.spartanfox.blocktoidz.GameObjects.HighScore;
import com.spartanfox.blocktoidz.Globals.LevelPreferences;
import static com.spartanfox.blocktoidz.Globals.LevelPreferences.preference;
import static com.spartanfox.blocktoidz.Globals.LevelPreferences.previousLevel;
import static com.spartanfox.blocktoidz.Globals.LevelPreferences.previousScore;
import com.spartanfox.blocktoidz.Globals.Sounds;
import com.spartanfox.blocktoidz.Globals.Storage;
import com.spartanfox.blocktoidz.Globals.Styles;
import com.spartanfox.blocktoidz.Globals.Textures;
import com.spartanfox.blocktoidz.Globals.Values;
import static com.spartanfox.blocktoidz.Globals.Values.AD_PADDING;
import com.spartanfox.blocktoidz.Main;
import static java.lang.Math.random;

/**
 *
 * @author Ben
 */
public class GameOverScreen implements Screen, GestureListener , InputProcessor{
    Main main;
    SpriteBatch batch;
    Stage stage;
    TextArea name;
    public GameOverScreen(Main main){
        this.main =  main;
        batch = new SpriteBatch();
    }
    public void LoadUI(){
        Gdx.app.log("Blocktoidz","Loading game over screen UI");
        if(!Values.transitioning)main.handler.showPopup();
        if(stage != null)stage.dispose();
        stage = new Stage(main.view);
        
        Table table = new Table(Styles.getSkin());
        table.setFillParent(true);
        table.setBackground(Textures.getDrawable("Background"));
        table.top();
        table.add(Styles.newImageButton("Title", "Title",null)).pad(50).padTop(AD_PADDING*4);
        table.row();
        table.add(Styles.newLabel((Storage.isHighScore(previousLevel,previousScore.getScore()))? "New High Score!" :Values.transferMessage)).width(800).height(200);
        table.row();
        
        name =Styles.newTextBox(previousScore.getName(),1.8f,
            new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    Gdx.input.setOnscreenKeyboardVisible(false);
                    Gdx.input.setOnscreenKeyboardVisible(true);
                }
            }
        ); 
        table.add(name).width(850).height(150).padRight(20);
        table.row();
        table.add(Styles.newString("Your Score: "+previousScore.getScore(),1.5f,Align.center)).width(800).height(150);
        table.row();
        table.add(Styles.newString("Your Time : "+previousScore.getTime (),1.5f,Align.center)).width(800).height(150);
        table.row();
        table.add(Styles.newButton("Save High Score",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        if(previousScore!=null){
                            if(name.getText().equals("XP2UBWZ2M")){
                                Values.transferMessage = "Youre a tit";
                                main.setScreen(main.gameOver);
                            }
                            else if(name.getText().replaceAll(" ","").isEmpty()){
                                Storage.SaveHighScore(previousLevel,previousScore);
                                previousScore = null;
                                main.setScreen(main.highScores);
                            }
                            else{
                                Storage.SaveHighScore(previousLevel,previousScore.setName(name.getText()));
                                if(Storage.isHighScore(previousLevel,previousScore.getScore()))main.handler.sendHighScore(previousLevel,previousScore);
                                previousScore = null;
                                main.setScreen(main.highScores);
                            }   
                        }
                    }
                }
                )).pad(30).width(600).height(250);
        table.row();
        table.add(Styles.newButton("Main menu",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("fart");
                        main.setScreen(main.menu);
                    }
                }
                )).pad(30).padBottom(200).width(600).height(250);
        /*
        table.setTransform(true);
        table.setScale(2);
        table.setOrigin((stage.getWidth()/2)+(table.getWidth()/2),(stage.getHeight()/2)+(table.getHeight()/2));
        */
        stage.addActor(table);

        stage.setDebugAll(Values.DEBUG_MODE);
        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(stage);
        multi.addProcessor(new GestureDetector(this));
        multi.addProcessor(this);
        if(!Values.transitioning)Gdx.input.setInputProcessor(multi);
        else Gdx.input.setInputProcessor(null);
    }
    @Override
    public void show() {
        LoadUI();
    }

    @Override
    public void render(float delta) {
        //batch.begin();
        //batch.draw(Textures.get("Background"),0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        //batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //LoadUI();    
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
//incase the keyboard dissapearing becomes a common thing        
    Gdx.input.setOnscreenKeyboardVisible(false);
    //Gdx.input.setOnscreenKeyboardVisible(true);
    stage.unfocusAll();
    return false;
    }

    @Override
    public boolean longPress(float x, float y) {return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {return false;
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
