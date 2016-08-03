package com.tomatrocho.game.entity.mob;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.entity.Mob;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.gfx.Art;
import com.tomatrocho.game.gfx.IAbstractBitmap;
import com.tomatrocho.game.world.level.World;

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
		yShadowOffset = 28;
		alphaShadow = 60;
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
