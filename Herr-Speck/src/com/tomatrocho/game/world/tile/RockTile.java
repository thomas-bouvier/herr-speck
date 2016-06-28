package com.tomatrocho.game.world.tile;

import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.gfx.Art;

public class RockTile extends Tile {

    /**
     * Default constructor for the {@link StoneTile} class.
     */
    public RockTile() {
        img = 0;
    }

    @Override
    public void render(IAbstractScreen screen) {
        screen.blit(Art.rocksTiles[img & 7][img / 8], x * Tile.W, y * Tile.H);
    }

    @Override
    public void neighbourChanged(Tile tile) { }

    @Override
    public void handleCollision(Entity entity, double xa, double ya) { }
}
