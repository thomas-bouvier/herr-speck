package com.tomatrocho.game.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.tomatrocho.game.level.tile.*;

public class WorldUtils {
	
	// 1, 257, 321, 577

    private static final int TILESET_FLOOR_FIRST_GID = 1;
    private static final int TILESET_FLOOR_3232_FIRST_GID = 1000;
    private static final int TILESET_OVERLAY_FIRST_GID = 257;
    private static final int TILESET_OVERLAY_1632_FIRST_GID = 1001;

    /**
     *
     * @param x
     * @param y
     * @param globalId
     * @return
     */
    public static Object getNewObjectForId(int x, int y, int globalId) {
        switch (globalId) {
            case TILESET_FLOOR_FIRST_GID:
                return new StoneTile();
            case TILESET_FLOOR_FIRST_GID + 1:
            	return new SandstoneTile();
            case TILESET_FLOOR_FIRST_GID + 2:
            	return new WaterTile();
            case TILESET_FLOOR_3232_FIRST_GID:
            	return new PuddleTile();
            case TILESET_OVERLAY_FIRST_GID:
                return new SandstoneWallTile();
            case TILESET_OVERLAY_FIRST_GID + 16:
                return new RockTile();
            case TILESET_OVERLAY_1632_FIRST_GID:
                return new HighRockTile();
            default:
                return null;
        }
    }
    
    
	/**
	 * The complete set of bitmasking values as they relate to the positions
	 * of tiles in the tileset.
	 */
	private static Map<Integer, Integer> bitmaskingValues = new HashMap<>();
    
    /**
	 * <p>
	 * Initializes the complete set of bitmasking values as they relate to the positions
	 * of tiles in the tileset.
	 * </p>
	 * <p>
	 * <code>bitmaskingValues</code> is a data structure containing the list of calculated bitmasking values
	 * and their corresponding tile values defined by the index.
	 * </p>
	 */
	public static void loadBitmaskingValues() {
		//TODO le try resources
		BufferedReader reader = new BufferedReader(new InputStreamReader(WorldUtils.class.getResourceAsStream("/bitmasking_values.json")));
		if (reader != null) {
			JSONParser parser = new JSONParser();
			try {
				JSONObject json = (JSONObject) parser.parse(reader);
				for (int j = 0; j < 48; j++) {
					JSONArray values = (JSONArray) json.get(Integer.toString(j));
					for (int i = 0; i < values.size(); i++) {
						//TODO sale
						bitmaskingValues.put(Integer.valueOf(values.get(i).toString()), j);
					}
				}
			} catch (IOException | ParseException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static int getIdForBitmaskValue(int value) {
		if (bitmaskingValues.containsKey(value)) {
			return bitmaskingValues.get(value);
		}
		return 47;
	}
}
