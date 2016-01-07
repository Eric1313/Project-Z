package items;

import java.awt.image.BufferedImage;
import states.ItemEffect;

/**
 * Subclass of Item that represents a consumable item in Project Z.
 * 
 * @author Patrick Liu, Eric Chee, Allen Han, Alosha Reymer
 * @see Item
 * @since 1.0
 * @version 1.0
 */
public class Consumable extends Item {
	private ItemEffect effect;
	private int effectValue;
	public Consumable(String name, int itemID, BufferedImage[] images,ItemEffect effect, int effectValue) {
		super(name, itemID, images);
		this.setEffect(effect);
		this.setEffect(effect);
		this.setEffectValue(effectValue);
	}
	/**
	 * @return the effect
	 */
	public ItemEffect getEffect() {
		return effect;
	}
	/**
	 * @param effect the effect to set
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
	 * @param effectValue the effectValue to set
	 */
	public void setEffectValue(int effectValue) {
		this.effectValue = effectValue;
	}
	
}