/*
 * This class controls what the player sees on the screen
 * @author Allen Han, Eric Chee, Patrick Liu, Alosha Reymer
 * @version January 4th, 2016
 */
package utilities;

import javax.swing.JFrame;

import main.Game;
import entities.Entity;
import gui.GameScreen;

public class GameCamera {
	private float xOffset, yOffset;
	private JFrame frame;
	private GameScreen panel;

	/**
	 * Constructor for the Game Camera.
	 * 
	 * @param game
	 *            the game.
	 * @param xOffset
	 *            the x value to offset the map by.
	 * @param yOffset
	 *            the y value to offset the map by.
	 */
	public GameCamera(Game game, float xOffset, float yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.frame = game.getDisplay().getFrame();
		this.panel = game.getDisplay().getGamePanel();
	}

	/**
	 * Center on entity.
	 * 
	 * @param entity
	 *            the entity to center the camera on.
	 */
	public void centerOnEntity(Entity entity) {
		// Calculates the amount to offset based on the player's position
		xOffset = (float) (entity.getPosition().getX() - frame.getWidth() / 2 + Assets.TILE_WIDTH / 2);
		yOffset = (float) (entity.getPosition().getY() - frame.getHeight() / 2 + Assets.TILE_HEIGHT / 2);
		// Checks the boundaries to ensure that the y offset doesn't set the
		// camera off the screen
		if (xOffset < 0) {
			xOffset = 0;
		} else if (xOffset > panel.getWorld().getWidth() * Assets.TILE_WIDTH
				- frame.getWidth()) {
			xOffset = panel.getWorld().getWidth() * Assets.TILE_WIDTH
					- frame.getWidth();
		}
		// Checks the boundaries to ensure that the x offset doesn't set the
		// camera off the screen
		if (yOffset < 0) {
			yOffset = 0;
		} else if (yOffset > panel.getWorld().getHeight() * Assets.TILE_HEIGHT
				- frame.getHeight()) {
			yOffset = panel.getWorld().getHeight() * Assets.TILE_HEIGHT
					- frame.getHeight();
		}
	}

	public float getxOffset() {
		return xOffset;
	}

	public float getyOffset() {
		return yOffset;
	}

	public void setxOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}

}