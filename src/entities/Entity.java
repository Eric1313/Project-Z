package entities;

import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import enums.ItemState;
import items.Item;
import main.Game;
import map.Chunk;

/**
 * Abstract Entity class for all entities in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @since 1.0
 * @version 1.0
 */
public abstract class Entity {
	protected int height, width;
	protected Point position;
	protected double rotation;

	protected int health;
	protected Inventory inventory;
	protected boolean solid;

	protected BufferedImage[] images;
	protected AudioClip[] clips;

	protected Game game;
	private Chunk[][] chunkMap;

	/**
	 * 
	 * @param height
	 * @param width
	 * @param position
	 * @param solid
	 * @param game
	 */
	public Entity(int height, int width, Point position, boolean solid, Game game) {
		this.height = height;
		this.width = width;
		this.position = position;
		this.rotation = 0;

		this.health = 100;
		this.inventory = new Inventory();
		this.solid = solid;

		this.game = game;
	}

	public Entity(int height, int width, Point position, double rotation, int health, boolean solid,
			BufferedImage[] images, AudioClip[] clips, Game game) {
		this.height = height;
		this.width = width;
		this.position = position;
		this.rotation = rotation;

		this.health = health;
		this.inventory = new Inventory();
		this.solid = solid;

		this.images = images;
		this.clips = clips;

		this.game = game;
	}

	public Point getPosition() {
		return this.position;
	}

	public int getHealth() {
		return this.health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void damage(int health) {
		this.health -= health;

		if (this.health <= 0) {
			for (int item = 0; item < Inventory.NO_OF_ITEMS; item++) {
				dropItem(item);
			}
			this.getChunkMap()[this.position.x / 512][this.position.y / 512].remove(this);
		}

		this.game.getDisplay().getGamePanel().getWorld().damage(health, this);
	}

	public boolean isFull() {
		return this.inventory.getNoOfItems() == Inventory.NO_OF_ITEMS;
	}

	public int addItem(Item item) {
		item.setState(ItemState.INVENTORY);
		return this.inventory.add(item);
	}

	public Item removeItem(int itemNo) {
		return this.inventory.remove(itemNo);
	}

	public int removeItem(Item item) {
		return this.inventory.remove(item);
	}

	public Item getItem(int itemNo) {
		return this.inventory.get(itemNo);
	}

	public void dropItem(int itemNo) {
		Item item = getItem(itemNo);
		if (item != null) {
			item.setPosition(new Point(this.position.x, this.position.y));
			item.setState(ItemState.DROPPED);
			removeItem(itemNo);
			this.getChunkMap()[this.position.x / 512][this.position.y / 512].add(item);
		}
	}

	public boolean isSolid() {
		return this.solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public BufferedImage[] getImages() {
		return this.images;
	}

	public void setImages(BufferedImage[] images) {
		this.images = images;
	}

	public AudioClip[] getClips() {
		return this.clips;
	}

	public Game getGame() {
		return this.game;
	}

	public Chunk[][] getChunkMap() {
		return chunkMap;
	}

	public void setChunkMap(Chunk[][] chunkMap) {
		this.chunkMap = chunkMap;
	}

	public abstract void render(Graphics g);
}