package com.tomatrocho.game.entity;

import com.tomatrocho.game.entity.mob.Player;
import com.tomatrocho.game.entity.mob.Team;
import com.tomatrocho.game.entity.weapon.Weapon;
import com.tomatrocho.game.gfx.IAbstractBitmap;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.math.Vec2;
import com.tomatrocho.game.world.level.World;
import com.tomatrocho.game.world.tile.Tile;

public abstract class Mob extends Entity {

	/**
	 * Default width of a {@link Mob} object.
	 */
	public static final int H = 32;

	/**
	 * Default height of a {@link Mob} object.
	 */
	public static final int W = 32;
	
	/**
	 * 
	 */
	protected static final float MAX_HEALTH = 100;
	
	/**
	 * 
	 */
	protected float health = MAX_HEALTH;
	
	/**
	 * 
	 */
	protected int hurtTime = 0;
	
	/**
	 * 
	 */
	protected int freezeTime = 0;

	/**
	 * 
	 */
	protected Weapon weapon;

	/**
	 * 
	 */
	protected Vec2 aimVector;
	
    /**
     * Orientation of the {@link Mob}.
     */
    protected int facing = 0;
	
	/**
	 * 
	 */
	protected double dir = 0;

	/**
	 * 
	 */
	protected boolean moving = false;

	/**
	 * 
	 */
	protected double speed = 1.0;
	
	/**
	 * 
	 */
	protected int ticks = 0;

	/**
	 * 
	 * @param level
	 * @param x
	 * @param y
	 */
	public Mob(World level, int x, int y, Team team) {
		super(level, x, y, team);
		aimVector = new Vec2(0, 1);
	}

	@Override
	public void tick() {
		if (hurtTime > 0) {
			hurtTime--;
		}
		if (freezeTime > 0) {
			freezeTime--;
		}
		if (health <= 0) {
			remove();
			return;
		}
	}
	
	@Override
	public void collide(Entity entity, double xa, double ya) {
		x += xa * 0.5;
		y += ya * 0.5;
	}
	
	/**
	 * 
	 * @param source
	 */
	public void hurt(Entity source) {
		if (source instanceof Bullet) {
			Bullet bullet = (Bullet) source;
			if (bullet.getShooter() instanceof Player) {
				if (isFriendOf((Player) bullet.getShooter())) {
					return;
				}
			}
			if (freezeTime <= 0) {
				freezeTime = bullet.getFreezeTime();
				hurtTime = 40;
				health -= bullet.getDamage();
				if (health < 0) {
					health = 0;
				}
			}
		}
	}

	@Override
	public void render(IAbstractScreen screen) {
		final IAbstractBitmap sprite = getSprite();
		if (hurtTime > 0) {
			int col = (int) (180 - health * 180 / MAX_HEALTH);
			screen.colorBlit(sprite, (int) (x - sprite.getW() / 2), (int) (y - sprite.getH() / 2), (col << 24) + 255 * 65536);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract IAbstractBitmap getSprite();
	
	/**
	 * 
	 * @return
	 */
	public int getVerticalBaseCoordinate() {
		return (int) (y + Tile.H) - 6;
	}
	
	/**
	 * 
	 * @param mob
	 * @return
	 */
	public boolean isFriendOf(Mob mob) {
		return team == mob.getTeam();
	}
	
	/**
	 * 
	 * @return
	 */
	public Vec2 getAimVector() {
		return aimVector;
	}
	
    /**
     * 
     * @return
     */
    public int getFacing() {
    	return facing;
    }
}