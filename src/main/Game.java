package main;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import utilities.Assets;
import enums.GameState;
import enums.GameState.State;

public class Game implements Runnable {
	private Assets tiles;

	private Display display;
	private String title;
	private int width;
	private int height;
	private Thread thread;
	private GameState state;
	private boolean running = false;
	private Graphics g;
	private BufferStrategy bs;

	public Game(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
	}

	private void initialize() {
		// Loads the tile assets
		tiles = new Assets("res/img/tiles.png");

		// Loads the display
		display = new Display(title, width, height);

		// Sets the state of the game
		state = new GameState();
		state.setGameState(State.INGAME, tiles);
		display.getFrame().createBufferStrategy(2);
		bs = display.getFrame().getBufferStrategy();
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

	/**
	 * The main game loop of the game
	 */
	public void run() {

		initialize();

		int fps = 60;
		double timePerUpdate = 1000000000 / fps;
		double timeElapsed = 0;
		long now;
		// Current time of computer in nanoseconds
		long lastTime = System.nanoTime();

		// Game loop
		while (running) {
			now = System.nanoTime();
			timeElapsed += (now - lastTime) / timePerUpdate;
			lastTime = now;

			// If the time elapsed has been 1/60th of a second then refresh the
			// game
			if (timeElapsed >= 1) {
				update();
				render();
				timeElapsed--;
			}
		}
		// Stops the game
		stop();
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

	public static void main(String[] args) {
		Game game = new Game("Project Z", 1024, 768);
		game.start();
	}

}
