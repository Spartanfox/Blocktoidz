package com.spartanfox.blocktoidz;

import com.spartanfox.blocktoidz.GameObjects.ImageFadeTransition;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.viewport.*;
import com.ixeption.libgdx.transitions.FadingGame;
import com.ixeption.libgdx.transitions.ScreenTransition;
import com.ixeption.libgdx.transitions.impl.*;
import com.spartanfox.blocktoidz.Globals.*;
import com.spartanfox.blocktoidz.Globals.LevelPreferences;
import com.spartanfox.blocktoidz.Screens.*;
import java.util.ArrayList;
import com.ixeption.libgdx.transitions.FadingListener;
import com.ixeption.libgdx.transitions.impl.RotatingTransition.TransitionScaling;
import static com.spartanfox.blocktoidz.Globals.Values.DEBUG_MODE;
import java.util.Random;

public class Main extends Game {
    

    public GameScreen game;
    public MainMenuScreen menu;
    public LevelSelectScreen levelSelect;
    public ModeSelectScreen modeSelect;
    public SettingsScreen settings;
    public GameOverScreen gameOver;
    public CreditsScreen credits;
    public HighScoresScreen highScores;
    public TutordialScreen tutordials;
    
    public ArrayList<ScreenTransition> transitions = new ArrayList<ScreenTransition>();
    public FadingGame transition;
    public Camera defaultCam;
    public IActivityRequestHandler handler;
    public Viewport view = new StretchViewport(Values.WIDTH,Values.HEIGHT);
	
    public Main(IActivityRequestHandler handler) {
        this.handler = handler;
    }
    
        @Override
	public void create () {
            Gdx.input.setCatchBackKey(true);
            //made for playtesters so the game isnt pirated
            //System.out.println(TimeUtils.millis());
            //if(TimeUtils.millis()>Values.DEADLINE){System.out.println("");Gdx.app.exit();}
            
            //this sets the default resolution of the game to half of 1080p to allow
            //for the UI to be twice the size and also allow better performance
            defaultCam = new OrthographicCamera(Values.WIDTH,Values.HEIGHT);
            defaultCam.update();
            
            
            //load in all the games assets
            
            LevelPreferences.load();
            Storage         .load();
            Textures        .load();
            
            Styles.create();
            Sounds.create();
            
            Sounds.play();
            
            //prepare the games screens
            game        = new GameScreen(this);
            menu        = new MainMenuScreen(this);
            settings    = new SettingsScreen(this);
            levelSelect = new LevelSelectScreen(this);
            highScores = new HighScoresScreen(this);
            credits = new CreditsScreen(this);
            gameOver = new GameOverScreen(this);
            tutordials = new TutordialScreen(this);
            modeSelect = new ModeSelectScreen(this);
            transition = new FadingGame(new SpriteBatch());
            transition.create();
            

            transitions.add(new ImageFadeTransition("background", Interpolation.sine));
            transitions.add(new SlidingTransition(SlidingTransition.Direction.LEFT, Interpolation.linear, true));
            transitions.add(new SlidingTransition(SlidingTransition.Direction.RIGHT, Interpolation.linear, true));
            
                        transitions.add(new AlphaFadingTransition());
            transitions.add(new SlidingTransition(SlidingTransition.Direction.LEFT, Interpolation.linear, true));
            transitions.add(new SlidingTransition(SlidingTransition.Direction.UP, Interpolation.bounce, false));
            transitions.add(new SlicingTransition(SlicingTransition.Direction.UPDOWN, 128, Interpolation.pow4));
            transitions.add(new SlicingTransition(SlicingTransition.Direction.DOWN, 8, Interpolation.bounce));
            transitions.add(new RotatingTransition(Interpolation.pow2Out, 720, TransitionScaling.IN));
            transitions.add(new RotatingTransition(Interpolation.bounce, 360, TransitionScaling.IN));
            transitions.add(new ColorFadeTransition(Color.BLUE, Interpolation.sine));
            transitions.add(new ColorFadeTransition(Color.WHITE, Interpolation.sine));
            
            transition.setTransition(transitions.get(0),1);
            
            
            transition.addTransitionListener(new FadingListener() {

                @Override
                public void onTransitionStart () {
                    Gdx.app.log("Blocktoidz","Transition start");
                    Values.transitioning = true;
                    
                }

                @Override
                public void onTransitionHalfway() {
                    Gdx.app.log("Blocktoidz","Transition halfway");
                        Textures.reloadAll();
                        Styles.create();
                        Sounds.play();
                }
                
                @Override
                public void onTransitionFinished () {
                    Gdx.app.log("Blocktoidz","Transition finished");
                    Values.transitioning = false;
                    transition.getScreen().show();
                    transition.setTransition(transitions.get(0),1.3f);
                    
                }

		});
            //start the game on the main menu
            transition.setScreen(menu);
            super.setScreen(menu);
	}

	@Override
	public void render() {
           Gdx.gl.glClearColor(0,0,0, 1);
           Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            /*
            implement debug screen
            */
            if(Values.DEBUG_MODE){
                //do shit
            }
           
            transition.render();
                //super.render();
	}
	@Override
        public void setScreen(Screen screen){
            if(DEBUG_MODE){
                Random ran = new Random();
                transition.setTransition(transitions.get(ran.nextInt(transitions.size())),1);
            }
            Values.transitioning = true;
            
            transition.setScreen(screen);
        }
        @Override
	public void resize (int width, int height) {
            transition.resize(width, height);
	}
	@Override
	public void dispose () {
            menu.dispose();
            menu = null;
            settings.dispose();
            settings = null;
            game.dispose();
            game = null;
            
	}
        @Override
	public void pause () {
		transition.pause();
                handler.paused();
	}
        
	@Override
	public void resume () {
		transition.resume();
                handler.resume();
	}
}
