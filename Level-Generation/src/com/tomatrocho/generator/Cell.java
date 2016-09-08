package com.tomatrocho.generator;

import java.awt.Color;

public enum Cell {

	WALL(0x404040),
	FLOOR(0x996633),
	SAND(0xccaf7e),
	ENTRANCE(Color.GREEN.getRGB());
	
	private int color;
	
	
	Cell(int color) {
		this.color = color;
	}
	
	int getColor() {
		return color;
	}
}
