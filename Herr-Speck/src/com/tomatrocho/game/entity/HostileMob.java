package com.tomatrocho.game.entity;

import com.tomatrocho.game.entity.mob.Team;
import com.tomatrocho.game.level.World;

public abstract class HostileMob extends Mob {

	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param team
	 */
	public HostileMob(World world, int x, int y, Team team) {
		super(world, x, y, team);
	}
}
