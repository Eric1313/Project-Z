package map;

import items.Item;

import java.util.ArrayList;

import entities.Entity;
import entities.Zombie;

public class Chunk {
	private ArrayList<Entity> solidEntities;
	private ArrayList<Entity> passibleEntities;
	private ArrayList<Item> items;
	private ArrayList<Zombie>zombies;

	public Chunk() {
		this.solidEntities = new ArrayList<Entity>();
		this.passibleEntities = new ArrayList<Entity>();
		this.zombies= new ArrayList<Zombie>();
		this.items = new ArrayList<Item>();
	}

	/**
	 * Adds entity to chunk
	 * 
	 * @param entity
	 *            entity to add
	 */
	public void add(Entity entity) {
		if (entity.isSolid()) {
			this.solidEntities.add(entity);
		} else {
			this.passibleEntities.add(entity);
		}
	}

	public void add(Item item) {
		this.items.add(item);
		// TODO Make sure the chunk isn't full
	}

	/**
	 * Removes Entities from chunks
	 * 
	 * @param entity
	 * @return
	 */
	public Entity remove(Entity entity) {
		if (entity.isSolid()) {
			this.solidEntities.remove(entity);
		} else {
			this.passibleEntities.remove(entity);
		}
		return entity;
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

	/**
	 * @return the zombies
	 */
	public ArrayList<Zombie> getZombies() {
		return zombies;
	}

	/**
	 * @param zombies the zombies to set
	 */
	public void addZombie(Zombie zombie) {
		zombies.add(zombie);
	}
	public Zombie removeZombie(Zombie zombie)
	{
		zombies.remove(zombie);
		return zombie;
	}
}