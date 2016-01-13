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
	public static final int MOVEMENT_SPEED = 3;
	private boolean hasTarget;
	private Stack<Node> path=new Stack<Node>();
	private short[][] map;
	private boolean[][] tiles;
	private Node[][] graph;
	PriorityQueue<Node> openList = new PriorityQueue<Node>();
	ArrayList<Node> closedList = new ArrayList<Node>();

	public Zombie(Point position, int health, BufferedImage[] images,
			AudioClip[] clips, Game game, Map map) {
		super(32, 32, position, 0, health, true, images, clips, game, map);
		this.map=map.getMap();
				
		this.tiles = new boolean[map.getWidth()][map.getHeight()];
		this.graph = new Node [map.getWidth()][map.getHeight()];
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {

				tiles[x][y] = ((this.map[x][y] & (1 << 14)) != 0);
				graph[x][y] = new Node(y, x);
			}
		}
		hasTarget = false;
	}

	public void update() {
		if (this.up) {
			this.getPosition().setLocation(this.getPosition().getX(),
					this.getPosition().getY() - this.movementSpeed);
		}
		if (this.down) {
			this.getPosition().setLocation(this.getPosition().getX(),
					this.getPosition().getY() + this.movementSpeed);
		}
		if (this.left) {
			this.getPosition().setLocation(
					this.getPosition().getX() - this.movementSpeed,
					this.getPosition().getY());
		}
		if (this.right) {
			this.getPosition().setLocation(
					this.getPosition().getX() + this.movementSpeed,
					this.getPosition().getY());
		}
		if (this.right || this.left || this.up || this.down) {
			makeNoise(100);
		}
	}

	public void findPath(int startX, int startY, int targetX, int targetY) {
		openList.clear();
		closedList.clear();
		path.clear();
		Node start = graph[startY][startX];
		Node current = start;
		openList.add(start);
		while (!openList.isEmpty()) {
			current = openList.peek();
			openList.remove();
			closedList.add(current);
			if (current.locationX == targetX && current.locationY == targetY) {
				while (current != start) {
					path.add(current);
					current = current.prev;
				}
				break;
			}
			for (int i = -1; i < 2; i++)
				for (int j = -1; j < 2; j++) {
					// out of bounds
					if (current.locationY + i < 0 || current.locationX + j < 0
							|| current.locationY + i > tiles.length - 1
							|| current.locationX + j > tiles[0].length - 1)
						continue;
					if (tiles[current.locationY + i][current.locationX + j] == true)
						continue;

					if (i == 0 && j == 0)
						continue;

					Node nextNode = graph[current.locationY + i][current.locationX
							+ j];
					int add = 10;
					// diagonal case
					if (i != 0 && j != 0) {
						add = 14;
						// if one of the two collisions is blocked then skip
						// this case
						if (tiles[current.locationY + i][current.locationX] == true
								|| tiles[current.locationY][current.locationX
										+ j] == true)
							continue;
					}
					// in closed List, then skip
					if (closedList.contains(nextNode))
						continue;
					int g = 0;
					if (current.prev != null) {
						g = current.g;
					}
					g += add;
					// in open List, then update
					if (openList.contains(nextNode)) {
						// if old g is greater than old g, swap it out
						if (nextNode.g > g) {
							nextNode.g = g;
							nextNode.setParent(current);
							;
						}

					} else {
						nextNode.setParent(current);
						nextNode.g = g;
						nextNode.h = (Math.abs(nextNode.locationX - targetX) + Math
								.abs(nextNode.locationY - targetY)) * 10.001;
						openList.add(nextNode);
					}
				}
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
		g.drawImage(this.getImages()[0], (int) (this.position.x),
				(int) (this.position.y), null);
	}

	// @Override
	// public void render(Graphics g) {
	// }
}