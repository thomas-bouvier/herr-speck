package com.tomatrocho.game.world.level.tmx;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Properties;

/**
 * An object from a object-group on the map
 */
public class TMXGroupObject {

    /**
     * The index of this object.
     **/
    private int index;

    /**
     * The name of this object.
     **/
    private String name;

    /**
     * The type of this object.
     **/
    private String type;

    /**
     * The x coordinate of this object.
     **/
    private int x;

    /**
     * The y coordinate of this object.
     **/
    private int y;

    /**
     * The width of this object.
     **/
    private int w;

    /**
     * The height of this object.
     **/
    private int h;

    /**
     * The image source.
     **/
    private String image;

    /**
     * The {@link Properties} of this group.
     **/
    private Properties props;

    /**
     * The gid reference to the image.
     **/
    private int gid = -1;

    /**
     * Indicates if this is an image object.
     **/
    public boolean isImageObject = false;

    /**
     * The {@link TMXTileMap} this object belongs to.
     **/
    private TMXTileMap map;

    /**
     * Create a new group based on the XML definition.
     *
     * @param element
     *      The XML element describing the layer
     * @throws Exception
     *      indicates a failure to parse the XML group
     */
    public TMXGroupObject(Element element) throws Exception {
        if(element.getAttribute("gid") != ""){
            gid = Integer.parseInt(element.getAttribute("gid"));
            isImageObject = true;
        }
        if(isImageObject){
            if(element.getAttribute("width") != ""){
                w = Integer.parseInt(element.getAttribute("width"));
            }
            if(element.getAttribute("height") != ""){
                h = Integer.parseInt(element.getAttribute("height"));
            }
            if(element.getAttribute("name") != ""){
                name = element.getAttribute("name");
            }
            if(element.getAttribute("type") != ""){
                type = element.getAttribute("type");
            }
        } else {
            w = Integer.parseInt(element.getAttribute("width"));
            h = Integer.parseInt(element.getAttribute("height"));
            name = element.getAttribute("name");
            type = element.getAttribute("type");
        }
        x = Integer.parseInt(element.getAttribute("x"));
        y = Integer.parseInt(element.getAttribute("y"));
        // now read the layer properties
        Element propsElement = (Element) element.getElementsByTagName("properties").item(0);
        if (propsElement != null) {
            NodeList properties = propsElement.getElementsByTagName("property");
            if (properties != null) {
                props = new Properties();
                for (int p = 0; p < properties.getLength(); p++) {
                    Element propElement = (Element) properties.item(p);
                    String name = propElement.getAttribute("name");
                    String value = propElement.getAttribute("value");
                    props.setProperty(name, value);
                }
            }
        }
    }

    /**
     * Create a new group based on the XML definition.
     *
     * @param element
     *      the XML element describing the layer
     * @param map
     *      the map this object belongs to
     * @throws Exception
     *      indicates a failure to parse the XML group
     */
    public TMXGroupObject(Element element, TMXTileMap map) throws Exception {
        this.map = map;
        if(element.getAttribute("gid") != ""){
            gid = Integer.parseInt(element.getAttribute("gid"));
            isImageObject = true;
        }
        if(isImageObject){
            if(element.getAttribute("width") != ""){
                w = Integer.parseInt(element.getAttribute("width"));
            }
            if(element.getAttribute("height") != ""){
                h = Integer.parseInt(element.getAttribute("height"));
            }
            if(element.getAttribute("name") != ""){
                name = element.getAttribute("name");
            }
            if(element.getAttribute("type") != ""){
                type = element.getAttribute("type");
            }
        } else {
            w = Integer.parseInt(element.getAttribute("width"));
            h = Integer.parseInt(element.getAttribute("height"));
            name = element.getAttribute("name");
            type = element.getAttribute("type");
        }
        x = Integer.parseInt(element.getAttribute("x"));
        y = Integer.parseInt(element.getAttribute("y"));
        // now read the layer properties
        Element propsElement = (Element) element.getElementsByTagName("properties").item(0);
        if (propsElement != null) {
            NodeList properties = propsElement.getElementsByTagName("property");
            if (properties != null) {
                props = new Properties();
                for (int p = 0; p < properties.getLength(); p++) {
                    Element propElement = (Element) properties.item(p);
                    String name = propElement.getAttribute("name");
                    String value = propElement.getAttribute("value");
                    props.setProperty(name, value);
                }
            }
        }
    }

    /**
     * Creates a new TMXGroupObject, using pre existing variables instead of XML elements
     * If inPixels is true, then all the integer values are treated in pixels.
     * If false, then it treats it as Tiled map co-ordinates.
     *
     * @param name
     *      the name of the object
     * @param x
     *      the x coordinate of the object
     * @param y
     *      the y co-ordinate of the object
     * @param type
     *      the type of this object
     * @param w
     *      the width of this object
     * @param h
     *      the height of this object
     * @param props
     *      the {@link Properties} of this object
     * @param inPixels
     *      if the integer values specified are in pixels or not
     */
    public TMXGroupObject(int x, int y, String type, int w, int h, Properties props, String name, boolean inPixels){
        if(inPixels){
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        } else {
            this.x = x * 16;
            this.y = y * 16;
            this.w = w * 16;
            this.h = h * 16;
        }
        this.type = type;
        this.props = props;
        this.name = name;
    }

    /**
     * Creates a new TMXGroupObject, using pre existing variables instead of XML elements
     * If inPixels is true, then all the integer values are treated in pixels. If false, then it treats it as Tiled map co-ordinates
     *
     * @param name
     *      the name of the object
     * @param x
     *      the x coordinate of the object
     * @param y
     *      the y coordinate of the object
     * @param type
     *      the type of this object
     * @param w
     *      the width of this object
     * @param h
     *      the height of this object
     * @param props
     *      the {@link Properties} of this object
     * @param inPixels
     *      if the integer values specified are in pixels or not
     * @param gid
     *      if this is an image object, the gid is set. If this isn't an image object, the gid is -1
     * @param map
     *      the {@link TMXTileMap} this object belongs to
     */
    public TMXGroupObject(int x, int y, String type, int w, int h, Properties props, String name, boolean inPixels, int gid, TMXTileMap map){
        this.map = map;
        if(gid != -1){
            this.gid = gid;
            this.isImageObject = true;
        }
        if(inPixels){
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        } else {
            this.x = x * 16;
            this.y = y * 16;
            this.w = w * 16;
            this.h = h * 16;
        }
        this.type = type;
        this.props = props;
        this.name = name;
    }

    /**
     * Puts a property to an object.
     *
     * @param propertyKey
     *      the key of the property to be put to the object
     * @param propertyValue
     *      the value mapped to the key of the property to be put to the object
     */
    public void putProperty(String propertyKey,String propertyValue){
        props.put(propertyKey, propertyValue);
    }

    /**
     * Puts a property to an object.
     *
     * @param propertyKey
     *      the key of the property to be put to the object
     */
    public void removeProperty(String propertyKey){
        props.remove(propertyKey);
    }

    /**
     *
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     *
     * @return
     */
    public int getIndex() {
        return index;
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
    public String getType() {
        return type;
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
    public String getImage() {
        return image;
    }

    /**
     *
     * @return
     */
    public Properties getProps() {
        return props;
    }

    /**
     *
     * @return
     */
    public int getGid() {
        return gid;
    }

    /**
     *
     * @return
     */
    public boolean isImageObject() {
        return isImageObject;
    }

    /**
     *
     * @return
     */
    public TMXTileMap getMap() {
        return map;
    }
}
