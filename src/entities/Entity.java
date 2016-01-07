package entities;

import java.applet.AudioClip;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

/**
 * Abstract Entity class for all entities in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @since 1.0
 * @version 1.0
 */
public abstract class Entity {
	protected int health;
	protected Point position;
	protected double rotation;
	protected Inventory inventory;
	protected boolean solid;
	protected BufferedImage[] images;
	protected AudioClip[] clips;

	public Entity(boolean solid) {
		this.position = new Point(0, 0);
		this.health = 100;
		this.rotation = 0;
		this.inventory = new Inventory();
		this.setSolid(solid);
	}

	public Entity(Point position, boolean solid) {
		this.position = position;
		this.health = 100;
		this.rotation = 0;
		this.inventory = new Inventory();
		this.setSolid(solid);
	}

	public Entity(Point position, boolean solid, int health, double rotation,
			BufferedImage[] images, AudioClip[] clips) {
		this.position = position;
		this.health = health;
		this.rotation = rotation;
		this.inventory = new Inventory();
		this.setSolid(solid);
		this.images = images;
		this.clips = clips;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public abstract void render(Graphics g);

	/**
	 * @return the images
	 */
	public BufferedImage[] getImages() {
		return images;
	}

	/**
	 * @param images
	 *            the images to set
	 */
	public void setImages(BufferedImage[] images) {
		this.images = images;
	}

	/**
	 * @return the solid
	 */
	public boolean isSolid() {
		return solid;
	}

	/**
	 * @param solid
	 *            the solid to set
	 */
	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	/**
	 * @return the clips
	 */
	public AudioClip[] getClips() {
		return clips;
	}

	/**
	 * @param clips
	 *            the clips to set
	 */
	public void setClips(AudioClip[] clips) {
		this.clips = clips;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
}