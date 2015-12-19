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

	short[][] map;

	public Map(int height, int width)
	{
		this.width = width;
		this.height = height;
		this.map = new short[width][height];

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
			int size)
	{
		int tempx = x;
		int tempy = y;
		// Connect to other roads if not edge
		if (tempy++ < width)
		{
			tempy++;
			tempx -= ((size - 1) / 2);
			setTile(tempx, tempy, 104, Direction.UP);
			tempx++;
			for (int i = 1; i < size - 2; i++)
			{
				setTile(tempx, tempy, 105, Direction.UP);
				tempx++;
			}
			setTile(tempx, tempy, 104, Direction.RIGHT);
			tempy++;
			tempx -= size;
			while ((map[tempx][tempy] & 0xFFF) == 100
					|| (map[tempx][tempy] & 0xFFF) == 101)
			{
				setTile(tempx, tempy, 105, Direction.LEFT);
				tempx++;
				for (int i = 0; i < size - 3; i++)
				{
					setTile(tempx, tempy, 101, Direction.UP);
					tempx++;
				}
				setTile(tempx, tempy, 105, Direction.RIGHT);
				tempx -= size;
				tempy++;
			}
		}
		tempx = x;
		tempy = y;
		while (map[tempx][tempy] != 0)
		{

		}
	}

	public void generateHorizontalRoad(int x, int y,
			int width)
	{

	}

	public void setTile(int x, int y, int i, Direction direction)
	{
		map[x][y] = (short) i;
		if (direction == Direction.RIGHT)
		{
			map[x][y] = (short) (map[x][y] | (1 << 12));
		}
		else if (direction == Direction.LEFT)
		{
			map[x][y] = (short) (map[x][y] | (1 << 13));
		}
		else if (direction == Direction.DOWN)
		{
			map[x][y] = (short) (map[x][y] | (1 << 12));
			map[x][y] = (short) (map[x][y] | (1 << 13));
		}
	}
}
