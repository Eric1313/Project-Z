package entities;

import java.applet.AudioClip;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import main.Game;
import map.Chunk;
import map.Map;

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
	protected Chunk[][]chunkMap;
	protected boolean up;
	protected boolean down;
	protected boolean left;
	protected boolean right;
	protected Map map;

	public Mob(boolean solid, Game game) {
		super(solid, game);
	}

	public Mob(int height, int width, Point position, boolean solid, Game game,Map map) {
		super(height, width, position, solid, game);
		this.map=map;
		this.chunkMap=map.getChunkMap();
	}

	public Mob(int height, int width, Point position, double rotation,
			int health, boolean solid, BufferedImage[] images,
			AudioClip[] clips, Game game,Map map) {
		super(height, width, position, rotation, health, solid, images, clips,
				game);
		this.map=map;
	this.chunkMap=map.getChunkMap();
	}

	public void makeNoise(int range) {
//		Ellipse2D.Double audibleArea= new Ellipse2D.Double(position.x,position.y,range,range);
		int chunkX = Math.max(position.x / 512,2);
		int chunkY = Math.max(position.y / 512,2);
		for (int x = chunkX - 2; x < chunkX + 3; x++) {
			for (int y = chunkY - 2; y < chunkY + 3; y++) {
				for (Iterator<Zombie> iterator = chunkMap[x][y].getZombies().iterator(); iterator
						.hasNext();) {
					Zombie zombie = iterator.next();
					if(Math.sqrt(    Math.pow(position.x-zombie.position.x, 2)  +Math.pow(position.y-zombie.position.y, 2))<range)
					{
System.out.println("hi");
						zombie.setPath(map.getPathFinder().findPath(zombie.getPath(), zombie.position.x/32, zombie.position.y/32, this.position.x/32, this.position.y/32));

						//						zombie.findPath(zombie.position.x/32, zombie.position.y/32, this.position.x/32, this.position.y/32);
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

	/**
	 * @return the up
	 */
	public boolean isUp() {
		return up;
	}

	/**
	 * @param up
	 *            the up to set
	 */
	public void setUp(boolean up) {
		this.up = up;
	}

	/**
	 * @return the down
	 */
	public boolean isDown() {
		return down;
	}

	/**
	 * @param down
	 *            the down to set
	 */
	public void setDown(boolean down) {
		this.down = down;
	}

	/**
	 * @return the left
	 */
	public boolean isLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(boolean left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public boolean isRight() {
		return right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(boolean right) {
		this.right = right;
	}

}