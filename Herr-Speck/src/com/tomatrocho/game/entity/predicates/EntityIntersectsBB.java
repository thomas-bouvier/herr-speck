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
	public boolean appliesTo(Entity entity, double xx0, double yy0, double xx1, double yy1) {
		return !entity.removed() && entity.intersects(xx0, yy0, xx1, yy1);
	}
}