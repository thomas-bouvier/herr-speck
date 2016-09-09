package com.tomatrocho.game.level;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.level.tmx.TMXTileMap;
import com.tomatrocho.generator.WorldGenerator;

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
    private String tileMapPath;
    
    /**
     * The {@link TMXTileMap} linked to the {@link WorldBuilder}.
     */
    protected TMXTileMap tileMap;
    
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
     * 
     */
    private long seed = -1;
    
    /**
     * 
     */
    private float frequency;
    
    /**
     * 
     */
    private boolean randomlyGenerated;
    
    /**
     * 
     */
    private static Random random = new Random();
    

    /**
     * Constructor for the {@link WorldInformation} class.
     *
     * @param name
     * @param filePath
     */
    public WorldInformation(String name, String filePath) {
        this.name = name;
        this.tileMapPath = filePath;
        
        try {
            this.tileMap = new TMXTileMap(HerrSpeck.class.getResourceAsStream(filePath));
            if (tileMap.getLayerCount() < 1) {
                throw new IOException("The world has no layer!");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
        this.w = tileMap.getW();
        this.h = tileMap.getH();
        this.randomlyGenerated = false;
        
        System.out.println("World info " + filePath + " added.");
    }
    
    /**
     * 
     * @param name
     * @param w
     * @param h
     */
    public WorldInformation(String name, int w, int h) {
    	this(name, w, h, getRandomSeed());
    }
    
    /**
     * 
     * @param name
     * @param w
     * @param h
     * @param frequency
     */
    public WorldInformation(String name, int w, int h, float frequency) {
    	this(name, w, h, getRandomSeed(), frequency);
    }
    
    /**
     * 
     * @param name
     * @param w
     * @param h
     * @param seed
     */
    public WorldInformation(String name, int w, int h, long seed) {
    	this(name, w, h, seed, WorldGenerator.DEFAULT_FREQUENCY);
    }
    
    /**
     * 
     * @param name
     * @param w
     * @param h
     * @param seed
     * @param frequency
     */
    public WorldInformation(String name, int w, int h, long seed, float frequency) {
    	this.name = name;
    	this.w = w;
    	this.h = h;
    	this.seed = seed;
    	this.frequency = frequency;
    	
    	this.randomlyGenerated = true;
    	
    	System.out.println("World seed " + seed + " added.");
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
     */
    public void setRandomSeed() {
    	this.seed = getRandomSeed();
    }
    
    /**
     * 
     * @return
     */
    public static long getRandomSeed() {
    	return random.nextLong();
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
    public String getTileMapPath() {
    	return tileMapPath;
    }
    
    /**
     * 
     * @return
     */
    public TMXTileMap getTileMap() {
    	return tileMap;
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
    public String getDescription() {
        return description;
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
    public float getFrequency() {
    	return frequency;
    }
    
    /**
     * 
     * @return
     */
    public boolean randomlyGenerated() {
    	return randomlyGenerated;
    }
}
