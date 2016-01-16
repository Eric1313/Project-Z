package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.io.File;

import map.Map;
import entities.Inventory;
import entities.Player;
import items.Consumable;
import items.Firearm;
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

			Item currentItem = player.getItem(item);

			if (currentItem != null) {
				g.drawImage(currentItem.getImages()[0], 200 + item * 60 + 14, 664, null, null);
				
				FontMetrics fm = g.getFontMetrics();
				
				if (currentItem instanceof Consumable) {
					String amount = ((Consumable) currentItem).getDurability() + "";
					
					g.drawString(amount, 256 + item * 60 - fm.stringWidth(amount), 704);
				} else if (currentItem instanceof Firearm) {
					String amount = ((Firearm) currentItem).getCurrentAmmo() + "";
					
					g.drawString(amount, 256 + item * 60 - fm.stringWidth(amount), 704);
				}
			}

			String itemNo;
			if (item < 9) {
				itemNo = item + 1 + "";
			} else {
				itemNo = "0";
			}
			g.drawString(itemNo, 200 + item * 60 + 5, 665);
		}

		g.setColor(Color.YELLOW);
		g.drawRect(200 + player.getSelectedItem() * 60, 650, 60, 60);

		Item selectedItem = player.getItem(player.getSelectedItem());
		if (selectedItem != null) {
			g.setColor(selectedItem.getColour());
			String itemName = selectedItem.getName();
			FontMetrics fm = g.getFontMetrics();

			g.drawString(itemName, 500 - fm.stringWidth(itemName) / 2, 625);
		}

		g.setColor(new Color(112, 112, 112));
		g.drawRect(939, 19, 21, 101);
		g.drawRect(979, 19, 21, 101);

		g.setColor(Color.RED);
		g.fillRect(940, 120 - player.getHealth(), 20, player.getHealth());

		try {
			Font f25font = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/VCR_OSD_MONO_1.001.ttf"))
					.deriveFont(12f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(f25font);
			g.setFont(f25font);
		} catch (Exception e) {
			e.printStackTrace();
		}

		g.setColor(Color.WHITE);
		for (int letter = 0; letter < "HEALTH".length(); letter++) {
			g.drawString("HEALTH".substring(letter, letter + 1), 943, 32 + letter * 12);
		}

		g.setColor(new Color(0, 200, 50));
		g.fillRect(980, 120 - player.getStamina() / 3, 20, player.getStamina() / 3);

		g.setColor(Color.WHITE);
		for (int letter = 0; letter < "STAMINA".length(); letter++) {
			g.drawString("STAMINA".substring(letter, letter + 1), 983, 32 + letter * 12);
		}

		g.setColor(Color.RED);
		g.drawString("ZOMBIES: " + Integer.toString(Map.zombieCount), 25, 25);
	}
}