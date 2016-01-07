package entities;

import java.awt.Point;

/**
 * Abstract Mob class for all mobs in Project Z.<br>
 * Mobs are any entities that can move.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Player
 * @see Zombie
 * @since 1.0
 * @version 1.0
 */
public abstract class Mob extends Entity {
	public Mob(boolean solid) {
		super(solid);
	}

	public Mob(Point position, int height, int width, boolean solid) {
		super(position, height, width, solid);
	}
}