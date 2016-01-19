package items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import entities.Player;
import enums.ItemEffect;
import enums.ItemState;
import main.Game;
import utilities.Effect;
import utilities.Sound;

/**
 * Subclass of Item that represents a consumable item in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Item
 * @since 1.0
 * @version 1.0
 */
public class Consumable extends Item {
	private ItemEffect effect;
	private int durability;

	public Consumable(int itemID, String name, int rarity, int effectValue, ItemState state, BufferedImage[] images,
			Effect[] clips, Game game, ItemEffect effect, int durability) {
		super(itemID, name, rarity, effectValue, state, images, clips, game);

		this.effect = effect;
		this.effectValue = effectValue;
		this.durability = durability;
	}

	@Override
	public void use(Player player) {
		switch (this.getEffect()) {
		case HEAL:
			if (player.getHealth() < 100) {
				clips[0].play();
				Consumable consumable = this;
				Thread reloadPause = new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(3500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						player.setHealth(Math.min(100, player.getHealth() + consumable.getEffectValue()));
						consumable.removeDurability();
						if (consumable.getDurability() <= 0) {
							player.removeItem(consumable);
						}
					}
				});
				reloadPause.start();

			}
			break;
		case AMMO:
			// for (int itemNo = 0; itemNo < Inventory.NO_OF_ITEMS; itemNo++) {
			// Item currentItem = player.getItem(itemNo);
			// if (currentItem instanceof Firearm) {
			// Firearm firearm = (Firearm) currentItem;
			//
			// if (firearm.getAmmoID() == this.getItemID()
			// && !firearm.isFull()) {
			// if (this.getDurability() > 0) {
			// firearm.setCurrentAmmo(firearm.getMaxAmmo());
			// this.removeDurability();
			// }
			//
			// if (this.getDurability() <= 0) {
			// player.removeItem(player.getSelectedItem());
			// }
			// }
			// }
			// }
			break;
		default:
			break;
		}
	}

	public Consumable(Consumable item) {
		super(item);

		this.effect = item.getEffect();
		this.durability = item.getDurability();
	}

	public ItemEffect getEffect() {
		return this.effect;
	}

	public void setEffect(ItemEffect effect) {
		this.effect = effect;
	}

	public int getEffectValue() {
		return this.effectValue;
	}

	public void setEffectValue(int effectValue) {
		this.effectValue = effectValue;
	}

	public int getDurability() {
		return this.durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public void removeDurability() {
		this.durability--;
	}

	@Override
	public void renderTooltip(Graphics g, Point mouseLocation) {
		g.setColor(new Color(getColour().getRed(), getColour().getGreen(), getColour().getBlue(), 75));
		g.fillRect(mouseLocation.x, mouseLocation.y - 175, 300, 175);

		g.setColor(new Color(0, 0, 0, 200));
		g.setFont(this.game.getUiFont());
		g.drawString(this.name, mouseLocation.x + 20, mouseLocation.y - 125);

		g.setFont(this.game.getUiFontXS());
		switch (this.rarity) {
		case 5:
			g.drawString("Common", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		case 4:
			g.drawString("Uncommon", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		case 3:
			g.drawString("Rare", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		case 2:
			g.drawString("Very Rare", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		case 1:
			g.drawString("Ultra Rare", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		}

		switch (this.effect) {
		case HEAL:
			g.drawString("Healing item", mouseLocation.x + 20, mouseLocation.y - 90);
			g.setFont(this.game.getUiFontS());
			g.drawString("Heals " + this.effectValue + " health", mouseLocation.x + 20, mouseLocation.y - 65);
			g.drawString("Can be used " + this.durability + " time(s)", mouseLocation.x + 20, mouseLocation.y - 40);
			break;
		case AMMO:
			g.drawString("Ammo", mouseLocation.x + 20, mouseLocation.y - 90);
			g.setFont(this.game.getUiFontS());
			g.drawString("Reloads " + this.durability + " ammo", mouseLocation.x + 20, mouseLocation.y - 65);
			break;
		}
	}
}