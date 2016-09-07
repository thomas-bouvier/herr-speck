package com.tomatrocho.game.entity.animation;

import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.gfx.IAbstractBitmap;
import com.tomatrocho.game.level.World;

public abstract class Animation extends Entity {
	
	/**
	 * 
	 */
	protected IAbstractBitmap[][] bitmap;
	
	/**
	 * 
	 */
	protected int numberFrames;
	
	/**
	 * 
	 */
	protected int duration;
	
	/**
	 * 
	 */
	protected int life;

	
	/**
	 * Constructor for the {@link Animation} class.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 */
	public Animation(World world, double x, double y, int duration) {
		super(world, x, y);
		this.duration = duration;
		this.life = duration;
	}

	@Override
	public void tick() {
		if (life-- < 0) remove();
	}
}
