package items;

import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import entities.Inventory;
import entities.Player;
import enums.ItemEffect;
import enums.ItemState;

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

	public Consumable(int itemID, String name, int rarity, int effectValue,
			ItemState state, BufferedImage[] images, AudioClip[] clips,
			Game game, ItemEffect effect, int durability) {
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
				player.setHealth(Math.min(100,
						player.getHealth() + this.getEffectValue()));
				this.removeDurability();
				if (this.getDurability() <= 0) {
					player.removeItem(player.getSelectedItem());
				}
			}
			break;
		case AMMO:
			for (int itemNo = 0; itemNo < Inventory.NO_OF_ITEMS; itemNo++) {
				Item currentItem = player.getItem(itemNo);
				if (currentItem instanceof Firearm) {
					Firearm firearm = (Firearm) currentItem;

					if (firearm.getAmmoID() == this.getItemID()
							&& !firearm.isFull()) {
						if (this.getDurability() > 0) {
							firearm.setCurrentAmmo(firearm.getMaxAmmo());
							this.removeDurability();
						}

						if (this.getDurability() <= 0) {
							player.removeItem(player.getSelectedItem());
						}
					}
				}
			}
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
}