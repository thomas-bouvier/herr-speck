
public enum Cell {

	WALL(0x555555),
	FLOOR(0x996633);
	
	private int color;
	
	
	Cell(int color) {
		this.color = color;
	}
	
	int getColor() {
		return color;
	}
}
