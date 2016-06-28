package com.tomatrocho.game.world.level;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.entity.mob.Bat;
import com.tomatrocho.game.entity.predicates.EntityIntersectsBB;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.gfx.Art;
import com.tomatrocho.game.gfx.IComparableDepth;
import com.tomatrocho.game.gfx.DepthComparator;
import com.tomatrocho.game.gfx.IAbstractBitmap;
import com.tomatrocho.game.math.BoundingBox;
import com.tomatrocho.game.math.IBoundingBoxPredicate;
import com.tomatrocho.game.math.Vec2;
import com.tomatrocho.game.world.tile.SandstoneTile;
import com.tomatrocho.game.world.tile.StoneTile;
import com.tomatrocho.game.world.tile.Tile;

import java.util.*;

public class World {

	/**
	 * Name of the {@link World}.
	 */
	private String name;
	
	/**
	 * Width of the generated area of the {@link World}, in tiles.
	 */
	private int w;

	/**
	 * Height of the generated area of the {@link World}, in tiles.
	 */
	private int h;

	/**
	 * {@link List} containing the {@link Tile} objects of this {@link World}.
	 */
	private List<LinkedList<Tile>> tiles = new ArrayList<>();
	
	/**
	 * 
	 */
	private boolean[] seen;

	/**
	 * {@link List} containing the {@link Entity} objects of this {@link World}.
	 */
	private List<Entity> entities = new ArrayList<>();
	
	/**
	 * 
	 */
	private List<List<Entity>> entityMap = new ArrayList<>();
	
	/**
	 *
	 */
	private static int[] neighbourOffsets;

	/**
	 * Time of the {@link World}.
	 */
	private int time = 0;
	
	/**
	 * 
	 */
	private boolean day = false;
	
	/**
	 * 
	 */
	private boolean night = false;
	
	/**
	 * 
	 */
	private int brightnessLevel = 0;


	/**
	 * Constructor for the {@link World} class.
	 * Creates a {@link World} of dimensions w, h and generates it.
	 *
	 * @param w
	 * 		width of the created {@link World}, in tiles
	 * @param h
	 * 		height of the created {@link World}, in tiles
     */
	public World(String name, int w, int h) {
		this.name = name;
		this.w = w;
		this.h = h;
		initNeighbourOffsets();
		initTileMap();
		for (int i = 0; i < w * h; i++) {
			entityMap.add(new ArrayList<>());
		}
		seen = new boolean[(w + 1) * (h + 1)];
	}

	/**
	 * Initializes the relative offset to be add to the the index of a {@link Tile}
	 * to retrieve adjacent {@link Tile} objects.
	 */
	private void initNeighbourOffsets() {
		neighbourOffsets = new int[] {-w - 1, -w, -w + 1, -1, 1, w - 1, w, w + 1};
	}

