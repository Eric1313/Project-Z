package entities;

import java.awt.Point;

import main.Game;

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
	public Mob(Game game, boolean solid) {
		super(game, solid);
	}

	public Mob(Game game, Point position, int height, int width, boolean solid) {
		super(game, position, height, width, solid);
	}
}