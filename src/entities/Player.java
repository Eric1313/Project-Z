package entities;

import java.awt.Graphics;
import java.awt.Point;

import utilities.Assets;
import main.Game;

/**
 * Subclass of Mob that represents a player in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Mob
 * @since 1.0
 * @version 1.0
 */
public class Player extends Mob {
	public static final int MOVEMENT_SPEED = 4;

	public Player(boolean solid, Game game) {
		super(solid, game);
		this.movementSpeed = Player.MOVEMENT_SPEED;
	}

	public Player(Point position, boolean solid, Game game) {
		super(32, 32, position, solid, game);
		this.movementSpeed = Player.MOVEMENT_SPEED;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(this.getImages()[0],
				(int) (this.getPosition().x - this.game.getCamera()
						.getxOffset()), (int) (this.getPosition().y - this.game
						.getCamera().getyOffset()), null);
	}

	// TODO Getters & setters VS protected?
	// Reorganize code; looks messy
	public void update() {
		if (this.game.getDisplay().getKeyHandler().isUp()) {
			this.getPosition().setLocation(this.getPosition().getX(),
					this.getPosition().getY() - this.movementSpeed);
		}
		if (this.game.getDisplay().getKeyHandler().isDown()) {
			this.getPosition().setLocation(this.getPosition().getX(),
					this.getPosition().getY() + this.movementSpeed);
		}
		if (this.game.getDisplay().getKeyHandler().isLeft()) {
			this.getPosition().setLocation(
					this.getPosition().getX() - this.movementSpeed,
					this.getPosition().getY());
		}
		if (this.game.getDisplay().getKeyHandler().isRight()) {
			this.getPosition().setLocation(
					this.getPosition().getX() + this.movementSpeed,
					this.getPosition().getY());
		}
		if (position.getX() < 0)
			position.setLocation(0, position.getY());
		else if (position.getX() > Assets.TILE_WIDTH
				* game.getDisplay().getGamePanel().getWorld().getWidth())
			position.setLocation(Assets.TILE_WIDTH
					* game.getDisplay().getGamePanel().getWorld().getWidth()
					- 32, position.getY());
		if (position.getY() < 0)
			position.setLocation(position.getX(), 0);
		else if (position.getX() > Assets.TILE_HEIGHT
				* game.getDisplay().getGamePanel().getWorld().getHeight())
			position.setLocation(Assets.TILE_HEIGHT
					* game.getDisplay().getGamePanel().getWorld().getHeight()
					- 32, position.getY());

		this.game.getCamera().centerOnEntity(this);
	}
}