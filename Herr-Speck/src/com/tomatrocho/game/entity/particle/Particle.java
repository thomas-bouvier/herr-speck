package com.tomatrocho.game.entity.particle;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.gfx.IAbstractBitmap;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.level.World;
import com.tomatrocho.game.math.IBoundingBoxOwner;

public class Particle extends Entity {
	
	/**
	 * 
	 */
	private int life;

	
	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 */
	public Particle(World world, double x, double y) {
		this(world, x, y, HerrSpeck.random.nextInt(30));
	}
	
	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 */
	public Particle(World world, double x, double y, int life) {
		super(world, x, y);
		
		this.xd = HerrSpeck.random.nextGaussian();
		this.yd = HerrSpeck.random.nextGaussian();
		
		this.life = life;
		
		System.out.println(life);
	}

	@Override
	public void render(IAbstractScreen screen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDepthLine() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IAbstractBitmap getSprite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void collide(IBoundingBoxOwner bbOwner, double xa, double ya) {
		// TODO Auto-generated method stub
		
	}
}
