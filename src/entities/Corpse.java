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
	 * Corpse constructor.
	 * 
	 * @param position
	 * @param images
	 * @param game
	 * @param map
	 * @param rotation
	 */
	public Corpse(Point position, BufferedImage[] images, Game game, Map map, double rotation) {
		super(32, 32, position, 0, 0, false, images, null, game, MapObjectType.CORPSE);

		this.rotation = rotation;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;

		AffineTransform originalTransform = g2D.getTransform();

		g2D.rotate(this.rotation, this.position.getX() + 16 - this.getGame().getCamera().getxOffset(),
				this.getPosition().getY() + 16 - this.getGame().getCamera().getyOffset());
		g2D.drawImage(this.getImages()[5], (int) (this.position.x - game.getCamera().getxOffset()),
				(int) (this.position.y - game.getCamera().getyOffset()), null);

		g2D.setTransform(originalTransform);
	}
}