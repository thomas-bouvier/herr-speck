package com.tomatrocho.game.world.level.tmx;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

public class TMXLayer {

    /**
     * The {@link TMXTileMap} this {@link TMXLayer} belongs to.
     */
    private final TMXTileMap map;

    /**
     * The index of this {@link TMXLayer}.
     */
    private int index;

    /**
     * The name of this {@link TMXLayer}.
     */
    private String name;

    /**
     * The width of this {@link TMXLayer}.
     */
    private int w;

    /**
     * The height of this {@link TMXLayer}.
     */
    private int h;

    /**
     * The {@link Properties} of this {@link TMXLayer}.
     */
    private Properties props;

    /**
     * The tile data representing this data.
     * index 0 : {@link TMXTileSet} index,
     * index 1 : tile localId,
     * index 2 : tile globalId.
     */
    private int[][][] data;


    /**
     * Constructor for the {@link TMXLayer} class.
     * Creates a new {@link TMXLayer} based on the TMX definition.
     *
     * @param element
     *      the XML element describing the {@link TMXLayer}
     * @param map
     *      the {@link TMXTileMap} this {@link TMXLayer} is part of
     * @throws Exception
     *      indicates a failure to parse the TMX layer
     */
    public TMXLayer(Element element, TMXTileMap map) throws Exception {
        this.map = map;
        name = element.getAttribute("name");
        w = Integer.parseInt(element.getAttribute("width"));
        h = Integer.parseInt(element.getAttribute("height"));
        data = new int[w][h][3];
        // acquire layer properties
        /**
        Element propsElement = (Element) element.getElementsByTagName("properties").item(0);
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
         */
        Element dataNode = (Element) element.getElementsByTagName("data").item(0);
        String encoding = dataNode.getAttribute("encoding");
        String compression = dataNode.getAttribute("compression");
        if (encoding.equals("base64") && compression.equals("gzip")) {
            try {
                Node cdata = dataNode.getFirstChild();
                char[] enc = cdata.getNodeValue().trim().toCharArray();
                byte[] dec = decodeBase64(enc);
                GZIPInputStream is = new GZIPInputStream(new ByteArrayInputStream(dec));
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        int tileGId = 0;
                        tileGId |= is.read();
                        tileGId |= is.read() <<  8;
                        tileGId |= is.read() << 16;
                        tileGId |= is.read() << 24;
                        if (tileGId == 0) {
                            data[x][y][0] = -1;
                            data[x][y][1] = 0;
                            data[x][y][2] = 0;
                        } else {
                            TMXTileSet set = map.getTileSetFromGId(tileGId);
                            if (set != null) {
                                data[x][y][0] = set.getIndex();
                                data[x][y][1] = tileGId - set.getFirstGId(); // localID
                            }
                            data[x][y][2] = tileGId; //globalID
                        }
                    }
                }
            } catch (IOException ex) {
                throw new Exception("Unable to decode base 64 block");
            }
        } else {
            NodeList tileNodes = dataNode.getElementsByTagName("tile");
            Element tile;
            int tileGId;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x <  w; x++) {
                    try {
                        tile = (Element) tileNodes.item(y * w + x);
                        tileGId = Integer.parseInt(tile.getAttribute("gid"));
                        TMXTileSet set = map.getTileSetFromGId(tileGId);
                        if (set != null) {
                            data[x][y][0] = set.getIndex();
                            data[x][y][1] = tileGId - set.getFirstGId(); // localID
                        }
                        data[x][y][2] = tileGId; //globalID
                    } catch (NullPointerException | NumberFormatException ex) {
                        data[x][y][0] = -1;
                        data[x][y][1] = 0;
                        data[x][y][2] = 0;
                    }
                }
            }
        }
    }

    /**
     * Retrieves the globalId of the {@link TMXTile} at the specified location in this {@link TMXLayer}.
     *
     * @param x
     *      x coordinate of the {@link TMXTile}
     * @param y
     *      y coordinate of the {@link TMXTile}
     * @return
     *      the globalId of the {@link TMXTile}
     */
    public int getTileGId(int x, int y) {
        return data[x][y][2];
    }

    /**
     * Sets the globalId of the {@link TMXTile} at a specified location in this {@link TMXLayer}.
     *
     * @param x
     *      x coordinate of the {@link TMXTile} to set
     * @param y
     *      y coordinate of the {@link TMXTile} to set
     * @param tileGId
     *      the globalId of the {@link TMXTile} to set
     */
    public void setTileID(int x, int y, int tileGId) {
        if (tileGId == 0) {
            data[x][y][0] = -1;
            data[x][y][1] = 0;
            data[x][y][2] = 0;
        } else {
            TMXTileSet set = map.getTileSetFromGId(tileGId);
            data[x][y][0] = set.getIndex();
            data[x][y][1] = tileGId - set.getFirstGId(); //localID
            data[x][y][2] = tileGId; //globalID
        }
    }

    /**
     * The code used to decode Base64 encoding.
     */
    private static byte[] baseCodes = new byte[256];

    /**
     * Static initializer for the codes created against Base64.
     */
    static {
        for (int i = 0; i < 256; i++) {
            baseCodes[i] = -1;
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            baseCodes[i] = (byte) (i - 'A');
        }
        for (int i = 'a'; i <= 'z'; i++) {
            baseCodes[i] = (byte) (26 + i - 'a');
        }
        for (int i = '0'; i <= '9'; i++) {
            baseCodes[i] = (byte) (52 + i - '0');
        }
        baseCodes['+'] = 62;
        baseCodes['/'] = 63;
    }

    /**
     * Decodes a Base64 string.
     *
     * @param data
     *      the string of character to decode
     * @return
     *      the byte array represented by character encoding
     */
    private byte[] decodeBase64(char[] data) {
        int temp = data.length;
        for (char c : data) {
            if ((c > 255) || baseCodes[c] < 0) {
                --temp;
            }
        }
        int len = (temp / 4) * 3;
        if ((temp % 4) == 3) {
            len += 2;
        }
        if ((temp % 4) == 2) {
            len += 1;
        }
        byte[] out = new byte[len];
        int shift = 0;
        int accum = 0;
        int index = 0;
        for (char c : data) {
            int value = (c > 255) ? -1 : baseCodes[c];
            if (value >= 0) {
                accum <<= 6;
                shift += 6;
                accum |= value;
                if (shift >= 8) {
                    shift -= 8;
                    out[index++] = (byte) ((accum >> shift) & 0xff);
                }
            }
        }
        if (index != out.length) {
            throw new RuntimeException(String.format("Data length appears to be wrong (wrote %s should be %s)", index, out.length));
        }
        return out;
    }

    /**
     * Retrieves the base codes of this {@link TMXLayer}.
     *
     * @return
     *      the base codes of this {@link TMXLayer}
     */
    public static byte[] getBaseCodes() {
        return baseCodes;
    }

    /**
     * Removes the {@link TMXTile} at the specified location from this {@link TMXLayer}.
     *
     * @param x
     *      x coordinate of the {@link TMXTile} to remove
     * @param y
     *      y coordinate of the {@link TMXTile} to remove
     */
    public void removeTile(int x, int y){
        this.data[x][y][0] = -1;
    }

    /**
     * Sets the index of this {@link TMXLayer}.
     *
     * @param index
     *      the new index of this {@link TMXLayer}
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Retrieves the {@link TMXTileMap} linked to this {@link TMXLayer}.
     *
     * @return
     *      the {@link TMXTileMap} linked to this {@link TMXLayer}
     */
    public TMXTileMap getTileMap() {
        return map;
    }

    /**
     * Retrieves the index of this {@link TMXLayer}.
     *
     * @return
     *      the index of this {@link TMXLayer}
     */
    public int getIndex() {
        return index;
    }

    /**
     * Retrieves the name of this {@link TMXLayer}.
     *
     * @return
     *      the name of this {@link TMXLayer}
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the width of this {@link TMXLayer}.
     *
     * @return
     *      the width of this {@link TMXLayer}, in tiles
     */
    public int getW() {
        return w;
    }

    /**
     * Retrieves the height of this {@link TMXLayer}.
     *
     * @return
     *      the height of this {@link TMXLayer}, in tiles
     */
    public int getH() {
        return h;
    }

    /**
     * Retrieves the data of this {@link TMXLayer}.
     *
     * @return
     *      the data of this {@link TMXLayer}
     */
    public int[][][] getData() {
        return data;
    }

    /**
     * Retrieves the {@link Properties} of this {@link TMXLayer}.
     *
     * @return
     *      the {@link Properties} of this {@link TMXLayer}
     */
    public Properties getProps() {
        return props;
    }
}