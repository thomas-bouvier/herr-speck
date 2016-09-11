package com.tomatrocho.game.gfx;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Screen extends Bitmap implements IAbstractScreen {

    /**
     * The {@link BufferedImage} linked to this {@link Screen}.
     */
    private BufferedImage image;

    /**
     * The number of pixels this {@link Screen} should be shifted on the x axis.
     */
    public int xOffset;

    /**
     * The number of pixels this {@link Screen} should be shifted on the y axis.
     */
    public int yOffset;


    /**
     * Constructor for the {@link Screen} class.
     *
     * @param w
     *      the width of the created {@link Screen}, in pixels
     * @param h
     *      the height of the created {@link Screen}, in pixels
     */
    public Screen(int w, int h) {
        super(w, h);
        
        this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        this.pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    @Override
    public IAbstractBitmap load(final String pathFile) {
        BufferedImage buffer;
        try {
            buffer = ImageIO.read(Screen.class.getResource(pathFile));
            return load(buffer);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }

    @Override
    public IAbstractBitmap load(BufferedImage buffer) {
        if (buffer == null)
            return null;
        
        final int w = buffer.getWidth();
        final int h = buffer.getHeight();
        return new Bitmap(w, h, buffer.getRGB(0, 0, w, h, null, 0, w));
    }

    @Override
    public void loadResources() {
        Art.loadAllRessources(this);
    }

    @Override
    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public IAbstractBitmap createBitmap(int w, int h) {
        return new Bitmap(w, h);
    }

    @Override
    public IAbstractBitmap createBitmap(int[][] pixels2d) {
        return new Bitmap(pixels2d);
    }

    @Override
    public void blit(IAbstractBitmap bitmap, int x, int y) {
        super.blit(bitmap, x - xOffset, y - yOffset);
    }
    
    @Override
    public void blit(IAbstractBitmap bitmap, double x, double y) {
        super.blit(bitmap, (int) x - xOffset, (int) y - yOffset);
    }

    @Override
    public void blit(IAbstractBitmap bitmap, int x, int y, int w, int h) {
        super.blit(bitmap, x - xOffset, y - yOffset, w, h);
    }
    
    @Override
    public void blit(IAbstractBitmap bitmap, double x, double y, int w, int h) {
        super.blit(bitmap, (int) x - xOffset, (int) y - yOffset, w, h);
    }

    @Override
    public void colorBlit(IAbstractBitmap bitmap, int x, int y, int color) {
        super.colorBlit(bitmap, x - xOffset, y - yOffset, color);
    }
    
    @Override
    public void colorBlit(IAbstractBitmap bitmap, double x, double y, int color) {
        super.colorBlit(bitmap, (int) x - xOffset, (int) y - yOffset, color);
    }
    
    @Override
    public void alphaBlit(IAbstractBitmap bitmap, int x, int y, int alpha) {
        super.alphaBlit(bitmap, x - xOffset, y - yOffset, alpha);
    }
    
    @Override
    public void alphaBlit(IAbstractBitmap bitmap, double x, double y, int alpha) {
        super.alphaBlit(bitmap, (int) x - xOffset, (int) y - yOffset, alpha);
    }

    @Override
    public void fill(int x, int y, int w, int h, int color) {
        super.fill(x - xOffset, y - yOffset, w, h, color);
    }

    @Override
    public void alphaFill(int x, int y, int w, int h, int color, int alpha) {
        super.alphaFill(x - xOffset, y - yOffset, w, h, color, alpha);
    }

    @Override
    public void rectangle(int x, int y, int width, int height, int color) {
        super.rectangle(x - xOffset, y - yOffset, width, height, color);
    }

    @Override
    public IAbstractBitmap rectangleBitmap(int x, int y, int w, int h, int color) {
        Bitmap bmp = new Bitmap(w, h);
        bmp.rectangle(x, y, w, h, color);
        return bmp;
    }

    @Override
    public IAbstractBitmap[][] cut(final String pathFile, int w, int h) {
        return cut(pathFile, w, h, 0, 0);
    }

    @Override
    public IAbstractBitmap[][] cut(final String pathFile, int w, int h, int bx, int by) {
        BufferedImage image = null;
        try {
        	image = ImageIO.read(Screen.class.getResource(pathFile));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (image != null) {
            final int xTiles = (image.getWidth() - bx) / w, yTiles = (image.getHeight() - by) / h;
            IAbstractBitmap[][] ret = new Bitmap[xTiles][yTiles];
            for (int y = 0; y < yTiles; y++) {
                for (int x = 0; x < xTiles; x++) {
                    ret[x][y] = new Bitmap(w, h, image.getRGB(bx + x * w, by + y * h, w, h, null, 0, w));
                }
            }
            return ret;
        }
        return null;
    }
    
    @Override
    public IAbstractBitmap[][] cut(final String pathFile, int h) {
    	BufferedImage image = null;
    	try {
    		image = ImageIO.read(Screen.class.getResource(pathFile));
    	}
    	catch (IOException ex) {
    		ex.printStackTrace();
    	}
    	
    	if (image != null) {
    		final int yTiles = image.getHeight() / h;
    		Bitmap[][] sprites = new Bitmap[yTiles][];
    		int xTiles = 0;
    		for (int y = 0; y < yTiles; y++) {
    			List<IAbstractBitmap> row = new ArrayList<>();
    			int xPosition = 0;
    			while (xPosition < image.getWidth()) {
    				int w = 0;
    				while (xPosition + w < image.getWidth() && image.getRGB(xPosition + w, y * h) != 0xff00ffff) {
    					w++;
    				}
    				
    				if (w > 0) {
    					Bitmap sprite = new Bitmap(w, h);
    					image.getRGB(xPosition, y * h, w, h, sprite.pixels, 0, w);
    					row.add(sprite);
    				}
    				
    				xPosition += w + 1;
    			}
    			
    			if (xTiles < row.size())
    				xTiles = row.size();
    			
    			sprites[y] = row.toArray(new Bitmap[0]);
    		}
    		
    		IAbstractBitmap[][] ret = new Bitmap[xTiles][yTiles];
    		for (int y = 0; y < yTiles; y++) {
    			for (int x = 0; x < xTiles; x++) {
    				try {
    					ret[x][y] = sprites[y][x];
    				}
    				catch (IndexOutOfBoundsException ex) {
    					ret[x][y] = null;
    				}
    			}
    		}
    		
    		return ret;
    	}
    	
    	return null;
    }

    @Override
    public int[][] getColors(IAbstractBitmap[][] tiles) {
        int[][] ret = new int[tiles.length][tiles[0].length];
        for (int y = 0; y < tiles[0].length; y++) {
            for (int x = 0; x < tiles.length; x++)
                ret[x][y] = getColor(tiles[x][y]);
        }
        
        return ret;
    }

    @Override
    public int getColor(IAbstractBitmap bitmap) {
        Bitmap bmp = (Bitmap) bitmap;
        int r = 0;
        int g = 0;
        int b = 0;
        
        for (int i = 0; i < bmp.pixels.length; i++) {
            int col = bmp.pixels[i];
            r += (col >> 16) & 0xff;
            g += (col >> 8) & 0xff;
            b += (col) & 0xff;
        }
        
        r /= bmp.pixels.length;
        g /= bmp.pixels.length;
        b /= bmp.pixels.length;
        
        return 0xff000000 | r << 16 | g << 8 | b;
    }

    @Override
    public IAbstractBitmap shrink(IAbstractBitmap bitmap) {
        return bitmap.shrink();
    }

    @Override
    public IAbstractBitmap scale(IAbstractBitmap bitmap, int w, int h) {
        return bitmap.scale(w, h);
    }

    /**
     * Retrieves the {@link BufferedImage} linked to this {@link Screen}.
     *
     * @return
     *      the {@link BufferedImage} linked to this {@link Screen}
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Creates a compatible {@link BufferedImage}.
     * 
     * Performance of writing an image to a screen is very much affected
     * by the format in which the image is stored. If the format is not
     * optimized for the screen memory, then a conversion must be done,
     * which can be very slow.
     * 
     * @return
     * 		the compatible {@link BufferedImage}
     */
    public BufferedImage toCompatibleFormat() {
        // getting the current system graphical settings
        GraphicsConfiguration gfxConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        
        // if image is already compatible and optimized for current system settings, simply return it
        if (image.getColorModel().equals(gfxConfig.getColorModel()))
            return image;
        
        // image is not optimized, so create a new image that is
        BufferedImage newImage = gfxConfig.createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());
        
        // getting the graphics context of the new image to draw the old image on
        Graphics2D g2d = (Graphics2D) newImage.getGraphics();
        
        // drawing the image and dispose of context no longer needed
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        
        // return the new optimized image
        return newImage;
    }

    @Override
    public int getW() {
        return w;
    }

    @Override
    public int getH() {
        return h;
    }
}
