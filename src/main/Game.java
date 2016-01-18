package main;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import enums.GameState;
import enums.GameState.State;
import enums.ItemEffect;
import enums.ItemState;
import items.Consumable;
import items.Firearm;
import items.Item;
import items.Melee;
import items.Throwable;
import utilities.Assets;
import utilities.GameCamera;
import utilities.Sound;

public class Game implements Runnable {
	private BufferedImage[][] tileImages;
	private BufferedImage[][] playerImages;
	private BufferedImage[][] zombieImages;
	private BufferedImage bloodSplatter;
	private BufferedImage mainMenu;
	private BufferedImage[] help;
	private Font uiFont;
	private Font uiFontL;
	private Font zombieFont;
	private Font zombieFontL;
	private Font zombieFontXL;
	private ArrayList<Item> items;
	private Display display;
	private String title;
	private int width;
	private int height;
	private Thread thread;
	private GameState state;
	private BufferStrategy bs;
	private Graphics g;

	private GameCamera camera;

	private boolean running = false;

	private long tickCount;
	private Font uiFontS;
	private Font uiFontXS;

	public Game(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
	}

	private void initialize() {
		// Loads the assets
		tileImages = new Assets("res/img/tiles.png", 1, 1).getSprites();
		playerImages = new Assets("res/img/player.png", 1, 1).getSprites();
		zombieImages = new Assets("res/img/zombie.png", 1, 1).getSprites();
		mainMenu = new Assets("res/img/menu.png").getImage();
		bloodSplatter = new Assets("res/img/bloodvisual.png").getImage();
		uiFont = new Assets("res/fonts/BEBASNEUE.ttf", 50).getFont();
		uiFontL = new Assets("res/fonts/BEBASNEUE.ttf", 100).getFont();
		uiFontS = new Assets("res/fonts/BEBASNEUE.ttf", 24).getFont();
		uiFontXS = new Assets("res/fonts/BEBASNEUE.ttf", 14).getFont();
		zombieFont = new Assets("res/fonts/youmurdererbb_reg.ttf", 150).getFont();
		zombieFontL = new Assets("res/fonts/youmurdererbb_reg.ttf", 380).getFont();
		zombieFontXL = new Assets("res/fonts/youmurdererbb_reg.ttf", 1000).getFont();
		help = new BufferedImage[6];
		help[0] = new Assets("res/img/1.png").getImage();
		help[1] = new Assets("res/img/2.png").getImage();
		help[2] = new Assets("res/img/3.png").getImage();
		help[3] = new Assets("res/img/4.png").getImage();
		help[4] = new Assets("res/img/5.png").getImage();
		help[5] = new Assets("res/img/6.png").getImage();
		// Load all of the items
		BufferedReader itemReader = null;

		try {
			itemReader = new BufferedReader(new InputStreamReader(new FileInputStream("res/items.txt")));
		} catch (FileNotFoundException e) {
			// TODO Make catch block more useful
			e.printStackTrace();
		}

		int[] noOfItems = new int[4];

		for (int itemType = 0; itemType < 4; itemType++) {
			try {
				// TODO Handle invalid input
				noOfItems[itemType] += Integer.parseInt(itemReader.readLine());
			} catch (IOException e) {
				// TODO Make catch block more useful
				e.printStackTrace();
			}
		}

		this.items = new ArrayList<Item>();

		for (int itemType = 0; itemType < 4; itemType++) {
			for (int item = 0; item < noOfItems[itemType]; item++) {
				try {
					String currentItem = itemReader.readLine();

					String[] stats = currentItem.split("~");
					BufferedImage[] images = new Assets(stats[4], 1, 1).getSprites()[0];
					String[] soundLinks = stats[5].split("`");

					Sound[] sounds = new Sound[soundLinks.length];

					for (int i = 0; i < sounds.length; i++) {
						sounds[i] = new Sound(soundLinks[i]);
					}
					// TODO: Add AudioClips to the sounds array

					switch (itemType) {
					case 0:
						this.items.add(new Consumable(Integer.parseInt(stats[0]), stats[1], Integer.parseInt(stats[2]),
								Integer.parseInt(stats[3]), ItemState.DROPPED, images, sounds, this,
								ItemEffect.values()[Integer.parseInt(stats[6])], Integer.parseInt(stats[7])));
						break;
					case 1:
						this.items.add(new Melee(Integer.parseInt(stats[0]), stats[1], Integer.parseInt(stats[2]),
								Integer.parseInt(stats[3]), ItemState.DROPPED, images, sounds, this,
								Integer.parseInt(stats[6]), Integer.parseInt(stats[7]), Integer.parseInt(stats[8]),
								Integer.parseInt(stats[9]), Integer.parseInt(stats[10])));
						break;
					case 2:
						this.items.add(new Firearm(Integer.parseInt(stats[0]), stats[1], Integer.parseInt(stats[2]),
								Integer.parseInt(stats[3]), ItemState.DROPPED, images, sounds, this,
								Integer.parseInt(stats[6]), Integer.parseInt(stats[7]), Integer.parseInt(stats[8]),
								Integer.parseInt(stats[9]), Integer.parseInt(stats[10])));
						break;
					case 3:
						this.items.add(new Throwable(Integer.parseInt(stats[0]), stats[1], Integer.parseInt(stats[2]),
								Integer.parseInt(stats[3]), ItemState.DROPPED, images, sounds, this,
								ItemEffect.values()[Integer.parseInt(stats[6])], Integer.parseInt(stats[7]),
								Integer.parseInt(stats[8])));
					}

				} catch (IOException e) {
					// TODO Make catch block more useful
					e.printStackTrace();
				}

			}
		}

		try {
			itemReader.close();
		} catch (IOException e) {
			// TODO Make catch block more useful
			e.printStackTrace();
		}
		// Loads the display
		display = new Display(title, width, height);

		camera = new GameCamera(this, 0, 0);
		// Sets the state of the game
		state = new GameState(this);
		state.setGameState(State.LOBBY, false);

		// display.getFrame().createBufferStrategy(2);
		display.getFrame().setIconImage(new Assets("res/img/icon.png").getImage());
		display.getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

	}

