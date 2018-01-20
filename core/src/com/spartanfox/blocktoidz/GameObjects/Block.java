/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.spartanfox.blocktoidz.Globals.*;
import com.spartanfox.blocktoidz.Levels.BreakerLevel;
import com.spartanfox.blocktoidz.Levels.Level;
import java.util.Random;

/**
 *
 * @author Ben
 */
public class Block extends Actor{
    public static int top   =0x2;
    public static int left  =0x1;
    public static int right =0x4;
    public static int bottom=0x8;
    public static int solid = left|top|bottom|right;
    public static int R = 0x10;
    public static int G = 0x20;
    public static int B = 0x40;
    public String type = "small";
    public int size =0;
    float speedX = .1f;
    float speedY = .1f;
    public float moveX,moveY;
    int width,height;
    byte shape;
    boolean movingX,movingY,moving;
    Level level;
    int stack;
    //simple commands to retreive the information needed about the block
    //and to drop the block when you swipe down
    
    public int getRow   (){return (int)moveY;}
    public int getColumn(){return (int)moveX;}
    public int getBits(){return Integer.bitCount(getShape());}
    public boolean isMoving(){return moving;}
    public byte getShape(){return (byte)(shape&0xF);}
    public byte getBlock(){return shape;}
    public byte getBlockColor(){return (byte) (shape&0x70);}
    public void setBlock(byte newBlock){shape = newBlock;}
    public void setBlock(int newBlock){shape = (byte) newBlock;}
    public void setColor(byte newColor){shape = (byte)(getShape()|newColor&0x70);}
    public void setColumn(int x){moveX = x;setOriginX(x);}
    public void Drop(){
        speedX = .5f;
        speedY = .5f;
    }
    public Block(Level level, float x,float y,float width,float height){
        super.setOrigin(x, y);
        speedX = .2f;
        speedY = .1f;
        this.level  = level;
        moveX = x;
        moveY = y;
        this.width  = (int)width ;
        this.height = (int)height;
        shape = randomBlock(level.r);
    }
    //generated a random block which isnt invisible and not black
    public byte randomBlock(Random r){
       int shape=0;
       int size = 0;
       
       //shape
         do{
            if(r.nextBoolean()&&(shape&top)==0){
                shape |= top;size++;
            }
            if(r.nextBoolean()&&r.nextBoolean()&&(shape&right)==0){
                shape |= right;size++;
            }
            if(r.nextBoolean()&&r.nextBoolean()&&size!=2&&(shape&bottom)==0){
                shape |= bottom;size++;
            }
            if(r.nextBoolean()&&size!=2&&(shape&left)==0){
                shape |= left;size++;
            }
            if(!(level instanceof BreakerLevel)){
                if(size==1&&r.nextFloat()>0.3){
                    return randomBlock(r);
                }
            }
        }while((shape&0x7)==0);
         //color
        if(r.nextBoolean()){
            shape |= R;
        }
        if(r.nextBoolean()){
            shape |= G;
        }
        if(r.nextBoolean()){
            shape |= B;
        }
        if(r.nextBoolean()){
        //    shape |= 0x80;
        }
        return (byte) shape;
   }
    public static boolean sameColor(byte block1,byte block2){
        if(block1==0||block2==0)return false;
        if((block1&0x70)==(block2&0x70))return true;
        return false;
    }
    @Override
    public void draw(Batch batch, float delta){
        //update first then work out which theme the games using
        //to use the correct colouration
        update(delta);
        batch.setColor(level.colors[(getBlock()&0xff)>>4]);
        if(this.getRow()>=level.blocksY)
            this.drawPreview((SpriteBatch) batch, this.getOriginX()*level.blocksWidth, this.getOriginY()*level.blocksHeight, width, height,0.5f);
        else
            this.drawPreview((SpriteBatch) batch, this.getOriginX()*level.blocksWidth, this.getOriginY()*level.blocksHeight, width+1, height+1,1);
        if(Values.control == Values.Controls.Simple){
            
            //preview for the fallthrough
            if(level.touch&&type.equals("small")&&Values.preview){
                for (int i = 1; i <= level.blocksY; i++) {
                        if(collide(getRelative(level.blockArray,-(i),0))){i--;
                        this.drawPreview((SpriteBatch) batch, this.getOriginX()*level.blocksWidth, (moveY-i)*level.blocksHeight, width+1, height+1,0.5f);
                        break;
                    }
                }
            }
            else 
                if(type.equals("small")&&Values.preview&&this.getHere(level.blockArray)==0){
                for (int i = 1; i <= level.blocksY; i++) {
                    if(getRelative(level.blockArray,-i,0)!=0&&!collide(getRelative(level.blockArray,-i,0))||collide(getRelative(level.blockArray,-(i),0))){
                        if(collide(getRelative(level.blockArray,-(i),0)))i--;
                        this.drawPreview((SpriteBatch) batch, this.getOriginX()*level.blocksWidth, (moveY-i)*level.blocksHeight, width+1, height+1,0.5f);
                        break;
                    }
                }
            }
        }
    }
    public void drawPreview(SpriteBatch batch,float x,float y,float width,float height){
        drawPreview(batch, x,y,width,height,1);
    }
    public void drawPreview(SpriteBatch batch,float x,float y,float width,float height, float opasity){
        batch.setColor(level.colors[(shape&0xff)>>4]);
        batch.setColor(batch.getColor().sub(0,0,0,1-opasity));
        //if the block is whole then just render the whole block instead of 4 individual peices
        if((shape&0xf)==0xf)batch.draw(level.block,x,y,width,height);
        else{
            if((shape&0x1)!=0)batch.draw(level.blockTop,x,y,width,height);
            if((shape&0x2)!=0)batch.draw(level.blockRight,x,y,width,height);
            if((shape&0x4)!=0)batch.draw(level.blockBottom,x,y,width,height);
            if((shape&0x8)!=0)batch.draw(level.blockLeft,x,y,width,height);
        }
        batch.setColor(Color.WHITE);
    }
    public int getTokens(byte[][] blockArray){
        //if(this.getShape()==solid)return 1;
        try{
            if(Block.sameColor(blockArray[getColumn()][getRow()],getBlock())){
                return Integer.bitCount(getShape());
            }
        }catch(Exception e){
            return 0;
        }
        return 0;
    }
    public boolean place(byte[][] blockArray){
         try{
            blockArray[getColumn()][getRow()] |= shape;
            return false;
        }
        catch(Exception e){return false;}
    }
    public void update(float delta){
        //deals with the animation of slowly moving the block to its new location
        //however needs to be redone to account for multiple movements since
        //moving left to right while the block is abotu to fall causes it to become offset
        moving = false;
        if(getOriginX() != moveX){
            if(getOriginX() > moveX)setOriginX(getOriginX()-(speedX*60*delta));
            if(getOriginX() < moveX)setOriginX(getOriginX()+(speedX*60*delta));
            if(Cal.range(getOriginX(), moveY)<speedX)setOriginX(moveX);
            moving = true;
        }
        if(getOriginY() != moveY){
            if(getOriginY() > moveY)setOriginY(getOriginY()-(speedY*60*delta));
            if(getOriginY() < moveY)setOriginY(getOriginY()+(speedY*60*delta));
            if(Cal.range(getOriginY(), moveY)<speedY)setOriginY(moveY);
            moving = true;
        } 
     
    }
    //used for the sliding mechanic of the game but not used as sliding just
    //became way too confusing so fuck it ill save it for another time
    public boolean down(byte[][] blockArray){
        byte here = (byte) (this.getHere(blockArray)&0xf);
        if(here!=0&&!collide(here)){
            //check to see if the shape of the blocks match something for sliding
            if((here&top)==0&&(shape&bottom)==0){
                Gdx.app.debug("Blocktoidz","Possible slide");
                if((here&right)==0){
                    Gdx.app.debug("Blocktoidz","possible slide right");
                    if(!collide(getBottomRight(blockArray))&&!collide(getRight(blockArray))){
                        Gdx.app.debug("Blocktoidz","Slide right");
                        move(1,-1);
                        if(canRotate(blockArray)){
                            this.rotate();
                            if(canRotate(blockArray)){
                                this.rotate();
                            }
                        }
                        return true;
                    }
                }if((here&left)==0){
                    if(!collide(getBottomLeft(blockArray))&&!collide(getLeft(blockArray))){
                        Gdx.app.debug("Blocktoidz","Slide left");
                        move(-1,-1);
                        return true;
                    }
                }
            }
        }
        if(this.collide(this.getBottom(blockArray))){
            return false;
        }
        Gdx.app.debug("Blocktoidz","Down");
        move(0,-1);
        return true;
        
        //return false;
    }
    //the method you call to move the block when needed
    public void move(float x, float y){
        setOriginX(moveX);
        setOriginY(moveY);
        moveX += x;
        moveY += y;
        if(this.getColumn()<0||this.getColumn()>=level.blocksX)
            moveX -= x*level.blocksWidth;
        if(this.getRow()<0||this.getRow()>=level.blocksY)
            moveY -= y*level.blocksHeight;
        moving = true;
    }
    public void moveX(float x){
        moveX = x;
        moving = true;
    }
    public void moveY(float y){
        moveY = y;
        moving = true;
    }
    public boolean canMove(byte[][] blockArray,float x, float y){
        //if(this.getRow()>=blockArray[0].length){
       //     return true;
       // }
        if(y<0)if(collide(getBottom(blockArray)))return false;
        if(x<0)if(collide(getLeft(blockArray)))return false;
        if(x>0)if(collide(getRight(blockArray)))return false;
        
        return true;
    }
    public boolean collides(byte[][] blockArray){
        return collide(getHere(blockArray));
    }
    public boolean collide(byte shape){
        
        if((this.getShape()&shape)!=0){
            return true;
        }
        return false;
    }
    
