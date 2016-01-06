/**
 * This class loads up all the assets (images and fonts) in the game
 * @author Allen Han
 * @version January 4th, 2016
 */
package utilities;

import java.awt.image.BufferedImage;

public class Assets {
	private final int tileWidth = 32;
	private final int tileHeight = 32;
	private BufferedImage[][] sprites;

	/**
	 * The constructor for the assets class
	 */
	public Assets() {
		loadAssets();
	}

	/**
	 * Loads in all of the assets from a sprite sheet
	 */
	private void loadAssets() {
		SpriteSheet sheet = new SpriteSheet(
				ImageLoader.loadImage("res/img/tiles.png"));
		sprites = new BufferedImage[sheet.getHeight() / tileHeight][sheet
				.getWidth() / tileWidth];
		for (int row = 0; row < sprites.length; row++) {
			for (int col = 0; col < sprites[row].length; col++) {
				sprites[row][col] = sheet.crop(tileWidth * col, tileHeight
						* row, tileWidth, tileHeight);
			}
		}
	}
}
