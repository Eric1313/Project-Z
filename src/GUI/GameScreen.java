package GUI;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import main.Game;
import map.Chunk;
import map.Map;
import map.World;
import entities.Inventory;

public class GameScreen extends Canvas {
	private static final long serialVersionUID = 1L;
	private Game game;
	private World world;
	private HUD hud;
	private boolean setUp;
	private Chunk[][] chunkMap;
	private Map map;

	public GameScreen() {
	}

	// @Override
	// public void paintComponent(Graphics g) {
	// super.paintComponent(g);
	// if (setUp) {
	// world.render(g);
	// hud.render(g);
	// }
	// }
	public void render(Graphics g) {
		if (setUp) {
			world.render(g);
			hud.render(g);
		}
		g.setColor(Color.WHITE);
		g.setFont(game.getUiFontS());
		g.drawString("LEVEL: " + game.getLevel(), 5, 25);
	}

	public void update() {
		world.getPlayer().update();
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

	public void setup(Game game) {
		this.game = game;
		world = new World(game, 400, 400, null, 0);
		hud = new HUD(world.getPlayer());
		setUp = true;
		this.map = world.getMap();
		this.chunkMap = map.getChunkMap();
	}

	public void setup(Game game, int size, Inventory inventory, int skinNo) {
		this.game = game;
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
