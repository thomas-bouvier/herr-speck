package com.tomatrocho.game.gfx;

import java.awt.image.BufferedImage;

public interface IAbstractScreen {

    /**
     *
     * @param pathFile
     * @return
     */
    public IAbstractBitmap load(String pathFile);

    /**
     *
     * @param image
     * @return
     */
    public IAbstractBitmap load(BufferedImage image);

    /**
     *
     * @param color
     */
    public void clear(int color);

    /**
     *
     */
    public void loadResources();

    /**
     *
     * @param xOffset
     * @param yOffset
     */
    public void setOffset(int xOffset, int yOffset);

    /**
     *
     * @param w
     * @param h
     * @return
     */
    public IAbstractBitmap createBitmap(int w, int h);

    /**
     *
     * @param pixels2d
     * @return
     */
    public IAbstractBitmap createBitmap(int[][] pixels2d);
    
    /**
     * 
     * @param bitmap
     * @param x
     * @param y
     */
    public void blit(IAbstractBitmap bitmap, double x, double y);

    /**
     *
     * @param bitmap
     * @param x
     * @param y
     */
    public void blit(IAbstractBitmap bitmap, int x, int y);

    /**
     *
     * @param bitmap
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void blit(IAbstractBitmap bitmap, int x, int y, int w, int h);

    /**
     *
     * @param bitmap
     * @param x
     * @param y
     * @param color
     */
    public void colorBlit(IAbstractBitmap bitmap, int x, int y, int color);

    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param color
     */
    public void fill(int x, int y, int w, int h, int color);

    /**
     *
     * @param bitmap
     * @param x
     * @param y
     * @param alpha
     */
    public void alphaBlit(IAbstractBitmap bitmap, int x, int y, int alpha);

    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param color
     * @param alpha
     */
    public void alphaFill(int x, int y, int w, int h, int color, int alpha);

    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param color
     */
    public void rectangle(int x, int y, int w, int h, int color);

    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param color
     * @return
     */
    public IAbstractBitmap rectangleBitmap(int x, int y, int w, int h, int color) ;

    /**
     *
     * @param pathFile
     * @param w
     * @param h
     * @return
     */
    public IAbstractBitmap[][] cut(String pathFile, int w, int h);

    /**
     *
     * @param pathFile
     * @param w
     * @param h
     * @param bx
     * @param by
     * @return
     */
    public IAbstractBitmap[][] cut(String pathFile, int w, int h, int bx, int by);
    
    /**
     * 
     * @param pathFile
     * @param h
     * @return
     */
    public IAbstractBitmap[][] cut(String pathFile, int h);

    /**
     *
     * @param tiles
     * @return
     */
    public int[][] getColors(IAbstractBitmap[][] tiles);

    /**
     *
     * @param bitmap
     * @return
     */
    public int getColor(IAbstractBitmap bitmap);

    /**
     *
     * @param bitmap
     * @return
     */
    public IAbstractBitmap shrink(IAbstractBitmap bitmap);

    /**
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public IAbstractBitmap scale(IAbstractBitmap bitmap, int w, int h);


    /**
     *
     * @return
     */
    public int getW();

    /**
     *
     * @return
     */
    public int getH();
}
