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
	int minSideLength = 24;
	final int maxArea = 2500;
	final double heightWidthRatio = 0.5;

	char[][] map;

	public Map(int height, int width)
	{
		this.width = width;
		this.height = height;
		this.map = new char[width][height];

		// Generate main road

		int startingPoint = (int) (width / 4 + Math.random() * (width / 2));
		generateVertiacalRoad(startingPoint, 0, 13);

	}

	public void generateSideRoads(Point start, Point end)
	{
		int boxWidth = (int) (Math.abs(end.getX() - start.getX()));
		int boxHeight = (int) (Math.abs(end.getY() - start.getY()));
		int startingX;
		int startingY;
		if (boxWidth * boxHeight > maxArea)
		{
			if (boxWidth > 2 * minSideLength && Math.random() > .5)
			{
				startingX = (int) (Math.min(start.getX(), end.getX()) + (minSideLength + (Math
						.random() * (boxWidth - minSideLength))));
				startingY = (int) Math.min(start.getY(), end.getY());
				generateVertiacalRoad(startingX, startingY, 7);
				// Recursive split new generated squares
				Point start1 = start;
				Point end1 = new Point(startingX - 3, startingY);
				generateSideRoads(start1, end1);
				Point start2 = new Point(startingX + 3, (int) start.getY());
				Point end2 = end;
				generateSideRoads(start2, end2);

			}
			else if (boxHeight > 2 * minSideLength)
			{
				startingY = (int) (Math.min(start.getY(), end.getY()) + (minSideLength + (Math
						.random() * (boxHeight - minSideLength))));
				startingX = (int) Math.min(start.getX(), end.getX());
				generateVertiacalRoad(startingX, startingY, 7);
				// Recursive split new generated squares
				Point start1 = start;
				Point end1 = new Point((int) end.getX(), startingY + 3);
				generateSideRoads(start1, end1);
				Point start2 = new Point((int) start.getX(), startingY - 3);
				Point end2 = end;
				generateSideRoads(start2, end2);
			}
			else
			{
				// generate plaza
			}
		}
		else
		{
			// generate plaza
		}
	}

	public void generateVertiacalRoad(int x, int y, 
			int width)
	{
		int 
		while(map[x][y]!=null)
		{
			
		}
	}

	public void generateHorizontalRoad(int x, int y,
			int width)
	{

	}
}
