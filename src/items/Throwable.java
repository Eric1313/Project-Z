package items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import entities.Player;
import enums.ItemEffect;
import enums.ItemState;
import main.Game;
import utilities.Sound;

/**
 * Subclass of Item that represents a throwable item in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Item
 * @since 1.0
 * @version 1.0
 */
public class Throwable extends Item {
	private ItemEffect effect;
	private int range;
	private int areaOfEffect;

	public Throwable(int itemID, String name, int rarity, int effectValue, ItemState state, BufferedImage[] images,
			Sound[] clips, Game game, ItemEffect effect, int range, int areaOfEffect) {
		super(itemID, name, rarity, effectValue, state, images, clips, game);

		this.effect = effect;
		this.areaOfEffect = areaOfEffect;
		this.range = range;
	}

	@Override
	public void use(Player player) {

		Line2D.Double line = new Line2D.Double(new Point(player.getPosition().x, player.getPosition().y),
				new Point(player.getPosition().x + game.getDisplay().getMouseHandler().getMouseLocation().x - 512,
						player.getPosition().y + game.getDisplay().getMouseHandler().getMouseLocation().y - 384));
		// System.out.println(( Math.sqrt(Math.pow(line.x2 - line.x1, 2)
		// + Math.pow(line.y2 - line.y1, 2))));
		if ((Math.sqrt(Math.pow(line.x1 - line.x2, 2) + Math.pow(line.y1 - line.y2, 2))) < range) {
			player.removeItem(this);
			this.state = ItemState.DROPPED;
			this.position = player.calculatePointOfImpact(line);
			player.getChunkMap()[this.position.x / 512][this.position.y / 512].add(this);
			makeNoise(150, true);
			clips[0].play();
		}
	}

	public Throwable(Throwable item) {
		super(item);

		this.effect = item.getEffect();
		this.range = item.getRange();
		this.areaOfEffect = item.getAreaOfEffect();
	}

	public ItemEffect getEffect() {
		return this.effect;
	}

	public void setEffect(ItemEffect effect) {
		this.effect = effect;
	}

	public int getRange() {
		return this.range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getAreaOfEffect() {
		return this.areaOfEffect;
	}

	public void setAreaOfEffect(int areaOfEffect) {
		this.areaOfEffect = areaOfEffect;
	}

	public void render(Graphics g) {
		super.render(g);

	}

	@Override
	public void renderTooltip(Graphics g, Point mouseLocation) {
		g.setColor(new Color(getColour().getRed(), getColour().getGreen(), getColour().getBlue(), 75));
		g.fillRect(mouseLocation.x, mouseLocation.y - 200, 300, 200);

		g.setColor(new Color(0, 0, 0, 200));
		g.setFont(this.game.getUiFont());
		g.drawString(this.name, mouseLocation.x + 20, mouseLocation.y - 150);

		g.setFont(this.game.getTinyUiFont());
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

		g.setFont(this.game.getMiniUiFont());
		switch (this.effect) {
		case NOISE:
			g.drawString("Creates noise of " + this.effectValue + " radius", mouseLocation.x + 20,
					mouseLocation.y - 105);
			break;
		case DAMAGE:
			g.drawString("Deals " + this.effectValue + " damage", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		}

		if (this.areaOfEffect >= 512) {
			g.drawString("Very large area of effect", mouseLocation.x + 20, mouseLocation.y - 80);
		} else if (this.areaOfEffect >= 256) {
			g.drawString("Large area of effect", mouseLocation.x + 20, mouseLocation.y - 80);
		} else if (this.areaOfEffect >= 128) {
			g.drawString("Normal area of effect", mouseLocation.x + 20, mouseLocation.y - 80);
		} else if (this.areaOfEffect >= 64) {
			g.drawString("Small area of effect", mouseLocation.x + 20, mouseLocation.y - 80);
		} else {
			g.drawString("Very small area of effect", mouseLocation.x + 20, mouseLocation.y - 80);
		}

		if (this.range >= 640) {
			g.drawString("Very far throwing range", mouseLocation.x + 20, mouseLocation.y - 55);
		} else if (this.range >= 480) {
			g.drawString("Far throwing range", mouseLocation.x + 20, mouseLocation.y - 55);
		} else if (this.range >= 320) {
			g.drawString("Normal throwing range", mouseLocation.x + 20, mouseLocation.y - 55);
		} else if (this.range >= 160) {
			g.drawString("Close throwing range", mouseLocation.x + 20, mouseLocation.y - 55);
		} else {
			g.drawString("Very close throwing range", mouseLocation.x + 20, mouseLocation.y - 55);
		}
	}
}