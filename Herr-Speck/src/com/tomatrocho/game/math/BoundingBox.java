package com.tomatrocho.game.math;

public class BoundingBox {

	/**
	 * 
	 */
	public double x0;
	
	/**
	 * 
	 */
	public double y0;
	
	/**
	 * 
	 */
	public double x1;
	
	/**
	 * 
	 */
	public double y1;
	
	/**
	 * 
	 */
	public IBoundingBoxOwner owner;

	
	/**
	 * 
	 * @param owner
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 */
	public BoundingBox(IBoundingBoxOwner owner, double x0, double y0, double x1, double y1) {
		this.owner = owner;
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}

	/**
	 * 
	 * @param bb
	 * @return
	 */
	public boolean intersects(BoundingBox bb) {
		return !(bb.x0 >= x1 || bb.y0 >= y1 || bb.x1 <= x0 || bb.y1 <= y0);
	}
	
	/**
	 * 
	 * @param xx0
	 * @param yy0
	 * @param xx1
	 * @param yy1
	 * @return
	 */
	public boolean intersects(double xx0, double yy0, double xx1, double yy1) {
		return !(xx0 >= x1 || yy0 >= y1 || xx1 <= x0 || yy1 <= y0);
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public BoundingBox grow(double amount) {
		return new BoundingBox(owner, x0 - amount, y0 - amount, x1 + amount, y1 + amount);
	}
	
	/**
	 * 
	 * @return
	 */
	public IBoundingBoxOwner getOwner() {
		return owner;
	}
}
