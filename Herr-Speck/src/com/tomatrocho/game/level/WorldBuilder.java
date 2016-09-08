package com.tomatrocho.game.level;

import java.io.IOException;

import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.level.tile.SandstoneTile;
import com.tomatrocho.game.level.tile.SandstoneWallTile;
import com.tomatrocho.game.level.tile.StoneTile;
import com.tomatrocho.game.level.tile.Tile;
import com.tomatrocho.game.level.tmx.TMXTileMap;
import com.tomatrocho.generator.WorldGenerator;

public class WorldBuilder {

    /**
     * The {@link World} linked to the {@link WorldBuilder}.
     */
    protected World world;


    /**
     * Build a {@link World} from its {@link WorldInformation}.
     *
     * @param wi
     * 		the {@link WorldInformation} object linked to the {@link World} to generate
     * @return
     * 		the generated {@link World}
     * @throws IOException
     */
    public World buildWorld(WorldInformation wi) throws IOException {
    	this.world = new World(wi.getName(), wi.getW(), wi.getH());
    	
    	if (wi.randomlyGenerated()) {
    		WorldGenerator generator = new WorldGenerator(wi.getW(), wi.getH(), wi.getSeed(), wi.getFrequency());
    		for (int i = 0; i < 5; i++) {
    			generator.doStep();
    		}
    		generator.removeDisconnectedCaverns();
    		generator.fillBorderCellsWithSand();
    		
    		world.setSpawnLocation(generator.placeEntrance());
    		
    		for (int y = 0; y < generator.getH(); y++) {
    			for (int x = 0; x < generator.getW(); x++) {
    				world.addTile(x, y, new SandstoneTile());
    				
    				switch (generator.grid[y * generator.getW() + x]) {
    				case WALL:
    					world.addTile(x, y, new SandstoneWallTile());
    					break;
    				case FLOOR:
    				case ENTRANCE:
    					world.setTile(x, y, new StoneTile());
    					break;
    				default:
    					break;
    				}
    			}
    		}
    	}
    	else {
    		processLevelMap(wi.getTileMap());
    	}
        
        world.sanitizeTileMap();
        world.calculateTileConnections();
        
        return world;
    }

    /**
     * 
     * @param map
     */
    private void processLevelMap(TMXTileMap map) {
        for (int i = 0; i < map.getLayerCount(); i++) {
            processLevelMapLayer(map, i);
        }
    }

    /**
     * 
     * @param map
     * @param layer
     */
    private void processLevelMapLayer(TMXTileMap map, int layer) {
        for (int y = 0; y < map.getH(); y++) {
            for (int x = 0; x < map.getW(); x++) {
                Object obj = WorldUtils.getNewObjectForId(x, y, map.getTileGId(x, y, layer));
                
                if (obj != null) {
                    if (obj instanceof Tile) {
                        world.addTile(x, y, (Tile) obj);
                    }
                    else if (obj instanceof Entity) {
                        world.addEntity((Entity) obj);
                    }
                }
            }
        }
    }
}
