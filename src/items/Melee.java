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
	private double swingSpeed, rechargeTime;

	public Melee(int itemID, String name, BufferedImage[] images,
			AudioClip[] clips, ItemState state, double swingSpeed,
			double rechargeTime) {
		super(itemID, name, images, clips, state);
		this.setSwingSpeed(swingSpeed);
		this.setRechargeTime(rechargeTime);
	}

	/**
	 * @return the swingSpeed
	 */
	public double getSwingSpeed() {
		return swingSpeed;
	}

	/**
	 * @param swingSpeed
	 *            the swingSpeed to set
	 */
	public void setSwingSpeed(double swingSpeed) {
		this.swingSpeed = swingSpeed;
	}

	/**
	 * @return the rechargeTime
	 */
	public double getRechargeTime() {
		return rechargeTime;
	}

	/**
	 * @param rechargeTime
	 *            the rechargeTime to set
	 */
	public void setRechargeTime(double rechargeTime) {
		this.rechargeTime = rechargeTime;
	}
}