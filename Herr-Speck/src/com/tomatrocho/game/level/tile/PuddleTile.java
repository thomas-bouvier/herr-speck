package com.tomatrocho.game.level.tile;

import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.gfx.Art;

public class PuddleTile extends Tile {

    /**
    *
    */
   private static final int H = 32;


   /**
    * Default constructor for the {@link PuddleTile} class.
    */
   public PuddleTile() {
       this.img = 0;
   }

   @Override
   public void render(IAbstractScreen screen) {
       screen.blit(Art.puddleTiles[img & 7][img / 8], x * Tile.W, (y - (PuddleTile.H - Tile.H)) * Tile.H);
   }

   @Override
   public void neighbourChanged(Tile tile) {}

   @Override
   public void handleCollision(Entity entity, double xa, double ya) {}
}
