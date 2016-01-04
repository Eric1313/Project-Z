/**
 * Loads the sprite sheet and allows cropping of images from the spread sheet
 * @author Allen Han & Peter Shi
 * @version June, 15th, 2015
 */
package utilities;
import java.awt.image.BufferedImage;

public class SpriteSheet {
	private BufferedImage sheet;

	/**
	 * Constructor for the sprite sheet
	 * @param sheet the sprite sheet image
	 */
	public SpriteSheet(BufferedImage sheet) {
		this.sheet = sheet;
	}
	
	/**
	 * Crops the image to the correct sprite
	 * @param x the x position of the sprite
	 * @param y the y position of the sprite
	 * @param width the width of the sprite
	 * @param height the heigh of the sprite
	 * @return
	 */
	public BufferedImage crop (int x, int y, int width, int height){
		return sheet.getSubimage(x, y, width, height);
	}
}
