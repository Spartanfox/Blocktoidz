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
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
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
public class SettingsScreen implements Screen{
    Stage stage;
    SpriteBatch batch;
    Main main;
    
    Slider   mainSlider  ;
    Slider   musicSlider ;
    Slider   soundSlider ;
    CheckBox hintsCheck  ;
    CheckBox controlsCheck;
    CheckBox previewCheck;
    boolean clearHighscores = false;
    public SettingsScreen(Main main){
        this.main = main;
        clearHighscores = false;
    }
    
    
    public void loadUI(){
        Gdx.app.log("Blocktoidz","Loading Settings screen");
        if(stage!=null)stage.dispose();
       Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        stage = new Stage(main.view);
        Table table = new Table();
        table.setTransform(true);
        
        hintsCheck = Styles.newCheck("Show hints",new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                
                Values.hints = hintsCheck.isChecked();
                if(Values.hints)Sounds.play("blop");else Sounds.play("fart");
            }
        });
        previewCheck = Styles.newCheck("Show preview",new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Values.preview = previewCheck.isChecked();
                if(Values.preview)Sounds.play("blop");else Sounds.play("fart");
            }
        });
        controlsCheck = Styles.newCheck("Complex Controls",new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Values.control = controlsCheck.isChecked() ? Values.Controls.Complex : Values.Controls.Simple;
                if(Values.preview)Sounds.play("blop");else Sounds.play("fart");
            }
        });
        mainSlider = Styles.newSlider(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            Sounds.masterVol = mainSlider.getValue();	
                            Sounds.setVolume     ((float)(musicSlider.getValue()));
                            Sounds.setSoundVolume((float)(soundSlider.getValue()));   
			}
                        @Override
                        public void touchDragged(InputEvent event, float x, float y, int pointer){
                            Sounds.masterVol = mainSlider.getValue();	
                            Sounds.setVolume     ((float)(musicSlider.getValue()));
                            Sounds.setSoundVolume((float)(soundSlider.getValue()));
                        }
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
                        }
        });
        musicSlider = Styles.newSlider(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            Sounds.setVolume((float)((musicSlider.getValue())));
                            
                                
			}
                        @Override
                        public void touchDragged(InputEvent event, float x, float y, int pointer){
                            Sounds.setVolume((float)((musicSlider.getValue())));
                        }
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                            return true;
                        }
        });
        soundSlider = Styles.newSlider(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Sounds.setSoundVolume((float)((soundSlider.getValue())));
                                Sounds.play("blop");
			}
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                                
				return true;
                        }
        });
        mainSlider.setValue(Sounds.masterVol);
        musicSlider.setValue(Sounds.musicVol);
        soundSlider.setValue(Sounds.soundsVol);
        hintsCheck.setChecked(Values.hints);
        previewCheck.setChecked(Values.preview);
        controlsCheck.setChecked(Values.control == Values.Controls.Complex);
        
        table.setFillParent(true);
        //table.setBackground(Textures.getDrawable("Background"));
        table.add(Styles.newImageButton("Title", "Title",null)).width(530).top().colspan(2).fillY().height(140).padTop(AD_PADDING);
        table.row();
       // table.add().padBottom(10);
        table.row();
        table.add(Styles.newString("Settings")).height(80).width(200).colspan(2).center();
        table.row();
        table.add(new Label("Master",Styles.getSkin()       )).colspan(2).center();
        table.row();
        table.add(mainSlider).colspan(2).width(500);
        table.row();
        table.add(new Label("Music",Styles.getSkin()        )).colspan(2).center();
        table.row();
        table.add(musicSlider).colspan(2).width(500);
        table.row();
        table.add(new Label("Sound effects",Styles.getSkin())).colspan(2).center();
        table.row();
        table.add(soundSlider).colspan(2).width(500);
        table.row();
        table.add(hintsCheck).pad(5).align(Align.right);
        table.add(previewCheck).pad(5).align(Align.left);
        table.row();
        table.add(controlsCheck).pad(5).colspan(2);
        table.row();
        table.add(Styles.newButton("Credits","small",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        main.setScreen(main.credits);
                    }
                }
                )).pad(5).padTop(20).colspan(2).center();
        table.row();
        
         table.add(Styles.newButton("Dev mode","small",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        if(!Values.DEBUG_MODE)Sounds.play("blop");
                        else Sounds.play("fart");
                        Values.DEBUG_MODE^=true;
                        loadUI();
                    }
                }
                )).pad(5).padBottom(5).colspan(2).center();
        table.row();
        if(!clearHighscores){
        table.add(Styles.newButton("Reset","small",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        clearHighscores = true;
                        loadUI();
                    }
                }
                )).pad(5).colspan(2).center();
        }else{
            table.add(Styles.newString("Reset highscores?",0.5f)).colspan(2);
            table.row();
            table.add(Styles.newButton("Yes","small",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        Storage.reset();
                        clearHighscores = false;
                        main.setScreen(main.menu);
                    }
                }
                )).padRight(5).height(40).right();
            table.add(Styles.newButton("No","small",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("blop");
                        clearHighscores = false;
                        loadUI();
                    }
                }
                )).padLeft(5).height(40).left();
        }
        table.row();
        table.add(Styles.newButton("Back",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event,float x, float y){
                        Sounds.play("fart");
                        Sounds.save();
                        Storage.SaveHint(Values.hints);
                        Storage.SavePreview(Values.preview);
                        Storage.saveControls(Values.control == Values.Controls.Complex);
                        main.setScreen(main.menu);
                    }
                }
                )).pad(10).colspan(2).center();
        table.setTransform(true);
        table.setScale(2);
        table.setOrigin((stage.getWidth()/2)+(table.getWidth()/2),(stage.getHeight()/2)+(table.getHeight()/2));
        
        stage.addActor(table);
        stage.setDebugAll(Values.DEBUG_MODE);
        if(!Values.transitioning)Gdx.input.setInputProcessor(stage);
        else Gdx.input.setInputProcessor(null);
 
    }
    @Override
    public void show() {
       loadUI(); 
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(Textures.get("Background"),0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.end();
        
        stage.act();
        stage.draw();

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
        stage.dispose();
        stage = null;
    }

    @Override
    public void dispose() {
        
    }
    
}