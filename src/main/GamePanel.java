package main;

import java.awt.Graphics;

import javax.swing.JPanel;

import entities.ZombieThread;
import utilities.World;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private World world;
	private boolean setUp;

	public GamePanel() {
		super(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (setUp) {
			world.render(g);
		}
	}

	public void update() {
		world.getPlayer().update();
		//Update Zombies
	}

	public void setup(Game game) {
		world = new World(game, 1600, 1600);
		ZombieThread zombies=new ZombieThread(game);
		 Thread thread = new Thread(zombies);
		 thread.start();
		setUp = true;
	}

	public World getWorld() {
		return world;
	}

}