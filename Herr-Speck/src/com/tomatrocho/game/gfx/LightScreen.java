package com.tomatrocho.game.gfx;

public class LightScreen extends Screen {

	/**
	 * 
	 */
	public static final int ambientLight = 0x0f00f0f0;
	
	
	/**
	 * 
	 * @param w
	 * @param h
	 */
	public LightScreen(int w, int h) {
		super(w, h);
	}
	
	/**
	 * 
	 */
	public void clear() {
		for (int i = 0; i < pixels.length; i++)
			pixels[i] = ambientLight;
	}
	
	/**
	 * 
	 * @param backgroundColor
	 * @param lightColor
	 * @return
	 */
	public int getLightBlend(int backgroundColor, int lightColor) {
		final int rr = backgroundColor & 0xff0000;
        final int gg = backgroundColor & 0xff00;
        final int bb = backgroundColor & 0xff;
        
        int r = lightColor & 0xff0000;
        int g = lightColor & 0xff00;
        int b = lightColor & 0xff;
        
        r = ((r * 90 + rr * 166) >> 8) & 0xff0000;
        g = ((g * 90 + gg * 166) >> 8) & 0xff00;
        b = ((b * 90 + bb * 166) >> 8) & 0xff;
        
        return 0xff000000 | r | g | b;
	}
}
