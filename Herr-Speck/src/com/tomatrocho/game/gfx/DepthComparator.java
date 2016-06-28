package com.tomatrocho.game.gfx;

import java.util.Comparator;

public class DepthComparator implements Comparator<IComparableDepth> {

	@Override
	public int compare(IComparableDepth o1, IComparableDepth o2) {
		if (o1.forceRender() && o2.forceRender()) {
			return 1;
		}
		if (o1.forceRender()) {
			return -1;
		}
		final int o1YCoordinate = o1.getVerticalBaseCoordinate();
		final int o2YCoordinate = o2.getVerticalBaseCoordinate();
		if (o1YCoordinate == o2YCoordinate) {
			return 1;
		}
		return o1YCoordinate - o2YCoordinate;
	}

}
