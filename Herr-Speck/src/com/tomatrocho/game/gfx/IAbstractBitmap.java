package com.tomatrocho.game.gfx;

public interface IAbstractBitmap {

    //TODO fonctions miroirs

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

    /**
     *
     * @return
     */
    public IAbstractBitmap copy();

    /**
     *
     * @param color
     */
    public void clear(int color);

    /**
     *
     * @param backgroundColor
     * @param pixelToBlendColor
     * @return
     */
    public int blendPixels(int backgroundColor, int pixelToBlendColor);

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
     * @param alpha
     */
    public void alphaBlit(IAbstractBitmap bitmap, int x, int y, int alpha);

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
     * @return
     */
    public IAbstractBitmap shrink();

    /**
     *
     * @param w
     * @param h
     * @return
     */
    public IAbstractBitmap scale(int w, int h);

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
     * @param pos
     * @param color
     */
    public void setPixel(int pos, int color);

    /**
     *
     * @param x
     * @param y
     * @param color
     */
    public void setPixel(int x, int y, int color);

    /**
     *
     * @param pos
     * @return
     */
    public int getPixel(int pos);

    /**
     *
     * @return
     */
    public int getPixelSize();
}