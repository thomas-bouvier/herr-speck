package com.tomatrocho.game.level.tile;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.gfx.IAbstractBitmap;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.gfx.IComparableDepth;
import com.tomatrocho.game.level.Material;
import com.tomatrocho.game.level.World;
import com.tomatrocho.game.level.WorldUtils;
import com.tomatrocho.game.math.BoundingBox;
import com.tomatrocho.game.math.IBoundingBoxOwner;

public abstract class Tile implements IComparableDepth, IBoundingBoxOwner {

	/**
	 * Default width of {@link Tile} objects.
	 */
	public static final int W = 16;

	/**
	 * Default height of {@link Tile} objects.
	 */
	public static final int H = 16;
	
	/**
	 * Default id of the texture attached to the {@link Tile}.
	 */
	public static final int PLAIN_IMG = 47;
	
	/**
	 * The {@link Material} attached to the {@link Tile}.
	 */
	protected Material material;
	
	/**
	 * The {@link World} the {@link Tile} is linked with.
	 */
	protected World world;

	/**
	 * x coordinate of the {@link Tile}, in tiles.
	 */
	protected int x;

	/**
	 * y coordinate of the {@link Tile}, in tiles.
	 */
	protected int y;
	
	/**
	 * z coordinate of the {@link Tile}, in tiles.
	 */
	protected int z;
	
	/**
	 * 
	 */
	protected BoundingBox bb;

	/**
	 * Index of the sprite to use on the sheet.
	 */
	protected int img = 0;
	
	/**
	 * 
	 */
	protected boolean overlay = false;
	
	/**
	 * 
	 */
	protected List<Material> connectableWith = new ArrayList<>();

	/**
	 * 
	 */
	protected Material neighbour;
	
	
	/**
	 * Default constructor for the {@link Tile} class.
     */
	public Tile() {
		
	}
	
	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 */
	public void init(World world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Computes a unique number for the {@link Tile}'s environment.
	 * Considers adjacent and corner {@link Tile} objects only.
	 *
	 *  1 | 2 | 4
	 *  8 |   |16
	 *  32|64 |128
	 */
	public void applyBitmask() {
		if (isConnectable()) {
			int res = 0, pow = 1;
			for (final List<Tile> tiles : world.getMooreNeighborhoodTiles(this)) {
				if (tiles != null && tiles.size() > z)
					res += pow * (isConnectableWith(tiles.get(z)) ? 1 : 0);
				pow *= 2;
			}
			
			this.img = WorldUtils.getIdForBitmaskValue(res);
		}
	}
	
	/**
	 * 
	 */
	public void drawDepthLine(IAbstractScreen screen) {
		final int w = Tile.W;
		final int h = 1;
		
		IAbstractBitmap sprite = screen.createBitmap(w, h);
		for (int x = 0; x <= w; x++) {
			for (int y = 0; y <= h; y++) {
				if (x == 0 || x == w - 1 || y == 0|| y == h - 1)
					sprite.setPixel(y * w + x, Color.LIGHT_GRAY.getRGB());
			}
		}
		
		screen.blit(sprite, x * Tile.W, getDepthLine());
	}
	
	/**
	 *
	 * @param tile
	 */
	public void neighbourChanged(Tile tile) {
		
	}

	/**
	 * 
	 * @param bbs
	 * @param entity
	 */
	public void addClipBoundingBoxes(List<BoundingBox> bbs, Entity entity) {
		if (!shouldBlock(entity))
			return;
		
		bbs.add(bb);
	}

	@Override
	public void handleCollision(BoundingBox bb, double xa, double ya) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collide(IBoundingBoxOwner bbOwner, double xa, double ya) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isBlocking() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldBlock(IBoundingBoxOwner bbOwner) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean blocks(IBoundingBoxOwner bbOwner) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 
	 * @param bb
	 */
	public void setBoundingBox(BoundingBox bb) {
		this.bb = bb;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean overlay() {
		return overlay;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isConnectable() {
		return false;
	}
	
	/**
	 * 
	 * @param tile
	 * @return
	 */
	public boolean isConnectableWith(Tile tile) {
		if (connectableWith.contains(tile.getMaterial()))
			return true;
		
		return material == tile.getMaterial();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getDepthLine() {
		return y * Tile.H + Tile.H;
	}
	
	/**
	 * 
	 * @return
	 */
	public Material getMaterial() {
		return material;
	}
	
	/**
	 * 
	 * @return
	 */
	public Material getNeighbourMaterial() {
		return neighbour;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getZ() {
		return z;
	}
	
	/**
	 * 
	 * @return
	 */
	public BoundingBox getBoundingBox() {
		return bb;
	}
	
	@Override
	public String toString() {
		return getClass() + ", x: " + x + ", y: " + y + ", z: " + z;
	}
}