	/**
	 *
	 */
	private void initTileMap() {
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				tiles.add(new LinkedList<>());
			}
		}
	}
	
	/**
	 * 
	 */
	public void validateTileMap() {
		// empty tiles
        for (int y = 0; y < h; y++) {
        	for(int x = 0; x < w; x++) {
        		if (getTiles(x, y).isEmpty()) {
        			addTile(x, y, new StoneTile());
        		}
        	}
        }
        //
        for (int y = 0; y < h; y++) {
        	for (int x = 0; x < w; x++) {
        		final Tile tile = getTiles(x, y).get(0);
        		for (final List<Tile> tiles : getAdjacentTiles(tile)) {
    				if (tiles != null) {
    					final Tile adjacentTile = tiles.get(0);
    					if (tile.getNeighbourMaterial() != null) {
    						if (adjacentTile.getMaterial() != tile.getMaterial() && adjacentTile.getMaterial() != tile.getNeighbourMaterial()) {
    							setTile(adjacentTile.getX(), adjacentTile.getY(), new SandstoneTile());
    						}
    					}
    				}
    			}
        	}
        }
	}
	
	/**
	 * 
	 */
	public void computeTileConnections() {
		WorldUtils.loadBitmaskingValues();
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				final List<Tile> tiles = this.tiles.get(y * w + x);
				for (int z = 0; z < tiles.size(); z++) {
					tiles.get(z).applyBitmask();
				}
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<List<Tile>> getAdjacentTiles(final Tile tile) {
		List<List<Tile>> tiles = new ArrayList<>();
		for (int offset : World.getNeighbourOffsets()) {
			tiles.add(getTiles(tile.getY() * w + tile.getX() + offset));
		}
		return tiles;
	}

	/**
	 * Adds an {@link Entity} to this {@link World}.
	 *
	 * @param entity
	 * 		the {@link Entity} to add to this {@link World}
	 */
	public void addEntity(Entity entity) {
		entities.add(entity);
		addToEntityMap(entity);
	}
	
	/**
	 * Adds an {@link Entity} to the entity map of the {@link World}.
	 * 
	 * @param entity
	 * 		 the {@link Entity} to add to the entity map of this {@link World}
	 */
	private void addToEntityMap(Entity entity) {
		entity.setXTo((int) (entity.getX() - entity.getRadius().x) / Tile.W);
		entity.setYTo((int) (entity.getY() - entity.getRadius().y) / Tile.H);
		final int x1 = entity.getXTo() + (int) (entity.getRadius().x * 2 + 1) / Tile.W;
		final int y1 = entity.getYTo() + (int) (entity.getRadius().y * 2 + 1) / Tile.H;
		for (int y = entity.getYTo(); y <= y1; y++) {
			if (y < 0 || y >= h) {
				continue;
			}
			for (int x = entity.getXTo(); x <= x1; x++) {
				if (x < 0 || x >= w) {
					continue;
				}
				entityMap.get(y * w + x).add(entity);
			}
		}
	}
	
	/**
	 * 
	 * @param entity
	 */
	private void updateEntityMap(Entity entity) {
		removeFromEntityMap(entity);
		addToEntityMap(entity);
	}
	
	/**
	 * 
	 * @param entity
	 */
	private void removeFromEntityMap(Entity entity) {
		final int x1 = entity.getXTo() + (int) (entity.getRadius().x * 2 + 1) / Tile.W;
		final int y1 = entity.getYTo() + (int) (entity.getRadius().y * 2 + 1) / Tile.H;
		for (int y = entity.getYTo(); y <= y1; y++) {
			if (y < 0 || y >= h) {
				continue;
			}
			for (int x = entity.getXTo(); x <= x1; x++) {
				if (x < 0 || x >= w) {
					continue;
				}
				entityMap.get(y * w + x).remove(entity);
			}
		}
	}
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	public List<BoundingBox> getClipBoundingBoxes(Entity entity) {
		List<BoundingBox> bbs = new ArrayList<>();
		BoundingBox bb = entity.getBoundingBox().grow(Tile.W);
		// computing corner pins
		int x0 = (int) (bb.x0 / Tile.W);
		int y0 = (int) (bb.y0 / Tile.H);
		int x1 = (int) (bb.x1 / Tile.W);
		int y1 = (int) (bb.y1 / Tile.H);
		// adding bounding boxes from tiles
		for (int y = y0; y <= y1; y++) {
			if (y < 0 || y >= h) {
				continue;
			}
			for (int x = x0; x <= x1; x++) {
				if (x < 0 || x >= w) {
					continue;
				}
				getTile(x, y).addClipBoundingBoxes(bbs, entity);
			}
		}
		// adding bounding boxes from other entities
		for (Entity e : getEntities(bb)) {
			if (e != entity && e.blocks(entity)) {
				bbs.add(e.getBoundingBox());
			}
		}
		return bbs;
	}
	
	/**
	 * Renders this entire {@link World} to the {@link IAbstractScreen}.
	 *
	 * @param screen
	 * 		{@link IAbstractScreen} to render this {@link World} on
	 * @param xScroll
	 *		offset on x axis
	 * @param yScroll
	 *		offset on y axis
	 */
	public void render(IAbstractScreen screen, int xScroll, int yScroll) {
		// computing corner pins
		int x0 = (xScroll) / Tile.W;
		int y0 = (yScroll) / Tile.H;
		int x1 = (xScroll + screen.getW()) / Tile.W;
		int y1 = (yScroll + screen.getH()) / Tile.H + Tile.H;
		if (xScroll < 0) {
			x0--;
		}
		if (yScroll < 0) {
			y0--;
		}
		// setting offsets equal to xScroll, yScroll
		screen.setOffset(xScroll, yScroll);
		// level rendering
		Set<IComparableDepth> objectsToRender = new TreeSet<>(new DepthComparator());
		// adding entities to render list
		entities.stream().forEach(entity -> objectsToRender.add(entity));
		// adding tiles to render list
		for (int y = y0; y <= y1; y++) {
			for (int x = x1; x >= x0; x--) {
				if (x < 0 || x >= w || y < 0 || y >= h) {
					screen.blit(Art.stoneTiles[0][0], x * Tile.W, y * Tile.H);
					continue;
				}
				final List<Tile> tiles = this.tiles.get(y * w + x);
				for (int i = 0; i < tiles.size(); i++) {
					objectsToRender.add(tiles.get(i));
				}
			}
		}
		// objects rendering
		objectsToRender.stream().forEach(object -> object.render(screen));
		// rendering bounding boxes
		if (HerrSpeck.debug()) {			
			renderBoundingBoxes(screen);
		}
		// reseting offset
		screen.setOffset(0, 0);
	}
	
	/**
	 * 
	 * @param screen
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 */
//	private void renderDarkness(IAbstractScreen screen, int x0, int y0, int x1, int y1) {
//		for (int y = y0; y <= y1; y++) {
//			if (y < 0 || y >= h) {
//				continue;
//			}
//			for (int x = x0; x <= x1; x++) {
//				if (x < 0 || x >= w) {
//					continue;
//				}
//				boolean unseenC0 = !seen[y * (w + 1) + x];
//				boolean unseenC1 = !seen[y * (w + 1) + (x + 1)];
//				boolean unseenC2 = !seen[(y + 1) * (w + 1) + x];
//				boolean unseenC3 = !seen[(y + 1) * (w + 1) + (x + 1)];
//				if (!(unseenC0 || unseenC1 || unseenC2 || unseenC3)) {
//					continue;
//				}
//				int count = 0;
//				if (unseenC0) count++;
//				if (unseenC1) count++;
//				if (unseenC2) count++;
//				if (unseenC3) count++;
//				switch (count) {
//				case 1 :
//					if (unseenC0) screen.blit(Art.darkness[2][2], x * Tile.W, y * Tile.H - Tile.H / 2);
//                    if (unseenC1) screen.blit(Art.darkness[0][2], x * Tile.W, y * Tile.H - Tile.H / 2);
//                    if (unseenC2) screen.blit(Art.darkness[2][0], x * Tile.W, y * Tile.H - Tile.H / 2);
//                    if (unseenC3) screen.blit(Art.darkness[0][0], x * Tile.W, y * Tile.H - Tile.H / 2);
//					break;
//				case 2 :
//					if (unseenC0 && unseenC3) screen.blit(Art.darkness[2][4], x * Tile.W, y * Tile.H - Tile.H / 2);
//                    if (unseenC1 && unseenC2) screen.blit(Art.darkness[2][3], x * Tile.W, y * Tile.H - Tile.H / 2);
//                    if (unseenC0 && unseenC1) screen.blit(Art.darkness[1][2], x * Tile.W, y * Tile.H - Tile.H / 2);
//                    if (unseenC2 && unseenC3) screen.blit(Art.darkness[1][0], x * Tile.W, y * Tile.H - Tile.H / 2);
//                    if (unseenC0 && unseenC2) screen.blit(Art.darkness[2][1], x * Tile.W, y * Tile.H - Tile.H / 2);
//                    if (unseenC1 && unseenC3) screen.blit(Art.darkness[0][1], x * Tile.W, y * Tile.H - Tile.H / 2);
//					break;
//				case 3 :
//					if (!unseenC0) screen.blit(Art.darkness[1][4], x * Tile.W, y * Tile.H - Tile.H / 2);
//					if (!unseenC1) screen.blit(Art.darkness[0][4], x * Tile.W, y * Tile.H - Tile.H / 2);
//					if (!unseenC2) screen.blit(Art.darkness[1][3], x * Tile.W, y * Tile.H - Tile.H / 2);
//					if (!unseenC3) screen.blit(Art.darkness[0][3], x * Tile.W, y * Tile.H - Tile.H / 2);
//					break;
//				case 4 :
//					screen.blit(Art.darkness[1][1], x * Tile.W, y * Tile.H - Tile.H / 2);
//					break;
//				default :
//					break;
//				}
//			}
//		}
//	}
	
	/**
	 * 
	 * @param screen
	 */
	public void renderBoundingBoxes(IAbstractScreen screen) {
		for (Entity entity : entities) {
			final BoundingBox bb = entity.getBoundingBox();
			final int w = (int) (bb.x1 - bb.x0);
			final int h = (int) (bb.y1 - bb.y0);
			IAbstractBitmap sprite = screen.createBitmap(w, h);
			for (int x = 0; x <= w; x++) {
				for (int y = 0; y <= h; y++) {
					if (x == 0 || x == w - 1 || y == 0|| y == h - 1) {
						sprite.setPixel(y * w + x, entity.getTeam().getCol());
					}
				}
			}
			screen.blit(sprite, bb.x0, bb.y0);
		}
	}
	
	/**
	 * 
	 * @param bb
	 * @return
	 */
	public Set<Entity> getEntities(BoundingBox bb) {
		return getEntities(bb.x0, bb.y0, bb.x1, bb.y1);
	}
	
	/**
	 * 
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @return
	 */
	public Set<Entity> getEntities(double x0, double y0, double x1, double y1) {
		return getEntities(x0, y0, x1, y1, EntityIntersectsBB.INSTANCE);
	}
	
	/**
	 * 
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param predicate
	 * @return
	 */
	public Set<Entity> getEntities(double x0, double y0, double x1, double y1, IBoundingBoxPredicate<Entity> predicate) {
		final int xx0 = Math.max((int) (x0) / Tile.W, 0);
		final int yy0 = Math.max((int) (y0) / Tile.H, 0);
		final int xx1 = Math.min((int) (x1) / Tile.W, w - 1);
		final int yy1 = Math.min((int) (y1) / Tile.H, h - 1);
		final Set<Entity> ret = new TreeSet<Entity>(new EntityComparator());
		for (int y = yy0; y <= yy1; y++) {
			for (int x = xx0; x <= xx1; x++) {
				for (Entity entity : entityMap.get(y * w + x)) {
					if (predicate.appliesTo(entity, x0, y0, x1, y1)) {
						ret.add(entity);
					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * 
	 */
	public void tick() {
		//TODO spawners
		if (HerrSpeck.random.nextInt(250) == 0) {
			addEntity(new Bat(this, time % (w * 16), time % (h * 16)));
		}
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (!entity.removed()) {
				entity.tick();
				// updating entity map
				final int xTo = ((int) (entity.getX() - entity.getRadius().x)) / Tile.W;
				final int yTo = ((int) (entity.getY() - entity.getRadius().y)) / Tile.H;
				if (xTo != entity.getXTo() || yTo != entity.getYTo()) {
					updateEntityMap(entity);
				}
			}
			if (entity.removed()) {
				entities.remove(i--);
				removeFromEntityMap(entity);
			}
		}
		// updating time
		time++;
		if (time % 10 == 0) {
			time();
		}
		if (time > 1000000) {
			time = 0;
		}
	}
	
	/**
	 * Updates the {@link Tile} at the specified location.
	 *
	 * @param x
	 * 		x coordinate of the {@link Tile} to update, in tiles
	 * @param y
	 * 		y coordinate of the {@link Tile} to update, in tiles
	 * @param tile
	 * 		the {@link Tile} to update
	 */
	public void updateTiles(int x, int y, Tile tile) {
		for (int offset : neighbourOffsets) {
			final int index = y * w + x + offset;
			if (index >= 0 && index < w * h) {
				final Tile neighbour = tiles.get(index).peekLast();
				if (neighbour != null) {
					//neighbour.neighbourUpdated(tile);
				}
			}
		}
	}
	
	/**
	 * 
	 */
	private void time() {
		if (brightnessLevel <= -125) {
			night = true;
			day = false;
		}
		if (brightnessLevel >= 100) {
			night = false;
			day = true;
		}
		if (night) {
			brightnessLevel++;
			return;
		}
		if (day) {
			brightnessLevel--;
			return;
		}
		brightnessLevel++;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean hasSeen(int x, int y) {
		return seen[y * (w + 1) + x] || seen[(y + 1) * (w + 1) + x] || seen[y * (w + 1) + (x + 1)] || seen[(y + 1) * (w + 1) + (x + 1)];
	}
	
	/**
	 * 
	 * @param pos
	 * @param radius
	 */
	public void reveal(Vec2 pos, int radius) {
		reveal((int) pos.x, (int) pos.y, radius);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param radius
	 */
	public void reveal(int x, int y, int radius) {
		for (int i = 0; i <= 2 * radius; i++) {
			revealLine(x, y, x - radius + i, y - radius, radius);
			revealLine(x, y, x - radius + i, y + radius, radius);
			revealLine(x, y, x - radius, y - radius + i, radius);
			revealLine(x, y, x + radius, y - radius + i, radius);
		}
	}
	
	/**
	 * 
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param radius
	 */
	public void revealLine(int x0, int y0, int x1, int y1, int radius) {
		for (int i = 0; i <= radius; i++) {
			int xx = x0 + (x1 - x0) * i / radius;
			int yy = y0 + (y1 - y0) * i / radius;
			if (xx < 0 || xx >= w || yy < 0 || yy >= h) {
				return;
			}
			seen[yy * (w + 1) + xx] = seen[(yy + 1) * (w + 1) + xx] = seen[yy * (w + 1) + (xx + 1)] = seen[(yy + 1) * (w + 1) + (xx + 1)] = true;
		}
	}

	/**
	 * Adds the {@link Tile} at the specified location.
	 *
	 * @param x
	 * 		x coordinate of the {@link Tile} to set, in tiles
	 * @param y
	 * 		y coordinate of the {@link Tile} to set, in tiles
	 * @param tile
	 * 		the {@link Tile} to insert
	 */
	public void addTile(int x, int y, Tile tile) {
		if (x < 0 || x >= w || y < 0 || y >= h) {
			return;
		}
		tile.init(this, x, y, tiles.get(y * w + x).size());
		tiles.get(y * w + x).add(tile);
	}
	
	/**
	 * Sets the {@link Tile} at the specified location.
	 *
	 * @param x
	 * 		x coordinate of the {@link Tile} to set, in tiles
	 * @param y
	 * 		y coordinate of the {@link Tile} to set, in tiles
	 * @param tile
	 * 		the {@link Tile} to insert
	 */
	public void setTile(int x, int y, Tile tile) {
		if (x < 0 || x >= w || y < 0 || y >= h) {
			return;
		}
		tiles.get(y * w + x).clear();
		tile.init(this, x, y, tiles.get(y * w + x).size());
		tiles.get(y * w + x).add(tile);
	}

	/**
	 * Retrieves the {@link Tile} at the specified location.
	 *
	 * @param x
	 * 		x coordinate of the {@link Tile} to retrieve, in tiles
	 * @param y
	 * 		y coordinate of the {@link Tile} to retrieve, in tiles
	 * @return
	 * 		the {@link Tile} to retrieve
	 */
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= w || y < 0 || y >= h) {
			return null;
		}
		return tiles.get(y * w + x).peekLast();
	}

	/**
	 * Retrieves the {@link Tile} at the specified index.
	 *
	 * @param pos
	 * 		index of the {@link Tile} to retrieve
	 * @return
	 * 		the {@link Tile} to retrieve
	 */
	public Tile getTile(int pos) {
		return getTile(pos % w, pos / h);
	}
	
	/**
	 * Retrieves the {@link Tile} list at the specified location.
	 *
	 * @param x
	 * 		x coordinate of the {@link Tile} list to retrieve, in tiles
	 * @param y
	 * 		y coordinate of the {@link Tile} list to retrieve, in tiles
	 * @return
	 * 		the {@link Tile} to retrieve
	 */
	public List<Tile> getTiles(int x, int y) {
		if (x < 0 || x >= w || y < 0 || y >= h) {
			return null;
		}
		return tiles.get(y * w + x);
	}
	
	/**
	 * Retrieves the {@link Tile} list at the specified index.
	 *
	 * @param pos
	 * 		index of the {@link Tile} to retrieve
	 * @return
	 * 		the {@link Tile} to retrieve
	 */
	public List<Tile> getTiles(int pos) {
		return getTiles(pos % w, pos / h);
	}
	
	/**
	 * 
	 * @return
	 */
	public static int[] getNeighbourOffsets() {
		return neighbourOffsets;
	}

	/**
	 * Converts location from world space to tile space.
	 *
	 * @param pos
	 * 		the location where to retrieve the {@link Tile}, in pixels
	 * @return
	 * 		the location of the {@link Tile}, in tiles
	 */
	public static Vec2 getTileFromPosition(Vec2 pos) {
		return new Vec2((int) pos.x / Tile.W, (int) pos.y / Tile.H);
	}

	/**
	 * Converts location from tile space to world space.
	 *
	 * @param x
	 * 		x coordinate of the {@link Tile}, in tiles
	 * @param y
	 * 		y coordinate of the {@link Tile}, in tiles
	 * @return
	 * 		the centered location of the {@link Tile}, in pixels
	 */
	public static Vec2 getPositionFromTile(int x, int y) {
		return new Vec2(x * Tile.W + (Tile.W / 2), y * Tile.H + (Tile.H / 2));
	}

	/**
	 * Retrieves the name of the {@link World}.
	 * 
	 * @return
	 * 		the name of this {@link World}
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retrieves the width of the {@link World}.
	 *
	 * @return
	 * 		the width of this {@link World}, in tiles
	 */
	public int getW() {
		return w;
	}

	/**
	 * Retrieves the height of the {@link World}.
	 *
	 * @return
	 * 		the height of this {@link World}, in tiles
	 */
	public int getH() {
		return h;
	}

	/**
	 * Retrieves the brightness level of this {@link World}.
	 *
	 * @return
	 * 		the brightness level of this {@link World}
	 */
	public int getBrightnessLevel() {
		return brightnessLevel;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Entity> getEntities() {
		return entities;
	}
	
	@Override
	public String toString() {
		return String.format("%sx%s", w, h);
	}
}