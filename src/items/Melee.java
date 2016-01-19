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
import utilities.Sound;

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

	public Melee(int itemID, String name, int rarity, int effectValue, ItemState state, BufferedImage[] images,
			Effect[] clips, Game game, int swingSpeed, int rechargeTime, int radius, int angle, int durability) {
		super(itemID, name, rarity, effectValue, state, images, clips, game);

		this.swingSpeed = swingSpeed;
		this.rechargeTime = rechargeTime;
		this.radius = radius;
		this.angle = angle;
		this.durability = durability;
	}

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

		double angle = -Math.atan2(
				game.getDisplay().getMouseHandler().getMouseLocation().y
						- (player.getPosition().y + 16 - game.getCamera().getyOffset()),
				game.getDisplay().getMouseHandler().getMouseLocation().x
						- (player.getPosition().x + 16 - game.getCamera().getxOffset()));

		if (angle < 0) {
			angle = 2 * Math.PI + angle;
		}

		long currentTick = this.game.getTickCount();
		if (currentTick - player.getLastItemTick() > this.rechargeTime + this.swingSpeed) {
			player.setLastItemTick(currentTick);

			player.swing(currentTick, angle, Math.toRadians(this.angle));

			angle = Math.toDegrees(angle);

			Arc2D arc = new Arc2D.Double();
			arc.setArcByCenter(player.getPosition().x, player.getPosition().y, this.radius, angle - this.angle,
					this.angle * 2, Arc2D.PIE);

			int enemiesHit = player.meleeCollision(arc, this.effectValue);

			if (enemiesHit == 0) {
				clips[0].play();
				player.makeNoise(100, true);
			} else {
				this.durability -= enemiesHit;
				clips[0].play();
				player.makeNoise(200, true);
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

	@Override
	public void renderTooltip(Graphics g, Point mouseLocation) {
		g.setColor(new Color(getColour().getRed(), getColour().getGreen(), getColour().getBlue(), 75));
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
			g.drawString("Uncommon", mouseLocation.x + 20, mouseLocation.y - 130);
			break;
		case 3:
			g.drawString("Rare", mouseLocation.x + 20, mouseLocation.y - 130);
			break;
		case 2:
			g.drawString("Very Rare", mouseLocation.x + 20, mouseLocation.y - 130);
			break;
		case 1:
			g.drawString("Ultra Rare", mouseLocation.x + 20, mouseLocation.y - 130);
			break;
		}

		g.setFont(this.game.getUiFontS());
		g.drawString("Deals " + this.effectValue + " damage", mouseLocation.x + 20, mouseLocation.y - 105);

		if (this.swingSpeed >= 60) {
			g.drawString("Very slow attack speed", mouseLocation.x + 20, mouseLocation.y - 80);
		} else if (this.swingSpeed >= 50) {
			g.drawString("Slow attack speed", mouseLocation.x + 20, mouseLocation.y - 80);
		} else if (this.swingSpeed >= 40) {
			g.drawString("Normal attack speed", mouseLocation.x + 20, mouseLocation.y - 80);
		} else if (this.swingSpeed >= 30) {
			g.drawString("Fast attack speed", mouseLocation.x + 20, mouseLocation.y - 80);
		} else {
			g.drawString("Very fast attack speed", mouseLocation.x + 20, mouseLocation.y - 80);
		}

		if (this.rechargeTime >= 40) {
			g.drawString("Very slow recharge time", mouseLocation.x + 20, mouseLocation.y - 55);
		} else if (this.rechargeTime >= 30) {
			g.drawString("Slow recharge time", mouseLocation.x + 20, mouseLocation.y - 55);
		} else if (this.rechargeTime >= 20) {
			g.drawString("Normal recharge time", mouseLocation.x + 20, mouseLocation.y - 55);
		} else if (this.rechargeTime >= 10) {
			g.drawString("Fast recharge time", mouseLocation.x + 20, mouseLocation.y - 55);
		} else {
			g.drawString("Very fast recharge time", mouseLocation.x + 20, mouseLocation.y - 55);
		}
	}

	public int getDurability() {
		return this.durability;
	}
}