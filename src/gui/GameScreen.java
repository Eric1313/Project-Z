package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import main.Game;
import map.Chunk;
import map.Map;
import map.World;
import entities.Inventory;

/**
 * The game screen which renders the actual game
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @since 1.0
 * @version 1.0
 */
public class GameScreen extends Canvas {
	private static final long serialVersionUID = 1L;
	private Game game;
	private World world;
	private HUD hud;
	private boolean setUp;
	private Chunk[][] chunkMap;
	private Map map;

	/**
	 * Used to render the game onto the screen.
	 * 
	 * @param g
	 *            the graphics object used to draw to the screen.
	 */
	public void render(Graphics g) {
		// Do not render the world and HUD if they have not been setup
		if (setUp) {
			world.render(g);
			hud.render(g);
		}
		// Draws the current level to the screen
		g.setColor(Color.WHITE);
		g.setFont(game.getUiFontS());
		g.drawString("LEVEL: " + game.getLevel(), 5, 25);
	}

	/**
	 * Updates the game screen.
	 */
	public void update() {
		// Updates the player location
		world.getPlayer().update();
		// Updates chunks of the map
		int chunkX = Math.max(
				(int) world.getPlayer().getPosition().getX() / 512, 2);
		int chunkY = Math.max(
				(int) world.getPlayer().getPosition().getY() / 512, 2);
		for (int x = chunkX - 2; x < Math.min(chunkX + 3, map.getWidth() / 16); x++) {
			for (int y = chunkY - 2; y < Math.min(chunkY + 3,
					map.getHeight() / 16); y++) {
				for (int i = 0; i < chunkMap[x][y].getZombies().size(); i++) {
					chunkMap[x][y].getZombies().get(i).update();
				}
			}
		}
	}

	/**
	 * Sets up the game screen.
	 * 
	 * @param game
	 *            the game.
	 */
	public void setup(Game game) {
		this.game = game;
		// Creates new world and HUD
		world = new World(game, 400, 400, null, 0);
		hud = new HUD(world.getPlayer());
		// World and HUD has been setup
		setUp = true;
		this.map = world.getMap();
		this.chunkMap = map.getChunkMap();
	}

	/**
	 * Overloaded setup class this one is used to make a new level.
	 * 
	 * @param game
	 *            the game.
	 * @param size
	 *            the size of the map.
	 * @param inventory
	 *            the player's inventory saved from the last level.
	 * @param skinNo
	 *            the player's current skin.
	 */
	public void setup(Game game, int size, Inventory inventory, int skinNo) {
		this.game = game;
		// Setup the new world with items saved from the last game
		world = new World(game, size, size, inventory, skinNo);
		hud = new HUD(world.getPlayer());
		setUp = true;
		this.map = world.getMap();
		this.chunkMap = map.getChunkMap();
	}

	public World getWorld() {
		return world;
	}
}
