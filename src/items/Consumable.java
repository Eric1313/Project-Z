package items;

import java.applet.AudioClip;
import java.awt.image.BufferedImage;

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
	private int effectValue, durability;

	public Consumable(int itemID, String name, BufferedImage[] images,
			AudioClip[] clips, ItemState state, int rarity, ItemEffect effect,
			int effectValue, int durability) {
		super(itemID, name, images, clips, state, rarity);

		this.effect = effect;
		this.effectValue = effectValue;
		this.durability = durability;
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
}