package com.spartanfox.blocktoidz;

import android.os.Bundle;
import android.os.Handler;

import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.InterstitialAd;
        
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.spartanfox.blocktoidz.GameObjects.HighScore;
import com.spartanfox.blocktoidz.Globals.Values;
import com.spartanfox.blocktoidz.Main;
import com.spartanfox.blocktoidz.IActivityRequestHandler;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler, RewardedVideoAdListener{

    private static final String AD_UNIT_ID = "ca-app-pub-8288577284855303/3669739828";
    private static final String APP_ID = "ca-app-pub-8288577284855303~2036294885";
    private static final String POPUP_ID = "ca-app-pub-8288577284855303/6080484971";

    protected  AdView bannerAd;
    protected  RewardedVideoAd videoAd;
    protected  InterstitialAd popupAd;
    private static boolean popupLoaded = false;
    private static boolean loaded = false;
    private FirebaseAnalytics mFirebaseAnalytics;
    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;   
    private Main main;
    protected Handler handler = new Handler()
    {
    @Override
    public void handleMessage(Message msg) {
        switch(msg.what) {
            case SHOW_ADS:
            {
                bannerAd.setVisibility(View.VISIBLE);
                break;
            }
            case HIDE_ADS:
            {
                bannerAd.setVisibility(View.GONE);
                break;
            }
        }
    }
    };


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //initialize(new Main(), config);
        RelativeLayout layout = new RelativeLayout(this);
        main = new Main(this);
        View gameView = initializeForView(main, config);

        bannerAd = new AdView(this);
        bannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Gdx.app.error("Blocktoidz","ad failed to load: "+errorCode);
            }
            @Override
            public void onAdLoaded() {
                    super.onAdLoaded();
                    Gdx.app.debug("Blocktoidz","Ad loaded wee");
            }
        });

        bannerAd.setAdSize(AdSize.SMART_BANNER);
        bannerAd.setAdUnitId("ca-app-pub-8288577284855303/3388372178");

        AdRequest.Builder builder = new AdRequest.Builder();
        //builder.addTestDevice("65EE701CDA518D3B5407DA80EEAD766F");
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        MobileAds.initialize(this,APP_ID);

    // Use an activity context to get the rewarded video instance.
        videoAd = MobileAds.getRewardedVideoAdInstance(this);
        videoAd.setRewardedVideoAdListener(this);
        loadVideo();
        
        popupAd = new InterstitialAd(this);
        popupAd.setAdUnitId(POPUP_ID);
        popupAd.loadAd(new AdRequest.Builder().build());
        popupAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                popupLoaded = true;
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Gdx.app.error("Blocktoidz","Reward video failed to load: "+errorCode);
                popupLoaded = false;
            }
            @Override
            public void onAdOpened() {
                popupLoaded = false;
            }
            @Override
            public void onAdClosed() {
                popupAd.loadAd(new AdRequest.Builder().build());
            }
        });
        
        layout.addView(gameView);
        layout.addView(bannerAd,adParams);

        bannerAd.loadAd(builder.build());
        setContentView(layout);
    }

    @Override
    public void showAds(boolean show) {
        handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
    }
    @Override
    public void showVideo(){
        //loaded = false;
        runOnUiThread(new Runnable() {
        @Override public void run() {
 /*      if (!videoAd.isLoaded()) {
            videoAd.loadAd(AD_UNIT_ID, new AdRequest.Builder().build());
}*/
            if (videoAd.isLoaded()) {
                videoAd.show();
               // loaded = false;
            }
        }
    });
        

    }
    @Override
    public void exit(){
        //android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
    }

    @Override
    public void onRewardedVideoAdClosed() {
        loaded = false;
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Gdx.app.error("Blocktoidz","Reqard video failed to load: "+errorCode);
        loaded = false;
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        loaded = true;
    }

    @Override
    public void onRewardedVideoAdOpened() {
        loaded = false;
    }

    @Override
    public void onRewarded(RewardItem reward) {
        main.levelSelect.unlockNext();
    }

    @Override
    public void onRewardedVideoStarted() {
        loaded = false;
    }
    @Override
    public void loadVideo() {
      //  loaded = true;
      runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //loaded = false;
                if (!videoAd.isLoaded()) {
                    videoAd.loadAd(AD_UNIT_ID, new AdRequest.Builder().build());
                  //  loaded = true;
                }//else{loaded = true;}
            }
        });
    }
    
    @Override
    public void sendHighScore(String level, HighScore score) {
        Bundle params = new Bundle();
        params.putString("Name",score.getName());
        params.putString("Time",score.getTime());
        params.putLong("Score",score.getScore());
        mFirebaseAnalytics.logEvent(level.toLowerCase().replaceAll(" ","_"), params);
    }
    
    
    @Override
    public boolean loaded(){
        return loaded;
    }
    @Override
    public void paused() {
    //    videoAd.pause(this);
    }

    @Override
    public void resume() {
    //    videoAd.resume(this);
    }

    @Override
    public void showPopup() {
        runOnUiThread(new Runnable() {
            @Override public void run() {
                if(popupLoaded)
                    popupAd.show();
            }
        });
    }

    @Override
    public boolean popupLoaded() {
        return popupLoaded;
    }

}
