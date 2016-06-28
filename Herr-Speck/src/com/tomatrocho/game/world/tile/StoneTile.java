package com.tomatrocho.game.world.tile;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.gfx.Art;
import com.tomatrocho.game.world.level.Material;

public class StoneTile extends Tile {

    /**
     * Default constructor for the {@link StoneTile} class.
     */
    public StoneTile() {
    	material = Material.STONE;
    	img = HerrSpeck.random.nextInt(4);
    }

    @Override
    public void render(IAbstractScreen screen) {
        screen.blit(Art.stoneTiles[img & 3][img / 4], x * Tile.W, y * Tile.H);
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