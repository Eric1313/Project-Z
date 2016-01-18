package entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import enums.MapObjectType;
import main.Game;
import map.Map;

/**
 * Subclass of MapObject for zombie corpses inside a map.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Entity
 * @since 1.0
 * @version 1.0
 */
public class Corpse extends MapObject {
	/**
	 * Constructs a new Corpse object.
	 * 
	 * @param position
	 *            the position of the corpse.
	 * @param images
	 *            an array of images of the corpse.
	 * @param game
	 *            the game to add the corpse to.
	 * @param map
	 *            the map to add the corpse to.
	 * @param rotation
	 *            the rotation of the corpse (in radians).
	 */
	public Corpse(Point position, BufferedImage[] images, Game game, Map map, double rotation) {
		super(32, 32, position, 0, 0, false, images, null, game, MapObjectType.CORPSE);

		this.rotation = rotation;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;

		// Store the original transform of the graphics variable
		AffineTransform originalTransform = g2D.getTransform();

		// Rotate the graphics variable and then draw the image in the corpse's
		// position relative to the camera
		g2D.rotate(this.rotation, this.position.getX() + 16 - this.getGame().getCamera().getxOffset(),
				this.getPosition().getY() + 16 - this.getGame().getCamera().getyOffset());
		g2D.drawImage(this.getImages()[5], (int) (this.position.x - game.getCamera().getxOffset()),
				(int) (this.position.y - game.getCamera().getyOffset()), null);

		// Reset the transform of the graphics variable to its original
		// transform
		g2D.setTransform(originalTransform);
	}
}