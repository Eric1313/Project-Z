package entities;

import utilities.Node;
import map.Map;

import java.applet.AudioClip;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
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
	private int imgNo;
	private Player player;

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
	public Zombie(Point position, int health, BufferedImage[] images, AudioClip[] clips, Game game, Map map,
			int imgNo) {
		super(32, 32, position, 0, health, true, images, clips, game, map);
		rotation = Math.random() * (2 * Math.PI);
		rotation = Math.random() * (2 * Math.PI);
		this.imgNo = imgNo;

	}

	/**
	 * Updates zombie's position based on current path
	 */
	public void update() {
		if(player==null)
			player = this.game.getDisplay().getGamePanel().getWorld().getPlayer();

		// Reset movement
		this.setDown(false);
		this.setUp(false);
		this.setRight(false);
		this.setLeft(false);

		boolean collideUp = false;
		boolean collideDown = false;
		boolean collideRight = false;
		boolean collideLeft = false;
		int chunkX = this.position.x / 512;
		int chunkY = this.position.y / 512;
		int targetX = 0;
		int targetY = 0;
		// Follow the path
		if (!this.getPath().isEmpty()) {
			// If path contains null clear
			if (this.path.peek() == null) {
				{
					this.path.clear();
					targetX = 0;
					targetY = 0;
				}
			} else {
				targetX = path.peek().locationX * 32;
				targetY = path.peek().locationY * 32;
				if ((this.getPosition().x == targetX) && (this.getPosition().y == targetY)) {
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
		if (targetX != 0 && targetY != 0) {
			double dx = this.position.x - targetX, dy = this.position.y - targetY;
			if (dx == 0)
				if (dy == 0)
					this.rotation = 0;
				else if (dy > 0)
					this.rotation = Math.PI / 2;
				else
					this.rotation = Math.PI * 3 / 2;
			else if (dy == 0)
				if (dx > 0)
					this.rotation = 0;
				else
					this.rotation = Math.PI;
			else if (dx < 0)
				this.rotation = Math.atan(dy / dx) + Math.PI;
			else if (dy < 0)
				this.rotation = Math.atan(dy / dx) + (2 * Math.PI);
			else
				this.rotation = Math.atan(dy / dx);
		}
		
		if ((Math.pow(player.getPosition().x - this.position.x, 2)
				+ Math.pow(player.getPosition().y - this.position.y, 2)) < 1000) {
player.damage(1);
			if (player.getPosition().y > this.position.y) {
				this.up = true;
				collideDown = true;
			} else
				collideDown = false;
			if (player.getPosition().y < this.position.y) {
				this.down = true;
				collideUp = true;
			} else
				collideUp = false;
			if (player.getPosition().x > this.position.x) {
				this.left = true;
				collideRight = true;
			} else
				collideRight = false;
			if (player.getPosition().x< this.position.x) {
				this.right = true;
				collideLeft = true;
			} else
				collideLeft = false;
		}

		for (int x = Math.max(chunkX - 1, 0); x < Math.min(chunkX + 2, map.getWidth() / 16 - 2); x++) {
			for (int y = Math.max(chunkY - 1, 0); y < Math.min(chunkY + 1, map.getHeight() / 16 - 1); y++) {
				for (int i = 0; i < chunkMap[x][y].getZombies().size(); i++) {
					if (1 < chunkMap[x][y].getZombies().size()) {
						Zombie checkZombie = chunkMap[x][y].getZombies().get(i);
						if ((Math.pow(checkZombie.getPosition().x - this.position.x, 2)
								+ Math.pow(checkZombie.getPosition().y - this.position.y, 2)) < 1100) {

							if (checkZombie.getPosition().y > this.position.y) {
								this.up = true;
								collideDown = true;
							} else
								collideDown = false;
							if (checkZombie.getPosition().y < this.position.y) {
								this.down = true;
								collideUp = true;
							} else
								collideUp = false;
							if (checkZombie.getPosition().x > this.position.x) {
								this.left = true;
								collideRight = true;
							} else
								collideRight = false;
							if (checkZombie.getPosition().x < this.position.x) {
								this.right = true;
								collideLeft = true;
							} else
								collideLeft = false;
						}

					}
				}
			}
		}

		// System.out.println(this.getPosition().x+" "+this.getPosition().y);
		if (this.up && !collideUp) {
			this.getPosition().setLocation(this.getPosition().getX(), this.getPosition().getY() - MOVEMENT_SPEED);
		}
		if (this.down && !collideDown) {
			this.getPosition().setLocation(this.getPosition().getX(), this.getPosition().getY() + MOVEMENT_SPEED);
		}
		if (this.left && !collideLeft) {
			this.getPosition().setLocation(this.getPosition().getX() - MOVEMENT_SPEED, this.getPosition().getY());
		}
		if (this.right && !collideRight) {
			this.getPosition().setLocation(this.getPosition().getX() + MOVEMENT_SPEED, this.getPosition().getY());
		}
		if (this.right || this.left || this.up || this.down) {
			makeNoise(100, false);
		}
		if (chunkX != this.position.x / 512 || chunkY != this.position.y / 512) {
			chunkMap[chunkX][chunkY].removeZombie(this);
			chunkX = this.position.x / 512;
			chunkY = this.position.y / 512;
			chunkMap[chunkX][chunkY].addZombie(this);
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
		Graphics2D g2D = (Graphics2D) g;

		AffineTransform originalTransform = g2D.getTransform();

		g2D.rotate(this.rotation, this.position.getX() + 16 - this.getGame().getCamera().getxOffset(),
				this.getPosition().getY() + 16 - this.getGame().getCamera().getyOffset());

		g2D.drawImage(this.getImages()[imgNo], (int) (this.position.x - game.getCamera().getxOffset()),
				(int) (this.position.y - game.getCamera().getyOffset()), null);

		g2D.setTransform(originalTransform);

		if (this.health < 100) {
			g2D.drawRect((int) (this.position.x - this.getGame().getCamera().getxOffset()) - 6,
					(int) (this.getPosition().y - this.getGame().getCamera().getyOffset()) + 33, 44, 6);
			g2D.setColor(Color.RED);
			g2D.fillRect((int) (this.position.x - this.getGame().getCamera().getxOffset()) - 5,
					(int) (this.getPosition().y - this.getGame().getCamera().getyOffset()) + 34,
					(int) (42 * (this.health / 100.0)), 5);
			g2D.setColor(Color.BLACK);
		}

	}
}
