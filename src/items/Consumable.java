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
			AudioClip[] clips, ItemState state, ItemEffect effect,
			int effectValue, int durability) {
		super(itemID, name, images, clips, state);
		this.setEffect(effect);
		this.setEffect(effect);
		this.setEffectValue(effectValue);
		this.setDurability(durability);
	}

	/**
	 * @return the effect
	 */
	public ItemEffect getEffect() {
		return effect;
	}

	/**
	 * @param effect
	 *            the effect to set
	 */
	public void setEffect(ItemEffect effect) {
		this.effect = effect;
	}

	/**
	 * @return the effectValue
	 */
	public int getEffectValue() {
		return effectValue;
	}

	/**
	 * @param effectValue
	 *            the effectValue to set
	 */
	public void setEffectValue(int effectValue) {
		this.effectValue = effectValue;
	}

	/**
	 * @return the durability
	 */
	public int getDurability() {
		return durability;
	}

	/**
	 * @param durability
	 *            the durability to set
	 */
	public void setDurability(int durability) {
		this.durability = durability;
	}
}