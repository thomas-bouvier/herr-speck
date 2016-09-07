package com.tomatrocho.game.level.tmx;

/**
 * A class for holding information about a particular {@link TMXTile} on a particular {@link TMXLayer}.
 */
public class TMXTile {

    /**
     * A unique ID given to each {@link TMXTile} of each {@link TMXTileSet} within a TMX file,
     * based on the position of the tile within the {@link TMXTileSet},
     * and the number of {@link TMXTileSet} objects referred to in the TMX file.
     */
    private int globalId;

    /**
     * The x coordinate of the {@link TMXTile}.
     **/
    private int x;

    /**
     * The y coordinate of the {@link TMXTile}.
     **/
    private int y;

    /**
     * The layer name on which this {@link TMXTile} is on.
     **/
    private String layerName;

    /**
     * The name of the {@link TMXTileSet} linked to this {@link TMXTile}.
     * */
    private String tileSetName;


    /**
     * Constructor for the {@link TMXTile} class.
     *
     * @param x
     *      the x coordinate of the {@link TMXTile}
     * @param y
     *      the y coordinate of the {@link TMXTile}
     * @param layerName
     *      the layer name on which this {@link TMXTile} is on
     * @param globalId
     *      the globalId of the {@link TMXTile}
     * @param tileSetName
     *      the name of the attached {@link TMXTileSet}
     */
    public TMXTile(int x, int y, String layerName, int globalId, String tileSetName){
        this.x = x;
        this.y = y;
        this.layerName = layerName;
        this.globalId = globalId;
        this.tileSetName = tileSetName;
    }

    /**
     * Retrieves the globalId of this {@link TMXTile}.
     *
     * @return
     *      the globalId of this {@link TMXTile}
     */
    public int getGlobalId() {
        return globalId;
    }

    /**
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * Retrieves the name of the {@link TMXLayer} linked to this {@link TMXTile}.
     *
     * @return
     *      the name of the {@link TMXLayer} linked to this {@link TMXTile}
     */
    public String getLayerName() {
        return layerName;
    }

    /**
     * Retrieves the name of the {@link TMXTileSet} linked to this {@link TMXTile}.
     *
     * @return
     *      the name of the {@link TMXTileSet} linked to this {@link TMXTile}
     */
    public String getTileSetName() {
        return tileSetName;
    }
}
