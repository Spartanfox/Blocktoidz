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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.spartanfox.blocktoidz.GameObjects.LevelPreference;
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
public class ModeSelectScreen implements Screen, GestureDetector.GestureListener , InputProcessor{
       Main main;
    SpriteBatch batch;
    Stage stage;
    public ModeSelectScreen(Main main){
        this.main =  main;
        batch = new SpriteBatch();
    }
    public void LoadUI(){
        Gdx.app.log("Blocktoidz","Loading mode select screen");
        if(stage!=null)stage.dispose();
        stage = new Stage(main.view);
        
        Table table = new Table(Styles.getSkin());
        table.setFillParent(true);
        table.setBackground(Textures.getDrawable("Background"));
        table.top();
        table.add(Styles.newImageButton("Title", "Title",null)).pad(50).padTop(AD_PADDING*4);
        table.row();
        table.add(Styles.newLabel("Select Game Mode")).width(800).height(200);
        table.row().fill();
        table.add(Styles.newButton("Default",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        main.levelSelect.setModes(LevelPreference.Mode.Normal,LevelPreference.Mode.Hard,LevelPreference.Mode.Insane);
                        main.setScreen(main.levelSelect);
                    }
                }
                )).pad(20).width(600).height(200);
        table.row().fill();
        table.add(Styles.newButton("Time trial",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        main.levelSelect.setModes(LevelPreference.Mode.TimeTrial);
                        main.setScreen(main.levelSelect);
                    }
                }
                )).pad(20).width(600).height(200);
        table.row().fill();
        table.add(Styles.newButton("Breaker\nLevel:"+Storage.getProgress("Breaker"),
            new ClickListener(){
                @Override
                public void clicked(InputEvent event,float x, float y){
                    Sounds.play("blop");
                    main.levelSelect.setModes(LevelPreference.Mode.Breaker);
                    main.setScreen(main.levelSelect);
                }
            }
        )).pad(20).width(600).height(200);
        table.row().fill();
        table.add(Styles.newButton("Back",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("fart");
                        main.setScreen(main.menu);
                    }
                }
        )).pad(20).width(600).height(200);
        
        
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
       // batch.begin();
        //batch.draw(Textures.get("Background"),0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        //batch.end();
        stage.act();
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
