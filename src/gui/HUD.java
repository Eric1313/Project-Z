package gui;

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

public class HUD {
	private Player player;

	/**
	 * Constructs a new HUD object.
	 * 
	 * @param player
	 *            the player whose stats will be rendered on the HUD.
	 */
	public HUD(Player player) {
		this.player = player;
	}

	/**
	 * Renders the HUD.
	 * 
	 * @param g
	 *            the graphics variable to draw the HUD.
	 */
	public void render(Graphics g) {
		g.setColor(new Color(112, 112, 112));

		// Go through the player's items
		for (int item = 0; item < Inventory.NO_OF_ITEMS; item++) {
			// Draw a grey box for the item slot
			if (item != player.getSelectedItem()) {
				g.drawRect(212 + item * 60, 650, 60, 60);
			}

			// Get the item in the current slot
			Item currentItem = player.getItem(item);
			if (currentItem != null) {
				// If an item exists, draw the item
				g.drawImage(currentItem.getImages()[0], 212 + item * 60 + 14, 664, null, null);

				FontMetrics fm = g.getFontMetrics();

				// If the item is a consumable, firearm, or melee, show the
				// number of uses the item has left
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

			// Draw the item slot number
			// Handle the 9th slot being slot number 0
			String itemNo;
			if (item < 9) {
				itemNo = item + 1 + "";
			} else {
				itemNo = "0";
			}
			g.drawString(itemNo, 216 + item * 60, 665);
		}

		// Highlight the item slot that is selected
		g.setColor(Color.YELLOW);
		g.drawRect(212 + player.getSelectedItem() * 60, 650, 60, 60);

		// Draw the selected item's name above the hotbar
		Item selectedItem = player.getItem(player.getSelectedItem());
		if (selectedItem != null) {
			g.setColor(selectedItem.getColour());
			String itemName = selectedItem.getName();
			FontMetrics fm = g.getFontMetrics();

			g.drawString(itemName, 512 - fm.stringWidth(itemName) / 2, 625);
		}

		// Draw the health and stamina bar
		g.setColor(new Color(112, 112, 112));
		g.drawRect(914, 9, 101, 21);
		g.drawRect(914, 39, 101, 21);

		g.setColor(Color.RED);
		g.fillRect(915, 10, player.getHealth(), 20);

		g.setColor(new Color(0, 200, 50));
		g.fillRect(915, 40, player.getStamina() / 3, 20);

		// Label the bars and show the exact value of the player's health
		g.setFont(player.getGame().getUiFontXS());

		g.setColor(Color.WHITE);
		g.drawString("HEALTH", 918, 25);
		g.drawString(player.getHealth() + " / 100", 953, 25);
		g.drawString("STAMINA", 918, 55);

		// If the mouse is hovering over an item, draw its tooltip
		Point mouseLocation = player.getMouse().getMouseLocation();
		if (mouseLocation.x > 212 && mouseLocation.x < 812 && mouseLocation.y > 650 && mouseLocation.y < 710) {
			Item item = player.getItem((mouseLocation.x - 212) / 60);
			if (item != null) {
				item.renderTooltip(g, mouseLocation);
			}
		}
	}
}