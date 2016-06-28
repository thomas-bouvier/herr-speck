package com.tomatrocho.game.math;

/**
 *
 */
public class Vec2 implements Cloneable {

    /**
     * x coordinate of the {@link Vec2}.
     */
    public double x;

    /**
     * y coordinate of the {@link Vec2}.
     */
    public double y;


    /**
     * Default constructor for the {@link Vec2} class.
     */
    public Vec2() {
    	this.x = 0;
    	this.y = 0;
    }
    
    /**
     * Constructor for the {@link Vec2} class.
     *
     * @param x
     *      x coordinate of the {@link Vec2}
     * @param y
     *      x coordinate of the {@link Vec2}
     */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the specified coordinates to the {Vec2}.
     *
     * @param x
     *      x coordinate of the {@link Vec2}
     * @param y
     *      y coordinate of the {@link Vec2}
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retrieves the length of the {@link Vec2}.
     *
     * @return
     *      the length of the {@link Vec2}
     */
    public double length() {
        return java.lang.Math.sqrt(x * x + y * y);
    }

    /**
     * Normalizes the {@link Vec2}.
     *
     * @return
     *      the normalized {@link Vec2}
     */
    public Vec2 normalize() {
        double factor = 1 / length();
        x *= factor;
        y *= factor;
        return this;
    }

    /**
     *
     * @return
     */
    public boolean notNull() {
        return x != 0 || y != 0;
    }

    @Override
    public Vec2 clone() {
        return new Vec2(x, y);
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
