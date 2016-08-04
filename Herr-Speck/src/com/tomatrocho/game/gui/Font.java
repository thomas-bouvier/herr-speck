package com.tomatrocho.game.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.tomatrocho.game.HerrSpeck;
import com.tomatrocho.game.gfx.Art;
import com.tomatrocho.game.gfx.Bitmap;
import com.tomatrocho.game.gfx.IAbstractBitmap;
import com.tomatrocho.game.gfx.IAbstractScreen;

public class Font {

	/**
	 * The {@link Align} enum represents the horizontal text alignment.
	 */
	public enum Align {
		
		/**
		 * Represents text alignment to the left.
		 */
		LEFT,
		
		/**
		 * Represents centered text alignment.
		 */
		CENTER,
		
		/**
		 * Represents text alignment to the right.
		 */
		RIGHT
	}
	
	/**
	 * 
	 */
	public static final Font FONT;
	
	/**
	 * 
	 */
	public static Font DEFAULT_FONT;
	
	static {
		final String characters = "0123456789<>=*/+-             " +
								  "ABCDEFGHIJKLMNOPQRSTU         " +
								  "VWXYZƒ¿∆…Œœ‘÷‹«               " +
								  "abcdefghijklmnopqrstuvw       " +
								  "xyz‡‚‰ÊÈËÍÎÓÔÙˆ˘˚¸Á           " +
								  ".,'\"(){}[]!?:\\%              ";
		final int glyphHeight = 9;
		final int systemFontHeight = 10;
		final int heightOffset = systemFontHeight - glyphHeight + 2;
		final int letterSpacing = 1;
		FONT = new Font(Art.font, characters, glyphHeight, heightOffset, letterSpacing, systemFontHeight);
		
		setDefaultFont(FONT);
	}
	
	/**
	 * 
	 */
	private String characters;
	
	/**
	 * 
	 */
	private IAbstractBitmap[][] bitmap;
	
	/**
	 * 
	 */
	private int glyphHeight;
	
	/**
	 * 
	 */
	private int heightOffset;
	
	/**
	 * 
	 */
	private int letterSpacing;
	
	/**
	 * 
	 */
	private int systemFontHeight;
	
	
	/**
	 * 
	 * @param bitmap
	 * @param characters
	 * @param glyphHeight
	 * @param letterSpacing
	 */
	private Font(IAbstractBitmap[][] bitmap, String characters, int glyphHeight, int heightOffset, int letterSpacing, int systemFontHeight) {
		this.bitmap = bitmap;
		this.characters = characters;
		this.glyphHeight = glyphHeight;
		this.heightOffset = heightOffset;
		this.letterSpacing = letterSpacing;
		this.systemFontHeight = systemFontHeight;
	}
	
	/**
	 * 
	 * @param screen
	 * @param text
	 * @param x
	 * @param y
	 * @param w
	 */
	public void draw(IAbstractScreen screen, String text, int x, int y, int w) {
		final int startX = x;
		for (int i = 0; i < text.length(); i++) {
			final char character = text.charAt(i);
			final IAbstractBitmap sprite = getCharacterSprite(character);
			int heightOffset = 0;
			if (characters.indexOf(character) < 0) {
				heightOffset = getHeightOffset(character);
			}
			screen.blit(sprite, x, y + heightOffset);
			x += sprite.getW() + letterSpacing;
			if (x + sprite.getW() > w) {
				x = startX;
				y += glyphHeight + 2;
			}
		}
	}
	
	/**
	 * 
	 * @param screen
	 * @param text
	 * @param x
	 * @param y
	 */
	public void draw(IAbstractScreen screen, String text, int x, int y) {
		draw(screen, text, x, y, Integer.MAX_VALUE);
	}
	
	/**
	 * 
	 * @param screen
	 * @param text
	 * @param x
	 * @param y
	 * @param align
	 */
	public void draw(IAbstractScreen screen, String text, int x, int y, Align align) {
		switch (align) {
		case LEFT :
			draw(screen, text, x, y);
			break;
		case CENTER :
			draw(screen, text, x - calculateStringWidth(text) / 2, y);
			break;
		case RIGHT :
			draw(screen, text, x - calculateStringWidth(text), y);
			break;
		default :
			break;
		}
	}
	
