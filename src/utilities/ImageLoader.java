package utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Loads an image
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @since 1.0
 * @version 1.0
 */
public class ImageLoader {
	/**
	 * Loads in an image.
	 * 
	 * @param path
	 *            location of the image.
	 * @return the image.
	 */
	public static BufferedImage loadImage(String path) {
		// In case of errors (e.g. image not found)
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
			// Do not run the game if images are not loaded properly
			System.exit(-1);
		}
		return null;
	}
}
