package com.tomatrocho.game.world.tile;

import java.util.ArrayList;
import java.util.List;

import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.gfx.IComparableDepth;
import com.tomatrocho.game.math.BoundingBox;
import com.tomatrocho.game.math.IBoundingBoxOwner;
import com.tomatrocho.game.world.level.Material;
import com.tomatrocho.game.world.level.World;
import com.tomatrocho.game.world.level.WorldUtils;

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
	 * Index of the sprite to use on the sheet.
	 */
	protected int img;
	
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
		img = 0;
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
			for (final List<Tile> tiles : world.getAdjacentTiles(this)) {
				if (tiles != null && tiles.size() > z) {
					res += pow * (isConnectableWith(tiles.get(z)) ? 1 : 0);
				}
				pow *= 2;
			}
			this.img = WorldUtils.getIdForBitmaskValue(res);
		}
	}
	

	@Override
	public boolean forceRender() {
		return false;
	}
	
	/**
	 *
	 * @param tile
	 */
	public abstract void neighbourChanged(Tile tile);

	/**
	 * 
	 * @param bbs
	 * @param entity
	 */
	public void addClipBoundingBoxes(List<BoundingBox> bbs, Entity entity) {
		if (canPass(entity)) {
			return;
		}
		bbs.add(new BoundingBox(this, x, y, x + Tile.W, y + Tile.H));
	}
	
	/**
	 *
	 * @param entity
	 * @return
     */
	public boolean canPass(Entity entity) {
		return true;
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
		if (connectableWith.contains(tile.getMaterial())) {
			return true;
		}
		return material == tile.getMaterial();
	}
	
	@Override
	public int getVerticalBaseCoordinate() {
		return y + Tile.H;
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
	
	@Override
	public String toString() {
		return getClass() + ", x: " + x + ", y: " + y + ", z: " + z;
	}
}