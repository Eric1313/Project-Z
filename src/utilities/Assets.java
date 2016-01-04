/**
 * This class loads up all the assets (images and fonts) in the game
 * @author Allen Han
 * @version January 4th, 2016
 */
package utilities;


public class Assets {
	public static void loadAssets() {
		SpriteSheet sheet = new SpriteSheet(
				ImageLoader.loadImage("/Images/spritesheet.png"));
	}
}
