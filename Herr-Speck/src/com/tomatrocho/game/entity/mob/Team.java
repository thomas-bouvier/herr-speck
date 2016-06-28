package com.tomatrocho.game.entity.mob;

public enum Team {

	NEUTRAL(0xff0000ff),
	TEAM_1(0xff00ff00),
	TEAM_2(0xffff0000);
	
	private int col;
	
	
	/**
	 * Constructor for the {@link Team} enum.
	 * 
	 * @param col
	 */
	Team(int col) {
		this.col = col;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCol() {
		return col;
	}
}
