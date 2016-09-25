package com.tomatrocho.game.gfx;

public class SceneScreen extends Screen {

	/**
	 * 
	 * @param w
	 * @param h
	 */
	public SceneScreen(int w, int h) {
		super(w, h);
	}
	
	/**
	 * 
	 * @param lightScreen
	 */
	public void combine(LightScreen lightScreen) {
		for (int i = 0; i < pixels.length; i++)
			pixels[i] = lightScreen.getLightBlend(pixels[i], lightScreen.getPixel(i));
	}
}
