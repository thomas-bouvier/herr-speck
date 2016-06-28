package com.tomatrocho.game.world.level;

import java.util.HashMap;
import java.util.Map;

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
        System.out.println("Level info added: " + filePath);
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
