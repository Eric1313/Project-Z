package main;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

import entities.Inventory;
import entities.Player;
import items.Consumable;
import items.Firearm;
import items.Item;
import items.Melee;
import map.Map;

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
				} else if (currentItem instanceof Melee) {
					String amount = ((Melee) currentItem).getDurability() + "";

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
		g.drawRect(904, 19, 101, 21);
		g.drawRect(904, 49, 101, 21);

		g.setColor(Color.RED);
		g.fillRect(905, 20, player.getHealth(), 20);

		g.setColor(new Color(0, 200, 50));
		g.fillRect(905, 50, player.getStamina() / 3, 20);

		g.setFont(player.getGame().getTinyUiFont());

		g.setColor(Color.WHITE);
		g.drawString("HEALTH", 908, 35);
		g.drawString("STAMINA", 908, 65);
		g.drawString(player.getHealth() + " / 100", 953, 35);

		g.setColor(Color.RED);
		g.drawString("ZOMBIES: " + Integer.toString(Map.zombieCount), 25, 25);

		Point mouseLocation = player.getMouse().getMouseLocation();

		if (mouseLocation.x > 200 && mouseLocation.x < 800 && mouseLocation.y > 650 && mouseLocation.y < 710) {
			Item item = player.getItem((mouseLocation.x - 200) / 60);
			if (item != null) {
				item.renderTooltip(g, mouseLocation);
			}
		}
	}
}