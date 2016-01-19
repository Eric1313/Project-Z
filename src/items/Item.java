package items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import entities.Player;
import entities.Zombie;
import enums.ItemState;
import main.Game;
import map.Map;
import utilities.Effect;
import utilities.Sound;

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
	 * Blue (uncommon) = 4<br>
	 * Yellow (rare) = 3<br>
	 * Orange (very rare) = 2<br>
	 * Green (ultra rare) = 1<br>
	 */
	protected int rarity;

	/**
	 * The value of the effect of an item.<br>
	 * If the item is a weapon (melee, firearm, some throwables), then the
	 * effect value will be the damage of the weapon.
	 */
	protected int effectValue;

	protected Point position;
	protected ItemState state;
	protected boolean hover;

	protected BufferedImage[] images;
	protected Sound[] clips;

	protected Game game;
	protected Map map;

	public abstract void use(Player player);

	public Item(int itemID, String name, int rarity, int effectValue, ItemState state, BufferedImage[] images,
			Effect[] clips, Game game) {
		this.itemID = itemID;
		this.name = name;
		this.rarity = rarity;
		this.effectValue = effectValue;

		this.state = state;

		this.images = images;
		this.clips = clips;

		this.game = game;
	}

	/**
	 * Clones an item template for multiple use.
	 * 
	 * @param item the item template.
	 */
	public Item(Item item) {
		this.itemID = item.itemID;
		this.name = item.name;
		this.rarity = item.rarity;
		this.effectValue = item.effectValue;

		this.state = item.state;

		this.images = item.images;
		this.clips = item.clips;

		this.game = item.game;
	}
	public void makeNoise(int range, boolean player) {
		if (map==null)
			map=game.getDisplay().getGameScreen().getWorld().getMap();
		int chunkX = Math.max(position.x / 512,2);
		int chunkY = Math.max(position.y / 512,2);
		for (int x = chunkX - 2; x < Math.min(chunkX + 3,map.getWidth()/16); x++) {
			for (int y = chunkY - 2; y <Math.min( chunkY + 3,map.getHeight()/16); y++) {
				if(x<100&&y<100)
				for (Iterator<Zombie> iterator = map.getChunkMap()[x][y].getZombies().iterator(); iterator
						.hasNext();) {
					Zombie zombie = iterator.next();
					if(Math.pow(position.x-zombie.getPosition().x, 2)  +Math.pow(position.y-zombie.getPosition().y, 2)<range*range)
					{
						if(player)
						zombie.setPath(map.getPathFinder().findPath(zombie.getPath(), (zombie.getPosition().x+16)/32, (zombie.getPosition().y+16)/32, (this.position.x+16)/32, (this.position.y+16)/32));
						
					}
				}
			}
		}
	}

	public int getItemID() {
		return this.itemID;
	}

	public String getName() {
		return this.name;
	}

	public int getRarity() {
		return this.rarity;
	}

	public Color getColour() {
		switch (this.rarity) {
		case 5:
			return new Color(173, 173, 173);
		case 4:
			return new Color(0, 54, 255);
		case 3:
			return new Color(246, 195, 72);
		case 2:
			return new Color(246, 146, 72);
		case 1:
			return new Color(0, 225, 0);
		}

		return null;
	}

	public int getEffectValue() {
		return this.effectValue;
	}

	public Point getPosition() {
		return this.position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public ItemState getState() {
		return this.state;
	}

	public void setState(ItemState state) {
		this.state = state;
	}

	public boolean isHover() {
		return hover;
	}

	public void setHover(boolean hover) {
		this.hover = hover;
	}

	public BufferedImage[] getImages() {
		return images;
	}

	public Sound[] getClips() {
		return clips;
	}

	public Game getGame() {
		return this.game;
	}

	public void render(Graphics g) {
		if (this.state == ItemState.DROPPED) {
			if (!hover) {
				g.drawImage(this.getImages()[0], (int) (this.getPosition().x - this.game.getCamera().getxOffset()),
						(int) (this.getPosition().y - this.game.getCamera().getyOffset()), null);
			} else {
				g.drawImage(this.getImages()[1], (int) (this.getPosition().x - this.game.getCamera().getxOffset()),
						(int) (this.getPosition().y - this.game.getCamera().getyOffset()), null);
			}
		}
	}

	public abstract void renderTooltip(Graphics g, Point mouseLocation);
}