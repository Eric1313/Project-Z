package utilities;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Loads the assets required based on the inputed path.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @since 1.0
 * @version 1.0
 */
public class Assets {
	// The default tile sizes
	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;
	private BufferedImage[][] sprites;
	private BufferedImage image;
	private String[][] scores;
	private Font font;

	/**
	 * Constructor for Assets.
	 * 
	 * @param path
	 *            the path of the file to be loaded.
	 * @param height
	 *            the height of the picture.
	 * @param width
	 *            the width of the the picture.
	 */
	public Assets(String path, int height, int width) {
		loadAssets(path, height, width);
	}

	/**
	 * Constructor for the Assets.
	 * 
	 * @param path
	 *            the path to the file to be loaded.
	 */
	public Assets(String path) {
		loadAssets(path);
	}

	/**
	 * Constructor for the Assets.
	 * 
	 * @param path
	 *            the path to the file to be loaded.
	 * @param fontSize
	 *            the size of the font.
	 */
	public Assets(String path, int fontSize) {
		loadFont(path, fontSize);
	}

	/**
	 * Constructor for the Assets used to load text.
	 * 
	 * @param path
	 *            the path to the file.
	 * @param text
	 *            if the file contains text or not.
	 */
	public Assets(String path, boolean text) {
		loadText(path);
	}

	/**
	 * Loads in text to an array
	 * 
	 * @param path
	 *            the path to the text file;
	 */
	private void loadText(String path) {
		scores = new String[2][5];
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			// Reads in text file of scores into an array
			for (int i = 0; i < scores.length; i++) {
				for (int j = 0; j < scores[i].length; j++) {

					scores[i][j] = br.readLine();
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads in the font.
	 * 
	 * @param path
	 *            the path to the font file.
	 * @param fontSize
	 *            the size of the font.
	 */
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

	/**
	 * Loads in the image.
	 * 
	 * @param path
	 *            the path to the image.
	 */
	private void loadAssets(String path) {
		image = ImageLoader.loadImage(path);
	}

	/**
	 * Loads sprite sheet.
	 * 
	 * @param height
	 *            the height of the sprite.
	 * @param width
	 *            the width of sprite.
	 */
	private void loadAssets(String path, int height, int width) {
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

	public String[][] getScores() {
		return scores;
	}

	public BufferedImage getImage() {
		return image;
	}

	public Font getFont() {
		return font;
	}
}
