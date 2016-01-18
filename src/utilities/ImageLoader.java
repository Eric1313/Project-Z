/**
 * Image loader which loads up the image
 * @author Allen Han 
 * @version January 4th, 2016
 */
package utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	/**
	 * Loads in an image
	 * 
	 * @param path
	 *            location of the image
	 * @return the buffer image object
	 */
	public static BufferedImage loadImage(String path) {
		// In case of errors (e.g. image not found)
		try {
			System.out.println(path);
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
			// Do not run the game if images are not loaded properly
			System.exit(1);
		}
		return null;
	}
}
