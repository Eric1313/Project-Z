package items;

import java.applet.AudioClip;
import java.awt.image.BufferedImage;

import enums.ItemState;

/**
 * Subclass of Item that represents a melee weapon item in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Item
 * @since 1.0
 * @version 1.0
 */
public class Melee extends Item {
	private int swingSpeed, rechargeTime;

	public Melee(int itemID, String name, BufferedImage[] images,
			AudioClip[] clips, ItemState state, int rarity, int effectValue,
			int swingSpeed, int rechargeTime) {
		super(itemID, name, images, clips, state, rarity);

		this.effectValue = effectValue;
		this.swingSpeed = swingSpeed;
		this.rechargeTime = rechargeTime;
	}

	public int getSwingSpeed() {
		return this.swingSpeed;
	}

	public void setSwingSpeed(int swingSpeed) {
		this.swingSpeed = swingSpeed;
	}

	public int getRechargeTime() {
		return this.rechargeTime;
	}

	public void setRechargeTime(int rechargeTime) {
		this.rechargeTime = rechargeTime;
	}
}