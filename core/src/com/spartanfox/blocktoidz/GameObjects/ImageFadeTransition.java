/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartanfox.blocktoidz.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.ixeption.libgdx.transitions.ScreenTransition;
import com.spartanfox.blocktoidz.Globals.Textures;

/**
 *
 * @author Ben
 */
public class ImageFadeTransition implements ScreenTransition {

	private final Color color;
	private final Interpolation interpolation;
	private final String texture;

	/** @param texture the {@link Texture} to fade to
	 * @param interpolation the {@link Interpolation} method */
	public ImageFadeTransition (String texture, Interpolation interpolation) {
		this.color = new Color(Color.WHITE);
		this.interpolation = interpolation;
                this.texture = texture;
		//this.texture = texture; //new Texture(texture.getWidth(),texture.getHeight(), Pixmap.Format.RGBA8888);
		//this.texture.draw(pixmap, 0, 0);
                
	}

	@Override
	public void render (Batch batch, Texture currentScreenTexture, Texture nextScreenTexture, float percent) {
		float width = currentScreenTexture.getWidth();
		float height = currentScreenTexture.getHeight();
		float x = 0;
		float y = 0;

		if (interpolation != null) percent = interpolation.apply(percent);

		batch.begin();
                batch.setColor(Color.WHITE);
		float fade = percent * 2;

		if (fade > 1.0f) {
			fade = 1.0f - (percent * 2 - 1.0f);
                        batch.draw(nextScreenTexture,0, Gdx.graphics.getHeight(),Gdx.graphics.getWidth(),-Gdx.graphics.getHeight());

		} else {
                    batch.draw(currentScreenTexture,0, Gdx.graphics.getHeight(),Gdx.graphics.getWidth(),-Gdx.graphics.getHeight());
		}

		color.a = fade;

		batch.setColor(color);
		batch.draw(Textures.get(texture), 0, 0, width, height);
		batch.end();
		batch.setColor(Color.WHITE);

	}

}
