package entities;

import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import main.Game;

/**
 * Abstract MapObject class for objects inside a map in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Entity
 * @since 1.0
 * @version 1.0
 */
public abstract class MapObject extends Entity {
	/**
	 * 
	 * @param height
	 *            the height of the map object in pixels.
	 * @param width
	 *            the width of the map object in pixels.
	 * @param position
	 *            the coordinate of the map object in the map in terms of
	 *            pixels.
	 * @param rotation
	 *            the rotation of the map object (in radians).
	 * @param health
	 *            the total health of the map object.
	 * @param solid
	 *            whether or not the map object is solid.
	 * @param images
	 *            an array of images of the map object.
	 * @param clips
	 *            an array of audio clips played by the map object.
	 * @param game
	 *            the game to add the map object to.
	 * @param type
	 *            the type of map object that this map object is (e.g. rubble,
	 *            corpse, etc.)
	 */
	public MapObject(int height, int width, Point position, double rotation, int health, boolean solid,
			BufferedImage[] images, AudioClip[] clips, Game game) {
		super(height, width, position, rotation, health, solid, images, clips, game);
	}

	public abstract void render(Graphics g);
}