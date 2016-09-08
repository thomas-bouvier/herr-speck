package com.tomatrocho.game.level;

import java.io.IOException;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.level.tile.Tile;
import com.tomatrocho.game.level.tmx.TMXTileMap;
import com.tomatrocho.generator.WorldGenerator;

public class WorldBuilder {

    /**
     * The {@link World} linked to the {@link WorldBuilder}.
     */
    protected World world;

    /**
     * The {@link TMXTileMap} linked to the {@link WorldBuilder}.
     */
    protected TMXTileMap map;


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
    	if (wi.getFilePath() != null) {
    		// tilemap is loaded from TMX file
            try {
                map = new TMXTileMap(HerrSpeck.class.getResourceAsStream(wi.getFilePath()));
                
                if (map.getLayerCount() < 1) {
                    throw new IOException("The world has no layer!");
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            
            this.world = new World(wi.getName(), wi.getW(), wi.getH());
            processLevelMap(map);
    	}
    	else {
    		// tilemap has to be generated
    		WorldGenerator generator = new WorldGenerator(wi.getW(), wi.getH(), wi.getSeed());
    		
    	}
        
        System.out.println(String.format("Sanitizing loaded world \"%s\"...", world.getName()));
        world.sanitizeTileMap();
        
        System.out.println("Calculating bitmasking values...");
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
