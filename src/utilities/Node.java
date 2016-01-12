package utilities;


public class Node implements Comparable<Object> {
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

	public int compareTo(Object arg) {
		double af = g + h;
		Node b = (Node) arg;
		double bf = b.g + b.h;
		if (af < bf) {
			return -1;
		} else if (af > bf) {
			return 1;
		} else {
			return 0;
		}
	}

}