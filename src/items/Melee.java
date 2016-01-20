package items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

import entities.Player;
import enums.ItemState;
import main.Game;
import utilities.Effect;

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
	private int radius;
	private int angle;
	private int durability;

	/**
	 * Constructs a new Melee object.
	 * 
	 * @param itemID
	 *            the item ID.
	 * @param name
	 *            the name of the item.
	 * @param rarity
	 *            the rarity of the item (from 1-5).
	 * @param effectValue
	 *            the effect's value. In the case of weapons, this would be the
	 *            damage.
	 * @param state
	 *            whether the item is in an inventory or dropped in the world.
	 * @param images
	 *            an array of images of the item.
	 * @param clips
	 *            an array of audio clips played by item.
	 * @param game
	 *            the game to add the item to.
	 * @param swingSpeed
	 *            the number of ticks it takes to complete a swing.
	 * @param rechargeTime
	 *            the number of ticks in between swings.
	 * @param radius
	 *            the radius of the arc that the melee weapon reaches.
	 * @param angle
	 *            the angle on each side of the swing that the melee weapon
	 *            reaches (in degrees).
	 * @param durability
	 *            the number of times the weapon can be used before breaking.
	 */
	public Melee(int itemID, String name, int rarity, int effectValue, ItemState state, BufferedImage[] images,
			Effect[] clips, Game game, int swingSpeed, int rechargeTime, int radius, int angle, int durability) {
		super(itemID, name, rarity, effectValue, state, images, clips, game);

		this.swingSpeed = swingSpeed;
		this.rechargeTime = rechargeTime;
		this.radius = radius;
		this.angle = angle;
		this.durability = durability;
	}

	/**
	 * Clones an item template for multiple use.
	 * 
	 * @param item
	 *            the item template.
	 */
	public Melee(Melee item) {
		super(item);

		this.swingSpeed = item.swingSpeed;
		this.rechargeTime = item.rechargeTime;
		this.radius = item.radius;
		this.angle = item.angle;
		this.durability = item.durability;
	}

	@Override
	public void use(Player player) {
		// Find the middle angle
		double angle = -Math.atan2(
				game.getDisplay().getMouseHandler().getMouseLocation().y
						- (player.getPosition().y + 16 - game.getCamera().getyOffset()),
				game.getDisplay().getMouseHandler().getMouseLocation().x
						- (player.getPosition().x + 16 - game.getCamera().getxOffset()));

		// Make sure the angle is positive
		if (angle < 0) {
			angle = 2 * Math.PI + angle;
		}

		// Check if enough ticks have passed for the item to be used again
		long currentTick = this.game.getTickCount();
		if (currentTick - player.getLastItemTick() > this.rechargeTime + this.swingSpeed) {
			player.setLastItemTick(currentTick);

			// Send a message to the player in order to render the swinging
			player.swing(currentTick, angle, Math.toRadians(this.angle));

			angle = Math.toDegrees(angle);

			// Create an arc given the middle angle, the radius, and the number
			// of angles on each side of the middle angle
			Arc2D arc = new Arc2D.Double();
			arc.setArcByCenter(player.getPosition().x, player.getPosition().y, this.radius, angle - this.angle,
					this.angle * 2, Arc2D.PIE);

			// Check the number of enemies hit by the arc
			int enemiesHit = player.meleeCollision(arc, this.effectValue);

			// Make a certain amount of noise depending on the number of zombies
			// hit
			if (enemiesHit == 0) {
				clips[0].play();
				player.makeNoise(100, true);
			} else {
				this.durability -= enemiesHit;
				clips[0].play();
				player.makeNoise(200, true);

				// If the item has run out of durability, remove it
				if (this.durability <= 0) {
					player.removeItem(this);
				}
			}
		}
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

	public int getRadius() {
		return this.radius;
	}

	public int getDurability() {
		return this.durability;
	}

	@Override
	public void renderTooltip(Graphics g, Point mouseLocation) {
		// Render the tooltip's background depending on its rarity
		g.setColor(new Color(getColour().getRed(), getColour().getGreen(), getColour().getBlue(), 75));
		g.fillRect(mouseLocation.x, mouseLocation.y - 250, 300, 250);

		// Write the item's name
		g.setColor(new Color(0, 0, 0, 200));
		g.setFont(this.game.getUiFont());
		g.drawString(this.name, mouseLocation.x + 20, mouseLocation.y - 200);

		// Write the item's rarity
		g.setFont(this.game.getUiFontXS());
		switch (this.rarity) {
		case 5:
			g.drawString("Common", mouseLocation.x + 20, mouseLocation.y - 180);
			break;
		case 4:
			g.drawString("Uncommon", mouseLocation.x + 20, mouseLocation.y - 180);
			break;
		case 3:
			g.drawString("Rare", mouseLocation.x + 20, mouseLocation.y - 180);
			break;
		case 2:
			g.drawString("Very Rare", mouseLocation.x + 20, mouseLocation.y - 180);
			break;
		case 1:
			g.drawString("Ultra Rare", mouseLocation.x + 20, mouseLocation.y - 180);
			break;
		}
		
		// Write the type of item this is
		g.drawString("Melee weapon", mouseLocation.x + 20, mouseLocation.y - 165);

		// Write the damage of the melee weapon
		g.setFont(this.game.getUiFontS());
		g.drawString("Deals " + this.effectValue + " damage", mouseLocation.x + 20, mouseLocation.y - 140);

		// Write the relative swing speed
		if (this.swingSpeed >= 60) {
			g.drawString("Very slow attack speed", mouseLocation.x + 20, mouseLocation.y - 115);
		} else if (this.swingSpeed >= 50) {
			g.drawString("Slow attack speed", mouseLocation.x + 20, mouseLocation.y - 115);
		} else if (this.swingSpeed >= 40) {
			g.drawString("Normal attack speed", mouseLocation.x + 20, mouseLocation.y - 115);
		} else if (this.swingSpeed >= 30) {
			g.drawString("Fast attack speed", mouseLocation.x + 20, mouseLocation.y - 115);
		} else {
			g.drawString("Very fast attack speed", mouseLocation.x + 20, mouseLocation.y - 115);
		}

		// Write the relative recharge time
		if (this.rechargeTime >= 40) {
			g.drawString("Very slow recharge time", mouseLocation.x + 20, mouseLocation.y - 90);
		} else if (this.rechargeTime >= 30) {
			g.drawString("Slow recharge time", mouseLocation.x + 20, mouseLocation.y - 90);
		} else if (this.rechargeTime >= 20) {
			g.drawString("Normal recharge time", mouseLocation.x + 20, mouseLocation.y - 90);
		} else if (this.rechargeTime >= 10) {
			g.drawString("Fast recharge time", mouseLocation.x + 20, mouseLocation.y - 90);
		} else {
			g.drawString("Very fast recharge time", mouseLocation.x + 20, mouseLocation.y - 90);
		}

		// Write the relative range of the melee weapon
		if (this.radius * this.angle >= 4500) {
			g.drawString("Very large range", mouseLocation.x + 20, mouseLocation.y - 65);
		} else if (this.radius * this.angle >= 4000) {
			g.drawString("Large range", mouseLocation.x + 20, mouseLocation.y - 65);
		} else if (this.radius * this.angle >= 2500) {
			g.drawString("Normal range", mouseLocation.x + 20, mouseLocation.y - 65);
		} else if (this.radius * this.angle >= 1000) {
			g.drawString("Small range", mouseLocation.x + 20, mouseLocation.y - 65);
		} else {
			g.drawString("Very small range", mouseLocation.x + 20, mouseLocation.y - 65);
		}

		// Write the amount of times the weapon can be used
		if (this.durability == 1) {
			g.drawString("Can be used " + this.durability + " time", mouseLocation.x + 20, mouseLocation.y - 40);
		} else {
			g.drawString("Can be used " + this.durability + " times", mouseLocation.x + 20, mouseLocation.y - 40);
		}
	}
}