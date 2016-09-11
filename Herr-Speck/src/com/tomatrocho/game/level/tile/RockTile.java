package com.tomatrocho.game.level.tile;

import com.tomatrocho.game.gfx.Art;
import com.tomatrocho.game.gfx.IAbstractScreen;

public class RockTile extends Tile {

    /**
     * Default constructor for the {@link StoneTile} class.
     */
    public RockTile() {
        this.img = 0;
    }

    @Override
    public void render(IAbstractScreen screen) {
        screen.blit(Art.rocksTiles[img & 7][img / 8], x * Tile.W, y * Tile.H);
    }
}
