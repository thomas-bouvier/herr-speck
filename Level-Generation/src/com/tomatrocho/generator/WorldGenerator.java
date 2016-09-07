package com.tomatrocho.generator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tomatrocho.game.math.Vec2;

public class WorldGenerator {
	
	/**
	 * 
	 */
	private int w;
	
	/**
	 * 
	 */
	private int h;
	
	/**
	 * 
	 */
	public Cell[] grid;
	
	/**
	 * 
	 */
	public int[] gridFloodFill;
	
	/**
	 * 
	 */
	private long seed;
	
	/**
	 * 
	 */
	private static Random random = new Random();
	
	/**
	 * 
	 */
	public float chanceToStartAlive = 0.45f;
	
	/**
	 * 
	 */
	public int birthLimit = 4;
	
	/**
	 * 
	 */
	public int deathLimit = 3;
	
	/**
	 * 
	 */
	public int stepCount = 0;
	
	/**
	 * 
	 */
	public List<List<Vec2>> caverns = new ArrayList<>();
	
	/**
	 * 
	 */
	public boolean removedCaverns = false;
	
	/**
	 * 
	 */
	public boolean borderCellsFilledWithSand = false;
	
	
	/**
	 * 
	 * @param w
	 * @param h
	 */
	public WorldGenerator(int w, int h) {
		setDimensions(w, h);
		
		populate();
	}
	
	/**
	 * 
	 * @param w
	 * @param h
	 * @param seed
	 */
	public WorldGenerator(int w, int h, long seed) {
		setDimensions(w, h);
		this.seed = seed;
		
		random.setSeed(seed);
		
		populate();
	}
	
	/**
	 * 
	 * @param w
	 * @param h
	 * @param chanceToStartAlive
	 * @param birthLimit
	 * @param deathLimit
	 */
	public WorldGenerator(int w, int h, float chanceToStartAlive, int birthLimit, int deathLimit) {
		setDimensions(w, h);
		
		this.chanceToStartAlive = chanceToStartAlive;
		this.birthLimit = birthLimit;
		this.deathLimit = deathLimit;
		
		populate();
	}
	
	/**
	 * 
	 * @param w
	 * @param h
	 * @param seed
	 * @param chanceToStartAlive
	 * @param birthLimit
	 * @param deathLimit
	 */
	public WorldGenerator(int w, int h, long seed, float chanceToStartAlive, int birthLimit, int deathLimit) {
		setDimensions(w, h);
		this.seed = seed;
		
		random.setSeed(seed);
		
		this.chanceToStartAlive = chanceToStartAlive;
		this.birthLimit = birthLimit;
		this.deathLimit = deathLimit;
		
		populate();
	}
	
	/**
	 * 
	 * @param w
	 * @param h
	 */
	public void setDimensions(int w, int h) {
		this.w = w;
		this.h = h;
		
		grid = new Cell[w * h];
		gridFloodFill = new int[grid.length];
	}
	
	/**
	 * 
	 * @param chanceToStartAlive
	 * @param birthLimit
	 * @param deathLimit
	 * @param seed
	 */
	private void populate() {
		System.out.println('\n' + "Generating cave with seed " + seed + "...");
		final long start = System.currentTimeMillis();
		
	    for (int y = 0; y < h; y++) {
	        for (int x = 0; x < w; x++) {
	        	if (random.nextFloat() < chanceToStartAlive) {
	        		grid[y * w + x] = Cell.FLOOR;
	        	} else {
	        		grid[y * w + x] = Cell.WALL;
	        	}
	        }
	    }
	    doStep();
	    
	    System.out.println("Generated cave in " + (System.currentTimeMillis() - start) + " ms");
	}
	
