package com.tomatrocho.game.level;

import com.tomatrocho.game.HerrSpeck;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorldList {

    /**
     *
     */
    private static List<WorldInformation> levels = new ArrayList<>();

    static {
    	levels.add(new WorldInformation("generated_world", 100, 100, 0.47f));
//    	levels.add(new WorldInformation("generated_level", "/levels/test.tmx"));
    }


    /**
     *
     */
    public static void createWorldList() {
        System.out.println(new File(HerrSpeck.getDir(), "worlds"));
        File worldsLocation = new File(HerrSpeck.getDir(), "worlds");
        
        if (!worldsLocation.exists()) {
            if (worldsLocation.mkdirs()) {
                loadDir(worldsLocation);
            }
        }
    }

    /**
     *
     * @param dir
     */
    private static void loadDir(File dir) {
        File[] children = dir.listFiles();
        if (children != null) {
            for (File child : children) {
                if (child.isDirectory()) {
                    loadDir(child);
                    continue;
                }
                
                final String fileName = child.getName();
                final int dotIndex = fileName.lastIndexOf(".");
                final String name = fileName.substring(0, dotIndex).toLowerCase();
                final String ext = fileName.substring(dotIndex + 1).toLowerCase();
                
                if (ext.equals("tmx")) {
                    levels.add(new WorldInformation(name, child.getPath()));
                    System.out.println("   Found level: " + name + '.' + ext);
                }
            }
        }
    }
    
    /**
     * 
     * @param worldName
     * @return
     */
    public static WorldInformation getWorldByName(String worldName) {
    	for (WorldInformation wi : levels) {
    		if (wi.getName().equalsIgnoreCase(worldName)) {
    			return wi;
    		}
    	}
    	
    	throw new UnknownWorldException("Unknown world: " + worldName);
    }
    
    
    /**
     * 
     *
     */
    public static class UnknownWorldException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		/**
    	 * 
    	 */
    	public UnknownWorldException(String message) {
    		super(message);
    	}
    }

    /**
     *
     * @return
     */
    public static List<WorldInformation> getLevels() {
        if (levels == null) {
            createWorldList();
        }
        return levels;
    }
}
