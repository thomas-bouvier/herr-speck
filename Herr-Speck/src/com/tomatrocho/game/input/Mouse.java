package com.tomatrocho.game.input;

public class Mouse {
	
	/**
	 * Delay before hiding the mouse cursor
	 */
	public static final int HIDE_DELAY = 60;
	
	/**
	 * Indicates whether the mouse cursor moved at the last tick.
	 */
	private boolean moved = false;
	
	/**
	 * Indicates whether the mouse was pressed at the last tick.
	 */
	private boolean pressed = false;

	/**
	 * Indicates whether the mouse cursor is hidden.
	 */
	private boolean hidden = false;
	
	/**
	 * 
	 */
	private int hideTime = 0;
	
    /**
     *
     */
    private boolean[] currentState = new boolean[4];

    /**
     *
     */
    private boolean[] nextState = new boolean[4];

    /**
     *
     */
    private int x;

    /**
     *
     */
    private int y;


    /**
     *
     */
    public void tick() {
        System.arraycopy(nextState, 0, currentState, 0, currentState.length);
    }

    /**
     *
     * @param x
     * @param y
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     */
    public void releaseAll() {
        for (int i = 0; i < nextState.length; i++) {
            nextState[i] = false;
        }
    }

    /**
     *
     * @param button
     * @param state
     */
    public void setNextState(int button, boolean state) {
        if (button > 3) {
            return;
        }
        nextState[button] = state;
    }
    
    /**
     * 
     */
    public void incrementHideTime() {
    	hideTime++;
    }
    
    /**
     * 
     * @param moved
     */
    public void setMoved(boolean moved) {
    	this.moved = moved;
    }
    
    /**
     * 
     * @param pressed
     */
    public void setPressed(boolean pressed) {
    	this.pressed = pressed;
    }
    
    /**
     * 
     * @return
     */
    public boolean moved() {
    	return moved;
    }
    
    /**
     * 
     * @return
     */
    public boolean pressed() {
    	return pressed;
    }
    
    /**
     * 
     * @param hideTime
     */
    public void setHideTime(int hideTime) {
    	this.hideTime = hideTime;
    }
    
    /**
     * 
     * @return
     */
    public int getHideTime() {
    	return hideTime;
    }
    
    /**
     * 
     * @param hidden
     */
    public void setHidden(boolean hidden) {
    	this.hidden = hidden;
    }
    
    /**
     * 
     * @return
     */
    public boolean isHidden() {
    	return hidden;
    }

    /**
     *
     * @param button
     * @return
     */
    public boolean isDown(int button) {
        return currentState[button];
    }

    /**
     *
     * @param button
     * @return
     */
    public boolean isPressed(int button) {
        return !currentState[button] && nextState[button];
    }

    /**
     *
     * @param button
     * @return
     */
    public boolean isReleased(int button) {
        return currentState[button] && !nextState[button];
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
}
