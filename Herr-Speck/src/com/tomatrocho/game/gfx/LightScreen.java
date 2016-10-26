package com.tomatrocho.game.gfx;

public class LightScreen extends Screen {

	/**
	 * 
	 */
	public static final int ambientLightColor = Bitmap.getColor(1, 0.1f, 0.1f, 0.1f);
	
	/**
	 * 
	 */
	private Shadow shadows[];
	
	
	/**
	 * 
	 * @param w
	 * @param h
	 */
	public LightScreen(int w, int h) {
		super(w, h);
		
		this.shadows = new Shadow[w * h];
		for (int i = 0; i < shadows.length; i++)
			shadows[i] = Shadow.NONE;
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
		float r = getR(lightColor);
		float g = getG(lightColor);
		float b = getB(lightColor);
		
		if (r < getR(ambientLightColor)) r = getR(ambientLightColor);
		if (g < getG(ambientLightColor)) g = getG(ambientLightColor);
		if (b < getB(ambientLightColor)) b = getB(ambientLightColor);
		
		return getColor(1, r * getR(backgroundColor), g * getG(backgroundColor), b * getB(backgroundColor));
	}
	
	/**
	 * 
	 * @param lightColor1
	 * @param lightColor2
	 * @return
	 */
	public static int getMaxLight(int lightColor1, int lightColor2) {
		return getColor(1, Math.max(getR(lightColor1), getR(lightColor2)), Math.max(getG(lightColor1), getG(lightColor2)), Math.max(getB(lightColor1), getB(lightColor2)));
	}
	
	/**
	 * 
	 * @return
	 */
	public Shadow[] getShadows() {
		return shadows;
	}
}