	/**
	 * 
	 * @param screen
	 * @param text
	 * @param x
	 * @param y
	 * @param align
	 */
	public void draw(IAbstractScreen screen, String text, double x, double y, Align align) {
		draw(screen, text, (int) x, (int) y, align);
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	public int calculateStringWidth(String text) {
		int w = 0;
		for (int i = 0; i < text.length(); i++) {
			w += getCharacterSprite(text.charAt(i)).getW() + letterSpacing;
		}
		w -= letterSpacing;
		return w;
	}
	
	/**
	 * 
	 * @param character
	 * @return
	 */
	private IAbstractBitmap getCharacterSprite(char character) {
		int charPosition = characters.indexOf(character);
		if (charPosition >= 0) {
			return bitmap[charPosition % 30][charPosition / 30];
		}
		return buildCharacterSprite(character);
	}
	
	/**
	 * Stores already builed sprites for missing characters.
	 */
	private Map<Character, IAbstractBitmap> characterCache = new HashMap<>();
	
	/**
	 * 
	 */
	private Map<Character, Integer> characterHeightOffset = new HashMap<>();
	
	
	/**
	 * Builds the {@link Bitmap} of a character if the latter is missing
	 * provided font spritesheet.
	 * 
	 * @param character
	 * @return
	 */
	public IAbstractBitmap buildCharacterSprite(char character) {
		if (characterCache.containsKey(character)) {
			return characterCache.get(character);
		}
		java.awt.Font font = new java.awt.Font("SansSerif", java.awt.Font.BOLD, systemFontHeight);
		// drawing the character
		final int size = font.getSize() * 3;
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		g2.setFont(font);
		g2.setColor(Color.MAGENTA);
		g2.drawString(Character.toString(character), font.getStyle(), 2 * font.getSize());
		g2.dispose();
		// building the sprite
		int[][] pixels2d = new int[size][size];
		for (int y = size - 1; y >= 0; y--) {
			for (int x = size - 1; x >= 0; x--) {
				if (image.getRGB(x, y) != 0) {
					pixels2d[x][y] = 0xffffffff;
				}
			}
		}
		int emptyRowsTop = 0;
		top :
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				if (pixels2d[x][y] != 0) {
					break top;
				}
			}
			emptyRowsTop++;
		}
		characterHeightOffset.put(character, emptyRowsTop - font.getSize() - heightOffset);
		IAbstractBitmap sprite = HerrSpeck.getScreen().createBitmap(cropCharacterSprite(pixels2d));
		characterCache.put(character, sprite);
		return sprite;
	}
	
	/**
	 * 
	 * @param pixels2d
	 * @return
	 */
	private int[][] cropCharacterSprite(int[][] pixels2d) {
		final int w = pixels2d.length;
		final int h = pixels2d[0].length;
		int emptyRowsTop = 0;
		top :
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (pixels2d[x][y] != 0) {
					break top;
				}
			}
			emptyRowsTop++;
		}
		int emptyRowsBottom = 0;
		bottom :
		for (int y = h - 1; y >= 0; y--) {
			for (int x = 0; x < w; x++) {
				if (pixels2d[x][y] != 0) {
					break bottom;
				}
			}
			emptyRowsBottom++;
		}
		int emptyColsLeft = 0;
		left :
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if (pixels2d[x][y] != 0) {
					break left;
				}
			}
			emptyColsLeft++;
		}
		int emptyColsRight = 0;
		right :
		for (int x = w - 1; x >= 0; x--) {
			for (int y = 0; y < h; y++) {
				if (pixels2d[x][y] != 0) {
					break right;
				}
			}
			emptyColsRight++;
		}
		if (emptyRowsTop + emptyRowsBottom >= h || emptyColsLeft + emptyColsRight >= w) {
			return new int[0][0];
		}
		int[][] pixels2dCropped = new int[w - emptyColsLeft - emptyColsRight][h - emptyRowsTop - emptyRowsBottom];
		for (int y = emptyRowsTop; y < h - emptyRowsBottom; y++) {
			for (int x = emptyColsLeft; x < w - emptyColsRight; x++) {
				pixels2dCropped[x - emptyColsLeft][y - emptyRowsTop] = pixels2d[x][y]; 
			}
		}
		return pixels2dCropped;
	}
	
	public int getHeightOffset(char character) {
		if (!characterHeightOffset.containsKey(character)) {
			buildCharacterSprite(character);
		}
		return characterHeightOffset.get(character);
	}
	
	/**
	 * Getter for the default {@link Font}.
	 * 
	 * @return
	 * 		the default {@link Font} object
	 */
	public static Font getDefaultFont() {
		return DEFAULT_FONT;
	}

	/**
	 * Setter for the default {@link Font}.
	 * 
	 * @param defaultFont
	 * 		the {@link Font} object to set by default
	 */
	public static void setDefaultFont(Font font) {
		DEFAULT_FONT = font;
	}
}
