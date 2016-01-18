package utilities;

public class Node implements Comparable<Node> {
	public int locationX;
	public int locationY;
	public Node prev;
	public int g;
	public double h;

	public Node(int locationX, int locationY) {
		this.locationX = locationX;
		this.locationY = locationY;
	}

	public void setParent(Node p) {
		prev = p;
	}

	public int compareTo(Node node) {
		return (int) ((g + h) - (node.g + node.h));
	}
}