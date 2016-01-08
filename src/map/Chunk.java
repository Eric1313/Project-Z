package map;

import java.util.ArrayList;
import entities.Entity;

public class Chunk {
	private ArrayList<Entity> solidEntities;
	private ArrayList<Entity> passibleEntities;

	public Chunk() {
		solidEntities = new ArrayList<Entity>();
		passibleEntities = new ArrayList<Entity>();
	}

	/**
	 * Adds entity to chunk
	 * 
	 * @param entity
	 *            entity to add
	 */
	public void add(Entity entity) {
		if (entity.isSolid())
			solidEntities.add(entity);
		else
			passibleEntities.add(entity);
	}

	/**
	 * Removes Entities from chunks
	 * 
	 * @param entity
	 * @return
	 */
	public Entity remove(Entity entity) {
		if (entity.isSolid())
			solidEntities.remove(entity);
		else
			passibleEntities.remove(entity);
		return entity;
	}

	/**
	 * @return the solidEntities
	 */
	public ArrayList<Entity> getSolidEntities() {
		return solidEntities;
	}

	/**
	 * @param solidEntities
	 *            the solidEntities to set
	 */
	public void setSolidEntities(ArrayList<Entity> solidEntities) {
		this.solidEntities = solidEntities;
	}

	/**
	 * @return the passibleEntities
	 */
	public ArrayList<Entity> getPassibleEntities() {
		return passibleEntities;
	}

	/**
	 * @param passibleEntities
	 *            the passibleEntities to set
	 */
	public void setPassibleEntities(ArrayList<Entity> passibleEntities) {
		this.passibleEntities = passibleEntities;
	}

}
