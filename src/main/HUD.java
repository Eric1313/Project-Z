package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.io.File;

import entities.Inventory;
import entities.Player;
import items.Item;

public class HUD {
	private Player player;

	public HUD(Player player) {
		this.player = player;
	}

	public void render(Graphics g) {
		g.setColor(new Color(112, 112, 112));
		// g.drawRect(200, 650, 600, 60);
		for (int item = 0; item < Inventory.NO_OF_ITEMS; item++) {
			if (item != player.getSelectedItem()) {
				g.drawRect(200 + item * 60, 650, 60, 60);
			}

			Item currentItem = player.getInventory().get(item);

			if (currentItem != null) {
				g.drawImage(currentItem.getImages()[1], 200 + item * 60 + 14, 664, null, null);
			}
		}

		g.setColor(Color.YELLOW);
		g.drawRect(200 + player.getSelectedItem() * 60, 650, 60, 60);

		g.setColor(new Color(112, 112, 112));
		g.drawRect(939, 19, 21, 101);
		g.drawRect(979, 19, 21, 101);

		g.setColor(Color.RED);
		g.fillRect(940, 120 - player.getHealth(), 20, player.getHealth());
		
		try {
			Font f25font = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/VCR_OSD_MONO_1.001.ttf")).deriveFont(12f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(f25font);
			g.setFont(f25font);
		} catch (Exception e) {
			e.printStackTrace();
		}

		g.setColor(Color.WHITE);
		for (int letter = 0; letter < "HEALTH".length(); letter++) {
			g.drawString("HEALTH".substring(letter, letter + 1), 945, 32 + letter * 12);
		}

		g.setColor(new Color(0, 200, 50));
		g.fillRect(980, 120 - player.getStamina() / 3, 20, player.getStamina() / 3);

		g.setColor(Color.WHITE);
		for (int letter = 0; letter < "STAMINA".length(); letter++) {
			g.drawString("STAMINA".substring(letter, letter + 1), 985, 32 + letter * 12);
		}
	}
}