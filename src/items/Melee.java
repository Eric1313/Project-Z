package items;

import java.awt.image.BufferedImage;

/**
 * Subclass of Item that represents a melee weapon item in Project Z.
 * 
 * @author Patrick Liu, Eric Chee, Allen Han, Alosha Reymer
 * @see Item
 * @since 1.0
 * @version 1.0
 */
public class Melee extends Item {
	private double swingSpeed,rechargeTime;
	public Melee(String name, int itemID, BufferedImage[] images, double swingSpeed, double rechargeTime) {
		super(name, itemID, images);
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
	 * @param swingSpeed the swingSpeed to set
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
	 * @param rechargeTime the rechargeTime to set
	 */
	public void setRechargeTime(double rechargeTime) {
		this.rechargeTime = rechargeTime;
	}
}