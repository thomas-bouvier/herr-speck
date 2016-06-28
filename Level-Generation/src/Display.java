import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class Display extends JPanel {

	private static final long serialVersionUID = 1L;

	public Map map;
	
	private int[] pixels;
	private BufferedImage image;
	
	
	public Display(Map map) {
		this.map = map;
		pixels = new int[map.width * map.height];
		image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
		update();
	}
	
	public void update() {
		for (int i = 0; i < pixels.length; i++) {
			if (map.grid[i]) {
				pixels[i] = 0xbc3434;
			} else {
				pixels[i] = 0x443333;
			}
		}
		image.setRGB(0, 0, 50, 50, pixels, 0, 50);
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image.getScaledInstance(getWidth(), getHeight(), 0), 0, 0, null) ;
	}
}