package entities;

import items.Item;

import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Game;

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

	public Entity(Game game, boolean solid) {
		this.height = 32;
		this.width = 32;
		this.position = new Point(0, 0);
		this.rotation = 0;
		// Set this.bounds?

		this.health = 100;
		this.inventory = new Inventory();
		this.solid = solid;

		this.game = game;
	}

	public Entity(Game game, Point position, int height, int width,
			boolean solid) {
		this.height = height;
		this.width = width;
		this.position = position;
		this.rotation = 0;
		// Set this.bounds?

		this.health = 100;
		this.inventory = new Inventory();
		this.solid = solid;

		this.game = game;
	}

	public Entity(Point position, int height, int width, boolean solid,
			int health, double rotation, BufferedImage[] images,
			AudioClip[] clips) {
		this.height = height;
		this.width = width;
		this.position = position;
		this.rotation = rotation;
		this.bounds = bounds;

		this.health = health;
		this.inventory = new Inventory();
		this.solid = solid;

		this.images = images;
		this.clips = clips;

		// Set this.game?
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

	public Rectangle[] getBounds() {
		return this.bounds;
	}

	public int getHealth() {
		return this.health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public int addItem(Item item) {
		return this.inventory.add(item);
	}

	public Item removeItem(int itemNo) {
		return this.inventory.remove(itemNo);
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
}