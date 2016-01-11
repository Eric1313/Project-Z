package items;

import java.applet.AudioClip;
import java.awt.Graphics;
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

	public Firearm(int itemID, String name, int rarity, int effectValue,
			ItemState state, BufferedImage[] images, AudioClip[] clips,
			int ammoID, int rateOfFire, int maxAmmo) {
		super(itemID, name, rarity, effectValue, state, images, clips);

		this.ammoID = ammoID;
		this.rateOfFire = rateOfFire;
		this.maxAmmo = maxAmmo;
	}

	public int getAmmoID() {
		return this.ammoID;
	}

	public void setAmmoID(int ammoID) {
		this.ammoID = ammoID;
	}

	public int getRateOfFire() {
		return this.rateOfFire;
	}

	public void setRateOfFire(int rateOfFire) {
		this.rateOfFire = rateOfFire;
	}

	public int getMaxAmmo() {
		return this.maxAmmo;
	}

	public void setMaxAmmo(int maxAmmo) {
		this.maxAmmo = maxAmmo;
	}
	
	@Override
	public void render(Graphics g) {
		// TODO
	}
}