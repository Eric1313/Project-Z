package map;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
	public enum Direction {
		UP, DOWN, RIGHT, LEFT
	};

	private int height;
	private int width;

	private ArrayList<Point> startingPoints = new ArrayList<Point>();
	private ArrayList<Point> endingPoints = new ArrayList<Point>();

	final int MAX_AREA = 900;
	final int MIN_SIDE_LENGTH = 24;
	final int MIN_BUILD_LENGTH = 10;
	final int MAX_BUILD_LENGTH = 10;
	
	final double heightWidthRatio = 0.5;
	private short[][] map;

	/**
	 * Creates map object
	 * @param height height of map to make
	 * @param width width of map to make
	 * @throws FileNotFoundException temp file writer
	 */
	public Map(int height, int width) throws FileNotFoundException
	{
		this.width = width;
		this.height = height;
		this.map = new short[width][height];

		// Generate main road
		int mainRoadX = (int) ((width / 4) + Math.random() * (width / 2));
		System.out.println(mainRoadX);
		generateVerticalRoad(mainRoadX, height - 1, 13);

		generateSideRoads(new Point(0, 0), new Point(mainRoadX - 7, height - 1));
		generateSideRoads(new Point(mainRoadX + 7, 0), new Point(height - 1,
				width - 1));
		
		for(int i=0;i<startingPoints.size();i++)
		{
			generatePlaza(startingPoints.get(i),endingPoints.get(i));
		}

		// TEMP WRITE TO FILE FOR TESTING
		File output = new File("map.txt");
		PrintWriter writer = new PrintWriter(output);
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				writer.printf("%3d ", (map[j][i] & 0xFFF));
			}
			writer.println();

		}
		writer.close();

	}

	public void generatePlaza(Point start, Point end){

		int boxWidth = (int) (Math.abs(end.getX() - (start.getX() - 1)));
		int boxHeight = (int) (Math.abs(end.getY() - (start.getY() - 1)));
		
		int cornerWidth;
		int cornerHeight;
		Point buildingStart;
		Point buildingEnd;
		
		//Top Left Corner
		cornerWidth = (int)(Math.random()*MAX_BUILD_LENGTH)+MIN_BUILD_LENGTH;
		cornerHeight = (int)(Math.random()*MAX_BUILD_LENGTH)+MIN_BUILD_LENGTH;
		buildingStart = new Point((int)start.getX(),(int)start.getY());
		buildingEnd = new Point((int)start.getX() + cornerWidth, (int)start.getY() + cornerHeight);
		generateBuilding (buildingStart, buildingEnd);	
		
		//Top Right Corner
		cornerWidth = (int)(Math.random()*MAX_BUILD_LENGTH)+MIN_BUILD_LENGTH;
		cornerHeight = (int)(Math.random()*MAX_BUILD_LENGTH)+MIN_BUILD_LENGTH;
		buildingStart = new Point((int) (end.getX()-cornerWidth),(int)start.getY());
		buildingEnd = new Point((int) end.getX(),(int)start.getY() + cornerHeight);
		//generateBuilding (buildingStart, buildingEnd);	
		
		//Bottom Left Corner
		cornerWidth = (int)(Math.random()*MAX_BUILD_LENGTH)+MIN_BUILD_LENGTH;
		cornerHeight = (int)(Math.random()*MAX_BUILD_LENGTH)+MIN_BUILD_LENGTH;
		buildingStart = new Point((int)start.getX(),(int)end.getY()-cornerHeight);
		buildingEnd = new Point((int)start.getX()+cornerWidth, (int)end.getY());
		//generateBuilding (buildingStart, buildingEnd);	
		
		//Bottom Right Corner
		cornerWidth = (int)(Math.random()*MAX_BUILD_LENGTH)+MIN_BUILD_LENGTH;
		cornerHeight = (int)(Math.random()*MAX_BUILD_LENGTH)+MIN_BUILD_LENGTH;
		buildingStart = new Point((int)end.getX()-cornerWidth, (int)end.getY()-cornerHeight);
		buildingEnd = new Point((int)end.getX(),(int)end.getY());
		//generateBuilding (buildingStart, buildingEnd);	
	}
	
	public void generateBuilding(Point start, Point end){
		
		for (int i = (int) start.getX(); i <= end.getX(); i++){
			for (int j = (int) start.getY(); j <= end.getY(); j++){
				setTile(i,j,201,Direction.UP);
			}
		}
	}
	
	
	/**
	 * Generates side road off of main road
	 * @param start
	 * @param end
	 */
	public void generateSideRoads(Point start, Point end)
	{
		int boxWidth = (int) (Math.abs(end.getX() - (start.getX() - 1)));
		int boxHeight = (int) (Math.abs(end.getY() - (start.getY() - 1)));
		System.out.println(boxWidth + " " + boxHeight);
		int roadX;
		int roadY;
		if (boxWidth * boxHeight > MAX_AREA)
		{

			if ((boxWidth > (2 * MIN_SIDE_LENGTH + 7))
					&& (((boxWidth * heightWidthRatio < boxHeight)
							&& (boxHeight * heightWidthRatio < boxWidth) && Math
							.random() > .5) || (boxHeight * heightWidthRatio < boxWidth)))
			{
				roadX = (int) ((Math.min(start.getX(), end.getX()) + MIN_SIDE_LENGTH) + ((Math
						.random() * (boxWidth - (2 * MIN_SIDE_LENGTH)))));
				roadY = (int) Math.max(start.getY(), end.getY());
				if (roadX == 1000 || roadY == 1000)
				{
					System.out.println("WTF");
				}
				generateVerticalRoad(roadX, roadY, 7);
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
			else if (boxHeight > (2 * MIN_SIDE_LENGTH + 7))
			{
				roadY = (int) ((Math.min(start.getY(), end.getY()) + MIN_SIDE_LENGTH) + (Math
						.random() * (boxHeight - (2 * MIN_SIDE_LENGTH))));
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
				// records corners of plaza
				startingPoints.add(start);
				endingPoints.add(end);
			}
		}
		else
		{
			// records corners of plaza
			startingPoints.add(start);
			endingPoints.add(end);
		}
	}

	/**
	 * Generates a vertical road
	 * @param x starting x
	 * @param y staring y
	 * @param size road size in blocks
	 */
	public void generateVerticalRoad(int x, int y,
			int size)
	{
		int tempx = x - ((size - 1) / 2);
		int tempy = y;
		// Starting intersection
		// end of map
		if (y == (height - 1))
		{
			for (int i = 1; i <= size; i++)
			{
				if (i == 1)
					setTile(tempx, tempy, 120, Direction.RIGHT);
				else if (i == size)
					setTile(tempx, tempy, 120, Direction.LEFT);
				else
					setTile(tempx, tempy, 121, Direction.UP);
				tempx++;
			}
			tempy--;
		}
		// Road intersection
		else
		{
			tempy++;
			for (int i = 1; i <= size; i++)
			{
				if (i == 1)
					setTile(tempx, (tempy + 1), 103, Direction.LEFT);
				else if (i == size)
					setTile(tempx, (tempy + 1), 103, Direction.DOWN);
				if (i == size - 1)
					setTile(tempx, tempy, 107, Direction.RIGHT);
				else if (i == (size + 1) / 2)
					setTile(tempx, tempy, 107, Direction.LEFT);
				else
					setTile(tempx, tempy, 106, Direction.RIGHT);
				tempx++;
			}
			tempy = y;
		}

		// Actual road generation
		tempx = x - ((size - 1) / 2);

		while (tempy > 0 && map[tempx][tempy] == 0)
		{
			// Places correct tile type and direction
			for (int i = 1; i <= size; i++)
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
			tempx -= size;
		}
		// End of map
		if (tempy == 0)
		{
			// Places correct tile type and direction
			for (int i = 1; i <= size; i++)
			{
				if (i == 1)
					setTile(tempx, tempy, 120, Direction.LEFT);
				else if (i == size)
					setTile(tempx, tempy, 120, Direction.RIGHT);
				else
					setTile(tempx, tempy, 121, Direction.DOWN);
				tempx++;
			}
		}
		// road intersection
		else
		{
			tempy--;
			// Places correct tile type and direction
			for (int i = 1; i <= size; i++)
			{
				if (i == 1)
					setTile(tempx, (tempy - 1), 103, Direction.UP);
				else if (i == size)
					setTile(tempx, (tempy - 1), 103, Direction.RIGHT);
				else if (i == size - 1)
					setTile(tempx, tempy, 107, Direction.LEFT);
				else if (i == (size + 1) / 2)
					setTile(tempx, tempy, 107, Direction.RIGHT);
				else
					setTile(tempx, tempy, 106, Direction.RIGHT);
				tempx++;
			}
		}

	}

	/**
	 * Generates a horizontal road
	 * @param x starting x
	 * @param y staring y
	 * @param size road size in blocks
	 */
	public void generateHorizontalRoad(int x, int y,
			int size)
	{
		int tempx = x;
		int tempy = y - ((size - 1) / 2);
		// End of map
		if (x == (width - 1))
		{
			// Places correct tile type and direction
			for (int i = 1; i <= size; i++)
			{
				if (i == 1)
					setTile(tempx, tempy, 120, Direction.UP);
				else if (i == size)
					setTile(tempx, tempy, 120, Direction.DOWN);
				else
					setTile(tempx, tempy, 121, Direction.RIGHT);
				tempy++;
			}
			tempx--;
		}
		// Road intersection
		else
		{
			// Places correct tile type and direction
			tempx++;
			for (int i = 1; i <= size; i++)
			{
				if (i == 1)
					setTile((tempx + 1), tempy, 103, Direction.RIGHT);
				else if (i == size)
					setTile((tempx + 1), tempy, 103, Direction.DOWN);
				if (i == size - 1)
					setTile(tempx, tempy, 107, Direction.UP);
				else if (i == (size + 1) / 2)
					setTile(tempx, tempy, 107, Direction.DOWN);
				else
					setTile(tempx, tempy, 106, Direction.UP);
				tempy++;
			}
			tempx = x;
		}

		// Road generation
		tempy -= size;
		while (tempx > 0 && map[tempx][tempy] == 0)// Until end of map or hit
													// another road
		{
			// Places correct tile type and direction
			for (int i = 1; i <= size; i++)
			{
				if (i == 1 || i == size)
					setTile(tempx, tempy, 100, Direction.UP);
				else if (i == 2)
					setTile(tempx, tempy, 102, Direction.DOWN);
				else if (i == size - 1)
					setTile(tempx, tempy, 102, Direction.UP);
				else if (i == y)
					setTile(tempx, tempy, 104, Direction.UP);
				else
					setTile(tempx, tempy, 101, Direction.UP);
				tempy++;
			}
			tempx--;
			tempy -= size;

		}

		// End of map
		if (tempx == 0)
		{
			// Places correct tile type and direction
			for (int i = 1; i <= size; i++)
			{
				if (i == 1)
					setTile(tempx, tempy, 120, Direction.LEFT);
				else if (i == size)
					setTile(tempx, tempy, 120, Direction.RIGHT);
				else
					setTile(tempx, tempy, 121, Direction.DOWN);
				tempy++;
			}
		}
		// Intersection
		else
		{
			// Places correct tile type and direction
			for (int i = 1; i <= size; i++)
			{
				if (i == 1)
					setTile((tempx - 1), tempy, 103, Direction.UP);
				else if (i == size)
					setTile((tempx - 1), tempy, 103, Direction.RIGHT);
				else if (i == size - 1)
					setTile(tempx, tempy, 107, Direction.LEFT);
				else if (i == (size + 1) / 2)
					setTile(tempx, tempy, 107, Direction.RIGHT);
				else
					setTile(tempx, tempy, 106, Direction.RIGHT);
				tempy++;
			}
		}
	}

	/**
	 * Sets tile to tile id and sets direction
	 * @param x x position
	 * @param y y position
	 * @param id tile ID
	 * @param direction tile direction
	 */
	public void setTile(int x, int y, int id, Direction direction)
	{
		// set id
		map[x][y] = (short) id;
		// Set bit 12/ 13 to indicate direction
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
	
	public short[][] getMap()
	{
		return map;
	}

	//TEMP MAIN FOR TESTING
	public static void main(String[] args) throws FileNotFoundException
	{
		Map map = new Map(1000, 1000);
		System.out.println("hi");
		System.out.println("bye");
	}
}
