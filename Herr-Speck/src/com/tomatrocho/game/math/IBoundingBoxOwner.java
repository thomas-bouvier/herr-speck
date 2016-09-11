package com.tomatrocho.game.math;

public interface IBoundingBoxOwner {

	/**
	 * 
	 * @param bb
	 * @param xa
	 * @param ya
	 */
	public void handleCollision(BoundingBox bb, double xa, double ya);

	/**
	 * 
	 * @param bbOwner
	 * @param xa
	 * @param ya
	 */
	public void collide(IBoundingBoxOwner bbOwner, double xa, double ya);
	
	/**
	 * 
	 * @param bbOwner
	 * @return
	 */
	public boolean blocks(IBoundingBoxOwner bbOwner);
	
	/**
	 * 
	 * @return
	 */
	public boolean isBlocking();
	
	/**
	 * 
	 * @param bbOwner
	 * @return
	 */
	public boolean shouldBlock(IBoundingBoxOwner bbOwner);
}
