package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Stack;
import main.Game;
import map.Map;
import utilities.Node;

/**
 * Thread for all of the enemies' AIs.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see entities.Zombie
 * @since 1.0
 * @version 1.0
 */
public class ZombieThread implements Runnable {

	Game game;
	// private Map map;
	private short[][] tiles;
	private boolean[][] map;
	private Node[][] graph;
	private Stack<Node> path = new Stack<Node>();
	ArrayList<Zombie> zombies;
	PriorityQueue<Node> openList = new PriorityQueue<Node>();
	ArrayList<Node> closedList = new ArrayList<Node>();

	public ZombieThread(Game game) {
		this.game = game;
		this.tiles = game.getDisplay().getGamePanel().getWorld().getMap()
				.getMap();
		this.map = new boolean[game.getDisplay().getGamePanel().getWorld()
				.getMap().getWidth()][game.getDisplay().getGamePanel()
				.getWorld().getMap().getHeight()];
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {

				map[x][y] = ((tiles[x][y] & (1 << 14)) != 0);
				graph[x][y] = new Node(y, x);
			}
		}
	}

	@Override
	public void run() {
		while (true) {

			for (Iterator<Zombie> iterator = zombies.iterator(); iterator
					.hasNext();) {
				Zombie zombie = iterator.next();
				if (zombie.getPath() != null) {
					int targetX = path.peek().locationX * 32 + 16;
					int targetY = path.peek().locationY * 32 + 16;

					if ((zombie.getPosition().x == targetX)
							&& (zombie.getPosition().y == targetY)) {
						path.pop();
					} else {
						if (zombie.getPosition().y > targetY)
							zombie.setUp(true);

						else if (zombie.getPosition().y < targetY)
							zombie.setDown(true);
						else {
							zombie.setDown(false);
							zombie.setUp(false);
						}
						if (zombie.getPosition().x > targetX)
							zombie.setLeft(true);
						else if (zombie.getPosition().x < targetX)
							zombie.setRight(true);
						else {
							zombie.setRight(false);
							zombie.setLeft(false);
						}
					}
				}
			}
		}
	}

	// public void findPath(int enemyX, int enemyY, int targetX, int targetY) {
	// openList.clear();
	// closedList.clear();
	// path.clear();
	// Node start = graph[enemyY][enemyX];
	// Node current = start;
	// openList.add(start);
	// while (!openList.isEmpty()) {
	// current = openList.peek();
	// openList.remove();
	// closedList.add(current);
	// if (current.locationX == targetX && current.locationY == targetY) {
	// while (current != start) {
	// path.add(current);
	// current = current.prev;
	// }
	// break;
	// }
	// for (int i = -1; i < 2; i++)
	// for (int j = -1; j < 2; j++) {
	// // out of bounds
	// if (current.locationY + i < 0 || current.locationX + j < 0
	// || current.locationY + i > map.length - 1
	// || current.locationX + j > map[0].length - 1)
	// continue;
	// if (map[current.locationY + i][current.locationX + j] == true)
	// continue;
	//
	// if (i == 0 && j == 0)
	// continue;
	//
	// Node nextNode = graph[current.locationY + i][current.locationX
	// + j];
	// int add = 10;
	// // diagonal case
	// if (i != 0 && j != 0) {
	// add = 14;
	// // if one of the two collisions is blocked then skip
	// // this case
	// if (map[current.locationY + i][current.locationX] == true
	// || map[current.locationY][current.locationX + j] == true)
	// continue;
	// }
	// // in closed List, then skip
	// if (closedList.contains(nextNode))
	// continue;
	// int g = 0;
	// if (current.prev != null) {
	// g = current.g;
	// }
	// g += add;
	// // in open List, then update
	// if (openList.contains(nextNode)) {
	// // if old g is greater than old g, swap it out
	// if (nextNode.g > g) {
	// nextNode.g = g;
	// nextNode.setParent(current);
	// ;
	// }
	//
	// } else {
	// nextNode.setParent(current);
	// nextNode.g = g;
	// nextNode.h = (Math.abs(nextNode.locationX - targetX) + Math
	// .abs(nextNode.locationY - targetY)) * 10.001;
	// openList.add(nextNode);
	// }
	// }
	// }
	// }

}