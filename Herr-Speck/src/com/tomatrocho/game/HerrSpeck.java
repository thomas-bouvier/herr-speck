package com.tomatrocho.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.tomatrocho.game.entity.mob.Player;
import com.tomatrocho.game.gfx.IAbstractBitmap;
import com.tomatrocho.game.gfx.IAbstractScreen;
import com.tomatrocho.game.gfx.Screen;
import com.tomatrocho.game.gui.Hud;
import com.tomatrocho.game.gui.Hud.TextLocation;
import com.tomatrocho.game.input.Input;
import com.tomatrocho.game.input.Keys;
import com.tomatrocho.game.input.Mouse;
import com.tomatrocho.game.level.World;
import com.tomatrocho.game.level.WorldBuilder;
import com.tomatrocho.game.level.WorldInformation;
import com.tomatrocho.game.level.WorldList;
import com.tomatrocho.game.level.tile.Tile;
import com.tomatrocho.game.sound.SoundPlayer;

public class HerrSpeck extends Canvas implements Runnable, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Game's title.
	 */
	private static final String NAME = "Herr Speck";

	/**
	 * Width of the scene, in pixels.
	 */
	public static final int W = 450;

	/**
	 * Height of the scene, in pixels. Computed in order to have a 19/6 format.
	 */
	public static final int H = W * 9 / 16;

	/**
	 * Scaling factor for display.
	 */
	public static final int SCALE = 3;

	/**
	 * Game's window.
	 */
	private JFrame frame;

	/**
	 * 
	 */
	private boolean fullscreen = false;

	/**
	 * 
	 */
	private static int debugLevel = 0;

	/**
	 * Thread handling display.
	 */
	private Thread thread;

	/**
	 * Current state of the game.
	 */
	private volatile boolean running = true;

	/**
	 * Current number of frames per second.
	 */
	private int fps;

	/**
	 * Current number of updates per second.
	 */
	private int ups;

	/**
	 *
	 */
	private static Screen screen;

	/**
	 * 
	 */
	private static Hud hud;

	/**
	 *
	 */
	private Input input;

	/**
	 *
	 */
	private static volatile File dir = null;

	/**
	 *
	 */
	private int ticks = 0;

	/**
	 *
	 */
	private int frames = 0;

	/**
	 * 
	 */
	private String worldName;

	/**
	 *
	 */
	private WorldBuilder worldBuilder = new WorldBuilder();

	/**
	 *
	 */
	private World world;

	/**
	 *
	 */
	private Player player;

	/**
	 *
	 */
	private Keys keys = new Keys();

	/**
	 *
	 */
	private Mouse mouse = new Mouse();

	/**
	 * 
	 */
	public static SoundPlayer soundPlayer;

	/**
	 * Delay, in milliseconds, between background musics. Sets to 4 minutes.
	 */
	private long nextMusicInterval = System.currentTimeMillis() + 1000;

	/**
	 * Global {@link Random} object.
	 */
	public static final Random random = new Random();

	/**
	 * Constructor for the {@link HerrSpeck} class. Set up the {@link JFrame}
	 * containing the game.
	 *
	 * @param frame
	 *            {@link JFrame} object representing the game window
	 */
	private HerrSpeck(JFrame frame) {
		this.frame = frame;

		frame.setPreferredSize(new Dimension(W * SCALE, H * SCALE));
		frame.setResizable(false);
		frame.add(this);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 *
	 */
	private synchronized void start() {
		requestFocus();

		try {
			init();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		// screen
		screen = new Screen(W, H);
		screen.loadResources();

		hud = new Hud(screen);

		// thread
		thread = new Thread(this, "Display");
		thread.start();
	}

	/**
	 *
	 */
	private void init() {
		initInput();
		initWorld();
		initCursor();
		
		// sound
		soundPlayer = new SoundPlayer();
	}

	/**
	 *
	 */
	private void initInput() {
		// keyboard
		input = new Input(keys);
		addKeyListener(input);

		// mouse
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/**
	*
	*/
	private void initWorld() {
		this.worldName = "generated_world";

		createWorld(WorldList.getWorldByName(worldName));

		if (world != null) {
			if (world.canPlayerSpawn()) {
				player = new Player(world, keys, mouse);
				world.addEntity(player);
			}
		}
	}
	
	/**
	 * 
	 */
	private void initCursor() {
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty"));
	}

	/**
	 *
	 * @param levelInformation
	 */
	private synchronized void createWorld(WorldInformation worldInformation) {
		try {
			this.world = worldBuilder.buildWorld(worldInformation);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Main loop of the game. Limit the update method to 60 launches per second.
	 */
	public void run() {
		long old = System.nanoTime();
		final int upsLimiter = 60;
		final double nanoU = 1000000000.0 / upsLimiter;
		double deltaU = 0;
		long timer = System.currentTimeMillis();

		while (running) {
			long now = System.nanoTime();
			deltaU += (now - old) / nanoU;
			old = now;

			if (deltaU >= 1) {
				tick();
				deltaU--;
			}

			BufferStrategy bs = getBufferStrategy();
			if (bs == null) {
				createBufferStrategy(3);
				continue;
			}

			render(bs.getDrawGraphics());
			bs.show();
			
			// update the fps and ups counters every second
			if (System.currentTimeMillis() - timer > 1000) {
				fps = frames;
				ups = ticks;
				frames = ticks = 0;
				timer += 1000;
			}

//			try {
//				Thread.sleep(4);
//			} catch (InterruptedException ex) {
//				ex.printStackTrace();
//			}
		}
		stop();
	}

	/**
	 * Main update function.
	 */
	private void tick() {
		tickInput();
		world.tick();

		// fullscreen mode
		if (keys.fullscreen.wasPressed())
			setFullscreen(!fullscreen);

		// sound system update
		soundPlayer.setListenerPosition(player.getX(), player.getY());
		if (System.currentTimeMillis() > nextMusicInterval) {
			soundPlayer.startBackgroundMusic();
			nextMusicInterval = System.currentTimeMillis() + nextMusicInterval;
		}

		ticks++;
	}

	/**
	 * 
	 */
	private void tickInput() {
		// keyboard
		keys.tick();
		if (keys.debug.wasPressed()) {
			debugLevel++;
			
			if (debugLevel > 2)
				debugLevel = 0;
		}
		if (keys.generateNewWorld.wasPressed()) {
			if (debugLevel > 0) {
				WorldList.getWorldByName(worldName).setRandomSeed();
				initWorld();
			}
		}

		// mouse
		final Point mousePosition = getMousePosition();
		if (mousePosition != null)
			mouse.setPosition(mousePosition.x / SCALE, mousePosition.y / SCALE);

		if (mouse.moved()) {
			mouse.setMoved(false);
			mouse.setHidden(false);
			mouse.setHideTime(0);
		}
		else
			mouse.incrementHideTime();

		if (mouse.getHideTime() > Mouse.HIDE_DELAY)
			mouse.setHidden(true);

		mouse.tick();
	}

	/**
	 * Render all the graphics using the given {@link Graphics} object.
	 */
	private synchronized void render(Graphics g) {
		renderWorld();
		renderGui();
		renderCursor();

		// rendering
		g.fillRect(0, 0, getWidth(), getHeight());
		g.translate((getWidth() - W * SCALE) / 2, (getHeight() - H * SCALE) / 2);
		g.clipRect(0, 0, W * SCALE, H * SCALE);

		// drawing the optimized image
		g.drawImage(screen.toCompatibleFormat(), 0, 0, W * SCALE, H * SCALE, null);
		
		frames++;
	}

	/**
	 * Render the world on the {@link Screen}.
	 */
	private void renderWorld() {
		if (world != null) {
			int xScroll = (int) (player.getX() - screen.getW() / 2);
			int yScroll = (int) (player.getY() - screen.getH() / 2);

			world.render(screen, xScroll, yScroll);
		}
		
	}

	/**
	 * Render the GUI elements on the {@link Screen}.
	 */
	private void renderGui() {
		// fonts
		if (debugLevel > 0) {
			// top left
			hud.add(String.format("%s (%s * %s, *%s)", NAME, W, H, SCALE));
			hud.add(String.format("%d ups, %d fps", ups, fps));

			final WorldInformation worldInformation = WorldList.getWorldByName(world.getName());
			if (worldInformation.randomlyGenerated()) {
				hud.add("Seed: " + worldInformation.getSeed());
			}

			hud.add(String.format("x: %f (%d)", player.getX(), (int) (player.getX() / Tile.W)));
			hud.add(String.format("y: %f (%d)", player.getY(), (int) (player.getY() / Tile.H)));
			hud.add(String.format("%d entities", world.getEntities().size()));

			// top right
			hud.add("F3 to generate new world", TextLocation.TOP_RIGHT);

			hud.render();
		}
	}
	
	/**
	 * 
	 * @param screen
	 */
	private void renderCursor() {
		if (mouse.isHidden())
			return;
		
		final int crosshairSize = 13;
		final int crosshairHalfSize = crosshairSize / 2;
		IAbstractBitmap crosshairSprite = screen.createBitmap(crosshairSize, crosshairSize);
		
		for (int i = 0; i < crosshairSize; i++) {
			if (i >= crosshairHalfSize - 1 && i <= crosshairHalfSize + 1)
				continue;

			//vertical line
			crosshairSprite.setPixel(crosshairHalfSize + i * crosshairSize, 0xffff0000);
			// horizontal line
			crosshairSprite.setPixel(i + crosshairHalfSize * crosshairSize, 0xffff0000);
		}

		screen.alphaBlit(crosshairSprite, mouse.getX() - crosshairHalfSize, mouse.getY() - crosshairHalfSize, 128);
	}

	/**
	 * 
	 * @param fullscreen
	 */
	private void setFullscreen(boolean fullscreen) {
		GraphicsDevice device = frame.getGraphicsConfiguration().getDevice();
		
		// disposing window
		frame.setVisible(false);
		frame.dispose();
		frame.setUndecorated(fullscreen);

		// change options
		device.setFullScreenWindow(fullscreen ? frame : null);

		// displaying window
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		this.fullscreen = fullscreen;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouse.setNextState(e.getButton(), true);
		mouse.setPressed(true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouse.setNextState(e.getButton(), false);
		mouse.setPressed(false);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouse.releaseAll();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouse.setMoved(true);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouse.setMoved(true);
	}

	/**
	 *
	 */
	private synchronized void stop() {
		soundPlayer.dispose();
		running = false;
		
		try {
			thread.join();
		}
		catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	public static IAbstractScreen getScreen() {
		return screen;
	}

	/**
	 *
	 * @return
	 */
	public static File getDir() {
		if (dir == null) {
			dir = OS.getAppDirectory("game");
		}
		return dir;
	}

	/**
	 * 
	 * @return
	 */
	public static int getDebugLevel() {
		return debugLevel;
	}

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		new HerrSpeck(new JFrame(HerrSpeck.NAME)).start();
	}
}
