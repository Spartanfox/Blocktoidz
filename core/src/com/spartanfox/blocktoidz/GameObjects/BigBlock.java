/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.GameObjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spartanfox.blocktoidz.Levels.Level;

/**
 *
 * @author Ben
 */
public final class BigBlock extends Block{
    Block[] blocks;
    public final static int[][] presets = new int[][]{
        {left|bottom,0,left|bottom,solid},
        {left|bottom,0,left|bottom,solid},
        ///////////////////////////////////
        //{bottom,0,left|bottom,solid},  //
        //{left|bottom,0,left|top,solid},//
        ///////////////////////////////////
        {solid,solid,0,solid},
        {solid,solid,0,solid},
        

//{bottom|right,solid,top|left,solid},    
    };
    public BigBlock(Level level, float x, float y, float width, float height) {
        super(level, x, y, width, height);
        type = "big";
        blocks = new Block[4];
        blocks[0] = new Block(level,x-1,y,width,height);
        blocks[3] = new Block(level,x-1,y-1,width,height);
        blocks[2] = new Block(level,x,y-1,width,height);
        blocks[1] = new Block(level,x,y,width,height);
        randomBlock(level.r.nextInt(presets.length));
        for(Block b : blocks)if(b.collide((byte) 0xFF)){b.setColor(shape);}
        for(Block b : blocks)b.type = type;
        
        rotate(level.r.nextInt(3));
        
        
    }
    @Override
    public int getBits(){
        int bits=0;
        for(Block b : blocks)bits+=b.getBits();
        return bits;
    }
    @Override
    public int getColumn(){
        if(blocks[1].getColumn()>=0&&blocks[1].getColumn()<level.blocksX/2)return blocks[1].getColumn();
        else return blocks[0].getColumn();
    }
    public void randomBlock(int block){
        for (int i = 0; i < blocks.length; i++) {
            blocks[i].setBlock(presets[block][i]);
        }
    }
    @Override
    public void update(float delta){
        for(Block b : blocks)b.update(delta);
    }
    @Override
    public void draw(Batch batch, float alpha){
        for(Block b : blocks)b.draw(batch, alpha);
    }

    @Override
    public boolean collides(byte[][] blockArray){
        for(Block b : blocks)if(b.collides(level.blockArray))return true;
        return false;
    }
    @Override
    public boolean canRotate(byte[][] blockArray){

        return true;
    }
    @Override
    public void rotate(){
        rotate(1);
        if(collides(level.blockArray))rotate(1);
        if(collides(level.blockArray))rotate(1);
        if(collides(level.blockArray))rotate(1);
        
       // for(Block B : blocks)if(B.collides(level.blockArray))rotate();
    }
    public void rotate(int iter){
        for (int i = 0; i < iter; i++) {
            for(Block b : blocks)b.rotate(1);
            byte placeHolder = blocks[0].shape;
            blocks[0].shape = blocks[3].shape;
            blocks[3].shape = blocks[2].shape;
            blocks[2].shape = blocks[1].shape;
            blocks[1].shape = placeHolder;
        }
    }
    @Override
    public void drawPreview(SpriteBatch batch,float x, float y,float width,float height){
        width/=2;
        height/=2;
        blocks[0].drawPreview(batch, x, y+height, width, height);
        blocks[1].drawPreview(batch, x+width, y+height, width, height);
        blocks[2].drawPreview(batch, x+width, y, width, height);
        blocks[3].drawPreview(batch, x, y, width, height);
    }
    @Override
    public boolean place(byte[][] blockArray){
        for(Block b : blocks)b.place(blockArray);
        return true;
    }
    @Override
    public boolean canMove(byte[][] blockArray,float x,float y){
        for(Block b : blocks)if(b.canMove(blockArray, x, y)==false)return false;
        return true;
    }
    @Override
    public void move(float x, float y){
        for(Block b : blocks)b.move(x,y);
    }
    @Override
    public void moveX(float x){
        for(Block b : blocks)b.moveX(x);
    }
    @Override
    public void moveY(float y){
        for(Block b : blocks)b.moveY(y);
    }
    @Override
    public int getTokens(byte[][] blockArray){
        int tokens =0;
        for(Block b : blocks)tokens+=b.getTokens(blockArray);
        return tokens;
    }
}
