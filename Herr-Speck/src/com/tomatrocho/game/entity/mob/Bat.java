package com.tomatrocho.game.entity.mob;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.entity.HostileMob;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.level.Material;
import com.tomatrocho.game.level.World;
import com.tomatrocho.game.level.tile.Tile;
import com.tomatrocho.game.math.BoundingBox;
import com.tomatrocho.game.gfx.Art;
import com.tomatrocho.game.gfx.IAbstractBitmap;

public class Bat extends HostileMob {
	
	/**
	 * Max amount of health a {@link Bat} can have.
	 */
	protected static final float MAX_HEALTH = 50;
	
	
	/**
	 * Constructor for the {@link Bat} class.
	 * 
	 * @param level
	 * @param x
	 * @param y
	 */
	public Bat(World level, int x, int y) {
		super(level, x, y, Team.TEAM_2);
		
		this.health = MAX_HEALTH;
		this.speed = 1.5;
		
		bbs.put("body", new BoundingBox(this, -8, -8, 8, 8));
	}

	@Override
	public void tick() {
		super.tick();
		ticks++;
		
		dir += (HerrSpeck.random.nextDouble() - HerrSpeck.random.nextDouble()) * 0.5;
		handleMovement(Math.cos(dir) * speed, Math.sin(dir) * speed);
		
		final Material below = getMaterialBelow();
		if (below != null) {			
			if (below == Material.SANDSTONE_WALL)
				yShadowOffset = 12;
			else
				yShadowOffset = 28;
			
			if (below == Material.WATER)
				alphaShadow = 30;
			else
				alphaShadow = 60;
		}
	}
	
	@Override
	public int getDepthLine() {
		return (int) pos.y + getSprite().getH() * 2;
	}

	@Override
	public void render(IAbstractScreen screen) {
		super.render(screen);
		
		// health
		if (HerrSpeck.getDebugLevel() > 0)
			renderBubble(screen, health + "/" + MAX_HEALTH);
	}

	@Override
	public IAbstractBitmap getSprite() {
		return Art.bat[(ticks / 4) & 3][0];
	}
	
	@Override
	public Material getMaterialBelow() {
		final Tile tile = world.getTile((int) pos.x / Tile.W, (int) (pos.y + Tile.H / 2) / Tile.H);
		if (tile != null)
			return tile.getMaterial();
		
		return null;
	}
}
