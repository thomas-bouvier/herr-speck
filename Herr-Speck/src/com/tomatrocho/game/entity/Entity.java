package com.tomatrocho.game.entity;

import java.awt.Color;
import java.util.List;

import com.tomatrocho.game.entity.mob.Team;
import com.tomatrocho.game.gfx.IAbstractBitmap;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.gfx.IComparableDepth;
import com.tomatrocho.game.level.Material;
import com.tomatrocho.game.level.World;
import com.tomatrocho.game.level.tile.Tile;
import com.tomatrocho.game.math.BoundingBox;
import com.tomatrocho.game.math.IBoundingBoxOwner;
import com.tomatrocho.game.math.Vec2;

public abstract class Entity implements IComparableDepth, IBoundingBoxOwner {
	
	/**
	 * The {@link World} the {@link Entity} is linked with.
	 */
	protected World world;
	
	/**
	 * Coordinates of the {@link Entity}, in pixels.
	 */
	protected Vec2 pos = new Vec2();
	
	/**
	 * 
	 */
	protected double xd;
	
	/**
	 * 
	 */
	protected double yd;
	
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
		this.pos.x = x;
		this.pos.y = y;
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
		this.pos.x = x;
		this.pos.y = y;
	}
	
	/**
	 * 
	 * @param level
	 * @param x
	 * @param y
	 */
	public void init(World world, double x, double y) {
		this.world = world;
		this.pos.x = x;
		this.pos.y = y;
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
		}
		else {
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
				}
				else if (xa < 0) {
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
				}
				else if (ya < 0) {
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
			pos.x += xa;
			pos.y += ya;
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
		pos.x += xa;
		pos.y += ya;
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
	public abstract IAbstractBitmap getSprite();
	
	/**
	 * 
	 * @return
	 */
	public BoundingBox getBoundingBox() {
		return new BoundingBox(this, pos.x - radius.x, pos.y - radius.y, pos.x + radius.x, pos.y + radius.y);
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
	 * @return
	 */
	public Material getMaterialBelow() {
		final Tile tile = world.getTile((int) pos.x / Tile.W, (int) pos.y / Tile.H);
		if (tile != null) {
			return tile.getMaterial();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param x
	 */
	public void setX(double x) {
		this.pos.x = x;
	}
	
	/**
	 * 
	 * @param y
	 */
	public void setY(double y) {
		this.pos.y = y;
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
	 * Retrieve the coordinates of the {@link Entity}.
	 * 
	 * @return
	 * 		the coordinates of the {@link Entity}
	 */
	public Vec2 getPos() {
		return pos;
	}
	
	/**
	 * Retrieve the x coordinate of the {@link Entity}.
	 *
	 * @return
	 * 		the x coordinate of the {@link Entity}
     */
	public double getX() {
		return pos.x;
	}

	/**
	 * Retrieve the y coordinate of the {@link Entity}.
	 * 
	 * @return
	 * 		the y coordinate of the {@link Entity}
     */
	public double getY() {
		return pos.y;
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