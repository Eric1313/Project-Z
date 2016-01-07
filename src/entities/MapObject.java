package entities;

import java.applet.AudioClip;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

import enums.MapObjectType;

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

	public MapObject(Point position, boolean solid, int health,
			double rotation, MapObjectType type,Rectangle[] bounds, BufferedImage[] images,
			AudioClip[] clips) {
		super(position, solid, health, rotation,bounds, images, clips);
		this.type = type;
	}

	public void render(Graphics g) {
	}
}