package com.tomatrocho.game.gfx;

import com.tomatrocho.game.level.World;

public class Light {
	
	/**
	 * 
	 */
	public static final int ambient = 0xff00000f;
	
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
		for (int i = 0; i <= 2 * radius; i++) {
			renderLine((LightScreen) screen, radius, radius, i, 0);
			renderLine((LightScreen) screen, radius, radius, i, radius * 2);
			renderLine((LightScreen) screen, radius, radius, 0, i);
			renderLine((LightScreen) screen, radius, radius, radius * 2, i);
		}
	}
	
	/**
	 * 
	 * @param screen
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 */
	private void renderLine(LightScreen screen, int x0, int y0, int x1, int y1) {	
		final int dx = Math.abs(x1 - x0);
		final int dy = Math.abs(y1 - y0);
		
		final int sx = x0 < x1 ? 1 : -1;
		final int sy = y0 < y1 ? 1 : -1;
		
		int err1 = dx - dy;
		int err2 = 0;
		
		int x, y;
		
		while (true) {
			if (x0 == x1 && y0 == y1)
				break;
			
			if (pixels[y0 * radius * 2 + x0] == 0xff000000)
				break;
			
			x = (int) x0 - screen.getXOffset() - radius + (int) this.x;
			y = (int) y0 - screen.getYOffset() - radius + (int) this.y;
			
			if (x >= 0 && x < screen.getW() && y >= 0 && y < screen.getH()) {
				if (screen.getShadows()[y * screen.getW() + x] == Shadow.TOTAL)
					break;
			}
			
			//screen.setPixel(x, y, pixels[y0 * radius * 2 + x0]);
			screen.setPixel(x, y, LightScreen.getMaxLight(pixels[y0 * radius * 2 + x0], screen.getPixel(x, y)));
			
			err2 = 2 * err1;
			
			if (err2 > -dy) {
				err1 -= dy;
				x0 += sx;
			}
			
			if (err2 < dx) {
				err1 += dx;
				y0 += sy;
			}
		}
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
