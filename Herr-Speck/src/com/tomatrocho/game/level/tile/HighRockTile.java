package com.tomatrocho.game.level.tile;

import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.gfx.Art;

public class HighRockTile extends Tile {

    /**
     *
     */
    private static final int H = 32;


    /**
     * Default constructor for the {@link HighRockTile} class.
     */
    public HighRockTile() {
        this.img = 0;
        this.overlay = true;
    }

    @Override
    public void render(IAbstractScreen screen) {
        screen.blit(Art.highRocksTiles[img & 7][0], x * Tile.W, (y - (HighRockTile.H - Tile.H)) * Tile.H);
    }
    
    @Override
    public void neighbourChanged(Tile tile) {}

    @Override
    public void handleCollision(Entity entity, double xa, double ya) {}
}