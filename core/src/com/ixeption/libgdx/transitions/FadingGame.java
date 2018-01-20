/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.ixeption.libgdx.transitions;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;

/** An {@link Game} that delegates to a {@link Screen}. Allows to apply different transitions to screens. 
 *
 * Screens are not disposed automatically. You must handle whether you want to keep screens around or dispose of them when another
 * screen is set.
 * @author iXeption */
public class FadingGame extends Game {

    final protected Batch batch;
    private final Array<FadingListener> listeners;
    protected FrameBuffer currentScreenFBO;
    protected FrameBuffer nextScreenFBO;
    
    protected Screen nextScreen;
    private float transitionDuration;
    private float currentTransitionTime;
    private boolean transitionRunning;
    private boolean transitionHalfway;
    private ScreenTransition screenTransition;

    public FadingGame(Batch batch) {
        this.batch = batch;
        this.listeners = new Array<FadingListener>();
    }

    @Override
    public void create() {
        this.currentScreenFBO = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        this.nextScreenFBO = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    @Override
    public void dispose() {
        if (screen != null) screen.hide();
        if (nextScreen != null) nextScreen.hide();

        this.currentScreenFBO.dispose();
        this.nextScreenFBO.dispose();

    }

    @Override
    public void pause() {
        if (screen != null) screen.pause();
        if (nextScreen != null) nextScreen.pause();
    }

    @Override
    public void resume() {
        if (screen != null) screen.resume();
        if (nextScreen != null) nextScreen.resume();
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        if (nextScreen == null) {
            // no other screen
            screen.render(delta);
        } else {
            if (transitionRunning && currentTransitionTime >= transitionDuration) {
                // is active and time limit reached
                this.screen.hide();
                this.screen = this.nextScreen;
                this.screen.resume();
                transitionRunning = false;
                //transitionHalfway = false;
                this.nextScreen = null;
                notifyFinished();
                this.screen.render(delta);

            } else {
                
                
                
                
                
                float percent = currentTransitionTime / transitionDuration;
                // transition is active
                if (screenTransition != null) {
                    
                    currentScreenFBO.begin();
                    this.screen.render(delta);
                    currentScreenFBO.end();
                    
                    nextScreenFBO.begin();
                    this.nextScreen.render(delta);
                    nextScreenFBO.end();
                    

                    
                    if(percent >0.5f&&transitionHalfway!=true){
                        notifyHalfway();
                        transitionHalfway = true;
                        nextScreenFBO.begin();
                        this.nextScreen.show();
                        this.nextScreen.render(delta);
                        nextScreenFBO.end();
                    }
                    screenTransition.render(batch, currentScreenFBO.getColorBufferTexture(), nextScreenFBO.getColorBufferTexture(),
                            percent);
                    currentTransitionTime += delta;

                }

            }

        }

    }

    @Override
    public void resize(int width, int height) {
        if (screen != null) screen.resize(width, height);
        if (nextScreen != null) nextScreen.resize(width, height);

        this.currentScreenFBO.dispose();
        this.nextScreenFBO.dispose();

        this.currentScreenFBO = new FrameBuffer(Format.RGBA8888, width, height, false);
        this.nextScreenFBO = new FrameBuffer(Format.RGBA8888, width, height, false);

    }

    /** Sets the {@link ScreenTransition} which is used. May be {@code null} to use instant switching.
     * @param screenTransition may be {@code null}
     * @param duration the transition duration in seconds
     * @return {@code true} if successful false if transition is running */
    public boolean setTransition(ScreenTransition screenTransition, float duration) {
        if (transitionRunning) return false;
        this.screenTransition = screenTransition;
        this.transitionDuration = duration;
        return true;

    }

    /** @return the currently active {@link Screen}. */
    @Override
    public Screen getScreen() {
        return screen;
    }

    /** Sets the current screen. {@link Screen#hide()} is called on any old screen, and {@link Screen#show()} is called on the new
     * screen, if any.
     * @param screen may be {@code null} */
    @Override
    public void setScreen(Screen screen) {
        
        screen.show();
        transitionHalfway = false;
        if (transitionRunning/*&&(currentTransitionTime / transitionDuration<=0.5f)*/){
                Gdx.app.log(FadingGame.class.getSimpleName(), "Changed Screen while transition in progress");
                
        }else{
        if (this.screen == null) {
            this.screen = screen;
        }else {
            if(screen.getClass() == this.screen.getClass()){
                Gdx.app.log(FadingGame.class.getSimpleName(), "Transitioning to same screen");
                this.transitionHalfway = true;
            }
            if (screenTransition == null) {
                this.screen.hide();
                this.screen = screen;
            } else {
                //this.screen.hide();
                this.nextScreen = screen;
                
                //this.screen.pause();
                currentTransitionTime = 0;
                transitionRunning = true;
                notifyStarted();
            }

        }
        }
        //this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    /** @return the next {@link Screen}. */
    public Screen getNextScreen() {
        return nextScreen;
    }

    /** @param listener to get transition events */
    public void addTransitionListener(FadingListener listener) {
        listeners.add(listener);
        return;
    }

    /** @param listener to remove
     * @return {@code true} if successful */
    public boolean removeTransitionListener(FadingListener listener) {
        return listeners.removeValue(listener, true);
    }

    /** Clear listeners */
    public void clearTransitionListeners() {
        listeners.clear();
    }

    private void notifyFinished() {
        for (FadingListener transitionListener : listeners) {
            transitionListener.onTransitionFinished();
        }
    }

    private void notifyStarted() {
        for (FadingListener transitionListener : listeners) {
            transitionListener.onTransitionStart();
        }
    }
    private void notifyHalfway(){
        for(FadingListener transitionListener : listeners){
            transitionListener.onTransitionHalfway();
        }
        
    }
}
