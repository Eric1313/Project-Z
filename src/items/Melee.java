package items;

import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Point;
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

	public Melee(int itemID, String name, int rarity, int effectValue,
			ItemState state, BufferedImage[] images, AudioClip[] clips,
			Game game, int swingSpeed, int rechargeTime, int radius) {
		super(itemID, name, rarity, effectValue, state, images, clips, game);

		this.swingSpeed = swingSpeed;
		this.rechargeTime = rechargeTime;
		this.radius = radius;
	}
	
	@Override
	public void use(Player player) {
		double angle = -Math.atan2(game.getDisplay().getMouseHandler()
				.getMouseLocation().y
				- (position.y + 16 - game.getCamera().getyOffset()), game
				.getDisplay().getMouseHandler().getMouseLocation().x
				- (position.x + 16 - game.getCamera().getxOffset()));

		long currentTick = this.game.getTickCount();
		if (currentTick - player.getLastItemTick() > this.getRechargeTime()) {
			player.setLastItemTick(currentTick);
			
			for (long frame = currentTick; frame < currentTick + this.getSwingSpeed(); frame++) {
				Line2D.Double line = new Line2D.Double(new Point(player.getPosition().x + 16, player.getPosition().y + 16),
						new Point((int) (player.getPosition().x + 16 + this.getRadius() * Math.cos(angle + (Math.PI / 3) * ((frame - currentTick - this.getSwingSpeed() / 2) / (this.getSwingSpeed() / 2.0)))),
								(int) (player.getPosition().y + 16 - this.getRadius() * Math.sin(angle + (Math.PI / 3) * ((frame - currentTick - this.getSwingSpeed() / 2) / (this.getSwingSpeed() / 2.0))))));
//				System.out.println(frame - currentTick - newItem.getSwingSpeed() / 2);
				System.out.println(line.getX2() + " " + line.getY2());
			}
		}		
	}
	
	public Melee(Melee item) {
		super(item);
		
		this.swingSpeed = item.getSwingSpeed();
		this.rechargeTime = item.getRechargeTime();
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