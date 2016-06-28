package com.tomatrocho.game.math;

import com.tomatrocho.game.entity.Entity;

/**
 * A {@link IBoundingBoxPredicate} is a generic predicate that tests whether an item that
 * has a bounding box fulfills some conditions in interaction with a given bounding box.
 */
public interface IBoundingBoxPredicate<T extends Entity> {
	
	/**
	 * Returns <code>true</code> if the specific conditions of this {@link IBoundingBoxPredicate}
	 * apply to the given item and the given bounding box.
	 *
	 * @param item
	 * 		the item that should be checked
	 * @param x0
	 * 		the top left corner of the box (x coordinate)
	 * @param y0
	 * 		the top left corner of the box (y coordinate)
	 * @param x1
	 * 		the bottom right corner of the box (x coordinate)
	 * @param y1
	 * 		the bottom right corner of the box (y coordinate)
	 * @return
	 * 		<code>true</code> if the predicate applies to the item and the box,
	 *		<code>false</code> otherwise
	 */
	boolean appliesTo(T item, double x0, double y0, double x1, double y1);
	
}