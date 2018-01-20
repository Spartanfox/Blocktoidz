package com.spartanfox.blocktoidz.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.spartanfox.blocktoidz.GameObjects.HighScore;
import com.spartanfox.blocktoidz.IActivityRequestHandler;
import com.spartanfox.blocktoidz.Main;




public class DesktopLauncher implements IActivityRequestHandler{
    private static DesktopLauncher application;
    private static Main main;
    private boolean  videoAd;
    public static void main(String[] arg){
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        int res = 0;
        double scale = 2;
        //*
        switch(res){
            case 0:
                config.width  = (int)(1080/scale);
                config.height = (int)(1920/scale);
                break;
            case 1:
                config.width  = 210;
                config.height = 320;
                break;
            case 2:
                config.width  = 320;
                config.height = 480;
                break;
            case 3:
                config.width = 640;
                config.height = 960;
                break;
            case 4:
                config.width = 480;
                config.height = 800;
                break;
            case 5: 
                config.width = 800/2;
                config.height = 1280/2;
                break;
            case 6:
                break;
        }
        config.resizable = true;

        config.fullscreen = false;
         /**/
        if(application == null){
            application = new DesktopLauncher();
        }
        main = new Main(application);
        new LwjglApplication(main, config);
    }

    @Override
    public void showAds(boolean show) {
        //do nothng since there aint no ads anyways
    }

    @Override
    public void exit() {
        System.exit(0);
    }

    @Override
    public void showVideo() {
        videoAd = false;
        main.levelSelect.unlockNext();
        System.out.println("showing video");
    }
    @Override
    public void loadVideo() {
        System.out.println("loading video");
        videoAd = true;
    }

    @Override
    public void paused() {
        System.out.println("paused video");
    }

    @Override
    public void resume() {
        System.out.println("play video");
    }

    @Override
    public boolean loaded() {
        return videoAd;
    }

    @Override
    public void sendHighScore(String level, HighScore score) {
        System.out.println(score.getName ());
        System.out.println(score.getTime ());
        System.out.println(score.getScore());
    }

    @Override
    public void showPopup() {

    }

    @Override
    public boolean popupLoaded() {
        return true;
    }
}





























/*
cory
*/