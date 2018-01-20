/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.Levels;

import com.spartanfox.blocktoidz.Globals.LevelPreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spartanfox.blocktoidz.GameObjects.BigBlock;
import com.spartanfox.blocktoidz.GameObjects.BiggerBlock;
import com.spartanfox.blocktoidz.GameObjects.Block;
import com.spartanfox.blocktoidz.GameObjects.HighScore;
import com.spartanfox.blocktoidz.Globals.Sounds;
import com.spartanfox.blocktoidz.Globals.Styles;
import com.spartanfox.blocktoidz.Globals.Textures;
import com.spartanfox.blocktoidz.Screens.GameScreen;
import static com.spartanfox.blocktoidz.Globals.LevelPreferences.preference;
import com.spartanfox.blocktoidz.Globals.Cal;
import com.spartanfox.blocktoidz.Globals.Values;
import com.spartanfox.blocktoidz.Globals.Values.*;
import static com.spartanfox.blocktoidz.Globals.Values.control;
import java.util.Random;

/**
 *
 * @author Ben
 */
public class Level {
    SpriteBatch batch;
    //the entire games board is stored in a byte two dimentional array 
    // 4 bits dedicated to the blocks shape then 4 bits to the colour
    //however 1 bit isnt used anymore so colour is 3bits currently
    public byte[][] blockArray;
    
    //contains the data for the next block
    public Block preview;
    
    Block focused;
    
    public float speed,cooldown;
    float x,y,width,height;
    //probably can find a way to remove this at some point
    BitmapFont font; 

    public double score;
    public long addScore;
    public float timer;
    //default settings that probably do nothing now
    public float topSpeed = 60;
    
    public int blocksX =  6;
    public int blocksY = 7;
    
    public float blocksWidth;
    public float blocksHeight;
    public float combo;
    public int previousRow;
    public int rows;
    public int tokens;
    public int pretokens;
    public int multiply;
    //Load the textures directly from the textures class to avoid having to find them constantly
    public Texture block;
    public Texture blockTop;
    public Texture blockLeft;
    public Texture blockRight;
    public Texture blockBottom;
    
    //stores the blocks colours
    public Color[] colors = new Color[8];
    
    //makew this equal to how many frames you want to delay the row delete
    public int scoreCooldown;
    public boolean deleted = false;
    
    public boolean touch = false;
    
    GameScreen gameScreen;
    
    public boolean paused = false;
    public boolean gameOver = false;
    public int placed;
    public Random r = new Random();
//creates the level that youre about to play by setting urythin up 
    public Level(GameScreen main,float x, float y, float width, float height){
        
        this.gameScreen = main;
        placed = 0;
        blocksX = preference.gridWidth;
        blocksY = preference.gridHeight;
        blockArray = new byte[blocksX][blocksY];
        
        block = Textures.get("Block");
        blockTop = Textures.get("Block1");
        blockRight = Textures.get("Block2");
        blockBottom = Textures.get("Block3");
        blockLeft = Textures.get("Block4");
        generateColors();
        if(preference.level!=null){
            for (int Bx = 0; Bx < blockArray.length; Bx++) {
                for (int By = 0; By < blockArray[0].length; By++) {
                    blockArray[Bx][(blocksY-1)-By] = (byte) preference.level[By][Bx];
                }
            }
        }
        
        // Random starting blocks
        if(preference.random&&preference.level==null){
            for (int Bx = 0; Bx < blockArray.length; Bx++) {
                for (int By = 0; By < blockArray[0].length/2; By++) {
                    //blockArray[Bx][By] = Block.randomBlock(r);
                }
            }
        }
        
        topSpeed = preference.startingSpeed;
        
        speed = topSpeed;
        batch = new SpriteBatch();
        font = Styles.getFont("font-big");
        font.getData().setScale(2);
        this.timer=0;
        this.x = (int)x;
        this.y = (int)y;
        this.width  = width ;
        this.height = height;
        this.blocksWidth = width/blocksX  ;
        this.blocksHeight = height/blocksY;
    }
    public void create() {

    }
    //basic controls for the movement of the block no clue why theres an up boolean
    //but i probably had a reason at some point so fuck it
    public void Left(boolean up){
        if(focused.canMove(blockArray,-1,0)&&!paused)focused.move(-1,0);
    }
    public void Right(boolean up){
        if(focused.canMove(blockArray,1,0)&&!paused)focused.move(1,0);
    }
    public void Rotate(boolean up){
        if(!paused){
                focused.move(0,0);
                focused.rotate();
        }
    }
    public void Down(){
        //has a cooldown to make sure you dont accidently make a block shoot down when its first created
        if(focused.getRow() < blocksY-1||cooldown<=(topSpeed/2)&&focused.type.equals("small")){      
            speed = 1;
            focused.Drop();
            cooldown = 0;
        }else if(focused.type.equals("big")){
            speed = 1;
            focused.Drop();
            cooldown = 0;
        }
    }
    public void tapped(){
        if(focused instanceof Block)if(focused.getHere(blockArray)!=0)placeBlock();
    }
    public void render() {
       //since the whole games renered to the screen clearing it is probably
       //pointless
       
        Gdx.gl20.glViewport((int)x,(int)y,(int)width,(int)height+(int)blocksHeight);
        Gdx.gl20.glClearColor(0,0, 0,1f);
        
        if(!paused&&!gameOver)update();
        batch.begin();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height+(int)blocksHeight);
        
