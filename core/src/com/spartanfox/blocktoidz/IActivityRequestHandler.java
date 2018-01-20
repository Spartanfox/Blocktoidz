/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz;

import com.spartanfox.blocktoidz.GameObjects.HighScore;

/**
 *
 * @author Ben
 */
public interface IActivityRequestHandler {
   void showAds(boolean show);
   void sendHighScore(String level, HighScore score);
   void showVideo();
   void loadVideo();
   boolean loaded();
   
   void showPopup();
   boolean popupLoaded();
   
   void paused();
   void resume();
   
   void exit();
}
