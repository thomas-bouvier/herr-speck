package com.tomatrocho.game.entity.predicates;

import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.math.IBoundingBoxPredicate;

/**
 * <p>
 * This predicate applies for an {@link Entity} if it intersects with the given
 * bounding box. The only exception is when the item is already removed.
 * </p>
 * <p>
 * The entity and the bounding box are considered to intersect if they share
 * at least one point together.
 * </p>
 */
public enum EntityIntersectsBB implements IBoundingBoxPredicate<Entity> {

	/**
	 * Singleton instance of {@link EntityIntersectsBB}.
	 */
	INSTANCE;

	
	@Override
	public boolean appliesTo(Entity entity, double x0, double y0, double x1, double y1) {
		return !entity.removed() && entity.intersects(x0, y0, x1, y1);
	}
}