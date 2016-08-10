import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.sun.prism.paint.Color;

public class Display extends JPanel {

	private static final long serialVersionUID = 1L;

	public Cave map;
	
	private int[] pixels;
	private BufferedImage image;
	
	
	/**
	 * 
	 */
	public Display() {
		
	}
	
	/**
	 * 
	 * @param map
	 */
	public Display(Cave map) {
		setMap(map);
	}
	
	/**
	 * 
	 * @param map
	 */
	public void setMap(Cave map) {
		this.map = map;
		pixels = new int[map.size * map.size];
		image = new BufferedImage(map.size, map.size, BufferedImage.TYPE_INT_RGB);
		update();
	}
	
	/**
	 * 
	 */
	public void update() {
		for (int i = 0; i < pixels.length; i++) {
			if (map.grid[i] == Cell.FLOOR) {
				pixels[i] = 0x996633;
			} else  {
				pixels[i] = 0x555555;
			}
		}
		image.setRGB(0, 0, map.size, map.size, pixels, 0, map.size);
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image.getScaledInstance(getWidth(), getHeight(), 0), 0, 0, null) ;
	}
}