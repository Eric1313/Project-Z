package main;

import utilities.Assets;

public class Game implements Runnable {
	private Assets tiles;

	private String title;
	private int width;
	private int height;

	public Game(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		initialize();
	}

	private void initialize() {
		// Loads the tile assets
		tiles = new Assets("res/img/tiles.png");
	}

	@Override
	public void run() {
	}

	public static void main(String[] args) {
	}
}
