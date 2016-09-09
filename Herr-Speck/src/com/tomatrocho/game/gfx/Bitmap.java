package com.tomatrocho.game.gfx;

import java.awt.Color;
import java.util.Arrays;

public class Bitmap implements IAbstractBitmap {

    /**
     *
     */
    protected int w;

    /**
     *
     */
    protected int h;

    /**
     *
     */
    protected int[] pixels;


    /**
     * Constructor for the {@link Bitmap} class.
     *
     * @param w
     * @param h
     */
    public Bitmap(int w, int h) {
        this.w = w;
        this.h = h;
        pixels = new int[w * h];
    }

    /**
     * Constructor for the {@link Bitmap} class.
     *
     * @param w
     * @param h
     * @param pixels
     */
    public Bitmap(int w, int h, int[] pixels) {
        this.w = w;
        this.h = h;
        this.pixels = pixels;
    }

    /**
     *
     * @param pixels2d
     */
    public Bitmap(int[][] pixels2d) {
        w = pixels2d.length;
        if (w > 0) {
            h = pixels2d[0].length;
            pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    pixels[y * w + x] = pixels2d[x][y];
                }
            }
        } else {
            h = 0;
            pixels = new int[0];
        }
    }

    @Override
    public int getW() {
        return w;
    }

    @Override
    public int getH() {
        return h;
    }

    @Override
    public IAbstractBitmap copy() {
        Bitmap bm = new Bitmap(w, h);
        bm.pixels = pixels.clone();
        return bm;
    }

    @Override
    public void clear(int color) {
        Arrays.fill(pixels, color);
    }

    @Override
    public int blendPixels(int backgroundColor, int pixelToBlendColor) {
        final int alphaBlend = (pixelToBlendColor >> 24) & 0xff;
        final int alphaBlendBackground = 256 - alphaBlend;
        final int rr = backgroundColor & 0xff0000;
        final int gg = backgroundColor & 0xff00;
        final int bb = backgroundColor & 0xff;
        int r = pixelToBlendColor & 0xff0000;
        int g = pixelToBlendColor & 0xff00;
        int b = pixelToBlendColor & 0xff;
        r = ((r * alphaBlend + rr * alphaBlendBackground) >> 8) & 0xff0000;
        g = ((g * alphaBlend + gg * alphaBlendBackground) >> 8) & 0xff00;
        b = ((b * alphaBlend + bb * alphaBlendBackground) >> 8) & 0xff;
        return 0xff000000 | r | g | b;
    }

    @Override
    public void blit(IAbstractBitmap bitmap, int x, int y) {
        Bitmap bmp = (Bitmap) bitmap;
        Rect blitArea = new Rect(x, y, bmp.w, bmp.h);
        adjustBlitArea(blitArea);
        int blitW = blitArea.getBottomRightX() - blitArea.getTopLeftX();
        for (int yy = blitArea.getTopLeftY(); yy < blitArea.getBottomRightY(); yy++) {
            // coordinate on the big bitmap
            int tp = yy * w + blitArea.getTopLeftX();
            // coordinate on the small bitmap
            int sp = (yy - y) * bmp.w + (blitArea.getTopLeftX() - x);
            for (int xx = 0; xx < blitW; xx++) {
                int col = bmp.pixels[sp + xx];
                int alpha = (col >> 24) & 0xff;
                if (alpha == 255) {
                    if (col != 0xffff00ff) {
                        pixels[tp + xx] = col;
                    }
                } else {
                    pixels[tp + xx] = blendPixels(pixels[tp + xx], col);
                }
                /*
                if (sp == 0 || xx == 0) {
                	pixels[tp + xx] = 0xffff678b;
                }
                */
            }
        }
    }

    @Override
    public void blit(IAbstractBitmap bitmap, int x, int y, int w, int h) {
        Bitmap bmp = (Bitmap) bitmap;
        Rect blitArea = new Rect(x, y, w, h);
        adjustBlitArea(blitArea);
        int blitW = blitArea.getBottomRightX() - blitArea.getTopLeftX();
        for (int yy = blitArea.getTopLeftY(); yy < blitArea.getBottomRightY(); yy++) {
            // coordinate on the big bitmap
            int tp = yy * this.w + blitArea.getTopLeftX();
            // coordinate on the small bitmap
            int sp = (yy - y) * bmp.w + (blitArea.getTopLeftX() - x);
            for (int xx = 0; xx < blitW; xx++) {
                int col = bmp.pixels[sp + xx];
                int alpha = (col >> 24) & 0xff;
                if (alpha == 255) {
                    if (col != 0xffff00ff) {
                        pixels[tp + xx] = col;
                    }
                } else {
                    pixels[tp + xx] = blendPixels(pixels[tp + xx], col);
                }
            }
        }
    }

    @Override
    public void alphaBlit(IAbstractBitmap bitmap, int x, int y, int alpha) {
        if (alpha == 255) {
            blit(bitmap, x, y);
            return;
        }
        Bitmap bmp = (Bitmap) bitmap;
        Rect blitArea = new Rect(x, y, bmp.w, bmp.h);
        adjustBlitArea(blitArea);
        int blitW = blitArea.getBottomRightX() - blitArea.getTopLeftX();
        for (int yy = blitArea.getTopLeftY(); yy < blitArea.getBottomRightY(); yy++) {
            // coordinate on the big bitmap
            int tp = yy * w + blitArea.getTopLeftX();
            // coordinate on the small bitmap
            int sp = (yy - y) * bmp.w + (blitArea.getTopLeftX() - x);
            for (int xx = 0; xx < blitW; xx++) {
                int col = bmp.pixels[sp + xx];
                if (col < 0) {
                    int r = (col & 0xff0000);
                    int g = (col & 0xff00);
                    int b = (col & 0xff);
                    col = (alpha << 24) | r | g | b;
                    pixels[tp + xx] = blendPixels(pixels[tp + xx], col);
                }
            }
        }
    }

    @Override
    public void colorBlit(IAbstractBitmap bitmap, int x, int y, int color) {
        Bitmap bmp = (Bitmap) bitmap;
        Rect blitArea = new Rect(x, y, bmp.w, bmp.h);
        adjustBlitArea(blitArea);
        int blitW = blitArea.getBottomRightX() - blitArea.getTopLeftX();
        int a2 = (color >> 24) & 0xff;
		int a1 = 256 - a2;
		int rr = color & 0xff0000;
		int gg = color & 0xff00;
		int bb = color & 0xff;
        for (int yy = blitArea.getTopLeftY(); yy < blitArea.getBottomRightY(); yy++) {
            // coordinate on the big bitmap
            int tp = yy * w + blitArea.getTopLeftX();
            // coordinate on the small bitmap
            int sp = (yy - y) * bmp.w + (blitArea.getTopLeftX() - x);
            for (int xx = 0; xx < blitW; xx++) {
            	int col = bmp.pixels[sp + xx];
                if (col < 0) {
                	int r = (col & 0xff0000);
					int g = (col & 0xff00);
					int b = (col & 0xff);
					r = ((r * a1 + rr * a2) >> 8) & 0xff0000;
					g = ((g * a1 + gg * a2) >> 8) & 0xff00;
					b = ((b * a1 + bb * a2) >> 8) & 0xff;
					pixels[tp + xx] = 0xff000000 | r | g | b;
                }
            }
        }
    }

    /**
     *
     * @param blitArea
     */
    public void adjustBlitArea(Rect blitArea) {
        if (blitArea.getTopLeftX() < 0) {
            blitArea.setTopLeftX(0);
        }
        if (blitArea.getTopLeftY() < 0) {
            blitArea.setTopLeftY(0);
        }
        if (blitArea.getBottomRightX() > w) {
            blitArea.setBottomRightX(w);
        }
        if (blitArea.getBottomRightY() > h) {
            blitArea.setBottomRightY(h);
        }
    }

    @Override
    public void fill(int x, int y, int w, int h, int color) {
        Rect blitArea = new Rect(x, y, w, h);
        adjustBlitArea(blitArea);
        int blitW = blitArea.getBottomRightX() - blitArea.getTopLeftX();
        for (int yy = blitArea.getTopLeftY(); yy < blitArea.getBottomRightY(); yy++) {
            int tp = yy * w + blitArea.getTopLeftX();
            for (int xx = 0; xx < blitW; xx++) {
                pixels[tp + xx] = color;
            }
        }
    }

    @Override
    public void alphaFill(int x, int y, int w, int h, int color, int alpha) {
        if (alpha == 255) {
            fill(x, y, w, h, color);
            return;
        }
        Bitmap bmp = new Bitmap(w, h);
        bmp.fill(0, 0, w, h, color);
        alphaBlit(bmp, x, y, alpha);
    }

    @Override
    public IAbstractBitmap shrink() {
        Bitmap bmp = new Bitmap(w / 2, h / 2);
        int pos = 0;
        for (int i = 0; i < pixels.length; i++) {
            if (pos >= bmp.pixels.length) {
                break;
            }
            if (i % 2 == 0) {
                bmp.pixels[pos++] = pixels[i];
            }
            if (i % w == 0) {
                i += w;
            }
        }
        return bmp;
    }

    @Override
    public IAbstractBitmap scale(int w, int h) {
        Bitmap bmp = new Bitmap(w, h);
        final int scaleRatioW = (this.w << 16) / w;
        final int scaleRatioH = (this.h << 16) / h;
        int pos = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                bmp.pixels[pos++] = pixels[((y * scaleRatioH) >> 16) * w + ((x * scaleRatioW) >> 16)];
            }
        }
        return null;
    }

    @Override
    public void rectangle(int x, int y, int w, int h, int color) {
        int x0 = x;
        int x1 = x + w;
        int y0 = y;
        int y1 = y + h;
        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (x1 > w) {
            x1 = w;
        }
        if (y1 > h) {
            y1 = h;
        }
        for (int yy = y0; yy < y1; yy++) {
            setPixel(x0, yy, color);
            setPixel(x1 - 1, yy, color);
        }
        for (int xx = x0; xx < x1; xx++) {
            setPixel(xx, y0, color);
            setPixel(xx, y1 - 1, color);
        }
    }

    @Override
    public void setPixel(int pos, int color) {
        if (pos >= 0 && pos < pixels.length) {
            pixels[pos] = color;
        }
    }

    @Override
    public void setPixel(int x, int y, int color) {
        final int pos = y * w + x;
        if (pos >= 0 && pos < pixels.length) {
            pixels[pos] = color;
        }
    }
    
    @Override
    public void setPixel(int pos, Color color) {
    	setPixel(pos, color.getRGB());
    }
    
    @Override
    public void setPixel(int x, int y, Color color) {
    	setPixel(x, y, color.getRGB());
    }

    @Override
    public int getPixel(int pos) {
        return pixels[pos];
    }

    @Override
    public int getPixelSize() {
        return pixels.length;
    }
}