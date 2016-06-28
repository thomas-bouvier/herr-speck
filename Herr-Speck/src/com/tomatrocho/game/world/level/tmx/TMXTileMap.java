package com.tomatrocho.game.world.level.tmx;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * The {@link TMXTileMap} is intended to parse XML maps.
 */
public class TMXTileMap {

    /**
     * The location prefix where {@link TMXTileSet} source images are retrieved.
     */
    private String tileSetsPath;

    /**
     * The width of the {@link TMXTileMap}, in tiles.
     */
    private int w;

    /**
     * The height of the {@link TMXTileMap}, in tiles.
     */
    private int h;

    /**
     * The width of the {@link TMXTile} objects used on the {@link TMXTileMap}.
     */
    private int tileW;

    /**
     * The height of the {@link TMXTile} objects used on the {@link TMXTileMap}.
     */
    private int tileH;

    /**
     * The {@link Properties} of the {@link TMXTileMap}.
     */
    private Properties props;

    /**
     * The list of {@link TMXTileSet} objects linked to the {@link TMXTileMap}.
     */
    private List<TMXTileSet> tmxTileSets = new ArrayList<>();

    /**
     * The list of {@link TMXLayer} objects linked in the {@link TMXTileMap}.
     */
    private List<TMXLayer> tmxLayers = new ArrayList<>();

    /**
     * The list of {@link TMXObject} defined in the {@link TMXTileMap}.
     */
    private List<TMXObject> tmxObjects = new ArrayList<>();


    /**
     * Constructor for the {@link TMXTileMap} class.
     * Loads a {@link TMXTileMap} from an arbitrary {@link InputStream}.
     *
     * @param in
     *      the {@link InputStream} to load from
     * @throws Exception
     *      indicates a failure to load the {@link TMXTileMap}
     */
    public TMXTileMap(InputStream in) throws Exception {
        load(in, "");
    }

    /**
     * Constructor for the {@link TMXTileMap} class.
     * Loads a {@link TMXTileMap} from an arbitrary {@link InputStream}.
     *
     * @param in
     *      the {@link InputStream} to load from
     * @param tileSetsLocation
     *      the location at which we can find {@link TMXTileSet} images
     * @throws Exception
     *      indicates a failure to load the {@link TMXTileMap}
     */
    public TMXTileMap(InputStream in, String tileSetsLocation) throws Exception {
        load(in, tileSetsLocation);
    }

