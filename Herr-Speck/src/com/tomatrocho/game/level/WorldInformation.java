package com.tomatrocho.game.level;

import java.util.HashMap;
import java.util.Map;

import com.tomatrocho.game.HerrSpeck;

public class WorldInformation {

    /**
     *
     */
    private static Map<String, WorldInformation> filesToInfo = new HashMap<>();

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String filePath;
    
    /**
     * 
     */
    private long seed = -1;
    
    /**
     * 
     */
    private int w = -1;
    
    /**
     * 
     */
    private int h = -1;

    /**
     *
     */
    private String description;
    

    /**
     * Constructor for the {@link WorldInformation} class.
     *
     * @param name
     * @param filePath
     */
    public WorldInformation(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
        
        System.out.println("World info added: " + filePath);
    }
    
    /**
     * 
     * @param name
     * @param w
     * @param h
     */
    public WorldInformation(String name, int w, int h) {
    	this(name, w, h, HerrSpeck.random.nextLong());
    }
    
    /**
     * 
     * @param name
     * @param w
     * @param h
     * @param seed
     */
    public WorldInformation(String name, int w, int h, long seed) {
    	this.name = name;
    	this.w = w;
    	this.h = h;
    	this.seed = seed;
    	
    	System.out.println("World seed added: " + seed);
    }

    /**
     *
     * @param path
     * @return
     */
    public static WorldInformation getForPath(String path){
        System.out.println("Path -> info: " + path);
        
        return filesToInfo.get(sanitizePath(path));
    }

    /**
     *
     * @param path
     * @return
     */
    private static String sanitizePath(String path){
        return path.substring(path.indexOf("levels"));
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }
    
    /**
     * 
     * @return
     */
    public long getSeed() {
    	return seed;
    }
    
    /**
     * 
     * @return
     */
    public int getW() {
    	return w;
    }
    
    /**
     * 
     * @return
     */
    public int getH() {
    	return h;
    }

    /**
     *
     * @return
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }
}
