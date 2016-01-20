package map;

import java.util.ArrayList;

import entities.Entity;
import entities.Zombie;
import items.Item;

/**
 * Object that represents a 16x16 block area.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Map
 * @since 1.0
 * @version 1.0
 */
public class Chunk {
	private ArrayList<Entity> solidEntities;
	private ArrayList<Entity> passibleEntities;
	private ArrayList<Zombie> zombies;
	private ArrayList<Item> items;

	/**
	 * Constructs a new Chunk object.
	 */
	public Chunk() {
		this.solidEntities = new ArrayList<Entity>();
		this.passibleEntities = new ArrayList<Entity>();
		this.zombies = new ArrayList<Zombie>();
		this.items = new ArrayList<Item>();
	}

	/**
	 * Adds an entity to the chunk.
	 * 
	 * @param entity
	 *            the entity to add.
	 */
	public void add(Entity entity) {
		if (entity.isSolid()) {
			this.solidEntities.add(entity);
		} else {
			this.passibleEntities.add(entity);
		}
	}

	/**
	 * Adds an item to the chunk.
	 * 
	 * @param item
	 *            the item to add.
	 */
	public void add(Item item) {
		this.items.add(item);
	}

	/**
	 * Removes an item from the chunk.
	 * 
	 * @param item
	 *            the item to remove
	 */
	public void remove(Item item) {
		this.items.remove(item);
	}

	/**
	 * Removes entities from the chunk.
	 * 
	 * @param entity
	 *            the entity to remove.
	 * @return the entity removed.
	 */
	public Entity remove(Entity entity) {
		if (entity instanceof Zombie) {
			return removeZombie((Zombie) entity);
		} else if (entity.isSolid()) {
			this.solidEntities.remove(entity);
		} else {
			this.passibleEntities.remove(entity);
		}
		return entity;
	}

	/**
	 * Adds a zombie to the chunk.
	 * 
	 * @param zombie
	 *            the zombie to add.
	 */
	public void addZombie(Zombie zombie) {
		this.zombies.add(zombie);
	}

	/**
	 * Removes a zombie from the chunk.
	 * 
	 * @param zombie
	 *            the zombie to remove.
	 * @return the zombie removed.
	 */
	public Zombie removeZombie(Zombie zombie) {
		this.zombies.remove(zombie);
		return zombie;
	}

	public ArrayList<Entity> getSolidEntities() {
		return this.solidEntities;
	}

	public void setSolidEntities(ArrayList<Entity> solidEntities) {
		this.solidEntities = solidEntities;
	}

	public ArrayList<Entity> getPassibleEntities() {
		return this.passibleEntities;
	}

	public void setPassibleEntities(ArrayList<Entity> passibleEntities) {
		this.passibleEntities = passibleEntities;
	}

	public ArrayList<Item> getItems() {
		return this.items;
	}

	public ArrayList<Zombie> getZombies() {
		return this.zombies;
	}
}