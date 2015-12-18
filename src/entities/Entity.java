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
	private Point position;

	public Entity(Point position) {
		if (position != null) {
			this.position = position;
		} else {
			position = new Point(0, 0);
		}
	}

	public void updatePosition(Point position) {
		this.position = position;
	}

	public Point getPosition() {
		return position;
	}

	public abstract void render(Graphics g);
}