package utilities;

import java.awt.Graphics;
import java.io.FileNotFoundException;

import main.Game;
import map.Map;

public class World {
	private Game game;
	private short[][] tileId;
	private Map map;
	private int width;
	private int height;

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
	}

	public void render(Graphics g) {
		int tileY = 0;
		int tileX = 0;
		for (int i = 0; i < tileId.length; i++) {
			tileX = 0;
			for (int j = 0; j < tileId[i].length; j++) {
				int id = (tileId[i][j] & 0xFFF);
				if (tileY * Assets.TILE_HEIGHT - game.getCamera().getyOffset() > -300
						&& tileY * Assets.TILE_HEIGHT
								- game.getCamera().getyOffset() < 900
						&& tileX * Assets.TILE_WIDTH
								- game.getCamera().getxOffset() > -300
						&& tileX * Assets.TILE_WIDTH
								- game.getCamera().getxOffset() < 1500)
					g.drawImage(game.getTiles()[(id / 100) - 1][(id % 100)],
							(int) (tileX * Assets.TILE_WIDTH - game.getCamera()
									.getxOffset()), (int) (tileY
									* Assets.TILE_HEIGHT - game.getCamera()
									.getyOffset()), null);
				tileX++;
			}
			tileY++;
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
