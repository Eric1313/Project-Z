package entities;

import java.awt.Point;
import java.awt.Graphics;

import main.Game;

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
	public Zombie(Game game, Point position, boolean solid) {
		super(game, position, 32, 32, solid);
		this.movementSpeed = 3;
	}

	@Override
	public void render(Graphics g) {
	}
}