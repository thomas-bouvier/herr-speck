package com.tomatrocho.game.level.tmx;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class containing the characteristics of an {@link TMXObject}.
 * An {@link TMXObject} gathers objects layer.
 */
public class TMXObject {

    /**
     * The index of this {@link TMXObject}.
     **/
    private int index;

    /**
     * The name of this {@link TMXObject}.
     **/
    public String name;

    /**
     * The Objects of this {@link TMXObject}.
     **/
    public List<TMXGroupObject> objects;

    /**
     * The width of this {@link TMXLayer}.
     **/
    public int w;

    /**
     * The height of this {@link TMXLayer}.
     **/
    public int h;

    /**
     * The mapping between object names and offsets.
     **/
    private Map<String,Integer> nameToObjectMap = new HashMap<>();

    /**
     * The {@link Properties} of this {@link TMXObject}.
     **/
    private Properties props;

    /**
     * The {@link TMXTileMap} of which this {@link TMXObject} belongs to
     **/
    private TMXTileMap map;


    /**
     * Constructor for the {@link TMXObject} class.
     * the {@link TMXObject} is based on the XML definition.
     *
     * @param element
     *      the XML element describing the {@link TMXLayer}
     * @param map
     *      the {@link TMXTileMap} to which the created {@link TMXObject} belongs
     * @throws Exception
     *      indicates a failure to parse the XML group
     */

    public TMXObject(Element element, TMXTileMap map) throws Exception {
        this.map = map;
        name = element.getAttribute("name");
        w = Integer.parseInt(element.getAttribute("width"));
        h = Integer.parseInt(element.getAttribute("height"));
        objects = new ArrayList<>();
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
        NodeList objectNodes = element.getElementsByTagName("object");
        for (int i = 0; i < objectNodes.getLength(); i++) {
            Element objElement = (Element) objectNodes.item(i);
            TMXGroupObject object;
            if (map == null){
                object = new TMXGroupObject(objElement);
            } else {
                object = new TMXGroupObject(objElement, map);
            }
            object.setIndex(i);
            objects.add(object);
        }
    }

    /**
     * Gets an object by its name
     *
     * @param objectName
     *      the name of the object
     */
    public TMXGroupObject getObject(String objectName){
        return objects.get(nameToObjectMap.get(objectName));
    }

    /**
     * Gets all objects of a specific type on a layer
     *
     * @param type
     *      the name of the type
     */
    public List<TMXGroupObject> getObjectsOfType(String type){
        return objects.stream().filter(obj -> obj.getType().equals(type)).collect(Collectors.toList());
    }

    /**
     * Removes an object.
     *
     * @param objectName
     *      the name of the object
     */
    public void removeObject(String objectName){
        int objectOffset = nameToObjectMap.get(objectName);
        objects.remove(objectOffset);
    }

    /**
     * Sets the mapping from object names to their offsets.
     *
     * @param map
     *      the name of the map
     */
    public void setObjectNameMapping(Map<String,Integer> map){
        nameToObjectMap = map;
    }

    /**
     * Adds an object to the object group.
     *
     * @param object
     *      the object to be added
     */
    public void addObject(TMXGroupObject object){
        this.objects.add(object);
        this.nameToObjectMap.put(object.getName(), objects.size());
    }

    /**
     * Gets all the objects from this group.
     *
     * @return
     */
    public List<TMXGroupObject> getObjects(){
        return objects;
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
    public Map<String, Integer> getNameToObjectMap() {
        return nameToObjectMap;
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
    public TMXTileMap getMap() {
        return map;
    }
}