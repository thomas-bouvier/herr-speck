package com.tomatrocho.game.entity;

import java.awt.Color;

import com.tomatrocho.game.entity.mob.Player;
import com.tomatrocho.game.entity.mob.Team;
import com.tomatrocho.game.gfx.Art;
import com.tomatrocho.game.gfx.IAbstractBitmap;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.gui.Font;
import com.tomatrocho.game.level.World;
import com.tomatrocho.game.math.IBoundingBoxOwner;
import com.tomatrocho.game.math.Vec2;
import com.tomatrocho.game.weapon.Weapon;

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
	 * Amount of health the {@link Mob} currently has.
	 */
	protected float health;
	
	/**
	 * 
	 */
	protected int yShadowOffset = 13;
	
	/**
	 * 
	 */
	protected int alphaShadow = 128;
	
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
	protected Vec2 aimVector = new Vec2(0, 1);;
	
	/**
	 * 
	 */
	protected Vec2 bumps = new Vec2();
	
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
	 * @param world
	 * @param x
	 * @param y
	 * @param team
	 */
	public Mob(World world, int x, int y, Team team) {
		super(world, x, y, team);
	}

	@Override
	public void tick() {
		if (hurtTime > 0)
			hurtTime--;
		
		if (freezeTime > 0)
			freezeTime--;
		
		if (freezeTime > 0) {
			if (bumps.notNull())
				move(bumps.x, bumps.y);
		}
		else {
			if (health <= 0)
				remove();
		}
	}
	
	@Override
	public void collide(IBoundingBoxOwner bbOwner, double xa, double ya) {
		pos.x += xa * 0.5;
		pos.y += ya * 0.5;
	}
	
	/**
	 * 
	 * @param source
	 */
	public void hurt(Entity source) {
		if (source instanceof Bullet) {
			Bullet bullet = (Bullet) source;
			
			if (bullet.getShooter() instanceof Player) {
				if (isFriendOf((Player) bullet.getShooter()))
					return;
			}
			
			if (freezeTime <= 0) {
				freezeTime = bullet.getFreezeTime();
				
				hurtTime = 40;
				
				health -= bullet.getDamage();
				if (health < 0)
					health = 0;
				
				// bump effect
				final double dist = pos.dist(source.getPos());
				bumps.x = (pos.x - source.getX()) / dist;
				bumps.y = (pos.y - source.getY()) / dist;
			}
		}
	}

	@Override
	public void render(IAbstractScreen screen) {
		// shadow
		screen.alphaBlit(Art.bigShadow, pos.x - Art.bigShadow.getW() / 2, pos.y - Art.bigShadow.getH() / 2 + yShadowOffset, alphaShadow);
		
		// hurt effect
		final IAbstractBitmap sprite = getSprite();
		if (hurtTime > 0) {
			if (hurtTime > 40 - 6 && hurtTime % 2 == 0)
				screen.colorBlit(sprite, pos.x - sprite.getW() / 2, pos.y - sprite.getH() / 2, 0x80ffffff);
			else
				screen.colorBlit(sprite, pos.x - sprite.getW() / 2, pos.y - sprite.getH() / 2, 0x80ff0000);
		}
		else
			screen.blit(sprite, pos.x - sprite.getW() / 2, pos.y - sprite.getH() / 2);
	}
	
	/**
	 * 
	 * @param screen
	 * @param string
	 */
	public void renderBubble(IAbstractScreen screen, String string) {
		Font.getDefaultFont().draw(screen, string, (int) pos.x, (int) pos.y - 28, Font.Align.CENTER, true); 
	}
	
	/**
	 * 
	 */
	public void drawDepthLine(IAbstractScreen screen) {
		final int w = getSprite().getW() / 2;
		final int h = 1;
		
		IAbstractBitmap sprite = screen.createBitmap(w, h);
		for (int x = 0; x <= w; x++) {
			for (int y = 0; y <= h; y++) {
				if (x == 0 || x == w - 1 || y == 0|| y == h - 1)
					sprite.setPixel(y * w + x, Color.LIGHT_GRAY.getRGB());
			}
		}
		
		screen.blit(sprite, pos.x - getSprite().getW() / 2 + w / 2, getDepthLine() - getSprite().getH() / 2);
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
	public int getDepthLine() {
		return (int) pos.y + getSprite().getH() - 7;
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