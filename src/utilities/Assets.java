/**
 * This class loads up all the assets (images and fonts) in the game
 * @author Allen Han
 * @version January 4th, 2016
 */
package utilities;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Assets {
	private String path;
	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;
	private BufferedImage[][] sprites;
	private BufferedImage image;
	private Font font;

	/**
	 * The constructor for the assets class
	 */
	public Assets(String path, int height, int width) {
		this.path = path;
		loadAssets(height, width);
	}

	public Assets(String path) {
		this.path = path;
		loadAssets(path);
	}

	public Assets(String path, int fontSize) {
		this.path = path;
		loadFont(path, fontSize);
	}

	private void loadFont(String path, int fontSize) {
		try {
			// create the font to use. Specify the size!
			font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(Font.PLAIN, fontSize);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
	}

	private void loadAssets(String path) {
		image = ImageLoader.loadImage(path);
	}

	/**
	 * Loads in all of the assets from a sprite sheet
	 */
	private void loadAssets(int height, int width) {
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage(path));
		sprites = new BufferedImage[sheet.getHeight() / (TILE_HEIGHT * height)][sheet.getWidth()
				/ (TILE_WIDTH * width)];
		for (int row = 0; row < sprites.length; row++) {
			for (int col = 0; col < sprites[row].length; col++) {
				sprites[row][col] = sheet.crop(TILE_WIDTH * width * col, TILE_HEIGHT * height * row, TILE_WIDTH * width,
						TILE_HEIGHT * height);
			}
		}
	}

	public BufferedImage[][] getSprites() {
		return sprites;
	}

	public BufferedImage getImage() {
		return image;
	}

	public Font getFont() {
		return font;
	}

}
