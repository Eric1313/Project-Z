package entities;

import java.awt.Point;
import java.awt.Graphics;

/**
 * Subclass of Entity that represents a player in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Mob
 * @since 1.0
 * @version 1.0
 */
public class Player extends Mob {
	public Player(boolean solid) {
		super(solid);
	}

	public Player(Point position, boolean solid) {
		super(position, 32, 32, solid);
	}

	@Override
	public void render(Graphics g) {
	}

	public void getInput() {
	}
}