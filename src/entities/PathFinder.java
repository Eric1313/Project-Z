package entities;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

import map.Map;
import utilities.Node;

/**
 * A* pathfinding algorithm used by zombies
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Zombie
 * @see Node
 * @since 1.0
 * @version 1.0
 */
public class PathFinder {

	private final int MAX_DEPTH = 1500;
	private Stack<Node> path = new Stack<Node>();
	private short[][] tileMap;// Tile map
	private boolean[][] tiles; // Array to check if tiles a solid
	private Node[][] graph;// Node graph
	PriorityQueue<Node> openList = new PriorityQueue<Node>();
	ArrayList<Node> closedList = new ArrayList<Node>();

	/**
	 * Creates a Pathfinder object
	 * 
	 * @param map
	 *            Map to pathfind in
	 */
	public PathFinder(Map map) {
		this.tiles = new boolean[map.getWidth()][map.getHeight()];
		this.graph = new Node[map.getWidth()][map.getHeight()];
		this.tileMap = map.getMap();
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {
				this.tiles[y][x] = ((this.tileMap[x][y] & (1 << 14)) != 0);
				this.graph[x][y] = new Node(y, x);
			}
		}
	}

	/**
	 * Finds path based on starting and ending points
	 * 
	 * @param oldPath
	 *            Zombie's existing path
	 * @param startX
	 *            Starting tile x coordinate
	 * @param startY
	 *            Starting tile y coordinate
	 * @param targetX
	 *            Target tile x coordinate
	 * @param targetY
	 *            Target tile y coordinate
	 * @return
	 */
	public Stack<Node> findPath(Stack<Node> oldPath, int startX, int startY,
			int targetX, int targetY) {
		// If path has the same target do not recalculate path
		if (!oldPath.isEmpty() && oldPath.get(0).locationX == targetX
				&& oldPath.get(0).locationY == targetY) {
			return oldPath;

		} else {
			// Reset path
			this.path = new Stack<Node>();
			openList.clear();
			closedList.clear();
			path.clear();
			// In case zombie get pushed out of map and has not been updated
			if (startY < 0 || startX < 0 || startY > tiles[0].length
					|| startX > tiles.length)
				return path;

			// Set starting point
			Node start = graph[startY][startX];
			Node currentNode = start;
			openList.add(start);
			int depthOfSearch = 0;
			// If target is unreachable return a empty path
			if (tiles[targetY][targetX] == false)
				// While the frontier is not empty and pathfinder has not
				// exceeded the max number of checks
				while (!openList.isEmpty() && depthOfSearch < MAX_DEPTH) {
					depthOfSearch++;
					// Remove next node from the queue and place into closed
					// list;
					currentNode = openList.peek();
					openList.remove(currentNode);
					closedList.add(currentNode);
					// If current node is the destination create the path
					if (currentNode.locationX == targetX
							&& currentNode.locationY == targetY) {
						while (currentNode != start) {
							path.add(currentNode);
							currentNode = currentNode.prev;
						}
						break;
					}
					// Add adjacent nodes to the frontier
					for (int i = -1; i < 2; i++)
						for (int j = -1; j < 2; j++) {
							// Ignore if out of bounds
							if (currentNode.locationY + i < 0
									|| currentNode.locationX + j < 0
									|| currentNode.locationY + i > tiles.length - 1
									|| currentNode.locationX + j > tiles[0].length - 1)
								continue;
							// Ignore if tile is solid
							if (tiles[currentNode.locationY + i][currentNode.locationX
									+ j] == true)
								continue;
							// Ignore if current node
							if (i == 0 && j == 0)
								continue;

							Node nextNode = graph[currentNode.locationY + i][currentNode.locationX
									+ j];
							int add = 10;
							// Diagonal case
							if (i != 0 && j != 0) {
								add = 14;
								// If any of the two nodes are solid diagonal
								// not possible (zombie would walk through a
								// tile0
								if (tiles[currentNode.locationY + i][currentNode.locationX] == true
										|| tiles[currentNode.locationY][currentNode.locationX
												+ j] == true)
									continue;
							}
							// If already on the closed list ignore
							if (closedList.contains(nextNode))
								continue;
							int g = 0;
							if (currentNode.prev != null) {
								g = currentNode.g;
							}
							g += add;
							// If in the open list movement cost if needed
							if (openList.contains(nextNode)) {
								// If old g is greater than old g, swap it out
								if (nextNode.g > g) {
									nextNode.g = g;
									nextNode.setParent(currentNode);
									;
								}
								//Else add to open  list
							} else {
								//Set parent to current node
								nextNode.setParent(currentNode);
								nextNode.g = g;
								//Calculate heuristic
								nextNode.h = Math.abs(nextNode.locationX
										- targetX)
										+ Math.abs(nextNode.locationY - targetY);
								openList.add(nextNode);
							}
						}
				}
		}
		return path;
	}
}
