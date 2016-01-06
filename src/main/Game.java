package main;

import utilities.Assets;
import enums.GameState;
import enums.GameState.State;
public class Game implements Runnable {
	private Assets tiles;
	
	private Display display;
	private String title;
	private int width;
	private int height;
	
	private GameState state;

	public Game(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
	
		initialize();
	}

	private void initialize() {
		// Loads the tile assets
		tiles = new Assets("res/img/tiles.png");

		// Loads the display
		display = new Display(title, width, height);
		
		// Sets the state of the game
		GameState state = new GameState();
		state.setGameState(State.INGAME);
	}

	@Override
	public void run() {
	}

	public static void main(String[] args) {
	}
}
