package com.tomatrocho.game.level;

import java.util.Comparator;

import com.tomatrocho.game.entity.Entity;

public class EntityComparator implements Comparator<Entity> {

	@Override
	public int compare(Entity entity0, Entity entity1) {
		if (entity0.getY() < entity1.getY()) {
			return -1;
		}
		if (entity0.getY() > entity1.getY()) {
			return +1;
		}
		if (entity0.getX() < entity1.getX()) {
			return -1;
		}
		if (entity0.getX() > entity1.getX()) {
			return +1;
		}
		return 0;
	}
}
