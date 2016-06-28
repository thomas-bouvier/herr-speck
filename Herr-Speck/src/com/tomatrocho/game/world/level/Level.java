package com.tomatrocho.game.world.level;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.entity.Entity;
import com.tomatrocho.game.world.level.tmx.TMXTileMap;
import com.tomatrocho.game.world.tile.Tile;

import java.io.IOException;

public class Level {

    /**
     * The {@link World} linked to the {@link Level}
     */
    protected World world;

    /**
     *
     */
    protected TMXTileMap map;


    /**
     * Generates a {@link World} from its {@link WorldInformation}.
     *
     * @param wi
     * 		the {@link WorldInformation} object linked to the {@link World} to generate
     * @return
     * 		the generated {@link World}
     * @throws IOException
     */
    public World generateWorld(WorldInformation wi) throws IOException {
    	System.out.println("\n");
        int w = 0, h = 0, layers = 0;
        try {
            map = new TMXTileMap(HerrSpeck.class.getResourceAsStream(wi.getFilePath()));
            w = map.getW();
            h = map.getH();
            layers = map.getLayerCount();
            if (layers < 1) {
                throw new IOException("The world has no layer!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        world = new World(wi.getName(), w, h);
        processLevelMap(w, h, layers);
        System.out.println(String.format("Validating loaded world \"%s\"..", world.getName()));
        world.validateTileMap();
        System.out.println("Calculating bitmasking values..");
        world.computeTileConnections();
        return world;
    }

    /**
     *
     * @param w
     * @param h
     * @param layerCount
     */
    private void processLevelMap(int w, int h, int layerCount) {
        for (int i = 0; i < layerCount; i++) {
            processMapLayer(w, h, i);
        }
    }

    /**
     *
     * @param w
     * @param h
     * @param layer
     */
    private void processMapLayer(int w, int h, int layer) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Object obj = WorldUtils.getNewObjectForId(x, y, map.getTileGId(x, y, layer));
                if (obj != null) {
                    if (obj instanceof Tile) {
                        world.addTile(x, y, (Tile) obj);
                    }
                    if (obj instanceof Entity) {
                        world.addEntity((Entity) obj);
                    }
                }
            }
        }
    }
}
