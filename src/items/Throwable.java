package items;

import java.applet.AudioClip;
import java.awt.image.BufferedImage;

import enums.ItemEffect;
import enums.ItemState;

/**
 * Subclass of Item that represents a throwable weapon item in Project Z.
 * 
 * @author Patrick Liu, Eric Chee, Allen Han, Alosha Reymer
 * @see Item
 * @since 1.0
 * @version 1.0
 */
public class Throwable extends Item {
	private int range, areaOfEffect;
	private ItemEffect effect;

	public Throwable(int itemID,String name,ItemState state, BufferedImage[] images,AudioClip[] clips,
			ItemEffect effect, int range, int areaOfEffect) {
		super(itemID,name, images,clips,state);
		this.effect = effect;
		this.areaOfEffect = areaOfEffect;
		this.range=range;
	}

	/**
	 * @return the range
	 */
	public int getRange() {
		return range;
	}

	/**
	 * @param range the range to set
	 */
	public void setRange(int range) {
		this.range = range;
	}

	/**
	 * @return the areaOfEffect
	 */
	public int getAreaOfEffect() {
		return areaOfEffect;
	}

	/**
	 * @param areaOfEffect the areaOfEffect to set
	 */
	public void setAreaOfEffect(int areaOfEffect) {
		this.areaOfEffect = areaOfEffect;
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

}