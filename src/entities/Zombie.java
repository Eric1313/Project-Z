package entities;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Stack;

import main.Game;
import map.Map;
import utilities.Node;

/**
 * Subclass of Mob that represents a zombie enemy in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Mob
 * @since 1.0
 * @version 1.0
 */
public class Zombie extends Mob {
	public static final int MOVEMENT_SPEED = 1;
	public static int damage = 5;
	public static int zombieHealth = 100;
	private int zombieVariant;
	private Player player;
	private int targetX;
	private int targetY;

	/**
	 * Constructs zombie object
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
		super(32, 32, position, 0, Zombie.zombieHealth, true, images, clips, game, map);
		rotation = Math.random() * (2 * Math.PI);
		rotation = Math.random() * (2 * Math.PI);
		this.zombieVariant = imgNo;

		this.health = Zombie.zombieHealth;
		if (Math.random() < .05) {
			this.movementSpeed = Zombie.MOVEMENT_SPEED * 2;
		} else if (Math.random() < 0.01) {
			this.movementSpeed = Zombie.MOVEMENT_SPEED * 3;
		} else {
			this.movementSpeed = Zombie.MOVEMENT_SPEED;
		}

	}

	/**
	 * Updates zombie's position based on current path
	 */
	public void update() {
		if (player == null)
			player = this.game.getDisplay().getGamePanel().getWorld().getPlayer();

		// Reset movement
		this.setDown(false);
		this.setUp(false);
		this.setRight(false);
		this.setLeft(false);

		// Reset collisions
		boolean collideUp = false;
		boolean collideDown = false;
		boolean collideRight = false;
		boolean collideLeft = false;

		// get current chunk
		int chunkX = this.position.x / 512;
		int chunkY = this.position.y / 512;
		// Follow the path
		if (!this.getPath().isEmpty()) {
			// If path contains null clear path
			if (this.path.peek() == null) {
				{
					this.path.clear();
					targetX = 0;
					targetY = 0;
				}
			} else {
				// Set pixel coordinate of next node in the path
				targetX = path.peek().locationX * 32;
				targetY = path.peek().locationY * 32;
				// If zombie reached node location pop off node and read in next
				// node
				if ((this.getPosition().x == targetX) && (this.getPosition().y == targetY)) {
					path.pop();
				} else {
					// Set zombie's desired movement based on rellative position
					// to target
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
		// Set the zombie's rotation towards the target
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

		// Collide with the player by moving zombie sway from player
		if ((Math.pow(player.getPosition().x - this.position.x, 2)
				+ Math.pow(player.getPosition().y - this.position.y, 2)) < 1000) {
			player.damage(Zombie.damage);
			// If player is below
			if (player.getPosition().y > this.position.y) {
				this.up = true;
				collideDown = true;
			}
			// If player is above
			if (player.getPosition().y < this.position.y) {
				this.down = true;
				collideUp = true;
			}
			// If player is right
			if (player.getPosition().x > this.position.x) {
				this.left = true;
				collideRight = true;
			}
			// If player is below
			if (player.getPosition().x < this.position.x) {
				this.right = true;
				collideLeft = true;
			}
		}
		// Collide with other zombies by moving zombie sway from player
		for (int x = Math.max(chunkX - 1, 0); x < Math.min(chunkX + 2, map.getWidth() / 16); x++) {
			for (int y = Math.max(chunkY - 1, 0); y < Math.min(chunkY + 1, map.getHeight() / 16); y++) {
				for (int i = 0; i < getChunkMap()[x][y].getZombies().size(); i++) {
					if (1 < getChunkMap()[x][y].getZombies().size()) {
						Zombie checkZombie = getChunkMap()[x][y].getZombies().get(i);
						if ((Math.pow(checkZombie.getPosition().x - this.position.x, 2)
								+ Math.pow(checkZombie.getPosition().y - this.position.y, 2)) < 1100) {
							// If player is below
							if (checkZombie.getPosition().y > this.position.y) {
								this.up = true;
								collideDown = true;
							}
							// If player is above
							if (checkZombie.getPosition().y < this.position.y) {
								this.down = true;
								collideUp = true;
							}
							// If player is right
							if (checkZombie.getPosition().x > this.position.x) {
								this.left = true;
								collideRight = true;
							}
							// If player is left
							if (checkZombie.getPosition().x < this.position.x) {
								this.right = true;
								collideLeft = true;
							}
						}

					}
				}
			}
		}

		// Change zombie's coordinates based on previous checks with collide
		// overwriting movement

		// Move up
		if (this.up && !collideUp) {
			this.getPosition().setLocation(this.getPosition().getX(), this.getPosition().getY() - this.movementSpeed);
		}
		// Move down
		if (this.down && !collideDown) {
			this.getPosition().setLocation(this.getPosition().getX(), this.getPosition().getY() + this.movementSpeed);
		}
		// Move right
		if (this.left && !collideLeft) {
			this.getPosition().setLocation(this.getPosition().getX() - this.movementSpeed, this.getPosition().getY());
		}
		// Move left
		if (this.right && !collideRight) {
			this.getPosition().setLocation(this.getPosition().getX() + this.movementSpeed, this.getPosition().getY());
		}
		// Zombie makes a noise when moving to alert other zombies
		if (this.right || this.left || this.up || this.down) {
			makeNoise(100, false);
		}
		// If zombie is no longer in the same chunk move zombie to correct chunk
		if (chunkX != this.position.x / 512 || chunkY != this.position.y / 512) {
			getChunkMap()[chunkX][chunkY].removeZombie(this);
			chunkX = this.position.x / 512;
			chunkY = this.position.y / 512;
			if (!(chunkX > getChunkMap().length - 1 || chunkY > getChunkMap()[0].length - 1 || chunkX < 0
					|| chunkY < 0))
				getChunkMap()[chunkX][chunkY].addZombie(this);
		}
	}

	public void damage(int health) {
		this.health -= health;

		if (this.health <= 0) {
			for (int item = 0; item < Inventory.NO_OF_ITEMS; item++) {
				dropItem(item);
			}
			this.getChunkMap()[this.position.x / 512][this.position.y / 512].remove(this);
			this.getChunkMap()[this.position.x / 512][this.position.y / 512]
					.add(new Corpse(position, rotation, images, game, map));
		}

		this.game.getDisplay().getGamePanel().getWorld().damage(health, this);
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

		// Store the original transform of the graphics variable
		AffineTransform originalTransform = g2D.getTransform();

		// Rotate the graphics variable and then draw the image in the zombie's
		// position relative to the camera
		g2D.rotate(this.rotation, this.position.getX() - this.game.getCamera().getxOffset() + this.width / 2,
				this.position.getY() - this.game.getCamera().getyOffset() + this.height / 2);
		g2D.drawImage(this.images[this.zombieVariant], (int) (this.position.x - this.game.getCamera().getxOffset()),
				(int) (this.position.y - this.game.getCamera().getyOffset()), null);

		// Reset the transform of the graphics variable to its original
		// transform
		g2D.setTransform(originalTransform);

		// If the zombie has less than full health, draw a health bar undernear
		// the zombie
		if (this.health < Zombie.zombieHealth) {
			g2D.drawRect((int) (this.position.x - this.game.getCamera().getxOffset()) - 6,
					(int) (this.position.y - this.game.getCamera().getyOffset()) + 33, 44, 6);
			g2D.setColor(Color.RED);
			g2D.fillRect((int) (this.position.x - this.game.getCamera().getxOffset()) - 5,
					(int) (this.position.y - this.game.getCamera().getyOffset()) + 34,
					(int) (42 * (this.health / (Zombie.zombieHealth * 1.0))), 5);
			g2D.setColor(Color.BLACK);
		}
	}
}