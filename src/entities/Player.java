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
	public Player(Game game, boolean solid) {
		super(game, solid);
		this.movementSpeed = 5;
	}

	public Player(Game game, Point position, boolean solid) {
		super(game, position, 32, 32, solid);
		this.movementSpeed = 5;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(this.getImages()[0], this.getPosition().x,
				this.getPosition().y, null);
	}

	public void update() {
		if (getGame().getDisplay().getKeyHandler().isUp()) {
			this.getPosition().setLocation(this.getPosition().getX(),
					this.getPosition().getY() - this.movementSpeed);
		}
		if (getGame().getDisplay().getKeyHandler().isDown()) {
			this.getPosition().setLocation(this.getPosition().getX(),
					this.getPosition().getY() + this.movementSpeed);
		}
		if (getGame().getDisplay().getKeyHandler().isLeft()) {
			this.getPosition().setLocation(
					this.getPosition().getX() - this.movementSpeed,
					this.getPosition().getY());
		}
		if (getGame().getDisplay().getKeyHandler().isRight()) {
			this.getPosition().setLocation(
					this.getPosition().getX() + this.movementSpeed,
					this.getPosition().getY());
		}
	}
}