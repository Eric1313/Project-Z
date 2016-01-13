package main;

import java.awt.Graphics;

import javax.swing.JPanel;

import utilities.World;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private World world;
	private HUD hud;
	private boolean setUp;

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
	}

	public void setup(Game game) {
		world = new World(game, 1600, 1600);
		
		hud = new HUD(world.getPlayer());

		setUp = true;
	}

	public World getWorld() {
		return world;
	}

}