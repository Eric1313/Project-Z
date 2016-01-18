package entities;

import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import enums.MapObjectType;
import main.Game;

/**
 * Abstract MapObject class for objects inside a map in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Entity
 * @since 1.0
 * @version 1.0
 */
public class MapObject extends Entity {
	private MapObjectType type;

	// Do we need game?
	public MapObject(int height, int width, Point position, double rotation,
			int health, boolean solid, BufferedImage[] images,
			AudioClip[] clips, Game game, MapObjectType type) {
		super(height, width, position, rotation, health, solid, images, clips,
				game);

		this.type = type;
	}

	public void render(Graphics g) {
	}
}