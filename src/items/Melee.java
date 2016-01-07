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
			AudioClip[] clips, ItemState state, int rarity, int swingSpeed,
			int rechargeTime) {
		super(itemID, name, images, clips, state, rarity);
		this.setSwingSpeed(swingSpeed);
		this.setRechargeTime(rechargeTime);
	}

	/**
	 * @return the swingSpeed
	 */
	public int getSwingSpeed() {
		return swingSpeed;
	}

	/**
	 * @param swingSpeed
	 *            the swingSpeed to set
	 */
	public void setSwingSpeed(int swingSpeed) {
		this.swingSpeed = swingSpeed;
	}

	/**
	 * @return the rechargeTime
	 */
	public int getRechargeTime() {
		return rechargeTime;
	}

	/**
	 * @param rechargeTime
	 *            the rechargeTime to set
	 */
	public void setRechargeTime(int rechargeTime) {
		this.rechargeTime = rechargeTime;
	}
}