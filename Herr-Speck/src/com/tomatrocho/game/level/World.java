package com.tomatrocho.game.level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.entity.Light;
import com.tomatrocho.game.entity.Mob;
import com.tomatrocho.game.entity.mob.Bat;
import com.tomatrocho.game.entity.predicates.EntityIntersectsBB;
import com.tomatrocho.game.gfx.Art;
import com.tomatrocho.game.gfx.DepthComparator;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.gfx.IComparableDepth;
import com.tomatrocho.game.gfx.LightScreen;
import com.tomatrocho.game.gfx.SceneScreen;
import com.tomatrocho.game.level.tile.SandstoneTile;
import com.tomatrocho.game.level.tile.StoneTile;
import com.tomatrocho.game.level.tile.Tile;
import com.tomatrocho.game.math.BoundingBox;
import com.tomatrocho.game.math.IBoundingBoxPredicate;
import com.tomatrocho.game.math.Vec2;

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
	private Vec2 spawnLocation;

	/**
	 * {@link List} containing the {@link Entity} objects of this {@link World}.
	 */
	private List<Entity> entities = new ArrayList<>();
	
	/**
	 * 
	 */
	private List<List<Entity>> entityMap = new ArrayList<>();

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

		for (int i = 0; i < w * h; i++) {
			tiles.add(new LinkedList<>());
			entityMap.add(new ArrayList<>());
		}
	}
	
	/**
	 * 
	 */
	public void sanitizeTileMap() {
		System.out.println(String.format("Sanitizing loaded world \"%s\"...", name));
		
		// empty tiles
        for (int y = 0; y < h; y++) {
        	for(int x = 0; x < w; x++) {
        		if (getTiles(x, y).isEmpty())
        			addTile(x, y, new StoneTile());
        	}
        }

        //
        for (int y = 0; y < h; y++) {
        	for (int x = 0; x < w; x++) {
        		final Tile tile = getTiles(x, y).get(0);
        		for (final List<Tile> tiles : getNeighborTiles(tile, Neighborhood.MOORE)) {
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
	public void initBoundingBoxes() {
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				for (int z = 1; z < getTiles(x, y).size(); z++) {
					final Tile tile = getTiles(x, y).get(z);
					
					if (!hasNeighbor(tile, World.NeighborLocation.TOP) && !hasNeighbor(tile, World.NeighborLocation.BOTTOM))
						tile.setBoundingBox(new BoundingBox(tile, x * Tile.W, y * Tile.H + 2, (x + 1) * Tile.W, (y + 1) * Tile.H - 6));
					
					else if (!hasNeighbor(tile, World.NeighborLocation.TOP))
						tile.setBoundingBox(new BoundingBox(tile, x * Tile.W, y * Tile.H + 2, (x + 1) * Tile.W, (y + 1) * Tile.H));
					
					else if (!hasNeighbor(tile, World.NeighborLocation.BOTTOM))
						tile.setBoundingBox(new BoundingBox(tile, x * Tile.W, y * Tile.H, (x + 1) * Tile.W, (y + 1) * Tile.H - 6));
						
					else
						tile.setBoundingBox(new BoundingBox(tile, x * Tile.W, y * Tile.H, (x + 1) * Tile.W, (y + 1) * Tile.H));
				}
			}
		}
		
	}
	
	/**
	 * 
	 */
	public void calculateTileConnections() {
		System.out.println("Calculating bitmasking values...");
		
		WorldUtils.loadBitmaskingValues();
		
		for (int i = 0; i < w * h; i++) {
			final List<Tile> tiles = this.tiles.get(i);
			for (int z = 0; z < tiles.size(); z++)
				tiles.get(z).applyBitmask();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean canPlayerSpawn() {
		return spawnLocation != null;
	}
	
	/**
	 * 
	 * @author thomas
	 *
	 */
	public enum Neighborhood {
		MOORE,
		VON_NEUMANN
	}
	
	/**
	 * 
	 * @return
	 */
	public List<List<Tile>> getNeighborTiles(final Tile tile, Neighborhood neighborhood) {
		List<List<Tile>> tiles = new ArrayList<>();
		final int x = tile.getX(), y = tile.getY();
		
		switch (neighborhood) {
		case MOORE:
			for (int j = y - 1; j <= y + 1; j++) {
				for (int i = x - 1; i <= x + 1; i++) {
					if (i == x && j == y)
						continue;
					
					tiles.add(getTiles(i, j));
				}
			}
			break;
			
		case VON_NEUMANN:
			if (hasNeighbor(tile, NeighborLocation.TOP))
				tiles.add(getTiles(x, y - 1));
			if (hasNeighbor(tile, NeighborLocation.RIGHT))
				tiles.add(getTiles(x + 1, y));
			if (hasNeighbor(tile, NeighborLocation.BOTTOM))
				tiles.add(getTiles(x, y + 1));
			if (hasNeighbor(tile, NeighborLocation.LEFT))
				tiles.add(getTiles(x - 1, y));
			break;
			
		default:
			break;
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
			if (y < 0 || y >= h)
				continue;
			
			for (int x = entity.getXTo(); x <= x1; x++) {
				if (x < 0 || x >= w)
					continue;
				
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
			if (y < 0 || y >= h)
				continue;
			
			for (int x = entity.getXTo(); x <= x1; x++) {
				if (x < 0 || x >= w)
					continue;
				
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
		BoundingBox bb = entity.getBoundingBox("body").grow(Tile.W);
		
		// computing corner pins
		int x0 = (int) (bb.x0 / Tile.W);
		int y0 = (int) (bb.y0 / Tile.H);
		int x1 = (int) (bb.x1 / Tile.W);
		int y1 = (int) (bb.y1 / Tile.H);
		
		// adding bounding boxes from tiles
		for (int y = y0; y <= y1; y++) {
			if (y < 0 || y >= h)
				continue;
			
			for (int x = x0; x <= x1; x++) {
				if (x < 0 || x >= w)
					continue;
				
				getTile(x, y).addClipBoundingBoxes(bbs, entity);
			}
		}
		
		// adding bounding boxes from other entities
		Set<Entity> visibleEntities = getEntities(bb);
		for (Entity e : visibleEntities) {
			if (e != entity && e.blocks(entity))
				bbs.add(e.getBoundingBox("body"));
		}
		
		return bbs;
	}
	
	/**
	 * Renders this entire {@link World} to the {@link IAbstractScreen}.
	 *
	 * @param xScroll
	 *		offset on x axis
	 * @param yScroll
	 *		offset on y axis
	 */
	public void render(int xScroll, int yScroll) {
		SceneScreen sceneScreen = (SceneScreen) HerrSpeck.getSceneScreen();
		LightScreen lightScreen = (LightScreen) HerrSpeck.getLightScreen();
		
		// computing corner pins
		int x0 = (xScroll) / Tile.W;
		int y0 = (yScroll) / Tile.H;
		int x1 = (xScroll + sceneScreen.getW()) / Tile.W;
		int y1 = (yScroll + sceneScreen.getH()) / Tile.H + Tile.H;
		
		if (xScroll < 0)
			x0--;
		if (yScroll < 0)
			y0--;
		
		final Set<Entity> visibleEntities = getVisibleEntities(sceneScreen, xScroll, yScroll);
		
		// setting offsets equal to xScroll, yScroll
		sceneScreen.setOffset(xScroll, yScroll);
		lightScreen.setOffset(xScroll, yScroll);
				
		// adding entities to render list
		Set<IComparableDepth> objectsToRender = new TreeSet<>(new DepthComparator());
		
		// adding visible entities to render list
		visibleEntities.stream().forEach(entity -> objectsToRender.add(entity));
		
		// floor rendering
		renderTiles(sceneScreen, x0, y0, x1, y1, 0);
		
		// adding tiles to render list
		for (int y = y0; y <= y1; y++) {
			for (int x = x1; x >= x0; x--) {
				if (x < 0 || x >= w || y < 0 || y >= h)
					continue;
				
				final List<Tile> tiles = this.tiles.get(y * w + x);
				for (int i = 1; i < tiles.size(); i++) {
					if (tiles.size() > i)
						objectsToRender.add(tiles.get(i));
				}
			}
		}
		
		// objects rendering
		objectsToRender.stream().forEach(object -> object.render(sceneScreen));
		
		renderLight(lightScreen);
		
		// reseting offset
		sceneScreen.setOffset(0, 0);
		lightScreen.setOffset(0, 0);
	}
	
	/**
	 * 
	 * @param screen
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param layer
	 */
	private void renderTiles(IAbstractScreen screen, int x0, int y0, int x1, int y1, int layer) {
		for (int y = y0; y <= y1; y++) {
			for (int x = x1; x >= x0; x--) {
				if (x < 0 || x >= w || y < 0 || y >= h) {
					screen.blit(Art.stoneTiles[0][0], x * Tile.W, y * Tile.H);
					continue;
				}
				
				if (tiles.get(y * w + x).size() > layer)
					tiles.get(y * w + x).get(layer).render(screen);
			}
		}
	}
	
	Light light = new Light(this, 100, 250, 150, 0xffffd97f);
	Light light2 = new Light(this, 250, 250, 150, 0xff00ff00);
	Light light3 = new Light(this, 250, 100, 150, 0xffff0000);
	
	/**
	 * 
	 * @param screen
	 */
	private void renderLight(IAbstractScreen screen) {
		HerrSpeck.getLightScreen().clear();
		
		light.render(screen);
		light2.render(screen);
		light3.render(screen);
	}
	
	/**
	 * 
	 * @param xScroll
	 * @param yScroll
	 */
	public void renderBoundingBoxes(int xScroll, int yScroll) {
		SceneScreen sceneScreen = (SceneScreen) HerrSpeck.getSceneScreen();
		
		// setting offsets equal to xScroll, yScroll
		sceneScreen.setOffset(xScroll, yScroll);
		
		Set<BoundingBox> bbs = new HashSet<>();
		for (Entity entity : getVisibleEntities(sceneScreen, xScroll, yScroll)) {
			for (final String key : entity.getBBs().keySet())
				bbs.add(entity.getBoundingBox(key));
			
			for (final BoundingBox bb : getClipBoundingBoxes(entity))
				bbs.add(bb);
			
			if (entity instanceof Mob)
				((Mob) entity).renderBubble(sceneScreen);
		}
		
		bbs.stream().forEach(bb -> bb.draw(sceneScreen));
		
		sceneScreen.setOffset(0, 0);
	}
	
	/**
	 * 
	 * @param xScroll
	 * @param yScroll
	 */
	public void renderDepthLines(int xScroll, int yScroll) {
		SceneScreen sceneScreen = (SceneScreen) HerrSpeck.getSceneScreen();
		
		// computing corner pins
		int x0 = (xScroll) / Tile.W;
		int y0 = (yScroll) / Tile.H;
		int x1 = (xScroll + sceneScreen.getW()) / Tile.W;
		int y1 = (yScroll + sceneScreen.getH()) / Tile.H + Tile.H;
				
		if (xScroll < 0)
			x0--;
		if (yScroll < 0)
			y0--;
		
		// setting offsets equal to xScroll, yScroll
		sceneScreen.setOffset(xScroll, yScroll);
		
		for (int y = y0; y <= y1; y++) {
			for (int x = x1; x >= x0; x--) {
				if (x < 0 || x >= w || y < 0 || y >= h)
					continue;
				
				final List<Tile> tiles = this.tiles.get(y * w + x);
				for (int i = 1; i < tiles.size(); i++) {
					if (tiles.size() > i)
						tiles.get(i).renderDepthLine(sceneScreen);
				}
			}
		}
		
		for (Entity entity : getVisibleEntities(sceneScreen, xScroll, yScroll)) {
			if (entity instanceof Mob)
				((Mob) entity).renderDepthLine(sceneScreen);
		}
		
		sceneScreen.setOffset(0, 0);
	}
	
	/**
	 * 
	 * @param screen
	 * @param xScroll
	 * @param yScroll
	 * @return
	 */
	public Set<Entity> getVisibleEntities(IAbstractScreen screen, int xScroll, int yScroll) {
		return getEntities(xScroll - Tile.W, yScroll - Tile.H, xScroll + screen.getW() + Tile.W, yScroll + screen.getH() + Tile.H);
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
	 * @param xx0
	 * @param yy0
	 * @param xx1
	 * @param yy1
	 * @return
	 */
	public Set<Entity> getEntities(double xx0, double yy0, double xx1, double yy1) {
		return getEntities(xx0, yy0, xx1, yy1, EntityIntersectsBB.INSTANCE);
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
	public Set<Entity> getEntities(double xx0, double yy0, double xx1, double yy1, IBoundingBoxPredicate<Entity> predicate) {
		// computing bounds
		final int x0 = Math.max((int) (xx0) / Tile.W, 0);
		final int y0 = Math.max((int) (yy0) / Tile.H, 0);
		final int x1 = Math.min((int) (xx1) / Tile.W, w - 1);
		final int y1 = Math.min((int) (yy1) / Tile.H, h - 1);
		
		final Set<Entity> ret = new TreeSet<Entity>(new EntityComparator());
		for (int y = y0; y <= y1; y++) {
			for (int x = x0; x <= x1; x++) {
				for (final Entity entity : entityMap.get(y * w + x)) {
					if (predicate.appliesTo(entity, xx0, yy0, xx1, yy1))
						ret.add(entity);
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * 
	 */
	public void tick() {
		if (HerrSpeck.random.nextInt(150) == 0)
			addEntity(new Bat(this, time % (w * 16), time % (h * 16)));
		
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			
			if (!entity.removed()) {
				entity.tick();
				
				// updating entity map
				final int xTo = ((int) (entity.getX() - entity.getRadius().x)) / Tile.W;
				final int yTo = ((int) (entity.getY() - entity.getRadius().y)) / Tile.H;
				
				if (xTo != entity.getXTo() || yTo != entity.getYTo())
					updateEntityMap(entity);
			}
			
			if (entity.removed()) {
				entities.remove(i--);
				removeFromEntityMap(entity);
			}
		}
		
		// updating time
		time++;
		if (time % 10 == 0)
			time();
		
		if (time > 1000000)
			time = 0;
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
//		for (int offset : mooreNeighborhoodOffsets) {
//			final int index = y * w + x + offset;
//			if (index >= 0 && index < w * h) {
//				final Tile neighbour = tiles.get(index).peekLast();
//				if (neighbour != null) {
//					//neighbour.neighbourUpdated(tile);
//				}
//			}
//		}
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
		if (x < 0 || x >= w || y < 0 || y >= h)
			return;
		
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
	 * 		the {@link Tile} to set
	 */
	public void setTile(int x, int y, Tile tile) {
		if (x < 0 || x >= w || y < 0 || y >= h)
			return;
		
		tiles.get(y * w + x).clear();
		tile.init(this, x, y, tiles.get(y * w + x).size());
		tiles.get(y * w + x).add(tile);
	}
	
	/**
	 * Sets the {@link Tile} at the specified location, including layer.
	 * 
	 * @param x
	 * 		x coordinate of the {@link Tile} to set, in tiles
	 * @param y
	 * 		y coordinate of the {@link Tile} to set, in tiles
	 * @param tile
	 * 		the {@link Tile} to set
	 * @param layer
	 * 		
	 */
	public void setTile(int x, int y, Tile tile, int layer) {
		if (x < 0 || x >= w || y < 0 || y >= h)
			return;
			
		tile.init(this, x, y, layer);
		tiles.get(y * w + x).set(layer, tile);
	}
	
	/**
	 * 
	 * @param spawnLocation
	 */
	public void setSpawnLocation(Vec2 spawnLocation) {
		this.spawnLocation = spawnLocation;
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
		if (x < 0 || x >= w || y < 0 || y >= h)
			return null;

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
		if (x < 0 || x >= w || y < 0 || y >= h)
			return null;
		
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
	 *
	 */
	public enum NeighborLocation {
		TOP,
		RIGHT,
		BOTTOM,
		LEFT
	}
	
	/**
	 * 
	 * @param tile
	 * @param neighborLocation
	 * @return
	 */
	public boolean hasNeighbor(Tile tile, NeighborLocation neighborLocation) {
		switch (neighborLocation) {
		case TOP:
			if (tile.getY() - 1 < 0) return false;
			return tiles.get((tile.getY() - 1) * w + tile.getX()).size() > tile.getZ();
			
		case RIGHT:
			if (tile.getX() + 1 >= w) return false;
			return tiles.get(tile.getY() * w + (tile.getX() + 1)).size() > tile.getZ();
			
		case BOTTOM:
			if (tile.getY() + 1 >= h) return false;
			return tiles.get((tile.getY() + 1) * w + tile.getX()).size() > tile.getZ();
			
		case LEFT:
			if (tile.getX() - 1 < 0) return false;
			return tiles.get(tile.getY() * w + (tile.getX() - 1)).size() > tile.getZ();
			
		default:
			return false;
		}
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
	 * 
	 * @return
	 */
	public Vec2 getSpawnLocation() {
		return spawnLocation;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Entity> getEntities() {
		return entities;
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
	
	
	@Override
	public String toString() {
		return String.format("%sx%s", w, h);
	}
}