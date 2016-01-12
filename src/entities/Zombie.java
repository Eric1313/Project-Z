package entities;

import utilities.Node;

import java.awt.Point;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

import main.Game;


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
	private boolean hastarget=false;
	private Stack<Node> path;
	private short[][] tiles;
	private boolean[][] map;
	private Node[][]graph;
	PriorityQueue<Node> openList = new PriorityQueue<Node>();
	ArrayList<Node> closedList = new ArrayList<Node>();
	
	public Zombie(boolean solid, Game game) {
		super(solid, game);
		this.movementSpeed = Zombie.MOVEMENT_SPEED;
	}

	public Zombie(Point position, boolean solid, Game game) {
		super(32, 32, position, solid, game);
		this.movementSpeed = Zombie.MOVEMENT_SPEED;
	}
	public void findPath(int enemyX, int enemyY, int targetX, int targetY) {
		openList.clear();
		closedList.clear();
		path.clear();
		Node start = graph[enemyY][enemyX];
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
							|| current.locationY + i > map.length - 1
							|| current.locationX + j > map[0].length - 1)
						continue;
					if (map[current.locationY + i][current.locationX + j] == true)
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
						if (map[current.locationY + i][current.locationX] == true
								|| map[current.locationY][current.locationX + j] == true)
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
	 * @param path the path to set
	 */
	public void setPath(Stack<Node> path) {
		this.path = path;
	}
	

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
	

	
//	@Override
//	public void render(Graphics g) {
//	}
}