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
public class Map {
	public enum Direction {
		UP, DOWN, RIGHT, LEFT
	};

	private int height;
	private int width;

	private ArrayList<Point> startingPoints = new ArrayList<Point>();
	private ArrayList<Point> endingPoints = new ArrayList<Point>();

	final int MAX_AREA = 1000;
	final int MIN_SIDE_LENGTH = 36;
	final int MIN_BUILD_LENGTH = 10;
	final int BUILD_LENGTH_RANGE = 6;

	final double heightWidthRatio = 0.5;
	private short[][] map;

	/**
	 * Creates map object
	 * 
	 * @param height
	 *            height of map to make
	 * @param width
	 *            width of map to make
	 * @throws FileNotFoundException
	 *             temp file writer
	 */
	public Map(int height, int width) throws FileNotFoundException {
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

		for(int i=0;i<startingPoints.size();i++){
			generatePlaza(startingPoints.get(i),endingPoints.get(i));
		}

		// TEMP WRITE TO FILE FOR TESTING
		File output = new File("map.txt");
		PrintWriter writer = new PrintWriter(output);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				writer.printf("%3d ", (map[j][i] & 0xFFF));
			}
			writer.println();

		}
		writer.close();

	}

	public void generatePlaza(Point start, Point end) {

		int boxWidth = (int) (Math.abs(end.getX() - (start.getX() - 1)));
		int boxHeight = (int) (Math.abs(end.getY() - (start.getY() - 1)));

		int[] cornerWidths = new int[4];
		int[] cornerHeights = new int[4];
		Point[] buildingStarts = new Point[4];
		Point[] buildingEnds = new Point[4];

		// Top Left Corner
		cornerWidths[0] = (int) (Math.random() * BUILD_LENGTH_RANGE)
				+ MIN_BUILD_LENGTH;
		cornerHeights[0] = (int) (Math.random() * BUILD_LENGTH_RANGE)
				+ MIN_BUILD_LENGTH;
		buildingStarts[0] = new Point((int) start.getX(), (int) start.getY());
		buildingEnds[0] = new Point((int) start.getX() + cornerWidths[0],
				(int) start.getY() + cornerHeights[0]);
		generateBuilding(buildingStarts[0], buildingEnds[0]);

		// Top Right Corner
		cornerWidths[1] = (int) (Math.random() * BUILD_LENGTH_RANGE)
				+ MIN_BUILD_LENGTH;
		cornerHeights[1] = (int) (Math.random() * BUILD_LENGTH_RANGE)
				+ MIN_BUILD_LENGTH;
		buildingStarts[1] = new Point((int) (end.getX() - cornerWidths[1]),
				(int) start.getY());
		buildingEnds[1] = new Point((int) end.getX(), (int) start.getY()
				+ cornerHeights[1]);
		generateBuilding(buildingStarts[1], buildingEnds[1]);


		// Bottom Left Corner
		cornerWidths[2] = (int) (Math.random() * BUILD_LENGTH_RANGE)
				+ MIN_BUILD_LENGTH;
		cornerHeights[2] = (int) (Math.random() * BUILD_LENGTH_RANGE)
				+ MIN_BUILD_LENGTH;
		buildingStarts[2] = new Point((int) start.getX(), (int) end.getY()
				- cornerHeights[2]);
		buildingEnds[2] = new Point((int) start.getX() + cornerWidths[2],
				(int) end.getY());
		generateBuilding(buildingStarts[2], buildingEnds[2]);

		// Left Row
		generateVerticalBuildings(new Point((int)buildingStarts[0].getX(), (int)buildingEnds[0].getY()), buildingStarts[2], 1, 3);

		// Bottom Right Corner
		cornerWidths[3] = (int) (Math.random() * BUILD_LENGTH_RANGE)
				+ MIN_BUILD_LENGTH;
		cornerHeights[3] = (int) (Math.random() * BUILD_LENGTH_RANGE)
				+ MIN_BUILD_LENGTH;
		buildingStarts[3] = new Point((int) end.getX() - cornerWidths[3],
				(int) end.getY() - cornerHeights[3]);
		buildingEnds[3] = new Point((int) end.getX(), (int) end.getY());
		generateBuilding(buildingStarts[3], buildingEnds[3]);

		// Bottom Row

		// Right Row
		generateVerticalBuildings(buildingEnds[1], new Point((int)buildingEnds[3].getX(), (int)buildingStarts[3].getY()), -1, 3);
	}
	
	public void generateVerticalBuildings(Point start, Point end, int dir, int numToGenerate){
		int sideLength = (int) (end.getY() - start.getY());
		int sideBuildingLength = 0;
		Point sideBuildingStart;
		Point sideBuildingEnd;
		
		if (numToGenerate == 1){
			sideBuildingLength = sideLength;
			if (dir == 1){
				sideBuildingStart = new Point((int) end.getX(),(int) start.getY());
				sideBuildingEnd = new Point((int) end.getX()+dir*((int) (Math.random() * BUILD_LENGTH_RANGE)
				+ MIN_BUILD_LENGTH), (int) start.getY()+sideBuildingLength);
			}
			else{
				sideBuildingStart = new Point((int) end.getX()+dir*((int) (Math.random() * BUILD_LENGTH_RANGE)
						+ MIN_BUILD_LENGTH),(int) start.getY());
				sideBuildingEnd = new Point((int) end.getX(), (int) start.getY()+sideBuildingLength);
			}
				
			generateBuilding(sideBuildingStart, sideBuildingEnd);
		}
		else if (sideLength < MIN_BUILD_LENGTH * numToGenerate){
			generateVerticalBuildings(start, end, dir, numToGenerate-1);
		}
		else{	
			do{
				sideBuildingLength = (int) (Math.random() * BUILD_LENGTH_RANGE)
						+ MIN_BUILD_LENGTH;
			}while(sideLength - sideBuildingLength*(numToGenerate-1) < MIN_BUILD_LENGTH);
			
			if (dir == 1){
				sideBuildingStart = new Point((int) end.getX(),(int) start.getY());
				sideBuildingEnd = new Point((int) end.getX()+dir*((int) (Math.random() * BUILD_LENGTH_RANGE)
				+ MIN_BUILD_LENGTH), (int) start.getY()+sideBuildingLength);
			}
			else{
				sideBuildingStart = new Point((int) end.getX()+dir*((int) (Math.random() * BUILD_LENGTH_RANGE)
						+ MIN_BUILD_LENGTH),(int) start.getY());
				sideBuildingEnd = new Point((int) end.getX(), (int) start.getY()+sideBuildingLength);
			}
			
			generateBuilding(sideBuildingStart, sideBuildingEnd);
			
			generateVerticalBuildings(new Point((int)sideBuildingStart.getX(),(int)sideBuildingEnd.getY()), end, dir, numToGenerate-1);
		}
	}
	
	public void generateHorizontalBuildings(Point start, Point end, int numToGenerate){
	
	}

	public void generateBuilding(Point start, Point end) {

		for (int i = (int) start.getX(); i <= end.getX(); i++) {
			for (int j = (int) start.getY(); j <= end.getY(); j++) {
				if (i == start.getX() || i == end.getX() || j == start.getY()
						|| j == end.getY())
					setTile(i, j, 199, Direction.UP);
				else
					setTile(i, j, 201, Direction.UP);
			}
		}
	}

	/**
	 * Generates side road off of main road
	 * 
	 * @param start
	 * @param end
	 */
	public void generateSideRoads(Point start, Point end) {
		int boxWidth = (int) (Math.abs(end.getX() - (start.getX() - 1)));
		int boxHeight = (int) (Math.abs(end.getY() - (start.getY() - 1)));
		System.out.println(boxWidth + " " + boxHeight);
		int roadX;
		int roadY;
		if (boxWidth * boxHeight > MAX_AREA) {

			if ((boxWidth > (2 * MIN_SIDE_LENGTH + 7))
					&& (((boxWidth * heightWidthRatio < boxHeight)
							&& (boxHeight * heightWidthRatio < boxWidth) && Math
							.random() > .5) || (boxHeight * heightWidthRatio < boxWidth))) {
				roadX = (int) ((Math.min(start.getX(), end.getX()) + MIN_SIDE_LENGTH) + ((Math
						.random() * (boxWidth - (2 * MIN_SIDE_LENGTH)))));
				roadY = (int) Math.max(start.getY(), end.getY());
				if (roadX == 1000 || roadY == 1000) {
					System.out.println("WTF");
				}
				generateVerticalRoad(roadX, roadY, 7);
				// Recursive split new generated squares

				Point start1 = new Point((int) start.getX(), (int) start.getY());
				Point end1 = new Point(roadX - 4, roadY);
				generateSideRoads(start1, end1);

				Point start2 = new Point(roadX + 4, (int) start.getY());
				Point end2 = new Point((int) end.getX(), (int) end.getY());
				generateSideRoads(start2, end2);

			} else if (boxHeight > (2 * MIN_SIDE_LENGTH + 7)) {
				roadY = (int) ((Math.min(start.getY(), end.getY()) + MIN_SIDE_LENGTH) + (Math
						.random() * (boxHeight - (2 * MIN_SIDE_LENGTH))));
				roadX = (int) Math.max(start.getX(), end.getX());

				generateHorizontalRoad(roadX, roadY, 7);
				// Recursive split new generated squares

				Point start1 = new Point((int) start.getX(), (int) start.getY());
				Point end1 = new Point(roadX, roadY - 4);
				generateSideRoads(start1, end1);

				Point start2 = new Point((int) start.getX(), (int) roadY + 4);
				Point end2 = new Point((int) end.getX(), (int) end.getY());
				generateSideRoads(start2, end2);
			} else {
				// records corners of plaza
				startingPoints.add(start);
				endingPoints.add(end);
			}
		} else {
			// records corners of plaza
			startingPoints.add(start);
			endingPoints.add(end);
		}
	}

	/**
	 * Generates a vertical road
	 * 
	 * @param x
	 *            starting x
	 * @param y
	 *            staring y
	 * @param size
	 *            road size in blocks
	 */
	public void generateVerticalRoad(int x, int y, int size) {
		int tempx = x - ((size - 1) / 2);
		int tempy = y;
		// Starting intersection
		// end of map
		if (y == (height - 1)) {
			for (int i = 1; i <= size; i++) {
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
		else {
			tempy++;
			for (int i = 1; i <= size; i++) {
				if (i == 1)
					setTile(tempx, (tempy + 1), 103, Direction.LEFT);
				else if (i == size)
					setTile(tempx, (tempy + 1), 103, Direction.DOWN);
				if (i == size - 1)
					setTile(tempx, tempy, 107, Direction.RIGHT);
				else if (i == 2)
					setTile(tempx, tempy, 107, Direction.LEFT);
				else
					setTile(tempx, tempy, 106, Direction.RIGHT);
				tempx++;
			}
			tempy = y;
		}

		// Actual road generation
		tempx = x - ((size - 1) / 2);

		while (tempy > 0 && map[tempx][tempy] == 0) {
			// Places correct tile type and direction
			for (int i = 1; i <= size; i++) {
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
		if (tempy == 0) {
			// Places correct tile type and direction
			for (int i = 1; i <= size; i++) {
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
		else {
			tempy--;
			// Places correct tile type and direction
			for (int i = 1; i <= size; i++) {
				if (i == 1)
					setTile(tempx, (tempy - 1), 103, Direction.UP);
				else if (i == size)
					setTile(tempx, (tempy - 1), 103, Direction.RIGHT);
				else if (i == size - 1)
					setTile(tempx, tempy, 107, Direction.LEFT);
				else if (i == 2)
					setTile(tempx, tempy, 107, Direction.RIGHT);
				else
					setTile(tempx, tempy, 106, Direction.RIGHT);
				tempx++;
			}
		}

	}

	/**
	 * Generates a horizontal road
	 * 
	 * @param x
	 *            starting x
	 * @param y
	 *            staring y
	 * @param size
	 *            road size in blocks
	 */
	public void generateHorizontalRoad(int x, int y, int size) {
		int tempx = x;
		int tempy = y - ((size - 1) / 2);
		// End of map
		if (x == (width - 1)) {
			// Places correct tile type and direction
			for (int i = 1; i <= size; i++) {
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
		else {
			// Places correct tile type and direction
			tempx++;
			for (int i = 1; i <= size; i++) {
				if (i == 1)
					setTile((tempx + 1), tempy, 103, Direction.RIGHT);
				else if (i == size)
					setTile((tempx + 1), tempy, 103, Direction.DOWN);
				if (i == size - 1)
					setTile(tempx, tempy, 107, Direction.UP);
				else if (i == 2)
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
			for (int i = 1; i <= size; i++) {
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
		if (tempx == 0) {
			// Places correct tile type and direction
			for (int i = 1; i <= size; i++) {
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
		else {
			// Places correct tile type and direction
			for (int i = 1; i <= size; i++) {
				if (i == 1)
					setTile((tempx - 1), tempy, 103, Direction.UP);
				else if (i == size)
					setTile((tempx - 1), tempy, 103, Direction.RIGHT);
				else if (i == size - 1)
					setTile(tempx, tempy, 107, Direction.LEFT);
				else if (i == 2)
					setTile(tempx, tempy, 107, Direction.RIGHT);
				else
					setTile(tempx, tempy, 106, Direction.RIGHT);
				tempy++;
			}
		}
	}

	/**
	 * Sets tile to tile id and sets direction
	 * 
	 * @param x
	 *            x position
	 * @param y
	 *            y position
	 * @param id
	 *            tile ID
	 * @param direction
	 *            tile direction
	 */
	public void setTile(int x, int y, int id, Direction direction) {
		// set id
		map[x][y] = (short) id;
		// Set bit 12/ 13 to indicate direction
		if (direction == Direction.RIGHT) {
		} else if (direction == Direction.LEFT) {
			map[x][y] = (short) (map[x][y] | (1 << 13));
		} else if (direction == Direction.DOWN) {
			map[x][y] = (short) (map[x][y] | (1 << 12));
			map[x][y] = (short) (map[x][y] | (1 << 13));
		}

	}

	public short[][] getMap() {
		return map;
	}

	// TEMP MAIN FOR TESTING
	public static void main(String[] args) throws FileNotFoundException {
		Map map = new Map(1000, 1000);
		System.out.println("hi");
		System.out.println("bye");
	}
}
