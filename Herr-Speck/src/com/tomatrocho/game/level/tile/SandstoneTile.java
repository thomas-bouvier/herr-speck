package com.tomatrocho.game.level.tile;

import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.level.Material;
import com.tomatrocho.game.gfx.Art;

public class SandstoneTile extends Tile {

    /**
     * Default constructor for the {@link SandstoneTile} class.
     */
    public SandstoneTile() {
    	material = Material.SANDSTONE;
    	img = Tile.PLAIN_IMG;
    	connectableWith.add(Material.WATER);
    }

    @Override
    public void render(IAbstractScreen screen) {
        screen.blit(Art.sandstoneTiles[img & 7][img / 8], x * Tile.W, y * Tile.H);
    }
    
    @Override
    public boolean forceRender() {
    	return true;
    }
    
    @Override
    public boolean isConnectable() {
    	return true;
    }

    @Override
    public void neighbourChanged(Tile tile) { }

    @Override
    public void handleCollision(Entity entity, double xa, double ya) { }
}