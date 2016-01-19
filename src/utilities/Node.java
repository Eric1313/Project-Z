package utilities;

/**
 * Node used for zombie pathfinding
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see PathFinder
 * @version 1.0
 */
public class Node implements Comparable<Node> {
	public int locationX;
	public int locationY;
	public Node prev;
	public int g;//Movement cost
	public double h;//Heuristic

	/**
	 * Constructs a Node object
	 * @param locationX x position (tiles)
	 * @param locationY y position (tiles)
	 */
	public Node(int locationX, int locationY) {
		this.locationX = locationX;
		this.locationY = locationY;
	}

	/**
	 * Sets parent node
	 * @param parent parent node of this node
	 */
	public void setParent(Node parent) {
		prev = parent;
	}

	/**
	 * Compares node to another node based on the movement cost and heuristic
	 */
	public int compareTo(Node node) {
		return (int) ((g + h) - (node.g + node.h));
	}
}