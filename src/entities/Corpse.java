package entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import main.Game;
import map.Map;

public class Corpse extends Mob {
	
	public Corpse(Point position, BufferedImage[] images, Game game, Map map, double rotation){
		super(32, 32, position, 0, 0, true, images, null, game, map);
		
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
