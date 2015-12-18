package map;

import java.awt.Point;

import com.sun.javafx.scene.traversal.Direction;

/**
 * Map of a game of Project Z.
 * 
 * @author Patrick Liu, Eric Chee, Allen Han, Alosha Reymer
 * @see MapObject
 * @since 1.0
 * @version 1.0
 */
public class Map
{

	int height;
	int width;
	final int maxArea = 2500;
	final double lengthWidthRatio=0.5;

	char[][] map;

	public Map(int height, int width) 
	{
		this.width = width;
		this.height = height;
		this.map = new char[width][height];

		// Generate main road

		int startingPoint = (int) (width / 4 + Math.random() * (width / 2));
		generateVertiacalRoad(startingPoint, 0, Direction.DOWN, 23);

	}

	public void generateSideRoads(Point start, Point end, double xyRatio,
			int minArea)
	{
		int boxWidth = (int) (Math.abs(end.getX() - start.getX()));
		int boxHeight = (int) (Math.abs(end.getY() - start.getY()));
		int startingPoint;
		if (boxWidth * boxHeight > maxArea)
		{
			if (Math.random() > .5)
			{
				
				// generateVertiacalRoad(startingPoint, 0, Direction.DOWN, 7);
			}
			else
			{
				// generateHorizontalRoad(startingPoint 0, Direction.WEST, 7);
			}
		}
	}

	public void generateVertiacalRoad(int x, int y, Direction direction,
			int width)
	{

	}

	public void generateHorizontalRoad(int x, int y, Direction direction,
			int width)
	{

	}
}
