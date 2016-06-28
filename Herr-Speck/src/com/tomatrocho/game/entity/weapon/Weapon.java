package com.tomatrocho.game.entity.weapon;

import com.tomatrocho.game.entity.Mob;

public abstract class Weapon {

	/**
	 * 
	 */
	protected Mob owner;
	
	
	/**
	 * 
	 * @param owner
	 */
	public Weapon(Mob owner) {
		this.owner = owner;
	}
	
	/**
	 * 
	 */
	public abstract void tick();
}
