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
	protected int ammoID;
	protected double rateOfFire;

	public Firearm(int itemID,String name, BufferedImage[] images,AudioClip[] clips,ItemState state, int ammoID, double rateOfFire) {
		super(itemID,name, images,clips,state);
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