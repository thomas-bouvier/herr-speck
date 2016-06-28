package com.tomatrocho.game.entity.animation;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.gfx.Art;
import com.tomatrocho.game.gfx.IAbstractBitmap;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.world.level.World;

public class MuzzleAnimation extends Animation {

	/**
	 * Constructor for the {@link MuzzleAnimation} class.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 */
	public MuzzleAnimation(World world, double x, double y, int duration) {
		super(world, x, y, duration);
		bitmap = Art.muzzle;
		radius.x = 6;
		radius.y = 6;
		numberFrames = bitmap.length * bitmap[0].length;
	}

	@Override
	public void render(IAbstractScreen screen) {
		if (HerrSpeck.random.nextInt(10) == 1) {
			final int frame = HerrSpeck.random.nextInt(numberFrames);
			final IAbstractBitmap sprite = bitmap[frame % bitmap.length][frame / bitmap.length];
			screen.blit(sprite, x - sprite.getW() / 2, y - sprite.getH() / 2);
		}
	}

	@Override
	public int getVerticalBaseCoordinate() {
		return (int) y + 6;
	}

	@Override
	public void collide(Entity entity, double xa, double ya) {
		
	}
}
