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
	 * @param position
	 *            pixel based position
	 * @param health
	 *            zombie health
	 * @param images
	 *            images for render
	 * @param clips
	 *            requited audio clips
	 * @param game
	 *            game that it is in
	 * @param map
	 *            map that it is in
	 */
	public Zombie(Point position, int health, BufferedImage[] images,
			AudioClip[] clips, Game game, Map map) {
		super(32, 32, position, 0, health, true, images, clips, game, map);
	}

	/**
	 * Updates zombie's position based on current path
	 */
	public void update() {
		// Reset movement
		this.setDown(false);
		this.setUp(false);
		this.setRight(false);
		this.setLeft(false);

		boolean collideUp = false;
		boolean collideDown = false;
		boolean collideRight = false;
		boolean collideLeft = false;
		// Remove from chunk
		int chunkX = this.position.x / 512;
		int chunkY = this.position.y / 512;
		// Follow the path
		if (!this.getPath().isEmpty()) {
			// If path contains null clear
			if (this.path.peek() == null) {
				{
					this.path.clear();
				}
			} else {
				int targetX = path.peek().locationX * 32 + 16;
				int targetY = path.peek().locationY * 32 + 16;
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

		for (int x = Math.max(chunkX - 1, 0); x < Math.min(chunkX + 2,
				map.getWidth() - 1); x++) {
			for (int y = Math.max(chunkY - 1, 0); y < Math.min(chunkY + 2,
					map.getHeight() - 1); y++) {
				for (int i = 0; i < chunkMap[x][y].getZombies().size(); i++) {
					Zombie checkZombie = chunkMap[x][y].getZombies().get(i);

					if ((Math.pow(
							checkZombie.getPosition().x - this.position.x, 2) + Math
							.pow(checkZombie.getPosition().y - this.position.y,
									2)) < 1100) {
						if (checkZombie.getPosition().y > this.position.y) {
							// if (collideDown)
							this.up = true;
							// else
							collideDown = true;
						} else
							collideDown = false;
						if (checkZombie.getPosition().y < this.position.y) {
							// if (collideUp)
							this.down = true;
							// else
							collideUp = true;
						} else
							collideUp = false;
						if (checkZombie.getPosition().x > this.position.x) {
							// if (collideRight)
							this.left = true;
							// else
							collideRight = true;
						} else
							collideRight = false;
						if (checkZombie.getPosition().x < this.position.x) {
							// if (collideLeft)
							this.right = true;
							// else
							collideLeft = true;
						} else
							collideLeft = false;
					}
					if (chunkX != this.position.x / 512 || chunkY != this.position.y / 512) {
						chunkMap[chunkX][chunkY].removeZombie(this);
						chunkX = this.position.x / 512;
						chunkY = this.position.y / 512;
						chunkMap[chunkX][chunkY].addZombie(this);
					}
				}
			}
		}

		// System.out.println(this.getPosition().x+" "+this.getPosition().y);
		if (this.up && !collideUp) {
			this.getPosition().setLocation(this.getPosition().getX(),
					this.getPosition().getY() - MOVEMENT_SPEED);
		}
		if (this.down && !collideDown) {
			this.getPosition().setLocation(this.getPosition().getX(),
					this.getPosition().getY() + MOVEMENT_SPEED);
		}
		if (this.left && !collideLeft) {
			this.getPosition().setLocation(
					this.getPosition().getX() - MOVEMENT_SPEED,
					this.getPosition().getY());
		}
		if (this.right && !collideRight) {
			this.getPosition().setLocation(
					this.getPosition().getX() + MOVEMENT_SPEED,
					this.getPosition().getY());
		}
		if (this.right || this.left || this.up || this.down) {
			makeNoise(100, false);
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
		g.drawImage(this.getImages()[0], (int) (this.position.x - 16 - game
				.getCamera().getxOffset()), (int) (this.position.y - 16 - game
				.getCamera().getyOffset()), null);
	}

	// @Override
	// public void render(Graphics g) {
	// }
}
