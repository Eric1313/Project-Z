package utilities;

import entities.Entity;
import main.Game;

public class GameCamera {

	private Game game;
	private float xOffset, yOffset;

	public GameCamera(Game game, float xOffset, float yOffset) {
		this.game = game;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public void centerOnEntity(Entity e) {
		xOffset = (float) (e.getPosition().getX() - game.getDisplay().getFrame().getWidth() / 2
				+ Assets.TILE_WIDTH / 2);
		yOffset = (float) (e.getPosition().getY() - game.getDisplay().getFrame().getHeight() / 2
				+ Assets.TILE_HEIGHT / 2);
		if (xOffset < 0) {
			xOffset = 0;
		} else if (xOffset > game.getDisplay().getGamePanel().getWorld().getWidth() * Assets.TILE_WIDTH
				- game.getDisplay().getFrame().getWidth()) {
			xOffset = game.getDisplay().getGamePanel().getWorld().getWidth() * Assets.TILE_WIDTH
					- game.getDisplay().getFrame().getWidth();
		}

		if (yOffset < 0) {
			yOffset = 0;
		} else if (yOffset > game.getDisplay().getGamePanel().getWorld().getHeight() * Assets.TILE_HEIGHT
				- game.getDisplay().getFrame().getHeight()) {
			yOffset = game.getDisplay().getGamePanel().getWorld().getHeight() * Assets.TILE_HEIGHT
					- game.getDisplay().getFrame().getHeight();
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