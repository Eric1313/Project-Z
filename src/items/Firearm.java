package items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.PriorityQueue;

import main.Game;
import utilities.Effect;
import entities.Entity;
import entities.Inventory;
import entities.Player;
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
	private int currentAmmo;
	private int noise;
	private double adjustedAngle;
	private int reloadTime;
	private int noProjectiles;
	private int penetration;
	private int automatic;

	private long reloadTick = -60;

	public Firearm(int itemID, String name, int rarity, int effectValue,
			ItemState state, BufferedImage[] images, Effect[] clips, Game game,
			int ammoID, int rateOfFire, int maxAmmo, int noise, int reloadTime,
			int noProjectiles, int penetration, int automatic) {
		super(itemID, name, rarity, effectValue, state, images, clips, game);

		this.ammoID = ammoID;
		this.rateOfFire = rateOfFire;
		this.maxAmmo = maxAmmo;
		this.currentAmmo = this.maxAmmo;
		this.noise = noise;
		this.reloadTime = reloadTime;
		this.noProjectiles = noProjectiles;
		this.penetration = penetration;
		this.automatic = automatic;
	}

	public Firearm(Firearm item) {
		super(item);

		this.ammoID = item.ammoID;
		this.rateOfFire = item.rateOfFire;
		this.maxAmmo = item.maxAmmo;
		this.currentAmmo = (int) (Math.random() * this.maxAmmo);
		this.noise = item.noise;
		this.reloadTime = item.reloadTime;
		this.noProjectiles = item.noProjectiles;
		this.penetration = item.penetration;
		this.automatic = item.automatic;
	}

	@Override
	public void use(Player player) {
		// Calculate angle of trajectory
		double angle = -Math.atan2(
				game.getDisplay().getMouseHandler().getMouseLocation().y
						- (player.getPosition().y + 16 - game.getCamera()
								.getyOffset()), game.getDisplay()
						.getMouseHandler().getMouseLocation().x
						- (player.getPosition().x + 16 - game.getCamera()
								.getxOffset()));
		// Fire the gun if not empty and the enough time has passed since last
		// shot
		if (!this.isEmpty()) {
			long currentTick = game.getTickCount();
			if (currentTick - player.getLastItemTick() > this.getRateOfFire()) {
				player.setLastItemTick(currentTick);
				// Play the firing sound
				clips[0].play();

				int range = 32 * 64;

				// Calculate the collisions for each shot
				for (int i = 1; i <= noProjectiles; i++) {
					// Adjust bullet spread for each shot
					double angleAdjust = ((i / 2) * .05);
					if (i % 2 == 1)
						angleAdjust = angleAdjust * (-1);
					adjustedAngle = angle + angleAdjust;
					// Create line used to check entity collisions
					Line2D.Double line = new Line2D.Double(new Point(
							player.getPosition().x + 16,
							player.getPosition().y + 16), new Point(
							(int) (player.getPosition().x + 16 + range
									* Math.cos(adjustedAngle)),
							(int) (player.getPosition().y + 16 - range
									* Math.sin(adjustedAngle))));
					// Get the entities hit
					PriorityQueue<Entity> collisions =  player.projectileTracer(
							line, this.getEffectValue(), 1000);
					if (collisions.size() > 0) {
						for (int j = 0; j < Math.min(collisions.size(),
								penetration); j++) {
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

	public void reload(Player player) {
		long currentTick = player.getGame().getTickCount();

		if (currentTick - this.reloadTick > 180) {
			this.reloadTick = currentTick;
			for (int itemNo = 0; itemNo < Inventory.NO_OF_ITEMS; itemNo++) {
				Item currentItem = player.getItem(itemNo);
				if (currentItem != null
						&& currentItem.getItemID() == this.ammoID) {
					Consumable ammo = ((Consumable) currentItem);
					ammo.clips[0].play();
					Firearm firearm = this;
					Thread reloadPause = new Thread(new Runnable() {
						public void run() {
							try {
								Thread.sleep(firearm.reloadTime);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							int bullets = ammo.getDurability();
							if (firearm.currentAmmo > 0) {
								ammo.setDurability(firearm.currentAmmo);
							} else {
								player.removeItem(currentItem);
							}
							firearm.currentAmmo = bullets;
						}
					});
					reloadPause.start();

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
		g.setColor(new Color(getColour().getRed(), getColour().getGreen(),
				getColour().getBlue(), 75));
		g.fillRect(mouseLocation.x, mouseLocation.y - 200, 300, 200);

		g.setColor(new Color(0, 0, 0, 200));
		g.setFont(this.game.getUiFont());
		g.drawString(this.name, mouseLocation.x + 20, mouseLocation.y - 150);

		g.setFont(this.game.getUiFontXS());
		switch (this.rarity) {
		case 5:
			g.drawString("Common", mouseLocation.x + 20, mouseLocation.y - 130);
			break;
		case 4:
			g.drawString("Uncommon", mouseLocation.x + 20,
					mouseLocation.y - 130);
			break;
		case 3:
			g.drawString("Rare", mouseLocation.x + 20, mouseLocation.y - 130);
			break;
		case 2:
			g.drawString("Very Rare", mouseLocation.x + 20,
					mouseLocation.y - 130);
			break;
		case 1:
			g.drawString("Ultra Rare", mouseLocation.x + 20,
					mouseLocation.y - 130);
			break;
		}

		g.setFont(this.game.getUiFontS());
		g.drawString("Deals " + this.effectValue + " damage",
				mouseLocation.x + 20, mouseLocation.y - 105);

		if (this.rateOfFire >= 60) {
			g.drawString("Very slow attack speed", mouseLocation.x + 20,
					mouseLocation.y - 80);
		} else if (this.rateOfFire >= 50) {
			g.drawString("Slow attack speed", mouseLocation.x + 20,
					mouseLocation.y - 80);
		} else if (this.rateOfFire >= 40) {
			g.drawString("Normal attack speed", mouseLocation.x + 20,
					mouseLocation.y - 80);
		} else if (this.rateOfFire >= 30) {
			g.drawString("Fast attack speed", mouseLocation.x + 20,
					mouseLocation.y - 80);
		} else {
			g.drawString("Very fast attack speed", mouseLocation.x + 20,
					mouseLocation.y - 80);
		}

		g.drawString(this.currentAmmo + " / " + this.maxAmmo + " ammo",
				mouseLocation.x + 20, mouseLocation.y - 55);
	}
}