        if(deleted == false)
        {   
            previousRow=0;
            combo = 0;
        }
        if(scoreCooldown<=0)deleted = false;
        scoreCooldown--;
        for(int y=0; y < blocksY; y++){
            byte rowCount = 0;
            for(int x = 0; x < blocksX;x++){
                char shape = (char) blockArray[x][y];
                if(blockArray[x][y]!=0){
                    batch.setColor(colors[(shape&0xff)>>4]);
                    //if the block is whole then just render the whole block instead of 4 individual peices
                    if((shape&0xf)==0xf){
                        rowCount+=4;
                        batch.draw(block,x*blocksWidth,y*blocksHeight,blocksWidth+1,blocksHeight+1);
                    }
                    else{
                        if((shape&0x1)!=0){
                            rowCount++;
                            batch.draw(blockTop,x*blocksWidth,y*blocksHeight,blocksWidth+1,blocksHeight+1);
                        }
                        if((shape&0x2)!=0){
                            rowCount++;
                            batch.draw(blockRight,x*blocksWidth,y*blocksHeight,blocksWidth+1,blocksHeight+1);
                        }
                        if((shape&0x4)!=0){
                            rowCount++;
                            batch.draw(blockBottom,x*blocksWidth,y*blocksHeight,blocksWidth+1,blocksHeight+1);
                        }
                        if((shape&0x8)!=0){
                            rowCount++;
                            batch.draw(blockLeft,x*blocksWidth,y*blocksHeight,blocksWidth+1,blocksHeight+1);
                        }
                    }
                }

            }
            if(y>=previousRow&&scoreCooldown<=0&&rowCount!=0&&!gameOver){
                if(combo<=preference.comboLimit&&rowCount >= (blocksX*4)-(preference.gap+((combo>0?(preference.comboGive+combo)-1:0)))){

                    batch.setColor(Color.WHITE);
                    scoreCooldown=5;
                    previousRow = y;
                    Sounds.play("fart",(((float)combo)/10)+1f);
                                        
                    deleteRow(blockArray,y);
                    
                    topSpeed-=(tokens/10)+0.5;
                    if(topSpeed<preference.maxSpeed)topSpeed = preference.maxSpeed;

                    //the points you get when you break a row
                    giveScore((long) ((rowCount*11)*(combo+1)*3));

                    if(combo>=1)tokens+=2;

                    if(multiply<=1)multiply=2;

                    if(combo==0){
                        if(rowCount >= (blocksX*4)){
                            combo++;
                        }
                    }else{combo++;}
                    
                    deleted=true;
                    break;
                }else{
                    combo=0;
                }
            }
        }
        
