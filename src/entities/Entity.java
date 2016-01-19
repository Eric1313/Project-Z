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
	/**
	 * The height of the entity in pixels.
	 */
	protected int height;

	/**
	 * The width of the entity in pixels.
	 */
	protected int width;

	protected Point position;

	/**
	 * The rotation of the entity in radians.
	 */
	protected double rotation;

	protected int health;
	protected Inventory inventory;
	protected boolean solid;

	protected BufferedImage[] images;
	protected AudioClip[] clips;

	protected Game game;
	protected Chunk[][] chunkMap;

	/**
	 * Constructs a new Entity object.
	 * 
	 * @param height
	 *            the height of the entity in pixels.
	 * @param width
	 *            the width of the entity in pixels.
	 * @param position
	 *            the coordinates of the entity in the map in terms of pixels.
	 * @param solid
	 *            whether or not the entity is solid.
	 * @param game
	 *            the game to add the entity to.
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

	/**
	 * Constructs a new Entity object.
	 * 
	 * @param height
	 *            the height of the entity in pixels.
	 * @param width
	 *            the width of the entity in pixels.
	 * @param position
	 *            the coordinates of the entity in the map in terms of pixels.
	 * @param rotation
	 *            the rotation of the entity in radians.
	 * @param health
	 *            the total health of the entity.
	 * @param solid
	 *            whether or not the entity is solid.
	 * @param images
	 *            an array of images of the entity.
	 * @param clips
	 *            an array of audio clips played by the entity.
	 * @param game
	 *            the game to add the entity to.
	 */
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

	public void damage(int health) {
		// Decrease the entity's health by the amount of damage
		this.health -= health;

		// Once the entity's health reaches zero, drop all of the items in the
		// entity's inventory in the position of the entity
		// Remove the entity from the chunk
		if (this.health <= 0) {
			for (int item = 0; item < Inventory.NO_OF_ITEMS; item++) {
				this.dropItem(item);
			}
			this.chunkMap[this.position.x / 512][this.position.y / 512].remove(this);
		}

		// Notify the world that the entity has been damaged in order to render
		// a damage indicator
		this.game.getDisplay().getGamePanel().getWorld().damage(health, this);
	}

	/**
	 * Checks if the entity's inventory is full.
	 * 
	 * @return whether or not the entity's inventory is full.
	 */
	public boolean isFull() {
		return this.inventory.getNoOfItems() == Inventory.NO_OF_ITEMS;
	}

	/**
	 * Adds an item to the entity's inventory.<br>
	 * Adds the item to the first available slot (lowest item number available).
	 * 
	 * @param item
	 *            the item to add to the inventory.
	 * @return the slot number that the item took. Returns -1 if it was not
	 *         added (meaning the inventory is full).
	 */
	public int addItem(Item item) {
		item.setState(ItemState.INVENTORY);
		return this.inventory.add(item);
	}

	/**
	 * Removes an item from the entity's inventory.
	 * 
	 * @param item
	 *            a reference to the Item to remove.
	 * @return the slot number that the Item was removed from. Returns -1 if it
	 *         was not removed (meaning the Item was not found).
	 */
	public int removeItem(Item item) {
		return this.inventory.remove(item);
	}

	/**
	 * Gets the item that is in a given slot of the inventory.
	 * 
	 * @param itemNo
	 *            the item number (from 0-9) of the item to find.
	 * @return the item in the item number's slot. Returns null if it is empty.
	 */
	public Item getItem(int itemNo) {
		return this.inventory.get(itemNo);
	}

	/**
	 * Drops an item at the player's position in the current chunk.
	 * 
	 * @param itemNo
	 *            the item number (from 0-9) of the item to drop.
	 */
	public void dropItem(int itemNo) {
		Item item = getItem(itemNo);
		if (item != null) {
			item.setPosition(new Point(this.position.x, this.position.y));
			item.setState(ItemState.DROPPED);
			this.removeItem(item);
			this.chunkMap[this.position.x / 512][this.position.y / 512].add(item);
		}
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