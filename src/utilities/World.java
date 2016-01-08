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
		for (int i = 0; i < 34; i++) {
			for (int j = 0; j < 60; j++) {
				int a = (tileId[j][i] & 0xFFF);
				g.drawImage(tiles[(a / 100) - 1][a % 100], (a % 100)
						* Assets.tileWidth,
						((a / 100) - 1) * Assets.tileHeight, null);
			}
		}
		System.out.println ("UPDATING");
	}
}