	private void update() {
		// Updates the current state only
		if (state.getGameState() != null)
			state.update();
	}

	/**
	 * Where the drawing happens in the program
	 */
	private void render() {
		bs = display.getGamePanel().getBufferStrategy();

		// If this is the first time running initialize the buffer strategy
		if (bs == null) {
			display.getGamePanel().createBufferStrategy(3);
			return;
		}

		g = bs.getDrawGraphics();
		// Clears the screen
		g.clearRect(0, 0, width, height);

		// Drawing
		if (state.getGameState() != null) {
			state.render(g);
		}
		// End drawing
		bs.show();
		g.dispose();
	}

	// /**
	// * The main game loop of the game
	// */
	// public void run() {
	//
	// initialize();
	//
	// int fps = 30;
	// double timePerUpdate = 1000000000 / fps;
	// double timeElapsed = 0;
	// long now;
	// // Current time of computer in nanoseconds
	// long lastTime = System.nanoTime();
	//
	// // Game loop
	// while (running) {
	// now = System.nanoTime();
	// timeElapsed += (now - lastTime) / timePerUpdate;
	// lastTime = now;
	//
	// // If the time elapsed has been 1/60th of a second then refresh the
	// // game
	// if (timeElapsed >= 1) {
	// update();
	// render();
	// timeElapsed--;
	// }
	// }
	// // Stops the game
	// stop();
	// }

	public void run() {
		initialize();

		double unprocessedSeconds = 0;
		long lastTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		tickCount = 0;

		while (running) {
			long now = System.nanoTime();
			long passedTime = now - lastTime;
			lastTime = now;
			if (passedTime < 0)
				passedTime = 0;
			if (passedTime > 100000000)
				passedTime = 100000000;

			unprocessedSeconds += passedTime / 1000000000.0;

			boolean ticked = false;
			while (unprocessedSeconds > secondsPerTick) {
				update();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;

				tickCount++;
				if (tickCount % 60 == 0) {
					// System.out.println(frames + " fps");
					lastTime += 1000;
				}
			}

			if (ticked) {
				render();
				// update();
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * Start the game
	 */
	public synchronized void start() {
		// Do not make a new thread if it is already running
		if (running)
			return;

		// Starts the game
		running = true;
		thread = new Thread(this);
		// Goes to run
		thread.start();
	}

	/**
	 * Stop the game
	 */
	public synchronized void stop() {
		// In case stop gets called and it is already not running
		if (!running)
			return;

		// Stops the game
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public GameState getState() {
		return state;
	}

	public Display getDisplay() {
		return this.display;
	}

	public GameCamera getCamera() {
		return this.camera;
	}

	public BufferedImage[][] getTileImages() {
		return this.tileImages;
	}

	public BufferedImage[][] getPlayerImages() {
		return this.playerImages;
	}

	public BufferedImage[][] getZombieImages() {
		return this.zombieImages;
	}

	public BufferedImage getBloodVisual() {
		return this.bloodSplatter;
	}

	public BufferedImage[] getHelp() {
		return this.help;
	}

	public Font getUiFont() {
		return this.uiFont;
	}

	public Font getUiFontXS() {
		return this.uiFontXS;
	}

	public Font getUiFontS() {
		return this.uiFontS;
	}

	public Font getUiFontL() {
		return this.uiFontL;
	}

	public Font getZombieFont() {
		return this.zombieFont;
	}

	public Font getZombieFontL() {
		return this.zombieFontL;
	}

	public Font getZombieFontXL() {
		return this.zombieFontXL;
	}

	public BufferedImage getMainMenu() {
		return this.mainMenu;
	}

	/**
	 * Get a list of all of the different types of items in the game.
	 * 
	 * @return a list of all of the different types of items.
	 */
	public ArrayList<Item> getItems() {
		return this.items;
	}

	/**
	 * Gets an item given an item ID.
	 * 
	 * @param itemID
	 *            the item ID of the item to find.
	 * @return the item found.
	 */
	public Item getItem(int itemID) {
		for (int item = 0; item < this.items.size(); item++) {
			if (this.items.get(item).getItemID() == itemID) {
				return this.items.get(item);
			}
		}

		return null;
	}

	/**
	 * Gets the current tick of the game (60 ticks per second).
	 * 
	 * @return the current tick.
	 */
	public long getTickCount() {
		return tickCount;
	}

	public static void main(String[] args) {
		Game game = new Game("Project Z", 1024, 768);
		game.start();
	}
}