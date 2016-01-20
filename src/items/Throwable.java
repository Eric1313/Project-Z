package items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import entities.Player;
import enums.ItemState;
import main.Game;
import utilities.SoundEffect;

/**
 * Subclass of Item that represents a throwable item in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Item
 * @since 1.0
 * @version 1.0
 */
public class Throwable extends Item {
	private int range;

	/**
	 * Constructs a new Throwable object.
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
	 * @param range
	 *            the radius of the circle of which the throwable can be thrown.
	 */
	public Throwable(int itemID, String name, int rarity, int effectValue, ItemState state, BufferedImage[] images,
			String[] clips, Game game, int range) {
		super(itemID, name, rarity, effectValue, state, images, clips, game);

		this.range = range;
	}

	@Override
	public void use(Player player) {
		// Create a line from the player to the point that the brick was thrown
		Line2D.Double line = new Line2D.Double(new Point(player.getPosition().x, player.getPosition().y),
				new Point(player.getPosition().x + game.getDisplay().getMouseHandler().getMouseLocation().x - 512,
						player.getPosition().y + game.getDisplay().getMouseHandler().getMouseLocation().y - 384));
		if ((Math.sqrt(Math.pow(line.x1 - line.x2, 2) + Math.pow(line.y1 - line.y2, 2))) < range) {
			// Remove the item, check for collisions, add the item to the map,
			// and make a noise/sound effect
			player.removeItem(this);
			this.state = ItemState.DROPPED;
			this.position = player.calculatePointOfImpact(line);
			player.getChunkMap()[this.position.x / 512][this.position.y / 512].add(this);
			makeNoise(150, true);
			new SoundEffect(clips[0]).play();
		}
	}

	/**
	 * Clones an item template for multiple use.
	 * 
	 * @param item
	 *            the item template.
	 */
	public Throwable(Throwable item) {
		super(item);

		this.range = item.range;
	}

	public int getRange() {
		return this.range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public void render(Graphics g) {
		super.render(g);
	}

	@Override
	public void renderTooltip(Graphics g, Point mouseLocation) {
		// Render the tooltip's background depending on its rarity
		g.setColor(new Color(getColour().getRed(), getColour().getGreen(), getColour().getBlue(), 75));
		g.fillRect(mouseLocation.x, mouseLocation.y - 175, 300, 175);

		// Write the item's name
		g.setColor(new Color(0, 0, 0, 200));
		g.setFont(this.game.getUiFont());
		g.drawString(this.name, mouseLocation.x + 20, mouseLocation.y - 125);

		// Write the item's rarity
		g.setFont(this.game.getUiFontXS());
		switch (this.rarity) {
		case 5:
			g.drawString("Common", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		case 4:
			g.drawString("Uncommon", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		case 3:
			g.drawString("Rare", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		case 2:
			g.drawString("Very Rare", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		case 1:
			g.drawString("Ultra Rare", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		}

		// Write the type of item
		g.drawString("Throwable item", mouseLocation.x + 20, mouseLocation.y - 90);

		// Write what the item does
		g.setFont(this.game.getUiFontS());
		g.drawString("Creates noise", mouseLocation.x + 20, mouseLocation.y - 65);

		// Write the relative throwing range
		if (this.range >= 640) {
			g.drawString("Very far throwing range", mouseLocation.x + 20, mouseLocation.y - 40);
		} else if (this.range >= 480) {
			g.drawString("Far throwing range", mouseLocation.x + 20, mouseLocation.y - 40);
		} else if (this.range >= 320) {
			g.drawString("Normal throwing range", mouseLocation.x + 20, mouseLocation.y - 40);
		} else if (this.range >= 160) {
			g.drawString("Close throwing range", mouseLocation.x + 20, mouseLocation.y - 40);
		} else {
			g.drawString("Very close throwing range", mouseLocation.x + 20, mouseLocation.y - 40);
		}
	}
}