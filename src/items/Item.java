package items;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Game;
import enums.ItemState;

/**
 * Abstract Item class for all items in Project Z.
 * 
 * @author Patrick Liu, Eric Chee, Allen Han, Alosha Reymer
 * @since 1.0
 * @version 1.0
 */
public abstract class Item {
	protected int itemID;
	protected String name;

	/**
	 * Integer that decides how rare it is to find this item in a map.<br>
	 * This value ranges from 1-5, where 1 is the most rare and 5 is the most
	 * common.<br>
	 * Colour of the item's name will change depending on rarity as well.<br>
	 * Grey (common) = 5<br>
	 * Blue (common?) = 4<br>
	 * Yellow (rare) = 3<br>
	 * Orange (?) = 2<br>
	 * Green (?) = 1<br>
	 * *Colours are subject to change.
	 */
	protected int rarity;

	/**
	 * The value of the effect of an item.<br>
	 * If the item is a weapon (melee, firearm, some throwables), then the
	 * effect value is the damage of the weapon.
	 */
	protected int effectValue;

	protected Point position;
	protected boolean held = false;
	protected ItemState state;
	protected boolean hover = true;

	protected BufferedImage[] images;
	protected AudioClip[] clips;

	protected Game game;

	// TODO Add effectValue?
	public Item(int itemID, String name, int rarity, int effectValue, ItemState state, BufferedImage[] images,
			AudioClip[] clips, Game game) {
		this.itemID = itemID;
		this.name = name;
		this.rarity = rarity;

		this.state = state;

		this.images = images;
		this.clips = clips;

		this.game = game;
	}
	
	public Item(Item item) {
		this.itemID = item.getItemID();
		this.name = item.getName();
		this.rarity = item.getRarity();

		this.state = item.getState();

		this.images = item.getImages();
		this.clips = item.getClips();

		this.game = item.getGame();
	}

	public int getItemID() {
		return this.itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRarity() {
		return this.rarity;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}
	
	public Color getColour() {
		switch (this.rarity) {
		case 5:
			return Color.GRAY;
		case 4:
			return Color.BLUE;
		case 3:
			return Color.YELLOW;
		case 2:
			return Color.ORANGE;
		case 1:
			return Color.GREEN;
		}
		
		return null;
	}

	public int getEffectValue() {
		return this.effectValue;
	}

	public void setEffectValue(int effectValue) {
		this.effectValue = effectValue;
	}

	public Point getPosition() {
		return this.position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public boolean isHeld() {
		return this.held;
	}

	public void setHeld(boolean held) {
		this.held = held;
	}

	public ItemState getState() {
		return this.state;
	}

	public void setState(ItemState state) {
		this.state = state;
	}

	public BufferedImage[] getImages() {
		return images;
	}

	public void setImages(BufferedImage[] images) {
		this.images = images;
	}

	public AudioClip[] getClips() {
		return clips;
	}

	public void setClips(AudioClip[] clips) {
		this.clips = clips;
	}

	public Game getGame() {
		return this.game;
	}

	public void render(Graphics g) {
		if (this.state == ItemState.DROPPED) {
			g.drawImage(this.getImages()[0], (int) (this.getPosition().x - this.game.getCamera().getxOffset()),
					(int) (this.getPosition().y - this.game.getCamera().getyOffset()), null);
			if (new Rectangle((int) (this.getPosition().x - this.game.getCamera().getxOffset()),
					(int) (this.getPosition().y - this.game.getCamera().getyOffset()) + 32, 32, 32)
							.contains(this.game.getDisplay().getMouseHandler().getMouseLocation())) {
				
				g.setColor(getColour());

				FontMetrics fm = g.getFontMetrics();

				g.drawString(this.name,
						(int) (this.getPosition().x - this.game.getCamera().getxOffset()) + 15
								- fm.stringWidth(this.name) / 2,
						(int) (this.getPosition().y - this.game.getCamera().getyOffset()) - 15);
			}
		} else {
			// TODO: Draw the item in the inventory
			g.drawImage(this.getImages()[1], 500, 600, null);
			if (this.state == ItemState.INHAND) {
				g.drawImage(this.getImages()[2], 0, 0, null);
			}
		}
	}
}