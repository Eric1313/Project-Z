package items;

import java.applet.AudioClip;
import java.awt.image.BufferedImage;

import enums.ItemEffect;
import enums.ItemState;

/**
 * Subclass of Item that represents a throwable item in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Item
 * @since 1.0
 * @version 1.0
 */
public class Throwable extends Item {
	private ItemEffect effect;
	private int range;
	private int areaOfEffect;

	public Throwable(int itemID, String name, int rarity, int effectValue,
			ItemState state, BufferedImage[] images, AudioClip[] clips,
			ItemEffect effect, int range, int areaOfEffect) {
		super(itemID, name, rarity, effectValue, state, images, clips);

		this.effect = effect;
		this.areaOfEffect = areaOfEffect;
		this.range = range;
	}

	public ItemEffect getEffect() {
		return this.effect;
	}

	public void setEffect(ItemEffect effect) {
		this.effect = effect;
	}

	public int getRange() {
		return this.range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getAreaOfEffect() {
		return this.areaOfEffect;
	}

	public void setAreaOfEffect(int areaOfEffect) {
		this.areaOfEffect = areaOfEffect;
	}
}