package entities;

import java.applet.AudioClip;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Stack;

import main.Game;
import map.Map;
import utilities.Node;

/**
 * Abstract Mob class for all mobs in Project Z.<br>
 * Mobs are any entities that can move.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Player
 * @see Zombie
 * @since 1.0
 * @version 1.0
 */
public abstract class Mob extends Entity {
	protected int movementSpeed;
	protected boolean up;
	protected boolean down;
	protected boolean left;
	protected boolean right;
	protected Map map;
	protected Stack<Node> path = new Stack<Node>();

	public Mob(int height, int width, Point position, boolean solid, Game game, Map map) {
		super(height, width, position, solid, game);
		this.map = map;
		this.setChunkMap(map.getChunkMap());
	}

	public Mob(int height, int width, Point position, double rotation, int health, boolean solid,
			BufferedImage[] images, AudioClip[] clips, Game game, Map map) {
		super(height, width, position, rotation, health, solid, images, clips, game);
		this.map = map;
		this.setChunkMap(map.getChunkMap());
	}

	public void makeNoise(int range, boolean player) {
		int chunkX = Math.max(this.position.x / 512, 2);
		int chunkY = Math.max(this.position.y / 512, 2);
		for (int x = chunkX - 2; x < Math.min(chunkX + 3, this.map.getWidth() / 16); x++) {
			for (int y = chunkY - 2; y < Math.min(chunkY + 3, this.map.getHeight() / 16); y++) {
				if (x < 100 && y < 100) {
					for (Iterator<Zombie> iterator = this.chunkMap[x][y].getZombies().iterator(); iterator.hasNext();) {
						Zombie zombie = iterator.next();
						if (Math.pow(this.position.x - zombie.position.x, 2)
								+ Math.pow(this.position.y - zombie.position.y, 2) < range * range) {
							if (player) {
								zombie.setPath(this.map.getPathFinder().findPath(zombie.getPath(),
										(zombie.position.x + 16) / 32, (zombie.position.y + 16) / 32,
										(this.position.x + 16) / 32, (this.position.y + 16) / 32));
							} else if (!this.path.isEmpty()) {
								zombie.setPath(this.map.getPathFinder().findPath(zombie.getPath(),
										(zombie.position.x) / 32, (zombie.position.y) / 32, this.path.get(0).locationX,
										this.path.get(0).locationY));
							}
						}
					}
				}
			}
		}
	}

	public int getMovementSpeed() {
		return this.movementSpeed;
	}

	public void setMovementSpeed(int movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	public boolean isUp() {
		return this.up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return this.down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isLeft() {
		return this.left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return this.right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}
	
}