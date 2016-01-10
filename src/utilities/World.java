package utilities;

import java.awt.Graphics;
import java.awt.Point;
import java.io.FileNotFoundException;

import entities.Player;
import main.Game;
import map.Map;

public class World {
	private Game game;
	private Player player;
	private short[][] tileId;
	private Map map;
	private int width;
	private int height;

	// Controls what is being rendered
	private int row;
	private int col;
	private final int renderControl = 32;
	private float previousXOffset;
	private float previousYOffset;
	private int yChange;
	private int xChange;

	public World(Game game, int width, int height) {
		this.width = width;
		this.height = height;
		this.game = game;
		try {
			map = new Map(width, height);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		tileId = map.getMap();
		// TODO Randomly place the player into the world rather than putting it
		// in the top left corner
		player = new Player(new Point(0, 0), true, game);
		player.setImages(game.getPlayer()[0]);
		this.row = 0;
		this.col = 0;
		this.yChange = 0;
		this.xChange = 0;
	}

	public void render(Graphics g) {
		int tileY = 0;
		int tileX = 0;
		for (int i = row; i < row + 26; i++) {
			tileX = 0;
			for (int j = col; j < col + 34; j++) {
				int id = (tileId[i][j] & 0xFFF);
				g.drawImage(game.getTiles()[(id / 100) - 1][(id % 100)],
						(int) (tileX * Assets.TILE_WIDTH - game.getCamera()
								.getxOffset()) + xChange - 3, (int) (tileY
								* Assets.TILE_HEIGHT
								- game.getCamera().getyOffset() + yChange - 2),
						null);
				tileX++;
			}
			tileY++;
		}

		if (previousXOffset < game.getCamera().getxOffset()) {
			if (xChange < 0) {
				xChange = -xChange;
			} else {
				if ((game.getCamera().getxOffset() - xChange) >= renderControl) {
					col++;
					xChange += renderControl;
				}
			}
		} else if (previousXOffset > game.getCamera().getxOffset()) {
			if (xChange > 0) {
				if ((game.getCamera().getxOffset() - xChange) <= -(renderControl - 31)) {
					col--;
					xChange -= renderControl;
				}
			}
		}
		if (previousYOffset < game.getCamera().getyOffset()) {
			if (yChange < 0) {
				yChange = -yChange;
			} else {
				if ((game.getCamera().getyOffset() - yChange) >= renderControl) {
					row++;
					yChange += renderControl;
				}
			}
		} else if (previousYOffset > game.getCamera().getyOffset()) {
			if (yChange > 0) {

				// System.out.println(game.getCamera().getyOffset() +
				// yChange);
				if ((game.getCamera().getyOffset() - yChange) <= -(renderControl - 31)) {
					row--;
					yChange -= renderControl;
				}
			}

		}
		previousXOffset = game.getCamera().getxOffset();
		previousYOffset = game.getCamera().getyOffset();
		player.render(g);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Player getPlayer() {
		return player;
	}
}
