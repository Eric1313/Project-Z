package items;

import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import main.Game;
import entities.Player;
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
	private int radius;

	public Melee(int itemID, String name, int rarity, int effectValue, ItemState state, BufferedImage[] images,
			AudioClip[] clips, Game game, int swingSpeed, int rechargeTime, int radius) {
		super(itemID, name, rarity, effectValue, state, images, clips, game);

		this.swingSpeed = swingSpeed;
		this.rechargeTime = rechargeTime;
		this.radius = radius;
	}

	@Override
	public void use(Player player) {
		double angle = -Math.atan2(
				game.getDisplay().getMouseHandler().getMouseLocation().y
						- (player.getPosition().y + 16 - game.getCamera().getyOffset()),
				game.getDisplay().getMouseHandler().getMouseLocation().x
						- (player.getPosition().x + 16 - game.getCamera().getxOffset()));

		long currentTick = this.game.getTickCount();
		if (currentTick - player.getLastItemTick() > this.getRechargeTime()) {
			player.setLastItemTick(currentTick);

			Arc2D arc = new Arc2D.Double();
			arc.setArcByCenter(player.getPosition().x + 16, player.getPosition().y + 16, this.radius,
					Math.toDegrees(angle) - 30, Math.toDegrees(angle) + 30, Arc2D.PIE);
			
			int enemiesHit = player.meleeCollision(arc, this.effectValue);
			
			if (enemiesHit == 0) {
				player.makeNoise(100, true);
			} else {
				player.makeNoise(200, true);
			}
		}
	}

	public Melee(Melee item) {
		super(item);

		this.swingSpeed = item.swingSpeed;
		this.rechargeTime = item.rechargeTime;
		this.radius = item.radius;
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
}