    /**
     * Loads a {@link TMXTileMap}.
     *
     * @param in
     *      the {@link InputStream} from which to load the {@link TMXTileMap}
     * @param tileSetsPath
     *      the location from which {@link TMXTileSet} images are retrieved
     * @throws Exception
     *      indicates a failure to parse the {@link TMXTileMap} or find a {@link TMXTileSet}
     */
    private void load(InputStream in, String tileSetsPath) throws Exception {
        this.tileSetsPath = tileSetsPath;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            //TODO convertir ça en lambda
            builder.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    return new InputSource(new ByteArrayInputStream(new byte[0]));
                }
            });
            Element docElement = builder.parse(in).getDocumentElement();
            String orient = docElement.getAttribute("orientation");
            if (!orient.equals("orthogonal")) {
                throw new Exception("Only orthogonal maps supported, found: " + orient);
            }
            w = Integer.parseInt(docElement.getAttribute("width"));
            h = Integer.parseInt(docElement.getAttribute("height"));
            tileW = Integer.parseInt(docElement.getAttribute("tilewidth"));
            tileH = Integer.parseInt(docElement.getAttribute("tileheight"));
            // acquire map properties
            Element propsElement = (Element) docElement.getElementsByTagName("properties").item(0);
            if (propsElement != null) {
                NodeList properties = propsElement.getElementsByTagName("property");
                if (properties != null) {
                    props = new Properties();
                    for (int p = 0; p < properties.getLength(); p++) {
                        Element propElement = (Element) properties.item(p);
                        props.setProperty(propElement.getAttribute("name"), propElement.getAttribute("value"));
                    }
                }
            }
            // acquire layers
            NodeList layerNodes = docElement.getElementsByTagName("layer");
            for (int i = 0; i < layerNodes.getLength(); i++) {
                TMXLayer tmxLayer = new TMXLayer((Element) layerNodes.item(i), this);
                tmxLayer.setIndex(i);
                tmxLayers.add(tmxLayer);
            }
            // acquire object-groups
            NodeList objectGroupNodes = docElement.getElementsByTagName("objectgroup");
            for (int i = 0; i < objectGroupNodes.getLength(); i++) {
                TMXObject tmxObject = new TMXObject((Element) objectGroupNodes.item(i), this);
                tmxObject.setIndex(i);
                tmxObjects.add(tmxObject);
            }
        } catch (Exception ex) {
            throw new Exception("Failed to parse tmx file (must be saved with gzip compression)", ex);
        }
    }

    /**
     * Retrieves the location of the {@link TMXTileSet} images specified.
     *
     * @return
     *      the location of the {@link TMXTileSet} images specified as a resource reference prefix
     */
    public String getTileSetsPath() {
        return tileSetsPath;
    }

    /**
     * Retrieves the {@link TMXTileSet} objects linked to this {@link TMXTileMap}.
     *
     * @return
     *      the {@link TMXTileSet} objects linked to this {@link TMXTileMap}
     */
    public List<TMXTileSet> getTmxTileSets() {
        return tmxTileSets;
    }

    /**
     * Retrieves the width of the {@link TMXTileMap}.
     *
     * @return
     *      the width of the {@link TMXTileMap}, in tiles
     */
    public int getW() {
        return w;
    }

    /**
     * Retrieves the height of the {@link TMXTileMap}.
     *
     * @return
     *      the height of the {@link TMXTileMap}, in tiles
     */
    public int getH() {
        return h;
    }

    /**
     * Retrieves the width of a single {@link TMXTile}.
     *
     * @return
     *      the width of a single {@link TMXTile}, in pixels
     */
    public int getTileW() {
        return tileW;
    }

    /**
     * Retrieves the height of a single {@link TMXTile}.
     *
     * @return
     *      the height of a single {@link TMXTile}, in pixels
     */
    public int getTileH() {
        return tileH;
    }

    /**
     * Retrieves the globalId of a {@link TMXTile} at specified location in the {@link TMXTileMap}.
     *
     * @param x
     *      x coordinate of the {@link TMXTile}, in tiles
     * @param y
     *      y coordinate of the {@link TMXTile}, in tiles
     * @param layerIndex
     *      the index of the {@link TMXLayer} to retrieve the {@link TMXTile} from
     * @return
     *      the globalId of the {@link TMXTile}
     */
    public int getTileGId(int x, int y, int layerIndex) {
        return tmxLayers.get(layerIndex).getTileGId(x, y);
    }

    /**
     * Sets the globalId of a {@link TMXTile} at specified location in the {@link TMXTileMap}.
     *
     * @param x
     *      x coordinate of the {@link TMXTile}, in tiles
     * @param y
     *      y coordinate of the {@link TMXTile}, in tiles
     * @param layerIndex
     *      the index of the {@link TMXLayer} to set the new globalId
     * @param globalId
     *      the globalId to be set
     */
    public void setTileId(int x, int y, int layerIndex, int globalId) {
        tmxLayers.get(layerIndex).setTileID(x, y, globalId);
    }

    /**
     * Retrieves a property given to the {@link TMXTileMap}.
     *
     * @param propertyName
     *      the name of the property of the {@link TMXTileMap} to retrieve
     * @param def
     *      the default value to return
     * @return
     *      the value assigned to the property on the {@link TMXTileMap},
     *      or <code>def</code> if none is supplied
     */
    public String getMapProperty(String propertyName, String def) {
        if (props == null) {
            return def;
        }
        return props.getProperty(propertyName, def);
    }

    /**
     * Retrieves a property given to a particular {@link TMXLayer}.
     *
     * @param layerIndex
     *      the index of the {@link TMXLayer} to retrieve
     * @param propertyName
     *      the name of the property of this {@link TMXLayer} to retrieve
     * @param def
     *      the default value to return
     * @return
     *      the value assigned to the property on the {@link TMXLayer},
     *      or <code>def</code> if none is supplied
     */
    public String getLayerProperty(int layerIndex, String propertyName, String def) {
        TMXLayer TMXLayer = tmxLayers.get(layerIndex);
        if (TMXLayer == null || TMXLayer.getProps() == null) {
            return def;
        }
        return TMXLayer.getProps().getProperty(propertyName, def);
    }

    /**
     * Retrieves a property given to a particular {@link TMXTile}.
     *
     * @param globalId
     *      the globalId of the {@link TMXTile} to retrieve
     * @param propertyName
     *      the name of the property to retrieve
     * @param def
     *      the default value to return
     * @return
     *      the value assigned to the property on the {@link TMXTile},
     *      or <code>def</code> if none is supplied
     */
    public String getTileProperty(int globalId, String propertyName, String def) {
        if (globalId == 0) {
            return def;
        }
        TMXTileSet TMXTileSet = getTileSetFromGId(globalId);
        Properties props = TMXTileSet.getProperties(globalId);
        if (props == null) {
            return def;
        }
        return props.getProperty(propertyName, def);
    }

    /**
     * Retrieves the number of defined {@link TMXLayer} objects in this {@link TMXTileMap}.
     *
     * @return
     *      the number of defined {@link TMXLayer} objects in this {@link TMXTileMap}
     */
    public int getLayerCount() {
        return tmxLayers.size();
    }

    /**
     * Retrieves the number of defined {@link TMXTileSet} objects in this {@link TMXTileMap}.
     *
     * @return
     *      the number of defined {@link TMXTileSet} objects in this {@link TMXTileMap}
     */
    public int getTileSetCount() {
        return tmxTileSets.size();
    }

    /**
     * Retrieves the number of {@link TMXObject} defined in this {@link TMXTileMap}.
     *
     * @return
     *      the number of {@link TMXObject} objects defined in this {@link TMXTileMap}
     */
    public int getObjectGroupCount() {
        return tmxObjects.size();
    }

    /**
     * Retrieves the number of objects of a specific {@link TMXObject}.
     *
     * @param groupId
     *      the index of this {@link TMXObject}
     * @return
     *      the number of the objects in the {@link TMXObject},
     *      or -1, when error occurred
     */
    public int getObjectCount(int groupId) {
        if (groupId >= 0 && groupId < tmxObjects.size()) {
            return tmxObjects.get(groupId).objects.size();
        }
        return -1;
    }

    /**
     * Retrieves a {@link TMXTileSet} for the given index in this {@link TMXTileMap}.
     *
     * @param index
     *      the index of the {@link TMXTileSet}
     * @return
     *      the requested {@link TMXTileSet}
     */
    public TMXTileSet getTileSetFromIndex(int index) {
        return tmxTileSets.get(index);
    }

    /**
     * Retrieves a {@link TMXTileSet} in which the {@link TMXTile} defined by its globalId lives for this {@link TMXTileMap}.
     *
     * @param globalId
     *      the globalId of the {@link TMXTile}
     * @return
     *      the {@link TMXTileSet} in which the {@link TMXTile} lives,
     *      or <code>null</code> if the globalId is not defined
     */
    public TMXTileSet getTileSetFromGId(int globalId) {
        return tmxTileSets.stream().filter(tileSet -> tileSet.contains(globalId)).findFirst().orElse(null);
    }

    /**
     * Retrieves the name of a specific object from a specific {@link TMXObject}.
     *
     * @param groupID
     *      index of a {@link TMXObject}
     * @param objectID
     *      index of an object
     * @return
     *      the name of an object,
     *      or <code>null</code> when error occurred
     */
    public String getObjectName(int groupID, int objectID) {
        if (groupID >= 0 && groupID < tmxObjects.size()) {
            TMXObject grp = tmxObjects.get(groupID);
            if (objectID >= 0 && objectID < grp.objects.size()) {
                TMXGroupObject object = grp.objects.get(objectID);
                return object.getName();
            }
        }
        return null;
    }

    /**
     * Retrieves the type of an specific object from a specific {@link TMXObject}.
     *
     * @param groupId
     *      index of a {@link TMXObject}
     * @param objectId
     *      index of an object
     * @return
     *      the type of an object,
     *      or <code>null</code> when error occurred
     */
    public String getObjectType(int groupId, int objectId) {
        if (groupId >= 0 && groupId < tmxObjects.size()) {
            TMXObject group = tmxObjects.get(groupId);
            if (objectId >= 0 && objectId < group.objects.size()) {
                TMXGroupObject object = group.objects.get(objectId);
                return object.getType();
            }
        }
        return null;
    }

    /**
     * Retrieves the x coordinate of a specific object from a specific {@link TMXObject}.
     *
     * @param groupId
     *      index of a {@link TMXObject}
     * @param objectId
     *      index of an object
     * @return
     *      the x coordinate of an object,
     *      or -1, when error occurred
     */
    public int getObjectX(int groupId, int objectId) {
        if (groupId >= 0 && groupId < tmxObjects.size()) {
            TMXObject group = tmxObjects.get(groupId);
            if (objectId >= 0 && objectId < group.objects.size()) {
                TMXGroupObject object = group.objects.get(objectId);
                return object.getX();
            }
        }
        return -1;
    }

    /**
     * Retrieves the y coordinate of a specific object from a specific {@link TMXObject}.
     *
     * @param groupId
     *      index of a {@link TMXObject}
     * @param objectId
     *      index of an object
     * @return
     *      the y coordinate of an object,
     *      or -1, when error occurred
     */
    public int getObjectY(int groupId, int objectId) {
        if (groupId >= 0 && groupId < tmxObjects.size()) {
            TMXObject grp = tmxObjects.get(groupId);
            if (objectId >= 0 && objectId < grp.objects.size()) {
                TMXGroupObject object = grp.objects.get(objectId);
                return object.getY();
            }
        }
        return -1;
    }

    /**
     * Retrieves the width of a specific object from a specific {@link TMXObject}.
     *
     * @param groupId
     *      index of a {@link TMXObject}
     * @param objectId
     *      index of an object
     * @return
     *      the width of an object,
     *      or -1 when error occurred
     */
    public int getObjectW(int groupId, int objectId) {
        if (groupId >= 0 && groupId < tmxObjects.size()) {
            TMXObject grp = tmxObjects.get(groupId);
            if (objectId >= 0 && objectId < grp.objects.size()) {
                TMXGroupObject object = grp.objects.get(objectId);
                return object.getW();
            }
        }
        return -1;
    }

    /**
     * Retrieves the height of a specific object from a specific {@link TMXObject}.
     *
     * @param groupId
     *      index of a {@link TMXObject}
     * @param objectId
     *      index of an object
     * @return
     *      the height of an object,
     *      or -1 when error occurred
     */
    public int getObjectH(int groupId, int objectId) {
        if (groupId >= 0 && groupId < tmxObjects.size()) {
            TMXObject grp = tmxObjects.get(groupId);
            if (objectId >= 0 && objectId < grp.objects.size()) {
                TMXGroupObject object = grp.objects.get(objectId);
                return object.getH();
            }
        }
        return -1;
    }

    /**
     * Retrieves the image source property for a given {@link TMXObject}.
     *
     * @param groupId
     *      index of a {@link TMXObject}
     * @param objectId
     *      index of an object
     * @return
     *      the image source reference,
     *      or <code>null</code> if one isn't defined
     */
    public String getObjectImage(int groupId, int objectId) {
        if (groupId >= 0 && groupId < tmxObjects.size()) {
            TMXObject grp = tmxObjects.get(groupId);
            if (objectId >= 0 && objectId < grp.objects.size()) {
                TMXGroupObject object = grp.objects.get(objectId);
                if (object == null) {
                    return null;
                }
                return object.getImage();
            }
        }
        return null;
    }

    /**
     * Retrieves the value of the property designed by the given name.
     * If no property is found, <code>def</code> is returned.
     *
     * @param groupId
     *      index of a {@link TMXObject}
     * @param objectId
     *      index of an object
     * @param propertyName
     *      name of a property
     * @param def
     *      default value to return if no property is found
     * @return
     *      the value of the property with the given name,
     *      or <code>def</code> if there is no property with that name
     */
    public String getObjectProperty(int groupId, int objectId, String propertyName, String def) {
        if (groupId >= 0 && groupId < tmxObjects.size()) {
            TMXObject grp = tmxObjects.get(groupId);
            if (objectId >= 0 && objectId < grp.objects.size()) {
                TMXGroupObject object = grp.objects.get(objectId);
                if (object == null) {
                    return def;
                }
                if (object.getProps() == null) {
                    return def;
                }
                return object.getProps().getProperty(propertyName, def);
            }
        }
        return def;
    }

    /**
     * Retrieves the index of the {@link TMXLayer} with given name.
     *
     * @param name
     *      the name of the {@link TMXTile} to search for
     * @return
     *      the index of the {@link TMXLayer},
     *      or -1 if there is no {@link TMXLayer} with given name
     */
    public int getLayerIndex(String name) {
        for (int i = 0; i < tmxLayers.size(); i++) {
            TMXLayer TMXLayer = (TMXLayer) tmxLayers.get(i);
            if (TMXLayer.getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}