	/**
	 * 
	 */
	public void doStep() {
		stepCount++;
		System.out.println("Performing transition step " + stepCount);

		Cell[] temp = new Cell[grid.length];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int countAlive = countAliveMooreNeighbors(x, y);
				final int index = y * w + x;
				
				if (grid[index] == Cell.FLOOR) {
					if (!(countAlive < deathLimit)) {
						temp[index] = Cell.FLOOR;
					} else {
						temp[index] = Cell.WALL;
					}
				}
				else {
					if (countAlive > birthLimit) {
						temp[index] = Cell.FLOOR;
					} else {
						temp[index] = Cell.WALL;
					}
				}
			}
		}
		grid = temp;
		
		identityCaverns();
	}
	
	/**
	 * 
	 * @param cellToInsert
	 * @param cellToReplace
	 */
	public void replaceCells(Cell cellToInsert, Cell cellToReplace) {
		for (int i = 0; i < grid.length; i++) {
			if (grid[i] == cellToReplace) {
				grid[i] = cellToInsert;
			}
		}
		
		if (cellToReplace == Cell.SAND) {
			borderCellsFilledWithSand = false;
		}
	}
	
	/**
	 * 
	 * @param cell
	 */
	public void fillBorderCellsWithSand() {
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				final int index = y * w + x;
				
				if (grid[index] == Cell.FLOOR) {
					if (countMooreNeighborsOfType(x, y, Cell.WALL) >= 1) {
						grid[index] = Cell.SAND;
					}
				}
			}
		}
		
		borderCellsFilledWithSand = true;
	}
	
	/**
	 * 
	 */
	public void identityCaverns() {
		for (int i = 0; i < gridFloodFill.length; i++) {
			gridFloodFill[i] = -1;
		}
		caverns.clear();
		
		int floodFillId = 0;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (grid[y * w + x] == Cell.FLOOR && gridFloodFill[y * w + x] == -1) {
					caverns.add(new ArrayList<>());
					floodFillCavern(x, y, floodFillId);
					floodFillId++;
				}
			}
		}
		System.out.println(countCaverns() + " caverns");
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param floodFillId
	 */
	public void floodFillCavern(int x, int y, int floodFillId) {
		// make sure this cell hasn't been visited yet
		if (gridFloodFill[y * w + x] != -1) {
			return;
		}
		
		// make sure this cell can be filled
		if (grid[y * w + x] == Cell.WALL) {
			return;
		}
		
		// mark this cell as visited
		gridFloodFill[y * w + x] = floodFillId;
		caverns.get(caverns.size() - 1).add(new Vec2(x, y));
		
		// recursively fill surrounding pixels
		if (x > 0) {
			floodFillCavern(x - 1, y, floodFillId);
		}
		if (x < w - 1) {
			floodFillCavern(x + 1, y, floodFillId);
		}
		if (y > 0) {
			floodFillCavern(x, y - 1, floodFillId);
		}
		if (y < h - 1) {
			floodFillCavern(x, y + 1, floodFillId);
		}
	}
	
	/**
	 * 
	 * @param cell
	 */
	private void fillDisconnectedCavernsWith(Cell cell) {
		final int mainCavernIndex = getMainCavernIndex();
		if (mainCavernIndex != -1) {
			for (int i = 0; i < caverns.size(); i++) {
				if (i != mainCavernIndex) {
					for (Vec2 pos : caverns.get(i)) {
						grid[(int) (pos.y * w + pos.x)] = cell;
					}
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public void removeDisconnectedCaverns() {
		removedCaverns = true;
		fillDisconnectedCavernsWith(Cell.WALL);
	}
	
	/**
	 * 
	 */
	public void restoreDisconnectedCaverns() {
		removedCaverns = false;
		fillDisconnectedCavernsWith(Cell.FLOOR);
	}
	
	/**
	 * 
	 */
	public void swapDisconnectedCavernsState() {
		if (removedCaverns) {
			restoreDisconnectedCaverns();
		} else {
			removeDisconnectedCaverns();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMainCavernIndex() {
		int mainCavernIndex = -1;
		int maxCavernSize = 0;
		for (int i = 0; i < caverns.size(); i++) {
			final List<Vec2> current = caverns.get(i);
			if (current.size() > maxCavernSize) {
				mainCavernIndex = i;
				maxCavernSize = current.size();
			}
		}
		return mainCavernIndex;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int countAliveMooreNeighbors(int x, int y) {
		return countMooreNeighborsOfType(x, y, Cell.FLOOR);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param cell
	 * @return
	 */
	private int countMooreNeighborsOfType(int x, int y, Cell cell) {
		int count = 0;
		for (int j = y - 1; j <= y + 1; j++) {
			for (int i = x - 1; i <= x + 1; i++) {
				if (i == x && j == y) {
					continue;
				}
				
				final int index = j * w + i;
				if (index < 0 || index >= w * h) {
					continue;
				}
				
				if (grid[index] == cell) {
					count++;
				}
			}
		}
		return count;
	}
	
	/**
	 * 
	 * @return
	 */
	public int countCaverns() {
		return caverns.size();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getW() {
		return w;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getH() {
		return h;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getSeed() {
		return seed;
	}
}
