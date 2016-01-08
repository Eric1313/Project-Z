package utilities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

import map.Map;

public class World {
	private BufferedImage[][] tiles;
	private short[][] tileId;
	private Map map;

	public World(BufferedImage[][] tiles) {
		this.tiles = tiles;
		try {
			map = new Map(1600, 1600);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		tileId = map.getMap();
	}

	public void render(Graphics g) {
		int tileY = 0;
		int tileX = 0;
		for (int i = 32; i < 64; i++) {
			tileX = 0;
			for (int j = 32; j < 64; j++) {
				int id = (tileId[j][i] & 0xFFF);
				g.drawImage(tiles[(id / 100) - 1][(id % 100)], tileX
						* Assets.TILE_WIDTH, tileY * Assets.TILE_HEIGHT, null);
				tileX++;
			}
			tileY++;
		}
	}
}
