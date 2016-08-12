import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tomatrocho.game.math.Vec2;

public class Cave {
	
	public int size;
	public Cell[] grid;
	public int[] gridFloodFill;
	
	public long seed;
	private static Random random = new Random();
	
	public float chanceToStartAlive = 0.45f;
	
	public int birthLimit = 4;
	public int deathLimit = 3;
	
	public int stepCount = 0;
	
	
	/**
	 * 
	 * @param size
	 */
	public Cave(int size) {
		setSize(size);
		populate();
	}
	
	/**
	 * 
	 * @param size
	 * @param chanceToStartAlive
	 * @param birthLimit
	 * @param deathLimit
	 */
	public Cave(int size, float chanceToStartAlive, int birthLimit, int deathLimit) {
		setSize(size);
		this.chanceToStartAlive = chanceToStartAlive;
		this.birthLimit = birthLimit;
		this.deathLimit = deathLimit;
		populate();
	}
	
	/**
	 * 
	 * @param size
	 * @param seed
	 * @param chanceToStartAlive
	 * @param birthLimit
	 * @param deathLimit
	 */
	public Cave(int size, long seed, float chanceToStartAlive, int birthLimit, int deathLimit) {
		setSize(size);
		this.seed = seed;
		random.setSeed(seed);
		this.chanceToStartAlive = chanceToStartAlive;
		this.birthLimit = birthLimit;
		this.deathLimit = deathLimit;
		populate();
	}
	
	/**
	 * 
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
		grid = new Cell[size * size];
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
		
	    for (int y = 0; y < size; y++) {
	        for (int x = 0; x < size; x++) {
	        	if (random.nextFloat() < chanceToStartAlive) {
	        		grid[y * size + x] = Cell.FLOOR;
	        	} else {
	        		grid[y * size + x] = Cell.WALL;
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
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				int countAlive = countAliveMooreNeighbors(x, y);
				final int index = y * size + x;
				if (grid[index] == Cell.FLOOR) {
					if (!(countAlive < deathLimit)) {
						temp[index] = Cell.FLOOR;
					} else {
						temp[index] = Cell.WALL;
					}
				} else {
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
	
	public List<List<Vec2>> caverns = new ArrayList<>();
	
	/**
	 * 
	 */
	public void identityCaverns() {
		for (int i = 0; i < gridFloodFill.length; i++) {
			gridFloodFill[i] = -1;
		}
		caverns.clear();
		
		int floodFillId = 0;
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				if (grid[y * size + x] == Cell.FLOOR && gridFloodFill[y * size + x] == -1) {
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
		if (gridFloodFill[y * size + x] != -1) {
			return;
		}
		// make sure this cell can be filled
		if (grid[y * size + x] == Cell.WALL) {
			return;
		}
		// mark this cell as visited
		gridFloodFill[y * size + x] = floodFillId;
		caverns.get(caverns.size() - 1).add(new Vec2(x, y));
		
		// recursively fill surrounding pixels
		if (x > 0) {
			floodFillCavern(x - 1, y, floodFillId);
		}
		if (x < size - 1) {
			floodFillCavern(x + 1, y, floodFillId);
		}
		if (y > 0) {
			floodFillCavern(x, y - 1, floodFillId);
		}
		if (y < size - 1) {
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
						grid[(int) (pos.y * size + pos.x)] = cell;
					}
				}
			}
		}
	}
	
	public boolean removedCaverns = false;
	
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
		int count = 0;
		for (int j = y - 1; j <= y + 1; j++) {
			for (int i = x - 1; i <= x + 1; i++) {
				if (i == x && j == y) {
					continue;
				}
				final int index = j * size + i;
				if (index < 0 || index >= size * size) {
					continue;
				}
				if (grid[index] == Cell.FLOOR) {
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
}
