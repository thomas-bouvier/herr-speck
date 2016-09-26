package com.tomatrocho.game.gfx;

/**
 * The {@link IComparableDepth} interface describes objects having
 * a depth parameter at render time.
 */
public interface IComparableDepth {
	
	/**
	 * 
	 * @param screen
	 */
	public void render(IAbstractScreen screen);
	
	/**
	 * 
	 * @return
	 */
	public int getDepthLine();
	
	/**
	 * 
	 * @param screen
	 */
	public void renderDepthLine(IAbstractScreen screen);
}
