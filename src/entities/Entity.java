package entities;

import java.awt.Point;

import com.sun.prism.Graphics;

/**
 * Abstract Entity class for all entities in Project Z.
 * 
 * @author Patrick Liu, Eric Chee, Allen Han, Alosha Reymer
 * @since 1.0
 * @version 1.0
 */
public abstract class Entity {
	protected int health;
	protected Point position;
	protected double rotation;

	public Entity() {
		this.position = new Point(0, 0);
		this.health = 100;
		this.rotation = 0;
	}
	
	public Entity(Point position) {
		this.position = position;
		this.health = 100;
		this.rotation = 0;
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
}