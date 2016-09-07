package com.tomatrocho.game.level.tmx;

import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A holder for {@link TMXTileSet} information
 */
public class TMXTileSet {

    /**
     * The {@link TMXTileMap} this {@link TMXTileSet} was loaded as part of.
     **/
    private final TMXTileMap map;

    /**
     * The index of the {@link TMXTileSet}.
     **/
    private int index;

    /**
     * The name of the {@link TMXTileSet}.
     **/
    private String name;

    /**
     * The first global tile id in {@link TMXTileSet}.
     **/
    private int firstGID;

    /**
     * The local global tile id in the {@link TMXTileSet}.
     **/
    private int lastGID = Integer.MAX_VALUE;

    /**
     * The width of the tiles.
     **/
    private int tileW;

    /**
     * The height of the tiles.
     **/
    private int tileH;

    /**
     * The number of tiles across the {@link TMXTileSet}.
     **/
    private int tilesAcross;

    /**
     * The number of tiles down the {@link TMXTileSet}.
     **/
    private int tilesDown;

    /**
     * The properties for each tile.
     **/
    private Map<Integer, Properties> props = new HashMap<>();

    /**
     * The image for this {@link TMXTileSet}.
     * */
    private String imageref;


    /**
     * Constructor for the {@link TMXTileSet} class.
     * A {@link TMXTileSet} object is based on an XML definition.
     *
     * @param element
     *      the XML describing the {@link TMXTileSet}
     * @param map
     *      the {@link TMXTileMap} this {@link TMXTileSet} was loaded from (gives context to paths)
     * @param loadImage
     *      <code>true</code> if the images should be loaded
     *      <code>false</code> if we're running somewhere images can't be loaded
     * @throws Exception
     *      indicates a failure to parse the {@link TMXTileSet}
     */
    public TMXTileSet(TMXTileMap map, Element element, boolean loadImage) throws Exception {
        this.map = map;
        firstGID = Integer.parseInt(element.getAttribute("firstgid"));
        name = element.getAttribute("name");
        String tileWidthString = element.getAttribute("tilewidth");
        String tileHeightString = element.getAttribute("tileheight");
        if(tileWidthString.length() == 0 || tileHeightString.length() == 0) {
            throw new Exception("TMXTileMap requires that the map be created with tilesets that use a single image.");
        }
        tileW = Integer.parseInt(tileWidthString);
        tileH = Integer.parseInt(tileHeightString);
        /**
        NodeList pElements = element.getElementsByTagName("tile");
        for (int i = 0; i < pElements.getLength(); i++) {
            Element tileElement = (Element) pElements.item(i);
            int id = Integer.parseInt(tileElement.getAttribute("id"));
            id += firstGID;
            Properties tileProps = new Properties();
            Element propsElement = (Element) tileElement.getElementsByTagName("properties").item(0);
            NodeList properties = propsElement.getElementsByTagName("property");
            for (int p = 0; p < properties.getLength(); p++) {
                Element propElement = (Element) properties.item(p);
                String name = propElement.getAttribute("name");
                String value = propElement.getAttribute("value");
                tileProps.setProperty(name, value);
            }
            props.put(id, tileProps);
        }
         */
    }

    /**
     * Retrieves the width of each {@link TMXTile} in this {@link TMXTileSet}
     *
     * @return
     *      the width of each {@link TMXTile} in this {@link TMXTileSet}
     */
    public int getTileW() {
        return tileW;
    }

    /**
     * Retrieves the height of each {@link TMXTile} in this {@link TMXTileSet}.
     *
     * @return
     *      the height of each {@link TMXTile} in this {@link TMXTileSet}
     */
    public int getTileH() {
        return tileH;
    }

    /**
     * Retrieves the {@link Properties} of a specific {@link TMXTile} in this {@link TMXTileSet}.
     *
     * @param globalId
     *      the globalId of the {@link TMXTile} whose properties should be retrieved
     * @return
     *      the {@link Properties} for the specified {@link TMXTile}
     *      <code>null</code> if no properties are defined
     */
    public Properties getProperties(int globalId) {
        return props.get(globalId);
    }

    /**
     * Retrieves the x position of a {@link TMXTile} on this {@link TMXTileSet}.
     *
     * @param id
     *      the {@link TMXTileSet} localId
     * @return
     *      the index of the {@link TMXTile} on the x axis
     */
    public int getTileX(int id) {
        return id % tilesAcross;
    }

    /**
     * Retrieves the y position of a {@link TMXTile} on this {@link TMXTileSet}.
     *
     * @param id
     *      the {@link TMXTileSet} localId
     * @return
     *      the index of the {@link TMXTile} on the y axis
     */
    public int getTileY(int id) {
        return id / tilesAcross;
    }

    /**
     * Sets the limit number of {@link TMXTile} objects in this {@link TMXTileSet}.
     *
     * @param limit
     *      the limit number of {@link TMXTile} objects in this {@link TMXTileSet}
     */
    public void setLimit(int limit) {
        lastGID = limit;
    }

    /**
     * Checks if this {@link TMXTileSet} contains a particular {@link TMXTile}.
     *
     * @param gid
     *      the global id of the {@link TMXTile} to search for
     * @return
     *      <code>true</code> if the ID is contained in this {@link TMXTileSet}
     */
    public boolean contains(int gid) {
        return (gid >= firstGID) && (gid <= lastGID);
    }

    /**
     * Retrieves the {@link TMXTileMap} of this {@link TMXTileSet}.
     *
     * @return
     *      the {@link TMXTileMap} of this {@link TMXTileSet}
     */
    public TMXTileMap getMap() {
        return map;
    }

    /**
     * Retrieves the index of this {@link TMXTileSet}.
     *
     * @return
     *      the index of this {@link TMXTileSet}
     */
    public int getIndex() {
        return index;
    }

    /**
     * Retrieves the name of this {@link TMXTileSet}.
     *
     * @return
     *      the name of this {@link TMXTileSet}
     */
    public String getName() {
        return name;
    }

    /**
     *
     */
    public int getFirstGId() {
        return firstGID;
    }

    /**
     *
     * @return
     */
    public int getLastGId() {
        return lastGID;
    }

    /**
     *
     * @return
     */
    public int getTilesAcross() {
        return tilesAcross;
    }

    /**
     *
     * @return
     */
    public int getTilesDown() {
        return tilesDown;
    }

    /**
     *
     * @return
     */
    public Map<Integer, Properties> getProps() {
        return props;
    }

    /**
     *
     * @return
     */
    public String getImageref() {
        return imageref;
    }
}