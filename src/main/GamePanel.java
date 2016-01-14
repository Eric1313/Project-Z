package main;

import java.awt.Graphics;
import java.util.Iterator;

import javax.swing.JPanel;

import map.Chunk;
import entities.Zombie;
import utilities.World;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private World world;
	private HUD hud;
	private boolean setUp;
	private Chunk[][] chunkMap;

	public GamePanel() {
		super(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (setUp) {
			world.render(g);
			hud.render(g);
		}
	}

	public void update() {
		world.getPlayer().update();
		int chunkX = Math.max((int) world.getPlayer().getPosition().getX() / 512, 2);
		int chunkY = Math.max((int) world.getPlayer().getPosition().getY() / 512, 2);
		for (int x = chunkX - 2; x < chunkX + 3; x++) {
			for (int y = chunkY - 2; y < chunkY + 3; y++) {
//				for (Iterator<Zombie> iterator = chunkMap[x][y].getZombies()
//						.iterator(); iterator.hasNext();) {
//					Zombie zombie= iterator.next();
//					zombie.update();
				for (int i=0;i<chunkMap[x][y].getZombies().size();i++)
				{
					chunkMap[x][y].getZombies().get(i).update();
				}
			}
		}
	}

	public void setup(Game game) {
		world = new World(game, 400, 400);
		hud = new HUD(world.getPlayer());
		setUp = true;
		chunkMap=world.getMap().getChunkMap();
	}

	public World getWorld() {
		return world;
	}

}