    /*using bitwise operations to rotate the block by adding its shape to itself
      then if overflow add 1
    */
    public byte getRotate(){
        byte rotated = (byte)(shape&0xf);
        rotated += rotated;
        if((rotated&0x10)!=0){rotated++;}
        return (byte)((shape&0xf0)|(rotated&0x0f));
    }
    public void rotate(){
        rotate(1);
        if(collides(level.blockArray))rotate(1);
        if(collides(level.blockArray))rotate(1);
        if(collides(level.blockArray))rotate(1);
        if(type.equals("small")&&collides(level.blockArray))rotate(1);
    }
    public void rotate(int iter){
        for (int i = 0; i < iter; i++) {
            byte rotated = (byte)(shape&0xf);
            rotated += rotated;
            if((rotated&0x10)!=0){rotated++;}
            shape = (byte)((shape&0xf0)|(rotated&0x0f));
        }
    }
    //check the location the block is in to see if its rotated version collides
    public boolean canRotate(byte[][] BlockArray){
        byte here = getHere(BlockArray);
            
        byte rotated = (byte)(shape&0xf);
        rotated += rotated;
        if((rotated&0x10)!=0){rotated++;}
        if((rotated&here)!=0)return false;
        return true;
    }
    //returns the blocks around the blocks current location
    public byte getHere(byte[][] Blocks){
        if(this.getRow()>=Blocks[0].length&&this.getRow()<40){return 0;}
        try{
            return (byte)(Blocks[this.getColumn()][this.getRow()]&0xf);
        }
        catch(Exception e){return (byte) solid;}
    }
    public byte getBottom(byte[][] Blocks){
        try{
            return (byte)(Blocks[this.getColumn()][this.getRow()-1]&0xf);
        }
        catch(Exception e){return 0xF;}
    }
    public byte getRelative(byte[][] Blocks,int x,int y){
        try{
            return (byte)(Blocks[this.getColumn()+y][this.getRow()+x]&0xf);
        }
        catch(Exception e){return 0xF;}
    }
    
    
    public byte getBottomLeft(byte[][] Blocks){
        
        try{
            return (byte)(Blocks[this.getColumn()-1][this.getRow()-1]&0xf);
        }
        catch(Exception e){return 0xF;}
    }
    public byte getBottomRight(byte[][] Blocks){
        try{
            return (byte)(Blocks[this.getColumn()+1][this.getRow()-1]&0xf);
        }
        catch(Exception e){return 0xF;}
    }
    
        public byte getLeft(byte[][] Blocks){
            if(this.getRow()>=Blocks[0].length){
                if(this.getColumn()>=1){
                    return 0;
                }else{
                    return 0xf;
                }
            }
        try{
            return (byte)(Blocks[this.getColumn()-1][this.getRow()]&0xf);
        }
        catch(Exception e){return 0xF;}
    }
    public byte getRight(byte[][] Blocks){
        if(this.getRow()>=Blocks[0].length)
            if(this.getColumn()<Blocks.length-1){
                return 0;
            }else {
                return 0xf;
            }
        try{
            return (byte)(Blocks[this.getColumn()+1][this.getRow()]&0xf);
        }
        catch(Exception e){return 0xF;}
    }
}
