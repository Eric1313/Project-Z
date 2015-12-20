package map;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

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
	final int maxArea = 1;
	final int minArea =900;
	final double heightWidthRatio = 0.5;

	public enum Direction {
		UP, DOWN, RIGHT, LEFT
	};

	short[][] map;

	public Map(int height, int width) throws FileNotFoundException
	{
		this.width = width;
		this.height = height;
		this.map = new short[width][height];

		// Generate main road

		int roadX = (int) ((width / 4) + Math.random() * (width / 2));
		System.out.println(roadX);
		generateVertiacalRoad(roadX, height - 1, 13);

		generateSideRoads(new Point(0, 0), new Point(roadX - 7, height - 1));
		generateSideRoads(new Point(roadX + 7, 0), new Point(height - 1,
				width - 1));

		File output = new File("map.txt");
		PrintWriter writer = new PrintWriter(output);
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				// System.out.print(map[i][j]);
				writer.printf("%3d ", (map[j][i] & 0xFFF));
				// System.out.printf("%3d ",(map[i][j]& 0xFFF));

			}
			writer.println();
			// System.out.println();

		}
		writer.close();

	}

	public void generateSideRoads(Point start, Point end)
	{
		int boxWidth = (int) (Math.abs(end.getX() - (start.getX()-1)));
		int boxHeight = (int) (Math.abs(end.getY() - (start.getY()-1)));
		System.out.println(boxWidth+" "+boxHeight);
		int roadX;
		int roadY;
		if (boxWidth * boxHeight > maxArea)
		{
//			if ((((boxWidth*heightWidthRatio < boxHeight)&&(boxHeight*heightWidthRatio < boxWidth)&&Math.random()>.5)||(boxWidth*heightWidthRatio > boxHeight)&&(boxHeight*heightWidthRatio < boxWidth))&&boxWidth>2 * minSideLength)
			if((boxWidth> (2 * minSideLength+7))&&(((boxWidth*heightWidthRatio < boxHeight)&&(boxHeight*heightWidthRatio < boxWidth)&&Math.random()>.5)||(boxHeight*heightWidthRatio < boxWidth)))
			{
				roadX = (int) ((Math.min(start.getX(), end.getX()) + minSideLength) + ((Math
						.random() * (boxWidth - (2*minSideLength)))));
				roadY = (int) Math.max(start.getY(), end.getY());
				if (roadX==1000||roadY==1000)
				{
					System.out.println("WTF");
				}
				generateVertiacalRoad(roadX, roadY, 7);
				// Recursive split new generated squares

				Point start1 = new Point((int) start.getX(),
						(int) start.getY());
				Point end1 = new Point(roadX - 4, roadY);
				generateSideRoads(start1, end1);

				Point start2 = new Point(roadX + 4, (int) start.getY());
				Point end2 = new Point((int) end.getX(),
						(int) end.getY());
				generateSideRoads(start2, end2);

			}
			else if (boxHeight> (2 * minSideLength+7))
			{
				roadY = (int) ((Math.min(start.getY(), end.getY()) + minSideLength) + (Math
						.random() * (boxHeight - (2*minSideLength))));
				roadX = (int) Math.max(start.getX(), end.getX());

				
				generateHorizontalRoad(roadX, roadY, 7);
				// Recursive split new generated squares

				Point start1 = new Point((int) start.getX(),
						(int) start.getY());
				Point end1 = new Point(roadX, roadY - 4);
				generateSideRoads(start1, end1);

				Point start2 = new Point((int) start.getX(),
						(int) roadY + 4);
				Point end2 = new Point((int) end.getX(),
						(int) end.getY());
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
		int tempx = x - ((size - 1) / 2);
		int tempy = y;

		while (tempy >= 0 && map[tempx][tempy] == 0)
		{
			for (int i = 1; i <=size; i++)
			{
				if (i == 1 || i == size)
					setTile(tempx, tempy, 100, Direction.UP);
				else if (i == 2)
					setTile(tempx, tempy, 102, Direction.RIGHT);
				else if (i == size - 1)
					setTile(tempx, tempy, 102, Direction.LEFT);
				else if (i == (size + 1) / 2)
					setTile(tempx, tempy, 104, Direction.RIGHT);
				else
					setTile(tempx, tempy, 101, Direction.UP);
				tempx++;
			}
			tempy--;
			tempx -=size;
		}

	}

	public void generateHorizontalRoad(int x, int y,
			int size)
	{
		int tempx = x;
		int tempy = y - ((size - 1) / 2);

		while (tempx >= 0 && map[tempx][tempy] == 0)
		{
			for (int i = 1; i <=size; i++)
			{
				if (i == 1 || i == size)
					setTile(tempx, tempy, 100, Direction.UP);
				else if (i == 2)
					setTile(tempx, tempy, 102, Direction.RIGHT);
				else if (i == size - 1)
					setTile(tempx, tempy, 102, Direction.LEFT);
				else if (i == y)
					setTile(tempx, tempy, 104, Direction.RIGHT);
				else
					setTile(tempx, tempy, 101, Direction.UP);
				tempy++;
			}
			tempx--;
			tempy -=size;

		}
	}

	public void setTile(int x, int y, int i, Direction direction)
	{
		map[x][y] = (short) i;
		if (direction == Direction.RIGHT)
		{
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

	public static void main(String[] args) throws FileNotFoundException
	{
		Map map = new Map(1000, 1000);
		System.out.println("hi");
	}
}
