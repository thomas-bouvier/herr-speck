import java.util.Random;

public class Map {
	
	public int width, height;
	public boolean[] grid;
	
	private static int[] neighbourOffsets;
	
	private static final Random random = new Random();
	
	public float chanceToStartAlive = 0.45f;
	
	public int birthLimit = 4;
	public int deathLimit = 3;
	
	
	public Map(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width * height];
		neighbourOffsets = new int[] {-width - 1, -width, -width + 1, -1, 1, width - 1, width, width + 1};
		populate(chanceToStartAlive, birthLimit, deathLimit);
	}
	
	public void populate(float chanceToStartAlive, int birthLimit, int deathLimit) {
		this.chanceToStartAlive = chanceToStartAlive;
		this.birthLimit = birthLimit;
		this.deathLimit = deathLimit;
	    for(int y = 0; y < height; y++){
	        for(int x = 0; x < width; x++){
	        	grid[y * width + x] = (random.nextFloat() < chanceToStartAlive);
	        }
	    }
	    doStep(birthLimit, deathLimit);
	}
	
	public void doStep(int birthLimit, int deathLimit) {
		this.birthLimit = birthLimit;
		this.deathLimit = deathLimit;
		boolean[] temp = new boolean[grid.length];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int countAlive = countAliveNeighbours(x, y);
				if (grid[y * width + x]) {
					temp[y * width + x] = !(countAlive < deathLimit);
				} else {
					temp[y * width + x] = (countAlive > birthLimit);
				}
			}
		}
		grid = temp;
	}
	
	private int countAliveNeighbours(int x, int y) {
		int count = 0;
		for (int offset : neighbourOffsets) {
			int pos = y * width + x + offset;
			if (pos >= 0 && pos < width * height) {
				if (grid[pos]) {
					count++;
				}
			} else {
				//count++;
			}
		}
		return count;
	}
}
