package items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.PriorityQueue;

import entities.Entity;
import entities.Inventory;
import entities.Player;
import enums.ItemState;
import main.Game;
import utilities.Effect;

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
	private int currentAmmo;
	private int noise;
	private double adjustedAngle;
	private int reloadTime;
	private int noOfProjectiles;
	private int penetration;
	private int automatic;

	private long reloadTick = -60;

	/**
	 * Constructs a new Firearm object.
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
	 * @param ammoID
	 *            the item ID of the firearm's ammo.
	 * @param rateOfFire
	 *            the number of ticks in between each shot.
	 * @param maxAmmo
	 *            the maximum ammo the item can hold.
	 * @param noise
	 *            the radius of the noise circle the firearm makes.
	 * @param reloadTime
	 *            the number of milliseconds it takes to reload.
	 * @param noOfProjectiles
	 *            the number of projectiles the firearm fires.
	 * @param penetration
	 *            the number of zombies that the firearm's bullets penetrate.
	 * @param automatic
	 *            whether or not the firearm is automatic (0 = false, 1 = true).
	 */
	public Firearm(int itemID, String name, int rarity, int effectValue, ItemState state, BufferedImage[] images,
			Effect[] clips, Game game, int ammoID, int rateOfFire, int maxAmmo, int noise, int reloadTime,
			int noOfProjectiles, int penetration, int automatic) {
		super(itemID, name, rarity, effectValue, state, images, clips, game);

		this.ammoID = ammoID;
		this.rateOfFire = rateOfFire;
		this.maxAmmo = maxAmmo;
		this.currentAmmo = this.maxAmmo;
		this.noise = noise;
		this.reloadTime = reloadTime;
		this.noOfProjectiles = noOfProjectiles;
		this.penetration = penetration;
		this.automatic = automatic;
	}

	/**
	 * Clones an item template for multiple use.
	 * 
	 * @param item
	 *            the item template.
	 */
	public Firearm(Firearm item) {
		super(item);

		this.ammoID = item.ammoID;
		this.rateOfFire = item.rateOfFire;
		this.maxAmmo = item.maxAmmo;
		this.currentAmmo = (int) (Math.random() * this.maxAmmo);
		this.noise = item.noise;
		this.reloadTime = item.reloadTime;
		this.noOfProjectiles = item.noOfProjectiles;
		this.penetration = item.penetration;
		this.automatic = item.automatic;
	}

	@Override
	public void use(Player player) {
		// Calculate angle of trajectory
		double angle = -Math.atan2(
				game.getDisplay().getMouseHandler().getMouseLocation().y
						- (player.getPosition().y + 16 - game.getCamera().getyOffset()),
				game.getDisplay().getMouseHandler().getMouseLocation().x
						- (player.getPosition().x + 16 - game.getCamera().getxOffset()));
		// Fire the gun if not empty and the enough time has passed since last
		// shot
		if (!this.isEmpty()) {
			long currentTick = this.game.getTickCount();
			if (currentTick - player.getLastItemTick() > this.rateOfFire) {
				player.setLastItemTick(currentTick);
				// Play the firing sound
				clips[0].play();

				int range = 32 * 64;

				// Calculate the collisions for each shot
				for (int i = 1; i <= this.noOfProjectiles; i++) {
					// Adjust bullet spread for each shot
					double angleAdjust = ((i / 2) * .05);
					if (i % 2 == 1)
						angleAdjust = angleAdjust * (-1);
					adjustedAngle = angle + angleAdjust;
					// Create line used to check entity collisions
					Line2D.Double line = new Line2D.Double(
							new Point(player.getPosition().x + 16, player.getPosition().y + 16),
							new Point((int) (player.getPosition().x + 16 + range * Math.cos(adjustedAngle)),
									(int) (player.getPosition().y + 16 - range * Math.sin(adjustedAngle))));
					// Get the entities hit
					PriorityQueue<Entity> collisions = player.projectileTracer(line, this.getEffectValue(), 1000);
					if (collisions.size() > 0) {
						for (int j = 0; j < Math.min(collisions.size(), penetration); j++) {
							collisions.remove().damage(this.getEffectValue());
						}

					}

					player.makeNoise(this.noise, true);
				}
				this.removeAmmo();
				player.shoot();
			}
		}
	}

	/**
	 * Reload the firearm with a delay.
	 * 
	 * @param player
	 *            the player that is reloading the firearm.
	 */
	public void reload(Player player) {
		// Check if enough time has passed to reload
		long currentTick = player.getGame().getTickCount();
		if (currentTick - this.reloadTick > this.reloadTime / 1000 * 60 + 10) {
			this.reloadTick = currentTick;
			
			// Look for the correct ammo for this firearm
			for (int itemNo = 0; itemNo < Inventory.NO_OF_ITEMS; itemNo++) {
				Item currentItem = player.getItem(itemNo);
				if (currentItem != null && currentItem.getItemID() == this.ammoID) {
					Consumable ammo = ((Consumable) currentItem);
					
					// Play the ammo sound
					ammo.clips[0].play();
					Firearm firearm = this;
					
					// Run a thread to delay the reload
					Thread reloadPause = new Thread(new Runnable() {
						public void run() {
							try {
								Thread.sleep(firearm.reloadTime);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							int bullets = ammo.getDurability();
							
							// Swap the ammo between the gun and ammo
							// If the ammo has run out, remove it
							if (firearm.currentAmmo > 0) {
								ammo.setDurability(firearm.currentAmmo);
							} else {
								player.removeItem(currentItem);
							}
							firearm.currentAmmo = bullets;
						}
					});
					reloadPause.start();
					return;
				}
			}
		}
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

	public int getCurrentAmmo() {
		return this.currentAmmo;
	}

	public void setCurrentAmmo(int currentAmmo) {
		this.currentAmmo = currentAmmo;
	}

	public void removeAmmo() {
		this.currentAmmo--;
	}

	public boolean isFull() {
		return this.currentAmmo == this.maxAmmo;
	}

	public boolean isEmpty() {
		return this.currentAmmo == 0;
	}

	public double getAdjustedAngle() {
		return adjustedAngle;
	}

	public void setAdjustedAngle(double adjustedAngle) {
		this.adjustedAngle = adjustedAngle;
	}

	public boolean isAutomatic() {
		return this.automatic == 1;
	}

	@Override
	public void renderTooltip(Graphics g, Point mouseLocation) {
		// Render the tooltip's background depending on its rarity
		g.setColor(new Color(getColour().getRed(), getColour().getGreen(), getColour().getBlue(), 75));
		g.fillRect(mouseLocation.x, mouseLocation.y - 275, 300, 275);

		// Write the item's name
		g.setColor(new Color(0, 0, 0, 200));
		g.setFont(this.game.getUiFont());
		g.drawString(this.name, mouseLocation.x + 20, mouseLocation.y - 225);

		// Write the item's rarity
		g.setFont(this.game.getUiFontXS());
		switch (this.rarity) {
		case 5:
			g.drawString("Common", mouseLocation.x + 20, mouseLocation.y - 205);
			break;
		case 4:
			g.drawString("Uncommon", mouseLocation.x + 20, mouseLocation.y - 205);
			break;
		case 3:
			g.drawString("Rare", mouseLocation.x + 20, mouseLocation.y - 205);
			break;
		case 2:
			g.drawString("Very Rare", mouseLocation.x + 20, mouseLocation.y - 205);
			break;
		case 1:
			g.drawString("Ultra Rare", mouseLocation.x + 20, mouseLocation.y - 205);
			break;
		}

		// Write whether or not the firearm is automatic
		if (this.automatic == 1) {
			g.drawString("Automatic firearm", mouseLocation.x + 20, mouseLocation.y - 190);
		} else {
			g.drawString("Semi-automatic firearm", mouseLocation.x + 20, mouseLocation.y - 190);
		}

		// Write the damage of the firearm
		g.setFont(this.game.getUiFontS());
		g.drawString("Deals " + this.effectValue + " damage", mouseLocation.x + 20, mouseLocation.y - 165);

		// Write a relative attack speed
		if (this.rateOfFire >= 40) {
			g.drawString("Very slow attack speed", mouseLocation.x + 20, mouseLocation.y - 140);
		} else if (this.rateOfFire >= 30) {
			g.drawString("Slow attack speed", mouseLocation.x + 20, mouseLocation.y - 140);
		} else if (this.rateOfFire >= 20) {
			g.drawString("Normal attack speed", mouseLocation.x + 20, mouseLocation.y - 140);
		} else if (this.rateOfFire >= 10) {
			g.drawString("Fast attack speed", mouseLocation.x + 20, mouseLocation.y - 140);
		} else {
			g.drawString("Very fast attack speed", mouseLocation.x + 20, mouseLocation.y - 140);
		}

		// Write a relative reload time
		if (this.reloadTime >= 5000) {
			g.drawString("Very slow reload time", mouseLocation.x + 20, mouseLocation.y - 115);
		} else if (this.reloadTime >= 4000) {
			g.drawString("Slow reload time", mouseLocation.x + 20, mouseLocation.y - 115);
		} else if (this.reloadTime >= 3000) {
			g.drawString("Normal reload time", mouseLocation.x + 20, mouseLocation.y - 115);
		} else if (this.reloadTime >= 2000) {
			g.drawString("Fast reload time", mouseLocation.x + 20, mouseLocation.y - 115);
		} else {
			g.drawString("Very fast reload time", mouseLocation.x + 20, mouseLocation.y - 115);
		}

		// Write how many projectiles the firearm shoots
		if (this.noOfProjectiles == 1) {
			g.drawString("Shoots " + this.noOfProjectiles + " projectile", mouseLocation.x + 20, mouseLocation.y - 90);
		} else {
			g.drawString("Shoots " + this.noOfProjectiles + " projectiles", mouseLocation.x + 20, mouseLocation.y - 90);
		}

		// Write how many zombies the firearm's bullets penetrates
		if (this.penetration == 1) {
			g.drawString("No penetration", mouseLocation.x + 20, mouseLocation.y - 65);
		} else {
			g.drawString("Penetrates up to " + this.penetration + " zombies", mouseLocation.x + 20,
					mouseLocation.y - 65);
		}

		// Write how much ammo the firearm has
		g.drawString(this.currentAmmo + " / " + this.maxAmmo + " ammo", mouseLocation.x + 20, mouseLocation.y - 40);
	}
}