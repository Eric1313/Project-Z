package entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Stack;

import map.Chunk;
import map.Map;
import utilities.Node;

public class PathFinder {

	private Stack<Node> path = new Stack<Node>();
	private short[][] map;
	private boolean[][] tiles;
	private Node[][] graph;
	private Chunk[][] chunkMap;
	private Chunk currentChunk;
	PriorityQueue<Node> openList = new PriorityQueue<Node>();
	ArrayList<Node> closedList = new ArrayList<Node>();

	public PathFinder(Map map) {
		this.tiles = new boolean[map.getWidth()][map.getHeight()];
		this.graph = new Node[map.getWidth()][map.getHeight()];
		this.map = map.getMap();
		this.chunkMap = map.getChunkMap();
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {
				tiles[y][x] = ((this.map[x][y] & (1 << 14)) != 0);
				graph[x][y] = new Node(y, x);
			}
		}
	}

	public Stack<Node> findPath(Stack<Node> oldPath, int startX, int startY,
			int targetX, int targetY) {
		if (!oldPath.isEmpty() && oldPath.get(0).locationX == targetX
				&& oldPath.get(0).locationY == targetY) {
			return oldPath;

		} else {
//			if (!oldPath.isEmpty()) {
//				currentChunk = chunkMap[oldPath.peek().locationX / 16][oldPath
//						.peek().locationY / 16];
//				if (currentChunk.getZombies().size() > 5)
//					for (Iterator<Zombie> iterator = currentChunk.getZombies()
//							.iterator(); iterator.hasNext();) {
//						Zombie zombie = iterator.next();
//						Stack<Node> checkPath = zombie.getPath();
//						if (!checkPath.isEmpty()&& (Math.abs(checkPath.peek().locationX
//								- targetX) > 2)
//						&& (Math.abs(checkPath.peek().locationY
//								- targetY) > 2)
//								&& checkPath.get(0).locationX == targetX
//								&& checkPath.get(0).locationY == targetY
//								)
//							return (checkPath);
//					}
//			}
			this.path = new Stack<Node>();
			openList.clear();
			closedList.clear();
			path.clear();
			Node start = graph[startY][startX];
			Node current = start;
			openList.add(start);
			int maxSteps = 0;
			if (tiles[targetX][targetY] == false)
				while (!openList.isEmpty() && maxSteps < 1000) {
					maxSteps++;
					current = openList.peek();
					openList.remove(current);
					closedList.add(current);
					// if is destination
					if (current.locationX == targetX
							&& current.locationY == targetY) {
						while (current != start) {
							path.add(current);
							current = current.prev;
						}
						break;
					}
					for (int i = -1; i < 2; i++)
						for (int j = -1; j < 2; j++) {
							// out of bounds
							if (current.locationY + i < 0
									|| current.locationX + j < 0
									|| current.locationY + i > tiles.length - 1
									|| current.locationX + j > tiles[0].length - 1)
								continue;
							if (tiles[current.locationY + i][current.locationX
									+ j] == true)
								continue;

							if (i == 0 && j == 0)
								continue;

							Node nextNode = graph[current.locationY + i][current.locationX
									+ j];
							int add = 10;
							// diagonal case
							if (i != 0 && j != 0) {
								add = 14;
								// if one of the two collisions is blocked then
								// skip
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
