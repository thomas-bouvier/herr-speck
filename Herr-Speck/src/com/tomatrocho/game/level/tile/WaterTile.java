package com.tomatrocho.game.level.tile;

import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.level.Material;
import com.tomatrocho.game.gfx.Art;

public class WaterTile extends Tile {

    /**
     * Default constructor for the {@link WaterTile} class.
     */
    public WaterTile() {
    	material = Material.WATER;
        img = Tile.PLAIN_IMG;
        neighbour = Material.SANDSTONE;
    }

    @Override
    public void render(IAbstractScreen screen) {
        screen.blit(Art.waterTiles[img & 7][img / 8], x * Tile.W, y * Tile.H);
    }
    
    @Override
    public boolean isConnectable() {
    	return true;
    }
    
    @Override
    public boolean forceRender() {
 	   return true;
    }

    @Override
    public void neighbourChanged(Tile tile) { }

    @Override
    public void handleCollision(Entity entity, double xa, double ya) { }
}