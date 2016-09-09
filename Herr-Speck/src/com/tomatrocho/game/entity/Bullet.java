package com.tomatrocho.game.entity;

import com.tomatrocho.game.gfx.Art;
import com.tomatrocho.game.gfx.IAbstractBitmap;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.level.World;
import com.tomatrocho.game.level.tile.Tile;

public class Bullet extends Entity {

	/**
	 * 
	 */
	private Mob shooter;
	
	/**
	 * 
	 */
	private double xa;
	
	/**
	 * 
	 */
	private double ya;
	
	/**
	 * 
	 */
	private float damage;
	
	/**
	 * 
	 */
	private int freezeTime = 15;
	
	/**
	 * 
	 */
	private int duration = 40;
	
	/**
	 * 
	 */
	private int facing;
	
	/**
	 * 
	 */
	private boolean hit = false;
	
	
	/**
	 * Constructor for the {@link Bullet} class.
	 * 
	 * @param world
	 * @param shooter
	 * @param x
	 * @param y
	 * @param xa
	 * @param ya
	 * @param damage
	 */
	public Bullet(World world, Mob shooter, double x, double y, double xa, double ya, float damage) {
		super(world, x, y, shooter.getTeam());
		
		this.xa = xa * 9;
		this.ya = ya * 9;
		
		this.shooter = shooter;
		this.damage = damage;
		this.facing = shooter.getFacing();
	}
	
	/**
	 * Constructor for the {@link Bullet} class.
	 * 
	 * @param world
	 * @param shooter
	 * @param xa
	 * @param ya
	 * @param damage
	 */
	public Bullet(World world, Mob shooter, double xa, double ya, float damage) {
		this(world, shooter, shooter.getX(), shooter.getY(), xa, ya, damage);
	}
	
	@Override
	public void tick() {
		duration--;
		if (duration <= 0) {
			remove();
			return;
		}
		
		if (!move(xa, ya)) {
			hit = true;
		}
		
		if (hit && !removed) {
			remove();
		}
	}
	
	@Override
	protected boolean shouldBlock(Entity entity) {
		if (entity instanceof Bullet) {
			return false;
		}
		
		return entity != shooter;
	}
	
	@Override
	public void collide(Entity entity, double xa, double ya) {
		if (entity instanceof Mob) {
			Mob mob = (Mob) entity;
			if (!mob.isFriendOf(shooter)) {
				mob.hurt(this);
				hit = true;
			}
		}
	}

	@Override
	public void render(IAbstractScreen screen) {
		screen.blit(getSprite(), pos.x - 8, pos.y - 10);
	}
	
	/**
	 * 
	 */
	public IAbstractBitmap getSprite() {
		return Art.bullets[facing][0];
	}

	/**
	 * 
	 * @return
	 */
	public int getDepthLine() {
		return (int) pos.y + Tile.H;
	}
	
	/**
	 * 
	 * @return
	 */
	public Mob getShooter() {
		return shooter;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getDamage() {
		return damage;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getFreezeTime() {
		return freezeTime;
	}
}
