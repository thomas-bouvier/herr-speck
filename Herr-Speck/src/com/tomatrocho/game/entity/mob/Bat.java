package com.tomatrocho.game.entity.mob;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.entity.Mob;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.level.Material;
import com.tomatrocho.game.level.World;
import com.tomatrocho.game.gfx.Art;
import com.tomatrocho.game.gfx.IAbstractBitmap;

public class Bat extends Mob {
	
	/**
	 * Constructor for the {@link Bat} class.
	 * 
	 * @param level
	 * @param x
	 * @param y
	 */
	public Bat(World level, int x, int y) {
		super(level, x, y, Team.TEAM_2);
		
		maxHealth = 50;
		health = maxHealth;
		speed = 1.5;
	}

	@Override
	public void tick() {
		super.tick();
		ticks++;
		
		dir += (HerrSpeck.random.nextDouble() - HerrSpeck.random.nextDouble()) * 0.5;
		handleMovement(Math.cos(dir) * speed, Math.sin(dir) * speed);
		
		final Material below = getMaterialBelow();
		if (below != null) {			
			if (below == Material.SANDSTONE_WALL) {
				yShadowOffset = 15;
			}
			else {
				yShadowOffset = 28;
			}
			
			if (below == Material.WATER) {
				alphaShadow = 30;
			}
			else {
				alphaShadow = 60;
			}
		}
	}
	
	public int getDepthLine() {
		return (int) pos.y + getSprite().getH() * 2;
	}

	@Override
	public void render(IAbstractScreen screen) {
		super.render(screen);
	}

	@Override
	public IAbstractBitmap getSprite() {
		return Art.bat[(ticks / 4) & 3][0];
	}
}
