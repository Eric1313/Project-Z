package items;

import java.awt.image.BufferedImage;

/**
 * Subclass of Item that represents a firearm weapon item in Project Z.
 * 
 * @author Patrick Liu, Eric Chee, Allen Han, Alosha Reymer
 * @see Item
 * @since 1.0
 * @version 1.0
 */
public class Firearm extends Item {
	protected int ammoID;
	protected double rateOfFire;

	public Firearm(String name, int itemID, BufferedImage[] images, int ammoID, double rateOfFire) {
		super(name, itemID, images);
		this.ammoID=ammoID;
		this.rateOfFire=rateOfFire;
	}

	/**
	 * @return the ammoID
	 */
	public int getAmmoID() {
		return ammoID;
	}

	/**
	 * @param ammoID the ammoID to set
	 */
	public void setAmmoID(int ammoID) {
		this.ammoID = ammoID;
	}

	/**
	 * @return the rateOfFire
	 */
	public double getRateOfFire() {
		return rateOfFire;
	}

	/**
	 * @param rateOfFire the rateOfFire to set
	 */
	public void setRateOfFire(double rateOfFire) {
		this.rateOfFire = rateOfFire;
	}

}