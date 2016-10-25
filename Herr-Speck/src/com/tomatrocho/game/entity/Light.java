package com.tomatrocho.game.entity;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.gfx.Bitmap;
import com.tomatrocho.game.gfx.IAbstractBitmap;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.gfx.LightScreen;
import com.tomatrocho.game.level.World;

public class Light {
	
	/**
	 * 
	 */
	public static final int ambient = 0xff00000f;
	
	/**
	 * 
	 */
	public IAbstractBitmap lightScreen;
	
	/**
	 * 
	 */
	private World world;
	
	/**
	 * 
	 */
	public double x;
	
	/**
	 * 
	 */
	public double y;
	
	/**
	 * 
	 */
	private int radius;
	
	/**
	 * 
	 */
	private int color;
	
	/**
	 * 
	 */
	private int[] pixels;
	
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param radius
	 * @param color
	 */
	public Light(World world, double x, double y, int radius, int color) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color;
		
		this.pixels = new int[radius * 2 * radius * 2];
		for (int j = 0; j < 2 * radius; j++) {
			for (int i = 0; i < 2 * radius; i++) {
				final float distance = (float) Math.sqrt(Math.pow(i - radius, 2) + Math.pow(j - radius, 2));
				
				if (distance < radius)
					pixels[j * radius * 2 + i] = Bitmap.getColorPower(color, 1 - distance / radius);
				else
					pixels[j * radius * 2 + i] = 0xff000000;
			}
		}
	}

	/**
	 * 
	 * @param screen
	 */
	public void render(IAbstractScreen screen) {
		final LightScreen lightScreen = HerrSpeck.getLightScreen();
		for (int j = 0; j < 2 * radius; j++) {
			for (int i = 0; i < 2 * radius; i++) {
				final int x = (int) this.x + i - lightScreen.getXOffset() - radius;
				final int y = (int) this.y + j - lightScreen.getYOffset() - radius;
				
				lightScreen.setPixel(x, y, LightScreen.getMaxLight(pixels[j * radius * 2 + i], lightScreen.getPixel(x, y)));
			}
		}
		
		/*
		final int dx = Math.abs(x1 - x0);
		final int dy = Math.abs(y1 - y0);
		
		final int sx = x0 < x1 ? 1 : -1;
		final int sy = y0 < y1 ? 1 : -1;
		
		final int err = dx - dy;
		*/
		
		/*
		Bitmap bitmap = new Bitmap(radius * 2, radius * 2, pixels);
		screen.blit(bitmap, x, y);
		*/
	}
	
	/**
	 * 
	 * @return
	 */
	public World getWorld() {
		return world;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getRadius() {
		return radius;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getColor() {
		return color;
	}
}
