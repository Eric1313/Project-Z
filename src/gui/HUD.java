package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

import entities.Inventory;
import entities.Player;
import entities.Zombie;
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
				g.drawRect(212 + item * 60, 650, 60, 60);
			}

			Item currentItem = player.getItem(item);

			if (currentItem != null) {
				g.drawImage(currentItem.getImages()[0], 212 + item * 60 + 14, 664, null, null);

				FontMetrics fm = g.getFontMetrics();

				if (currentItem instanceof Consumable) {
					String amount = ((Consumable) currentItem).getDurability() + "";

					g.drawString(amount, 268 + item * 60 - fm.stringWidth(amount), 704);
				} else if (currentItem instanceof Firearm) {
					String amount = ((Firearm) currentItem).getCurrentAmmo() + "";

					g.drawString(amount, 268 + item * 60 - fm.stringWidth(amount), 704);
				} else if (currentItem instanceof Melee) {
					String amount = ((Melee) currentItem).getDurability() + "";

					g.drawString(amount, 268 + item * 60 - fm.stringWidth(amount), 704);
				}
			}

			String itemNo;
			if (item < 9) {
				itemNo = item + 1 + "";
			} else {
				itemNo = "0";
			}
			g.drawString(itemNo, 216 + item * 60, 665);
		}

		g.setColor(Color.YELLOW);
		g.drawRect(212 + player.getSelectedItem() * 60, 650, 60, 60);

		Item selectedItem = player.getItem(player.getSelectedItem());
		if (selectedItem != null) {
			g.setColor(selectedItem.getColour());
			String itemName = selectedItem.getName();
			FontMetrics fm = g.getFontMetrics();

			g.drawString(itemName, 512 - fm.stringWidth(itemName) / 2, 625);
		}

		g.setColor(new Color(112, 112, 112));
		g.drawRect(914, 9, 101, 21);
		g.drawRect(914, 39, 101, 21);

		g.setColor(Color.RED);
		g.fillRect(915, 10, player.getHealth(), 20);

		g.setColor(new Color(0, 200, 50));
		g.fillRect(915, 40, player.getStamina() / 3, 20);

		g.setFont(player.getGame().getUiFontXS());

		g.setColor(Color.WHITE);
		g.drawString("HEALTH", 918, 25);
		g.drawString(player.getHealth() + " / 100", 953, 25);
		g.drawString("STAMINA", 918, 55);

		Point mouseLocation = player.getMouse().getMouseLocation();

		if (mouseLocation.x > 212 && mouseLocation.x < 812 && mouseLocation.y > 650 && mouseLocation.y < 710) {
			Item item = player.getItem((mouseLocation.x - 212) / 60);
			if (item != null) {
				item.renderTooltip(g, mouseLocation);
			}
		}
	}
}