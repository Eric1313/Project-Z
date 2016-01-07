package entities;

import java.awt.Point;
import java.awt.Graphics;

/**
 * Subclass of Entity that represents a zombie enemy in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Mob
 * @see main.NPC
 * @since 1.0
 * @version 1.0
 */
public class Zombie extends Mob {

	public Zombie(Point position, boolean solid) {
		super(position, 32, 32, solid);
	}

	@Override
	public void render(Graphics g) {
	}
}