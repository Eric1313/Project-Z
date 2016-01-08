/**
 * This class loads up all the assets (images and fonts) in the game
 * @author Allen Han
 * @version January 4th, 2016
 */
package utilities;

import java.awt.image.BufferedImage;

public class Assets {
	private String path;
	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;
	private BufferedImage[][] sprites;

	/**
	 * The constructor for the assets class
	 */
	public Assets(String path) {
		this.path = path;
		loadAssets();
	}

	/**
	 * Loads in all of the assets from a sprite sheet
	 */
	private void loadAssets() {
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage(path));
		sprites = new BufferedImage[sheet.getHeight() / TILE_HEIGHT][sheet
				.getWidth() / TILE_WIDTH];
		for (int row = 0; row < sprites.length; row++) {
			for (int col = 0; col < sprites[row].length; col++) {
				sprites[row][col] = sheet.crop(TILE_WIDTH * col, TILE_HEIGHT
						* row, TILE_WIDTH, TILE_HEIGHT);
			}
		}
	}

	public BufferedImage[][] getSprites() {
		return sprites;
	}
}
