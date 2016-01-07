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
			map = new Map(1000, 1000);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tileId = map.getMap();
	}

	public void render(Graphics g) {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				int a = (tileId[j][i] & 0xFFF);
				g.drawImage(tiles[(a / 100) - 1][a % 100], (a % 100)
						* Assets.tileWidth,
						((a / 100) - 1) * Assets.tileHeight, null);
			}
		}
	}
}
