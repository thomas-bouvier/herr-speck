package com.tomatrocho.game.entity;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tomatrocho.game.entity.mob.Bat;
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
	protected Map<String, BoundingBox> bbs = new HashMap<>();
	
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
		init(world, x, y);
		
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
		init(world, x, y);
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
			if (!removed)
				moved |= partMove(bbs, xa, 0);
			if (!removed)
				moved |= partMove(bbs, 0, ya);
			
			return moved;
		}
		else {
			boolean moved = true;
			if (!removed)
				moved &= partMove(bbs, xa, 0);
			if (!removed) 
				moved &= partMove(bbs, 0, ya);
			
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
		
		final BoundingBox from = getBoundingBox("body");
		
		BoundingBox closest = null;
		final double epsilon = 0.01;
		
		for (BoundingBox to : bbs) {
			if (from.intersects(to))
				continue;
			
			if (ya == 0) {
				if (to.y0 >= from.y1 || to.y1 <= from.y0)
					continue;
				
				if (xa > 0) {
					final double xrd = to.x0 - from.x1;
					
					// to is at right
					if (xrd >= 0 && xa > xrd) {
						closest = to;
						xa = xrd - epsilon;
						
						if (xa < 0)
							xa = 0;
					}
				}
				else if (xa < 0) {
					final double xld = from.x0 - to.x1;
					
					// to is at left
					if (xld >= 0 && -xa > xld) {
						closest = to;
						xa = -xld + epsilon;
						
						if (xa > 0)
							xa = 0;
					}
				}
			}
			
			if (xa == 0) {
				if (to.x0 >= from.x1 || to.x1 <= from.x0)
					continue;
				
				if (ya > 0) {
					final double yrd = to.y0 - from.y1;
					
					// to is down
					if (yrd >= 0 && ya > yrd) {
						closest = to;
						ya = yrd - epsilon;
						
						if (ya < 0)
							ya = 0;
					}
				}
				else if (ya < 0) {
					final double yld = from.y0 - to.y1;
					
					// to is up
					if (yld >= 0 && -ya > yld) {
						closest = to;
						ya = -yld + epsilon;
						
						if (ya > 0)
							ya = 0;
					}
				}
			}
		}
		
		if (closest != null && closest.getOwner() != null) {
			if (closest.getOwner() instanceof Bat) System.out.println("move()");
			closest.getOwner().handleCollision(from, xxa, yya);
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
	
	@Override
	public void handleCollision(BoundingBox bb, double xa, double ya) {
		final IBoundingBoxOwner bbOwner = bb.getOwner();
		if (blocks(bbOwner)) {
			collide(bbOwner, xa, ya);
			bbOwner.collide(this, -xa, -ya);
		}
	}
	
	@Override
	public boolean blocks(IBoundingBoxOwner bbOwner) {
		return isBlocking && bbOwner.isBlocking() && shouldBlock(bbOwner) && bbOwner.shouldBlock(this);
	}
	
	@Override
	public boolean shouldBlock(IBoundingBoxOwner bbOwner) {
		return true;
	}
	
	/**
	 * 
	 * @param xx0
	 * @param yy0
	 * @param xx1
	 * @param yy1
	 * @return
	 */
	public boolean intersects(double xx0, double yy0, double xx1, double yy1) {
		for (BoundingBox bb : bbs.values()) {
			if (bb.intersects(xx0, yy0, xx1, yy1))
				return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public BoundingBox getBoundingBox(String key) {
		if (bbs.containsKey(key)) {			
			final BoundingBox bb = bbs.get(key);
			return new BoundingBox(this, bb.x0 + pos.x, bb.y0 + pos.y, bb.x1 + pos.x, bb.y1 + pos.y);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public Vec2 getRadius() {
		int minX = 0, maxX = 0, minY = 0, maxY = 0;
		for (final BoundingBox bb : bbs.values()) {
			if (bb.x0 < minX)
				minX = (int) bb.x0;
			if (bb.x1 > maxX)
				maxX = (int) bb.x1;
			
			if (bb.y0 < minY)
				minY = (int) bb.y0;
			if (bb.y1 > maxY)
				maxY = (int) bb.y1;
			
		}
		
		return new Vec2((maxX - minX) / 2, (maxY - minY) / 2);
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract IAbstractBitmap getSprite();

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
		if (tile != null)
			return tile.getMaterial();
		
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
	public Map<String, BoundingBox> getBBs() {
		return bbs;
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