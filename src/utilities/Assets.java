/*
 * Loads the assets required based on the inputed path
 * @author Allen Han, Eric Chee, Patrick Liu, Alosha Reymer
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
	// The default tile sizes
	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;
	private BufferedImage[][] sprites;
	private BufferedImage image;
	private Font font;

	/**
	 * Constructor for Assets
	 * 
	 * @param path
	 *            the path of the file to be loaded
	 * @param height
	 *            the height of the picture
	 * @param width
	 *            the width of the the picture
	 */
	public Assets(String path, int height, int width) {
		this.path = path;
		loadAssets(height, width);
	}

	/**
	 * Constructor for the Assets
	 * 
	 * @param path
	 *            the path to the file to be loaded
	 */
	public Assets(String path) {
		this.path = path;
		loadAssets(path);
	}

	/**
	 * Constructor for the Assets
	 * 
	 * @param path
	 *            the path to the file to be loaded
	 * @param fontSize
	 *            the size of the font
	 */
	public Assets(String path, int fontSize) {
		this.path = path;
		loadFont(path, fontSize);
	}

	/**
	 * Loads in the font
	 * 
	 * @param path
	 *            the path to the font file
	 * @param fontSize
	 *            the size of the font
	 */
	private void loadFont(String path, int fontSize) {
		try {
			// create the font to use. Specify the size!
			font = Font.createFont(Font.TRUETYPE_FONT, new File(path))
					.deriveFont(Font.PLAIN, fontSize);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads in the image
	 * 
	 * @param path
	 *            the path to the image
	 */
	private void loadAssets(String path) {
		image = ImageLoader.loadImage(path);
	}

	/**
	 * Loads sprite sheet
	 * 
	 * @param height
	 *            the height of the sprite
	 * @param width
	 *            the width of sprite
	 */
	private void loadAssets(int height, int width) {
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage(path));
		sprites = new BufferedImage[sheet.getHeight() / (TILE_HEIGHT * height)][sheet
				.getWidth() / (TILE_WIDTH * width)];
		for (int row = 0; row < sprites.length; row++) {
			for (int col = 0; col < sprites[row].length; col++) {
				sprites[row][col] = sheet.crop(TILE_WIDTH * width * col,
						TILE_HEIGHT * height * row, TILE_WIDTH * width,
						TILE_HEIGHT * height);
			}
		}
	}

	/**
	 * Returns an array of sprite images
	 * 
	 * @return an array of sprite images
	 */
	public BufferedImage[][] getSprites() {
		return sprites;
	}

	/**
	 * Returns the image
	 * 
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Returns the font
	 * 
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

}
