package utilities;

import java.awt.image.BufferedImage;

/**
 * Crops images from the sprite sheet.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @since 1.0
 * @version 1.0
 */
public class SpriteSheet {
	private BufferedImage sheet;
	private int width;
	private int height;

	/**
	 * Constructor for the sprite sheet.
	 * 
	 * @param sheet
	 *            the image containing all the sprite images.
	 */
	public SpriteSheet(BufferedImage sheet) {
		this.sheet = sheet;
		width = sheet.getWidth();
		height = sheet.getHeight();
	}

	/**
	 * Crops the correct sprite from the image.
	 * 
	 * @param x
	 *            the x position of the sprite.
	 * @param y
	 *            the y position of the sprite.
	 * @param width
	 *            the width of the sprite.
	 * @param height
	 *            the height of the sprite.
	 * @return
	 */
	public BufferedImage crop(int x, int y, int width, int height) {
		return sheet.getSubimage(x, y, width, height);
	}

	/**
	 * Get the width of the sprite sheet.
	 * 
	 * @return the width of the sprite sheet.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of the sprite sheet.
	 * 
	 * @return the width of the sprite sheet.
	 */
	public int getHeight() {
		return height;
	}
}
