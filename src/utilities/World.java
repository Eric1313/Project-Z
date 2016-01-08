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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tileId = map.getMap();
	}

	public void render(Graphics g) {
		for (int i = 0; i < 32; i++) {
			for (int j = 0; j < 32; j++) {
				int id = (tileId[j][i] & 0xFFF);
				g.drawImage(tiles[(id/100)-1][(id%100)], j*Assets.TILE_WIDTH, i*Assets.TILE_HEIGHT, null);
			}
		}
	}
}
