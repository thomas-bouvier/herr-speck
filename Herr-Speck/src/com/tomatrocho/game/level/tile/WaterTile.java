package com.tomatrocho.game.level.tile;

import com.tomatrocho.game.gfx.Art;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.level.Material;

public class WaterTile extends Tile {

    /**
     * Default constructor for the {@link WaterTile} class.
     */
    public WaterTile() {
    	this.material = Material.WATER;
        this.img = Tile.PLAIN_IMG;
        this.neighbour = Material.SANDSTONE;
    }

    @Override
    public void render(IAbstractScreen screen) {
        screen.blit(Art.waterTiles[img & 7][img / 8], x * Tile.W, y * Tile.H);
    }
    
    @Override
    public boolean isConnectable() {
    	return true;
    }
}