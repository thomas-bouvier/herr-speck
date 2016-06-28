package com.tomatrocho.game.math;

import com.tomatrocho.game.entity.Entity;

public interface IBoundingBoxOwner {

	public void handleCollision(Entity entity, double xa, double ya);
}
