package com.tomatrocho.game.weapon;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.entity.Bullet;
import com.tomatrocho.game.entity.Mob;
import com.tomatrocho.game.entity.mob.Player;
import com.tomatrocho.game.math.Vec2;

public class Rifle extends Weapon {
	
	/**
	 * 
	 */
	protected float bulletDamage;
	
	/**
	 * 
	 */
	protected float accuracy;
	
	/**
	 * 
	 */
	protected final int shootDelay;
	
	/**
	 * 
	 */
	private int currentShootDelay = 0;
	
	/**
	 * 
	 */
	private boolean readyToShoot = true;
	
	
	/**
	 * 
	 * @param owner
	 */
	public Rifle(Mob owner) {
		super(owner);
		this.bulletDamage = 20f;
		this.accuracy = 0.1f;
		this.shootDelay = 6;
	}
	
	/**
	 * 
	 */
	public void fire() {
		if (readyToShoot) {
			double dir = getBulletDirection(accuracy);
			double xDir = Math.cos(dir);
			double yDir = Math.sin(dir);
			
			Bullet bullet = getAmmo(xDir, yDir);
			owner.getWorld().addEntity(bullet);
			
			if (owner instanceof Player) {
				final Player player = (Player) owner;
				
				if (HerrSpeck.random.nextInt(2) == 0) {				
					player.setMuzzleTicks(3);
					player.setMuzzlePosition(new Vec2(bullet.getX() + xDir - 7, bullet.getY() - 2 * yDir - 8));					
				}
				
				player.applyImpulse(xDir, yDir, 2);
			}
			
			currentShootDelay = shootDelay;
			readyToShoot = false;
		}
	}
	
	public void tick() {
		if (!readyToShoot) {
			if (currentShootDelay > 0) {
				currentShootDelay--;
			}
			else {
				readyToShoot = true;
			}
		}
	}
	
	/**
	 * 
	 * @param xd
	 * @param yd
	 * @return
	 */
	protected Bullet getAmmo(double xd, double yd) {
		if (owner instanceof Player) {
			Vec2 barrelOffsets = ((Player) owner).getBarrelOffsets();
			return new Bullet(owner.getWorld(), owner, owner.getX() + barrelOffsets.x, owner.getY() + barrelOffsets.y, xd, yd, bulletDamage);
		}
		
		return new Bullet(owner.getWorld(), owner, xd, yd, bulletDamage);
	}
	
	/**
	 * 
	 * @param accuracy
	 * @return
	 */
	protected double getBulletDirection(float accuracy) {
		return Math.atan2(owner.getAimVector().y, owner.getAimVector().x) - accuracy + Math.random() * accuracy;
	}
}
