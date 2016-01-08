package entities;

import java.awt.Graphics;
import java.awt.Point;

import main.Game;

/**
 * Subclass of Entity that represents a player in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Mob
 * @since 1.0
 * @version 1.0
 */
public class Player extends Mob {
	// *********THIS IS UGLY, ADD THIS TO ENTITIES OR SOMETHING LATER
	// ***********GOOD LUCK PATRICK ~ALLEN HAN
	// *********BTW THIS CONTROLS THE MOVEMENT SPEED
	private int movementSpeed = 5;

	public Player(Game game, boolean solid) {
		super(game, solid);
	}

	public Player(Game game, Point position, boolean solid) {
		super(game, position, 32, 32, solid);
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(this.getImages()[0], this.getPosition().x,
				this.getPosition().y, null);
	}

	public void update() {
		if (getGame().getDisplay().getKeyHandler().isUp()) {
			this.getPosition().setLocation(this.getPosition().getX(),
					this.getPosition().getY() - movementSpeed);
		}
		if (getGame().getDisplay().getKeyHandler().isDown()) {
			this.getPosition().setLocation(this.getPosition().getX(),
					this.getPosition().getY() + movementSpeed);
		}
		if (getGame().getDisplay().getKeyHandler().isLeft()) {
			this.getPosition().setLocation(
					this.getPosition().getX() - movementSpeed,
					this.getPosition().getY());
		}
		if (getGame().getDisplay().getKeyHandler().isRight()) {
			this.getPosition().setLocation(
					this.getPosition().getX() + movementSpeed,
					this.getPosition().getY());
		}
	}
}