        focused.draw(batch,Gdx.graphics.getDeltaTime());
        batch.end();
        Gdx.gl20.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
                
        
        
    }
    public  void drawLevel(byte[][] blockArray){
                Gdx.gl20.glViewport((int)x,(int)y,(int)width,(int)height+(int)blocksHeight);
        batch.begin();
        for(int y=0; y < blocksY; y++){
            for(int x = 0; x < blocksX;x++){
                char shape = (char) blockArray[x][y];
                if(blockArray[x][y]!=0){
                    batch.setColor(colors[(shape&0xff)>>4]);
                    //if the block is whole then just render the whole block instead of 4 individual peices
                    if((shape&0xf)==0xf)
                        batch.draw(block,x*blocksWidth,y*blocksHeight,blocksWidth+1,blocksHeight+1);
                    
                    else{
                        if((shape&0x1)!=0)batch.draw(blockTop,x*blocksWidth,y*blocksHeight,blocksWidth+1,blocksHeight+1);
                        if((shape&0x2)!=0)batch.draw(blockRight,x*blocksWidth,y*blocksHeight,blocksWidth+1,blocksHeight+1);
                        if((shape&0x4)!=0)batch.draw(blockBottom,x*blocksWidth,y*blocksHeight,blocksWidth+1,blocksHeight+1);
                        if((shape&0x8)!=0)batch.draw(blockLeft,x*blocksWidth,y*blocksHeight,blocksWidth+1,blocksHeight+1);
                    }
                }
            }
        }
        batch.end();
                Gdx.gl20.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

    }
    public void updateScore(){
    if(addScore>=100*preference.multiplier){score+=10*preference.multiplier;addScore-=10*preference.multiplier;} else 
        if(addScore>0){
            score+=1*preference.multiplier;addScore-=1*preference.multiplier;
        }
    }
    public void giveScore(long points){
        if(pretokens!=tokens){
                 if(tokens>15) multiply = 0;
            else if(tokens>10) multiply = 1;
            else if(tokens>5 ) multiply = 2;
            else               multiply = 3;
        }
        else if(multiply>0){
            multiply--;
        }
        else if(multiply==0&&tokens!=0){
            tokens--;
                 if(tokens>15) multiply = 0;
            else if(tokens>10) multiply = 1;
            else if(tokens>5 ) multiply = 1; 
            else               multiply = 2;
        }
        points*=(tokens/4)+1;
        addScore+=points*preference.multiplier;
        pretokens = tokens;
    }
    
    public void drawBlock(SpriteBatch batch, byte shape,int x,int y,int size){
        batch.setColor(colors[(shape&0xff)>>4]);
        //if the block is whole then just render the whole block instead of 4 individual peices
        if((shape&0xf)==0xf)batch.draw(block,x,y,size,size);
        else{
            if((shape&0x1)!=0)batch.draw(blockTop,x,y,size,size);
            if((shape&0x2)!=0)batch.draw(blockRight,x,y,size,size);
            if((shape&0x4)!=0)batch.draw(blockBottom,x,y,size,size);
            if((shape&0x8)!=0)batch.draw(blockLeft,x,y,size,size);
            
        }
        batch.setColor(Color.WHITE);
    }
    
    public void generateColors(){
        colors = new Color[16];
        Gdx.app.log("Blocktoidz",preference.theme);
        for( int i = 0; i < colors.length; i++) {
            if(preference.theme.equals("metal/")){
                colors[i] = new Color(
                     (i&0x1)!=0 ? 1:.5f,
                     (i&0x2)!=0 ? 1:.5f,
                     (i&0x4)!=0 ? 1:.5f,
                     1f
                 );
            }
            else if(preference.theme.equals("paper/")){
                colors[i] = new Color(
                     (i&0x1)!=0 ? .5f:0, 
                     (i&0x2)!=0 ? .5f:0,
                     (i&0x4)!=0 ? .5f:0,
                     1f
                 );
            }
            else if(preference.theme.equals("wood/")){
                colors[i] = new Color(
                     (i&0x1)!=0 ? .8f:.4f, 
                     (i&0x2)!=0 ? .8f:.4f,
                     (i&0x4)!=0 ? .8f:.4f,
                     1f
                 );
            }
            else{
                colors[i] = new Color(
                    (i&0x1)!=0 ? .9f:.3f,// - (shape&0x80)!=0 ? 0f:.5f, 
                    (i&0x2)!=0 ? .9f:.3f, //- (shape&0x80)!=0 ? 0f:1,
                    (i&0x4)!=0 ? .9f:.3f,//- (shape&0x80)!=0 ? 0f:.5f,
                    1f
                );
            }
        }
    }
    
    public void updateTimer(){
        timer+=Gdx.graphics.getDeltaTime();
    }
    public void update(){
       
        //add 1 to the timer per second
        if(!paused&&!gameOver)updateTimer();
       
        
       //get called once when the game begins
       if(focused == null){
           focused = new Block(this,1,blocksY-1,width/blocksX,height/blocksY);
           preview = new Block(this,1,blocksY-1,width/blocksX,height/blocksY);
           cooldown = 60;
       }
       //if the cooldown fo the block is smaller than 0 itll initiate the checks for if the block shouls be placed or move
       //when i improve the physics this will also contain functionality for falling down slopes
       else if(cooldown <= 0){
           cooldown = speed;
           if(control == Controls.Simple){
            
            if(focused.getHere(blockArray)!=0&&(cooldown==1||!touch)&&focused.type.equals("small")){
               placeBlock();
            }
            else if(!focused.canMove(blockArray,0,-1)){
                    placeBlock();
            }
            else focused.move(0,-1);
           }else{
            if(focused.getHere(blockArray)!=0&&cooldown==1&&focused.type.equals("small")){
               placeBlock();
            }
           else if(!focused.canMove(blockArray,0,-1)){
                    placeBlock();
            }
            else focused.move(0,-1);
           }
       }
       float slowdown = 1;
       if(focused.getClass() == BigBlock.class||focused.getClass() ==BiggerBlock.class)slowdown=2;
       
       if(!focused.canMove(blockArray,0,-1)){
           cooldown-=(Cal.timeScale((0.5f)/slowdown));
       }else{
            if(touch){
                cooldown-=Cal.timeScale(3/slowdown);
            }else{
                cooldown-=Cal.timeScale(1/slowdown);
            }
       }
       
    }
   public void placeBlock(){
       
        if(focused.collides(blockArray)){
            if(!gameOver){
                Sounds.play("fart");
                gameOver();
            }
        }else{
            
            tokens+=focused.getTokens(blockArray);
            //blockArray[focused.getColumn()][focused.getRow()] |= focused.getBlock();
            focused.place(blockArray);
            //based on the 1 bits within the block you places will determin the score youll receive
            if(focused.type.equals("big")){
                giveScore(focused.getBits()*20);
                Sounds.play("blop",0.8f);
            }
            else{
                giveScore(focused.getBits()*10);
                Sounds.play("blop");
            }
            
            placed++;
            if(preview.type.equals("small")){
                preview.setColumn(focused.getColumn());
                //preview.setOriginX(focused.getColumn());
            }
            //then generate a new focused block and reset the speeds
            focused = preview;
            if(r.nextFloat()>=preference.bigBlock||focused.type.equals("big")){
                preview = new Block(this,blocksY/2,blocksY-1,width/blocksX,height/blocksY);
            }else{
                if((r.nextInt((BigBlock.presets.length+BiggerBlock.presets.length)-1)+1)>(BigBlock.presets.length)){
                    preview = new BiggerBlock(this,blocksY/2,blocksY-1,width/blocksX,height/blocksY);
                    preview.setBlock(0);
                    if(preview.collides(blockArray)){
                        preview.rotate();
                        if(preview.collides(blockArray)){
                            preview = new Block(this,blocksY/2,blocksY-1,width/blocksX,height/blocksY);
                        }
                    }
                }else{
                    preview = new BigBlock(this,blocksY/2,blocksY,width/blocksX,height/blocksY);
                    preview.setBlock(0);
                    if(preview.collides(blockArray)){
                        preview.rotate();
                        if(preview.collides(blockArray)){
                            preview = new Block(this,blocksY/2,blocksY-1,width/blocksX,height/blocksY);
                        }
                    }
                }
            }
            cooldown = speed = topSpeed-(rows/30);
        }
   }
   public void gameOver(){
       gameOver("Game Over");
   }
    public void gameOver(String gameOverMessage){
        gameOver = true;
        score+=addScore;
        addScore = 0;
        if(score<=0){
            LevelPreferences.setLevel("Default");
            gameScreen.main.setScreen(gameScreen.main.levelSelect);

        }
        else{
            int minutes = (int)(timer/60);
            int seconds = (int)((timer)%60);
                    int milli = (int)((timer*100)%100);
            LevelPreferences.previousScore = new HighScore("",((minutes>=10)? minutes: ("0"+minutes))+":"+((seconds>=10)? seconds: ("0"+seconds))+":"+((milli>=10)? milli: ("0"+milli)),(long)score);
            LevelPreferences.previousLevel = preference.name;
            LevelPreferences.setLevel("Default"); 
            Values.transferMessage = gameOverMessage;
            gameScreen.main.setScreen(gameScreen.main.gameOver);
        }
    }
   
   public void deleteRow(byte[][] a, int v) {
       rows++;
        for(int y = v; y < blocksY; y++){
            for(int x = 0; x < blocksX;x++){
                if((y+1)<blocksY)
                a[x][y]=a[x][y+1];
                else
                a[x][y]=0;
            }
        }
    }
 
   
}
