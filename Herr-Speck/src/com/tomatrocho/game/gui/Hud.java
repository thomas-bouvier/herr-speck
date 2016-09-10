package com.tomatrocho.game.gui;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.gfx.IAbstractScreen;

public class Hud {
	
	/**
	 * 
	 */
	private final static int GAP = 5;
	
	/**
	 *
	 */
	public enum TextLocation {
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT
	}

	/**
	 * 
	 */
	private IAbstractScreen screen;
	
	/**
	 * 
	 */
	private Font font = Font.getDefaultFont();
	
	/**
	 * 
	 */
	private Map<String, TextLocation> strings = new LinkedHashMap<>();
	
	
	/**
	 * 
	 * @param screen
	 */
	public Hud(IAbstractScreen screen) {
		this.screen = screen;
	}
	
	/**
	 * 
	 * @param string
	 */
	public void add(String string) {
		add(string, TextLocation.TOP_LEFT);
	}
	
	/**
	 * 
	 * @param string
	 * @param textLocation
	 */
	public void add(String string, TextLocation textLocation) {
		strings.put(string, textLocation);
	}
	
	/**
	 * 
	 */
	public void clear() {
		strings.clear();
	}
	
	/**
	 * 
	 */
	public void render() {
		int topLeftHeight = font.getGlyphHeight();
		int topRightHeight = font.getGlyphHeight();
		int bottomLeftHeight = HerrSpeck.H - font.getGlyphHeight() - 7;
		int bottomRightHeight = HerrSpeck.H - font.getGlyphHeight() - 7;
		
		for (Entry<String, TextLocation> entry : strings.entrySet()) {
			switch (entry.getValue()) {
			default:
			case TOP_LEFT:
				font.draw(screen, entry.getKey(), GAP, topLeftHeight);
				topLeftHeight += font.getGlyphHeight() + 1;
				break;
				
			case TOP_RIGHT:
				font.draw(screen, entry.getKey(), HerrSpeck.W - font.calculateStringWidth(entry.getKey()) - GAP, topRightHeight);
				topRightHeight += font.getGlyphHeight() + 1;
				break;
				
			case BOTTOM_LEFT:
				font.draw(screen, entry.getKey(), GAP, bottomLeftHeight);
				bottomLeftHeight -= font.getGlyphHeight() + 1;
				break;
				
			case BOTTOM_RIGHT:
				font.draw(screen, entry.getKey(), HerrSpeck.W - font.calculateStringWidth(entry.getKey()) - GAP, bottomRightHeight);
				bottomRightHeight -= font.getGlyphHeight() + 1;
				break;
			}
			
		}
		
		clear();
	}
}
