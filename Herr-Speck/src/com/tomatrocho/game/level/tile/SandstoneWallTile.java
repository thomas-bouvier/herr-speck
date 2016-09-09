package com.tomatrocho.game.level.tile;

import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.entity.mob.Player;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.level.Material;
import com.tomatrocho.game.gfx.Art;

public class SandstoneWallTile extends Tile {

    /**
     * Default constructor for the {@link SandstoneWallTile} class.
     */
    public SandstoneWallTile() {
    	this.material = Material.SANDSTONE_WALL;
    	this.img = Tile.PLAIN_IMG;
    }

    @Override
    public void render(IAbstractScreen screen) {
        screen.blit(Art.sandstoneWallTiles[img & 7][img / 8], x * Tile.W, y * Tile.H);
    }
    
    @Override
	public boolean canPass(Entity entity) {
		return !(entity instanceof Player);
	}
    
    @Override
    public boolean isConnectable() {
    	return true;
    }

    @Override
    public void neighbourChanged(Tile tile) {}

    @Override
    public void handleCollision(Entity entity, double xa, double ya) {}
}
