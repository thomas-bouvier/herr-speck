package com.tomatrocho.game.input;

public class Mouse {
	
	/**
	 * 
	 */
	public static final int MAX_HIDE_TIME = 60;
	
	/**
	 * 
	 */
	private boolean moved = false;

	/**
	 * 
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
     * @return
     */
    public boolean moved() {
    	return moved;
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
