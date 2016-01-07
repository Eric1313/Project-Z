package entities;

import java.awt.Point;

import com.sun.prism.Graphics;

/**
 * Subclass of Entity that represents a zombie enemy in Project Z.
 * 
 * @author Patrick Liu, Eric Chee, Allen Han, Alosha Reymer
 * @see Entity
 * @since 1.0
 * @version 1.0
 */
public class Zombie extends Mob {

	public Zombie(Point position,boolean solid) {
		super(position, solid);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}
}