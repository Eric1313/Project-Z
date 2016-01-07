package items;

import java.applet.AudioClip;
import java.awt.image.BufferedImage;

import enums.ItemState;

/**
 * Subclass of Item that represents a firearm weapon item in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Item
 * @since 1.0
 * @version 1.0
 */
public class Firearm extends Item {
	private int ammoID;
	private int rateOfFire;
	private int maxAmmo;

	public Firearm(int itemID, String name, BufferedImage[] images,
			AudioClip[] clips, ItemState state, int rarity, int ammoID,
			int rateOfFire, int maxAmmo) {
		super(itemID, name, images, clips, state, rarity);
		this.ammoID = ammoID;
		this.rateOfFire = rateOfFire;
		this.maxAmmo = maxAmmo;
	}

	/**
	 * @return the ammoID
	 */
	public int getAmmoID() {
		return ammoID;
	}

	/**
	 * @param ammoID
	 *            the ammoID to set
	 */
	public void setAmmoID(int ammoID) {
		this.ammoID = ammoID;
	}

	/**
	 * @return the rateOfFire
	 */
	public int getRateOfFire() {
		return rateOfFire;
	}

	/**
	 * @param rateOfFire
	 *            the rateOfFire to set
	 */
	public void setRateOfFire(int rateOfFire) {
		this.rateOfFire = rateOfFire;
	}

	public int getMaxAmmo() {
		return maxAmmo;
	}

	public void setMaxAmmo(int maxAmmo) {
		this.maxAmmo = maxAmmo;
	}
}