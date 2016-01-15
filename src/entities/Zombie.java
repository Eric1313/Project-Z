package entities;

import utilities.Node;
import map.Map;

import java.applet.AudioClip;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

import main.Game;
import map.Chunk;

/**
 * Subclass of Mob that represents a zombie enemy in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Mob
 * @see entities.ZombieThread
 * @since 1.0
 * @version 1.0
 */
public class Zombie extends Mob {
	public static final int MOVEMENT_SPEED = 1;


	/**
	 * Zombie constructor
	 * 
	 * @param position pixel based position
	 * @param health zombie health
	 * @param images images for render
	 * @param clips requited audio clips
	 * @param game game that it is in
	 * @param map map that it is in
	 */
	public Zombie(Point position, int health, BufferedImage[] images,
			AudioClip[] clips, Game game,Map map) {
		super(32, 32, position, 0, health, true, images, clips, game, map);
	}

	/**
	 * Updates zombie's position based on current path
	 */
	public void update() {
		//Reset movement
		this.setDown(false);
		this.setUp(false);
		this.setRight(false);
		this.setLeft(false);
		//Remove from chunk
		int chunkX= this.position.x/512;
		int chunkY=this.position.y/512;
		//Follow the path
		if (!this.getPath().isEmpty()) {
			//If path contains null clear
			if (this.path.peek() == null) {
				{
					this.path.clear();
				}
			} else {
				int targetX = path.peek().locationX * 32 ;
				int targetY = path.peek().locationY * 32 ;
				if ((this.getPosition().x == targetX)
						&& (this.getPosition().y == targetY)) {
					path.pop();
				} else {
					if (this.getPosition().y > targetY)
						this.setUp(true);
					else if (this.getPosition().y < targetY)
						this.setDown(true);
					if (this.getPosition().x > targetX)
						this.setLeft(true);
					else if (this.getPosition().x < targetX)
						this.setRight(true);

				}
			}
		}
		if(chunkX!=this.position.x/512||chunkY!=this.position.y/512)
		{
		chunkMap[chunkX][chunkY].removeZombie(this);
		chunkMap[this.position.x/512][this.position.y/512].addZombie(this);
		}
		// System.out.println(this.getPosition().x+" "+this.getPosition().y);
		if (this.up) {
			this.getPosition().setLocation(this.getPosition().getX(),
					this.getPosition().getY() - MOVEMENT_SPEED);
		}
		if (this.down) {
			this.getPosition().setLocation(this.getPosition().getX(),
					this.getPosition().getY() + MOVEMENT_SPEED);
		}
		if (this.left) {
			this.getPosition().setLocation(
					this.getPosition().getX() - MOVEMENT_SPEED,
					this.getPosition().getY());
		}
		if (this.right) {
			this.getPosition().setLocation(
					this.getPosition().getX() + MOVEMENT_SPEED,
					this.getPosition().getY());
		}
		if (this.right || this.left || this.up || this.down) {
			makeNoise(100,false);
		}
	}



	/**
	 * @return the path
	 */
	public Stack<Node> getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(Stack<Node> path) {
		this.path = path;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(this.getImages()[0], (int) (this.position.x - game
				.getCamera().getxOffset()), (int) (this.position.y - game
				.getCamera().getyOffset()), null);
	}

	// @Override
	// public void render(Graphics g) {
	// }
}
