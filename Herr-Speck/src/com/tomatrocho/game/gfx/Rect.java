package com.tomatrocho.game.gfx;

public class Rect {

    /**
     *
     */
    private int topLeftX;

    /**
     *
     */
    private int topLeftY;

    /**
     *
     */
    private int bottomRightX;

    /**
     *
     */
    private int bottomRightY;

    /**
     *
     */
    private int w;

    /**
     *
     */
    private int h;


    /**
     * Constructor for the {@link Rect} class.
     *

     * @param x
     * @param y
     * @param w
     * @param h
     */
    public Rect(int x, int y, int w, int h) {
        this.w = w;
        this.h = h;
        topLeftX = x;
        topLeftY = y;
        bottomRightX = topLeftX + w;
        bottomRightY = topLeftY + h;
    }

    /**
     *
     * @param topLeftX
     */
    public void setTopLeftX(int topLeftX) {
        this.topLeftX = topLeftX;
    }

    /**
     *
     * @param topLeftY
     */
    public void setTopLeftY(int topLeftY) {
        this.topLeftY = topLeftY;
    }

    /**
     *
     * @param bottomRightX
     */
    public void setBottomRightX(int bottomRightX) {
        this.bottomRightX = bottomRightX;
    }

    /**
     *
     * @param bottomRightY
     */
    public void setBottomRightY(int bottomRightY) {
        this.bottomRightY = bottomRightY;
    }

    /**
     *
     * @param w
     */
    public void setW(int w) {
        bottomRightX = topLeftX + w;
        this.w = w;
    }

    /**
     *
     * @param h
     */
    public void setH(int h) {
        bottomRightY = topLeftY + h;
        this.h = h;
    }

    /**
     *
     * @return
     */
    public int getTopLeftX() {
        return topLeftX;
    }

    /**
     *
     * @return
     */
    public int getTopLeftY() {
        return topLeftY;
    }

    /**
     *
     * @return
     */
    public int getBottomRightX() {
        return bottomRightX;
    }

    /**
     *
     * @return
     */
    public int getBottomRightY() {
        return bottomRightY;
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
}
