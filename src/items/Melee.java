package items;

import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
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
	private int swingSpeed;
	private int rechargeTime;

	public Melee(int itemID, String name, int rarity, int effectValue,
			ItemState state, BufferedImage[] images, AudioClip[] clips,
			Game game, int swingSpeed, int rechargeTime) {
		super(itemID, name, rarity, effectValue, state, images, clips, game);

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