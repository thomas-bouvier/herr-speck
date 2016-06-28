package com.tomatrocho.game.entity;

import java.util.List;

import com.tomatrocho.game.entity.mob.Team;
import com.tomatrocho.game.gfx.IComparableDepth;
import com.tomatrocho.game.math.BoundingBox;
import com.tomatrocho.game.math.IBoundingBoxOwner;
import com.tomatrocho.game.math.Vec2;
import com.tomatrocho.game.world.level.World;

public abstract class Entity implements IComparableDepth, IBoundingBoxOwner {
	
	/**
	 * The {@link World} the {@link Entity} is linked with.
	 */
	protected World world;

	/**
	 * x coordinate of the {@link Entity}, in pixels.
	 */
	protected double x;

	/**
	 * y coordinate of the {@link Entity}, in pixels.
	 */
	protected double y;
	
	/**
	 * 
	 */
	protected int xTo;
	
	/**
	 * 
	 */
	protected int yTo;
	
	/**
	 * 
	 */
	protected Vec2 radius = new Vec2(10, 10);
	
	/**
	 * Whether this {@link Entity} is removed from the {@link World}.
	 */
	protected boolean removed = false;
	
	/**
	 * 
	 */
	protected boolean isBlocking = true;
	
	/**
	 * 
	 */
	protected Team team = Team.NEUTRAL;

	
	/**
	 * Constructor for the {@link Entity} class.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 */
	public Entity(World world, double x, double y, Team team) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.team = team;
	}
	
	/**
	 * Constructor for the {@link Entity} class.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 */
	public Entity(World world, double x, double y) {
		this.world = world;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 
	 * @param level
	 * @param x
	 * @param y
	 */
	public void init(World world, double x, double y) {
		this.world = world;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean forceRender() {
		return false;
	}

	/**
	 *
	 */
	public abstract void tick();
	
	/**
	 * 
	 * @param xa
	 * @param ya
	 * @return
	 */
	protected boolean move(double xa, double ya) {
		List<BoundingBox> bbs = world.getClipBoundingBoxes(this);
		if (xa == 0 || ya == 0) {
			boolean moved = false;
			if (!removed) {
				moved |= partMove(bbs, xa, 0);
			}
			if (!removed) {
				moved |= partMove(bbs, 0, ya);
			}
			return moved;
		} else {
			boolean moved = true;
			if (!removed) {
				moved &= partMove(bbs, xa, 0);
			}
			if (!removed) {
				moved &= partMove(bbs, 0, ya);
			}
			return moved;
		}
	}
	
	/**
	 * 
	 * @param bbs
	 * @param xa
	 * @param ya
	 * @return
	 */
	protected boolean partMove(List<BoundingBox> bbs, double xa, double ya) {
		double xxa = xa;
		double yya = ya;
		final BoundingBox from = getBoundingBox();
		BoundingBox closest = null;
		final double epsilon = 0.01;
		for (BoundingBox to : bbs) {
			if (from.intersects(to)) {
				continue;
			}
			if (ya == 0) {
				if (to.y0 >= from.y1 || to.y1 <= from.y0) {
					continue;
				}
				if (xa > 0) {
					final double xrd = to.x0 - from.x1;
					// to is at right
					if (xrd >= 0 && xa > xrd) {
						closest = to;
						xa = xrd - epsilon;
						if (xa < 0) {
							xa = 0;
						}
					}
				} else if (xa < 0) {
					final double xld = from.x0 - to.x1;
					// to is at left
					if (xld >= 0 && -xa > xld) {
						closest = to;
						xa = -xld + epsilon;
						if (xa > 0) {
							xa = 0;
						}
					}
				}
			}
			if (xa == 0) {
				if (to.x0 >= from.x1 || to.x1 <= from.x0) {
					continue;
				}
				if (ya > 0) {
					final double yrd = to.y0 - from.y1;
					// to is down
					if (yrd >= 0 && ya > yrd) {
						closest = to;
						ya = yrd - epsilon;
						if (ya < 0) {
							ya = 0;
						}
					}
				} else if (ya < 0) {
					final double yld = from.y0 - to.y1;
					// to is up
					if (yld >= 0 && -ya > yld) {
						closest = to;
						ya = -yld + epsilon;
						if (ya > 0) {
							ya = 0;
						}
					}
				}
			}
		}
		if (closest != null && closest.getOwner() != null) {
			closest.getOwner().handleCollision(this, xxa, yya);
		}
		if (xa != 0 || ya != 0) {
			x += xa;
			y += ya;
			return true;
		}
		return false;
	}
	
	/**
	 *
	 * @param xa
	 * @param ya
    */
	protected void handleMovement(double xa, double ya) {
		x += xa;
		y += ya;
	}
	
	/**
	 * 
	 */
	public void handleCollision(Entity entity, double xa, double ya) {
		if (blocks(entity)) {
			collide(entity, xa, ya);
			entity.collide(this, -xa, -ya);
		}
	}
	
	/**
	 * 
	 * @param entity
	 * @param xa
	 * @param ya
	 */
	public abstract void collide(Entity entity, double xa, double ya);
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	public boolean blocks(Entity entity) {
		return isBlocking && entity.isBlocking() && shouldBlock(entity) && entity.shouldBlock(this);
	}
	
	/**
	 * 
	 * @param x0
	 * @param x1
	 * @param y0
	 * @param y1
	 * @return
	 */
	public boolean intersects(double x0, double y0, double x1, double y1) {
		return getBoundingBox().intersects(x0, y0, x1, y1);
	}
	
	/**
	 * 
	 * @return
	 */
	public BoundingBox getBoundingBox() {
		return new BoundingBox(this, x - radius.x, y - radius.y, x + radius.x, y + radius.y);
	}
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	protected boolean shouldBlock(Entity entity) {
		return true;
	}

	/**
	 *
	 */
	public void remove() {
		removed = true;
	}
	
	/**
	 * 
	 * @param x
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * 
	 * @param y
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * 
	 * @param xTo
	 */
	public void setXTo(int xTo) {
		this.xTo = xTo;
	}
	
	/**
	 * 
	 * @param yTo
	 */
	public void setYTo(int yTo) {
		this.yTo = yTo;
	}
	
	/**
	 *
	 * @return
   */
	public World getWorld() {
		return world;
	}
	
	/**
	 * Retrieves the x coordinate of the {@link DrawableObject}.
	 *
	 * @return
	 * 		the x coordinate of the {@link DrawableObject}
   */
	public double getX() {
		return x;
	}

	/**
	 * Retrieves the y coordinate of the {@link DrawableObject}.
	 * 
	 * @return
	 * 		the y coordinate of the {@link DrawableObject}
   */
	public double getY() {
		return y;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getXTo() {
		return xTo;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getYTo() {
		return yTo;
	}
	
	/**
	 * 
	 * @return
	 */
	public Vec2 getRadius() {
		return radius;
	}

	/**
	 *
	 * @return
     */
	public boolean removed() {
		return removed;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isBlocking() {
		return isBlocking;
	}
	
	/**
	 * 
	 * @return
	 */
	public Team getTeam() {
		return team;
	}
}