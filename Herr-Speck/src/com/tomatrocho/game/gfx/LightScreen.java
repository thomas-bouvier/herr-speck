package com.tomatrocho.game.gfx;

public class LightScreen extends Screen {

	/**
	 * 
	 */
	public static final int ambientLightColor = 0xff000000;
	
	
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
			pixels[i] = ambientLightColor;
	}
	
	/**
	 * 
	 * @param backgroundColor
	 * @param lightColor
	 * @return
	 */
	public int getLightBlend(int backgroundColor, int lightColor) {
		final int weight = 170;
		
		final int rrr = ambientLightColor & 0xff0000;
		final int ggg = ambientLightColor & 0xff00;
		final int bbb = ambientLightColor & 0xff;
		
		final int rr = lightColor & 0xff0000;
        final int gg = lightColor & 0xff00;
        final int bb = lightColor & 0xff;
		
		int r = backgroundColor & 0xff0000;
        int g = backgroundColor & 0xff00;
        int b = backgroundColor & 0xff;
        
        r = ((rr * weight + r * (256 - weight)) >> 8) & 0xff0000;
        g = ((gg * weight + g * (256 - weight)) >> 8) & 0xff00;
        b = ((bb * weight + b * (256 - weight)) >> 8) & 0xff;
        
        if (r < rrr) r = rrr;
        if (g < ggg) g = ggg;
        if (b < bbb) b = bbb;
        
        return 0xff000000 | r | g | b;
	}
}
