package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import entities.Player;

public class HUD {
	private Player player;
	
	public HUD(Player player) {
		this.player = player;
	}
	
	public void render(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.setColor(new Color(112, 112, 112));
		g2D.drawRect(300, 1200, 1000, 200);
	}
}
