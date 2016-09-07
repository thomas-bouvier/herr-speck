package com.tomatrocho.generator;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Display extends JPanel {

	private static final long serialVersionUID = 1L;

	public WorldGenerator generator;
	
	private int[] pixels;
	private BufferedImage image;
	
	
	/**
	 * 
	 * @param generator
	 */
	public Display(WorldGenerator generator) {
		setMinimumSize(new Dimension(500, 500));
		setWorldGenerator(generator);
	}
	
	/**
	 * 
	 * @param generator
	 */
	public void setWorldGenerator(WorldGenerator generator) {
		this.generator = generator;
		
		pixels = new int[generator.getW() * generator.getH()];
		image = new BufferedImage(generator.getW(), generator.getH(), BufferedImage.TYPE_INT_RGB);
		
		update();
	}
	
	/**
	 * 
	 */
	public void update() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = generator.grid[i].getColor();
		}
		image.setRGB(0, 0, generator.getW(), generator.getH(), pixels, 0, generator.getW());
		
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(image.getScaledInstance(getWidth(), getHeight(), 0), 0, 0, null) ;
	}
}