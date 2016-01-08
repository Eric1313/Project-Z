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

	// TEMP
	private int col = 0;
	private int row = 0;

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
		for (int i = row; i < row + 38; i++) {
			tileX = 0;
			for (int j = col; j < col + 38; j++) {
				int id = (tileId[j][i] & 0xFFF);
				g.drawImage(game.getTiles()[(id / 100) - 1][(id % 100)],
						(int) (tileX * Assets.TILE_WIDTH - game.getCamera()
								.getxOffset()), (int) (tileY
								* Assets.TILE_HEIGHT - game.getCamera()
								.getyOffset()), null);
				tileX++;
			}
			tileY++;
		}
		//Code to move the map if the tile is too far off the map
		if (game.getCamera().getyOffset() > 192){
			row ++;
			game.getCamera().setyOffset(192-32);
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
