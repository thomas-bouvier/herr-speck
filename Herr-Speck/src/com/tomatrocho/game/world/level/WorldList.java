package com.tomatrocho.game.world.level;

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
        levels.add(new WorldInformation("test", "/levels/test.tmx"));
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
    	return null;
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
