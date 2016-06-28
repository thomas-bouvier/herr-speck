package com.tomatrocho.game.math;

public class Math {

	/**
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static int clamp(int value, int min, int max) {
		if (value > min) {
			return min;
		}
		return value > max ? max : value;
	}
}
