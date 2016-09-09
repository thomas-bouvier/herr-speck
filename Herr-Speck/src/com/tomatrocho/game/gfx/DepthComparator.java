package com.tomatrocho.game.gfx;

import java.util.Comparator;

public class DepthComparator implements Comparator<IComparableDepth> {

	@Override
	public int compare(IComparableDepth o1, IComparableDepth o2) {
		final int o1YCoordinate = o1.getDepthLine();
		final int o2YCoordinate = o2.getDepthLine();
		
		if (o1YCoordinate == o2YCoordinate)
			return 1;
		
		return o1YCoordinate - o2YCoordinate;
	}

}