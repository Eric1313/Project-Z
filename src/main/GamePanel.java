package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import utilities.World;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private World world;
	private boolean setup;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (setup)
			world.render(g);
	}

	public void setup(BufferedImage[][] assets) {
		world = new World(assets);
		setup = true;
	}
}