/**
 * The actual game that contains the game loop, and assets.
 * @author Allen Han, Patrick Liu, Alosha Reymer, & Eric Chee
 * @version January 4th, 2016
 */
package main;

import items.Consumable;
import items.Firearm;
import items.Item;
import items.Melee;
import items.Throwable;

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

import utilities.Assets;
import utilities.Effect;
import utilities.GameCamera;
import enums.GameState;
import enums.GameState.State;
import gui.Display;
import enums.ItemEffect;
import enums.ItemState;

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
	private int level;

	private GameCamera camera;

	private boolean running = false;

	private long tickCount;
	private Font uiFontS;
	private Font uiFontXS;

	/**
	 * Constructor for Game.
	 * 
	 * @param title
	 *            the title of the game.
	 * @param width
	 *            the width of the game.
	 * @param height
	 *            the height of the game.
	 */
	public Game(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.level = 1;
	}

	/**
	 * Initializes everything needs for the game.
	 */
	private void initialize() {
		// Load in all of the assets used
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

		// Load in all of the items
		BufferedReader itemReader = null;
		try {
			itemReader = new BufferedReader(new InputStreamReader(new FileInputStream("res/items.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int[] noOfItems = new int[4];

		for (int itemType = 0; itemType < 4; itemType++) {
			try {
				noOfItems[itemType] += Integer.parseInt(itemReader.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		this.items = new ArrayList<Item>();

		for (int itemType = 0; itemType < 4; itemType++) {
			for (int item = 0; item < noOfItems[itemType]; item++) {
				try {
					// Read in all of the stats of the current item separated by
					// '~' delimiters
					// Load all of the images and sounds
					String currentItem = itemReader.readLine();

					String[] stats = currentItem.split("~");
					BufferedImage[] images = new Assets(stats[4], 1, 1).getSprites()[0];
					String[] soundLinks = stats[5].split("`");

					Effect[] sounds = new Effect[soundLinks.length];

					for (int i = 0; i < sounds.length; i++) {
						sounds[i] = new Effect(soundLinks[i]);
					}

					// Depending on which item type the current item, use the
					// respective constructor and add it to the game's list of
					// items
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
								Integer.parseInt(stats[9]), Integer.parseInt(stats[10]), Integer.parseInt(stats[11]),
								Integer.parseInt(stats[12]), Integer.parseInt(stats[13])));

						break;
					case 3:
						this.items.add(new Throwable(Integer.parseInt(stats[0]), stats[1], Integer.parseInt(stats[2]),
								Integer.parseInt(stats[3]), ItemState.DROPPED, images, sounds, this,
								ItemEffect.values()[Integer.parseInt(stats[6])], Integer.parseInt(stats[7]),
								Integer.parseInt(stats[8])));
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		// Close the file

		try {
			itemReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Load the display
		display = new Display(title, width, height);

		// Create the camera
		camera = new GameCamera(this, 0, 0);

		// Set the state of the game
		state = new GameState(this);
		state.setGameState(State.LOBBY, false);

		// Set the icon
		display.getFrame().setIconImage(new Assets("res/img/icon.png").getImage());

		// Change the mouse to a cross hair
		display.getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}

	/**
	 * Updates the game.
	 */
	private void update() {
		// Updates the current state only
		if (state.getGameState() != null)
			state.update();
	}

	/**
	 * Renders the correct screen.
	 */
	private void render() {
		// Check if there is a buffer strategy
		bs = display.getGamePanel().getBufferStrategy();

		// If this is the first time running initialize the buffer strategy
		if (bs == null) {
			display.getGamePanel().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();

		// Clear the screen
		g.clearRect(0, 0, width, height);

		// Render the screen
		if (state.getGameState() != null) {
			state.render(g);
		}
		// End the drawing
		bs.show();
		g.dispose();
	}

	/**
	 * The main game loop of the game where the rendering and updating is done.
	 */
	public void run() {
		// Initialize all assets and sets the state
		initialize();

		// Keep track of ticks
		double unprocessedSeconds = 0;
		long lastTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		tickCount = 0;

		// Game loop
		while (running) {
			// Calculate the time passed
			long now = System.nanoTime();
			long passedTime = now - lastTime;
			lastTime = now;
			if (passedTime < 0)
				passedTime = 0;
			if (passedTime > 100000000)
				passedTime = 100000000;

			// Calculate the seconds since the last tick
			unprocessedSeconds += passedTime / 1000000000.0;

			// Update the game
			boolean ticked = false;
			while (unprocessedSeconds > secondsPerTick) {
				update();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 60 == 0) {
					lastTime += 1000;
				}
			}
			// Render the game
			if (ticked) {
				render();
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
	 * Starts the game.
	 */
	public synchronized void start() {
		// Do not make a new thread if it is already running
		if (running) {
			return;
		}

		// Start the game
		running = true;
		thread = new Thread(this);

		// Go to run
		thread.start();
	}

	/**
	 * Stops the game.
	 */
	public synchronized void stop() {
		// In case stop gets called and the game is already not running
		if (!running) {
			return;
		}

		// Stop the game
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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
				switch (itemID / 100) {
				case 1:
					return new Consumable((Consumable) this.items.get(item));
				case 2:
					return new Melee((Melee) this.items.get(item));
				case 3:
					Firearm newItem = new Firearm((Firearm) this.items.get(item));
					newItem.setCurrentAmmo(newItem.getMaxAmmo());
					return newItem;
				case 4:
					return new Throwable((Throwable) this.items.get(item));
				}
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
