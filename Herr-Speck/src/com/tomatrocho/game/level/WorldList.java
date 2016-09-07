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
//    	levels.add(new WorldInformation("generated_level", 25, 25, 0));
    	levels.add(new WorldInformation("tmx_level", "/levels/test.tmx"));
    }


    /**
     *
     */
    public static void createLevelList() {
        System.out.println(new File(HerrSpeck.getDir(), "levels"));
        File levelsLocation = new File(HerrSpeck.getDir(), "levels");
        
        if (!levelsLocation.exists()) {
            if (levelsLocation.mkdirs()) {
                loadDir(levelsLocation);
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
     * @param name
     * @return
     */
    public static WorldInformation getLevelByName(String name) {
    	for (WorldInformation levelInformation : levels) {
    		if (levelInformation.getName().equalsIgnoreCase(name)) {
    			return levelInformation;
    		}
    	}
    	
    	throw new UnknownWorldException("Unknown world: " + name);
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
            createLevelList();
        }
        return levels;
    }
}
