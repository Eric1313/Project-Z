package entities;

import items.Item;

import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import enums.ItemState;
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
	protected Rectangle[] bounds;

	protected int health;
	protected Inventory inventory;
	protected boolean solid;

	protected BufferedImage[] images;
	protected AudioClip[] clips;

	protected Game game;
	private Chunk[][]chunkMap;

	public Entity(boolean solid, Game game) {
		// TODO Add map for future features
		this.height = 32;
		this.width = 32;
		this.position = new Point(0, 0);
		this.rotation = 0;

		this.health = 100;
		this.inventory = new Inventory();
		this.solid = solid;

		this.game = game;
	}

	public Entity(int height, int width, Point position, boolean solid,
			Game game) {
		this.height = height;
		this.width = width;
		this.position = position;
		this.rotation = 0;

		this.health = 100;
		this.inventory = new Inventory();
		this.solid = solid;

		this.game = game;
	}

	public Entity(int height, int width, Point position, double rotation,
			int health, boolean solid, BufferedImage[] images,
			AudioClip[] clips, Game game) {
		this.height = height;
		this.width = width;
		this.position = position;
		this.rotation = rotation;

		this.health = health;
		this.inventory = new Inventory();
		this.solid = solid;

		this.images = images;
		this.clips = clips;

		// Parameter was not here before; reason?
		this.game = game;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Point getPosition() {
		return this.position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public double getRotation() {
		return this.rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
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

	public Inventory getInventory() {
		return this.inventory;
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

	public void setClips(AudioClip[] clips) {
		this.clips = clips;
	}

	public Game getGame() {
		return this.game;
	}

	public abstract void render(Graphics g);

	/**
	 * @return the chunkMap
	 */
	public Chunk[][] getChunkMap() {
		return chunkMap;
	}

	/**
	 * @param chunkMap the chunkMap to set
	 */
	public void setChunkMap(Chunk[][] chunkMap) {
		this.chunkMap = chunkMap;
	}
}