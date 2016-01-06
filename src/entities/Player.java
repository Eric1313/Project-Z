package entities;

import java.awt.Point;

import com.sun.prism.Graphics;

/**
 * Subclass of Entity that represents a player in Project Z.
 * 
 * @author Patrick Liu, Eric Chee, Allen Han, Alosha Reymer
 * @see Entity
 * @since 1.0
 * @version 1.0
 */
public class Player extends Mob {
	public Player() {
		super();
	}
	
	public Player(Point position) {
		super(position);
	}

	@Override
	public void render(Graphics g) {
	}

	public void getInput() {
	}
}