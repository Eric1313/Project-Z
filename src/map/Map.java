package map;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import entities.MapObject;
import enums.MapObjectType;

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

	// Map paramaters
	private int height;
	private int width;

	// Generation
	final int MAX_AREA = 1000;
	final int MIN_SIDE_LENGTH = 42;
	final int MIN_BUILD_LENGTH = 10;
	final int BUILD_LENGTH_RANGE = 6;
	final double HEIGHT_WIDTH_RATIO = 0.5;
	final int ROAD_WIDTH = 11;
	// STores locations of all plazas

	// Map storage
	private short[][] tileMap;
	private Chunk[][] chunkMap;

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
		this.tileMap = new short[width][height];
		this.chunkMap = new Chunk[this.width / 16][this.height / 16];

		for (int i = 0; i < this.width / 16; i++)
			for (int j = 0; j < this.height / 16; j++)
				chunkMap[i][j] = new Chunk();

		// Generate main road
		int mainRoadX = (int) ((width / 4) + Math.random() * (width / 2));
		// System.out.println(mainRoadX);
		generateVerticalRoad(mainRoadX, height - 1, 13);

		// Generates all other roads
		generateSideRoads(new Point(0, 0), new Point(mainRoadX - 7, height - 1));
		generateSideRoads(new Point(mainRoadX + 7, 0), new Point(height - 1,
				width - 1));

		// TEMP WRITE TO FILE FOR TESTING
		File output = new File("map.txt");
		PrintWriter writer = new PrintWriter(output);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				writer.printf("%3d ", (tileMap[j][i] & 0xFFF));
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

		for (int i = (int) start.getX(); i <= end.getX(); i++) {
			for (int j = (int) start.getY(); j <= end.getY(); j++) {
				setTile(i, j, 108, Direction.UP);
			}
		}

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

		// Bottom Right Corner
		cornerWidths[3] = (int) (Math.random() * BUILD_LENGTH_RANGE)
				+ MIN_BUILD_LENGTH;
		cornerHeights[3] = (int) (Math.random() * BUILD_LENGTH_RANGE)
				+ MIN_BUILD_LENGTH;
		buildingStarts[3] = new Point((int) end.getX() - cornerWidths[3],
				(int) end.getY() - cornerHeights[3]);
		buildingEnds[3] = new Point((int) end.getX(), (int) end.getY());
		generateBuilding(buildingStarts[3], buildingEnds[3]);

		// Generates the vertical buildings
		generateVerticalBuildings(new Point((int) buildingStarts[0].getX(),
				(int) buildingEnds[0].getY()), buildingStarts[2], 1, 3);
		generateVerticalBuildings(buildingEnds[1], new Point(
				(int) buildingEnds[3].getX(), (int) buildingStarts[3].getY()),
				-1, 5);

		// Generates the horizontal buildings
		if (buildingEnds[0].getY() <= buildingEnds[1].getY())
			generateHorizontalBuildings(new Point((int) buildingEnds[0].getX(),
					(int) buildingStarts[0].getY()), buildingStarts[1], 1, 5,
					cornerHeights[0] - MIN_BUILD_LENGTH);
		else
			generateHorizontalBuildings(new Point((int) buildingEnds[0].getX(),
					(int) buildingStarts[0].getY()), buildingStarts[1], 1, 5,
					cornerHeights[1] - MIN_BUILD_LENGTH);

		if (buildingStarts[2].getY() >= buildingStarts[3].getY())
			generateHorizontalBuildings(buildingEnds[2],
					new Point((int) buildingStarts[3].getX(),
							(int) buildingEnds[3].getY()), -1, 5,
					cornerHeights[2] - MIN_BUILD_LENGTH);
		else
			generateHorizontalBuildings(buildingEnds[2],
					new Point((int) buildingStarts[3].getX(),
							(int) buildingEnds[3].getY()), -1, 5,
					cornerHeights[3] - MIN_BUILD_LENGTH);
	}

	/**
	 * Generates buildings for a column
	 * 
	 * @param start
	 *            The start of the column
	 * @param end
	 *            The end of the column
	 * @param dir
	 *            The direction of the column (1 = West, -1 = East)
	 * @param numToGenerate
	 *            The number of buildings to generate
	 */
	public void generateVerticalBuildings(Point start, Point end, int dir,
			int numToGenerate) {
		int sideLength = (int) (end.getY() - start.getY());
		int sideBuildingLength = 0;
		Point sideBuildingStart;
		Point sideBuildingEnd;

		if (numToGenerate == 1) {
			sideBuildingLength = sideLength;
			if (dir == 1) {
				sideBuildingStart = new Point((int) end.getX(),
						(int) start.getY());
				sideBuildingEnd = new Point(
						(int) end.getX()
								+ dir
								* ((int) (Math.random() * BUILD_LENGTH_RANGE) + MIN_BUILD_LENGTH),
						(int) start.getY() + sideBuildingLength);
			} else {
				sideBuildingStart = new Point(
						(int) end.getX()
								+ dir
								* ((int) (Math.random() * BUILD_LENGTH_RANGE) + MIN_BUILD_LENGTH),
						(int) start.getY());
				sideBuildingEnd = new Point((int) end.getX(),
						(int) start.getY() + sideBuildingLength);
			}

			generateBuilding(sideBuildingStart, sideBuildingEnd);
		} else if (sideLength < MIN_BUILD_LENGTH * numToGenerate) {
			generateVerticalBuildings(start, end, dir, numToGenerate - 1);
		} else {
			do {
				sideBuildingLength = (int) (Math.random() * BUILD_LENGTH_RANGE)
						+ MIN_BUILD_LENGTH;
			} while (sideLength - sideBuildingLength * (numToGenerate - 1) < MIN_BUILD_LENGTH);

			if (dir == 1) {
				sideBuildingStart = new Point((int) end.getX(),
						(int) start.getY());
				sideBuildingEnd = new Point(
						(int) end.getX()
								+ dir
								* ((int) (Math.random() * BUILD_LENGTH_RANGE) + MIN_BUILD_LENGTH),
						(int) start.getY() + sideBuildingLength);
			} else {
				sideBuildingStart = new Point(
						(int) end.getX()
								+ dir
								* ((int) (Math.random() * BUILD_LENGTH_RANGE) + MIN_BUILD_LENGTH),
						(int) start.getY());
				sideBuildingEnd = new Point((int) end.getX(),
						(int) start.getY() + sideBuildingLength);
			}

			generateBuilding(sideBuildingStart, sideBuildingEnd);

			generateVerticalBuildings(new Point((int) sideBuildingStart.getX(),
					(int) sideBuildingEnd.getY()), end, dir, numToGenerate - 1);
		}
	}

	/**
	 * Generates buildings for a row
	 * 
	 * @param start
	 *            The start of the row
	 * @param end
	 *            The end of the row
	 * @param dir
	 *            The direction (1 = North, -1 = South)
	 * @param numToGenerate
	 *            The number of buildings to generate
	 * @param maxRange
	 *            The maximum height of the buildings
	 */
	public void generateHorizontalBuildings(Point start, Point end, int dir,
			int numToGenerate, int maxRange) {
		int sideLength = (int) (end.getX() - start.getX());
		int sideBuildingLength = 0;
		Point sideBuildingStart;
		Point sideBuildingEnd;

		// System.out.println("Max Range: " + maxRange);

		if (numToGenerate == 1) {
			sideBuildingLength = sideLength;
			if (dir == 1) {
				sideBuildingStart = new Point((int) start.getX(),
						(int) start.getY());
				sideBuildingEnd = new Point((int) start.getX()
						+ sideBuildingLength, (int) start.getY() + dir
						* ((int) (Math.random() * maxRange) + MIN_BUILD_LENGTH));
			} else {
				sideBuildingStart = new Point(
						(int) start.getX(),
						(int) start.getY()
								+ dir
								* ((int) (Math.random() * maxRange) + MIN_BUILD_LENGTH));
				sideBuildingEnd = new Point((int) start.getX()
						+ sideBuildingLength, (int) start.getY());
			}

			generateBuilding(sideBuildingStart, sideBuildingEnd);
		} else if (sideLength < MIN_BUILD_LENGTH * numToGenerate) {
			generateHorizontalBuildings(start, end, dir, numToGenerate - 1,
					maxRange);
		} else {
			do {
				sideBuildingLength = (int) (Math.random() * BUILD_LENGTH_RANGE)
						+ MIN_BUILD_LENGTH;
			} while (sideLength - sideBuildingLength * (numToGenerate - 1) < MIN_BUILD_LENGTH);

			if (dir == 1) {
				sideBuildingStart = new Point((int) start.getX(),
						(int) start.getY());
				sideBuildingEnd = new Point((int) start.getX()
						+ sideBuildingLength, (int) start.getY() + dir
						* ((int) (Math.random() * maxRange) + MIN_BUILD_LENGTH));
			} else {
				sideBuildingStart = new Point(
						(int) start.getX(),
						(int) start.getY()
								+ dir
								* ((int) (Math.random() * maxRange) + MIN_BUILD_LENGTH));
				sideBuildingEnd = new Point((int) start.getX()
						+ sideBuildingLength, (int) start.getY());
			}

			generateBuilding(sideBuildingStart, sideBuildingEnd);

			if (dir == 1)
				generateHorizontalBuildings(
						new Point((int) sideBuildingEnd.getX(),
								(int) sideBuildingStart.getY()), end, dir,
						numToGenerate - 1, maxRange);
			else
				generateHorizontalBuildings(sideBuildingEnd, end, dir,
						numToGenerate - 1, maxRange);
		}
	}

	public void generateBuilding(Point start, Point end) {

		for (int i = (int) start.getX(); i <= end.getX(); i++) {
			for (int j = (int) start.getY(); j <= end.getY(); j++) {
				if (i == start.getX() || i == end.getX() || j == start.getY()
						|| j == end.getY())
					setTile(i, j, 200, Direction.UP);
				else if (i == start.getX() + 1 && j == start.getY() + 1) {
					chunkMap[i / 16][j / 16].add(new MapObject(32, 32,
							new Point(i * 32, j * 32), 180, 1000, true, null,
							null, null, MapObjectType.WALL_CORNER));
					setTile(i, j, 201, Direction.UP);
				} else if (i == start.getX() + 1 && j == end.getY() - 1) {
					chunkMap[i / 16][j / 16].add(new MapObject(32, 32,
							new Point(i * 32, j * 32), 270, 1000, true, null,
							null, null, MapObjectType.WALL_CORNER));
					setTile(i, j, 201, Direction.UP);
				} else if (i == end.getX() - 1 && j == start.getY() + 1) {
					chunkMap[i / 16][j / 16].add(new MapObject(32, 32,
							new Point(i * 32, j * 32), 90, 1000, true, null,
							null, null, MapObjectType.WALL_CORNER));
					setTile(i, j, 201, Direction.UP);
				} else if (i == end.getX() - 1 && j == end.getY() - 1) {
					chunkMap[i / 16][j / 16].add(new MapObject(32, 32,
							new Point(i * 32, j * 32), 0, 1000, true, null,
							null, null, MapObjectType.WALL_CORNER));
					setTile(i, j, 201, Direction.UP);
				} else if (i == start.getX() + 1) {
					chunkMap[i / 16][j / 16].add(new MapObject(32, 32,
							new Point(i * 32, j * 32), 270, 1000, true, null,
							null, null, MapObjectType.WALL));
					setTile(i, j, 201, Direction.UP);
				} else if (j == start.getY() + 1) {
					chunkMap[i / 16][j / 16].add(new MapObject(32, 32,
							new Point(i * 32, j * 32), 0, 1000, true, null,
							null, null, MapObjectType.WALL));
					setTile(i, j, 201, Direction.UP);
				} else if (i == end.getX() - 1) {
					chunkMap[i / 16][j / 16].add(new MapObject(32, 32,
							new Point(i * 32, j * 32), 90, 1000, true, null,
							null, null, MapObjectType.WALL));
					setTile(i, j, 201, Direction.UP);
				} else if (j == end.getY() - 1) {
					chunkMap[i / 16][j / 16].add(new MapObject(32, 32,
							new Point(i * 32, j * 32), 180, 1000, true, null,
							null, null, MapObjectType.WALL));
					setTile(i, j, 201, Direction.UP);
				} else
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
		// System.out.println(boxWidth + " " + boxHeight);
		int roadX;
		int roadY;
		if (boxWidth * boxHeight > MAX_AREA) {

			if ((boxWidth > (2 * MIN_SIDE_LENGTH + ROAD_WIDTH))
					&& (((boxWidth * HEIGHT_WIDTH_RATIO < boxHeight)
							&& (boxHeight * HEIGHT_WIDTH_RATIO < boxWidth) && Math
							.random() > .5) || (boxHeight * HEIGHT_WIDTH_RATIO < boxWidth))) {
				roadX = (int) ((Math.min(start.getX(), end.getX()) + MIN_SIDE_LENGTH) + ((Math
						.random() * (boxWidth - (2 * MIN_SIDE_LENGTH)))));
				roadY = (int) Math.max(start.getY(), end.getY());
				generateVerticalRoad(roadX, roadY, ROAD_WIDTH);
				// Recursive split new generated squares

				Point start1 = new Point((int) start.getX(), (int) start.getY());
				Point end1 = new Point(roadX - (ROAD_WIDTH + 1) / 2, roadY);
				generateSideRoads(start1, end1);

				Point start2 = new Point(roadX + (ROAD_WIDTH + 1) / 2,
						(int) start.getY());
				Point end2 = new Point((int) end.getX(), (int) end.getY());
				generateSideRoads(start2, end2);

			} else if (boxHeight > (2 * MIN_SIDE_LENGTH + ROAD_WIDTH)) {
				roadY = (int) ((Math.min(start.getY(), end.getY()) + MIN_SIDE_LENGTH) + (Math
						.random() * (boxHeight - (2 * MIN_SIDE_LENGTH))));
				roadX = (int) Math.max(start.getX(), end.getX());

				generateHorizontalRoad(roadX, roadY, ROAD_WIDTH);
				// Recursive split new generated squares

				Point start1 = new Point((int) start.getX(), (int) start.getY());
				Point end1 = new Point(roadX, roadY - (ROAD_WIDTH + 1) / 2);
				generateSideRoads(start1, end1);

				Point start2 = new Point((int) start.getX(), (int) roadY
						+ (ROAD_WIDTH + 1) / 2);
				Point end2 = new Point((int) end.getX(), (int) end.getY());
				generateSideRoads(start2, end2);
			} else {
				// records corners of plaza
				generatePlaza(start, end);
			}
		} else {
			// records corners of plaza
			generatePlaza(start, end);
		}
	}

	/**
	 * Generates a vertical road
	 * 
	 * @param x
	 *            starting x
	 * @param y
	 *            starting y
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
				if (i == 1 || i == size) {

				}
				// if (i == 1)
				// setTile(tempx, (tempy + 1), 102, Direction.LEFT);
				// else if (i == size)
				// setTile(tempx, (tempy + 1), 102, Direction.DOWN);
				else if (i == size - 1) {
					setTile(tempx, tempy, 107, Direction.RIGHT);
					setTile(tempx, (tempy + 1), 102, Direction.LEFT);
				} else if (i == 2) {
					setTile(tempx, tempy, 107, Direction.LEFT);
					setTile(tempx, (tempy + 1), 102, Direction.DOWN);
				} else {
					setTile(tempx, tempy, 106, Direction.RIGHT);
					setTile(tempx, (tempy + 1), 101, Direction.RIGHT);
				}
				tempx++;
			}
			tempy = y;
		}

		// Actual road generation
		tempx = x - ((size - 1) / 2);

		while (tempy > 0 && tileMap[tempx][tempy] == 0) {
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
				else if (size == 13
						&& (i == (((size + 1) / 2) + 1) / 2 || i == (size + 1)
								/ 2 + (((size + 1) / 2) + 1) / 2 - 1))
					setTile(tempx, tempy, 105, Direction.RIGHT);
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
				if (i == size || i == 1) {

				}
				// if (i == 1)
				// setTile(tempx, (tempy - 1), 102, Direction.UP);
				// else if (i == size)
				// setTile(tempx, (tempy - 1), 102, Direction.RIGHT);
				else if (i == size - 1) {
					setTile(tempx, tempy, 107, Direction.LEFT);
					setTile(tempx, (tempy - 1), 102, Direction.RIGHT);
				} else if (i == 2) {
					setTile(tempx, tempy, 107, Direction.RIGHT);
					setTile(tempx, (tempy - 1), 102, Direction.UP);

				} else {
					setTile(tempx, tempy, 106, Direction.RIGHT);
					setTile(tempx, (tempy - 1), 101, Direction.UP);
				}
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
	 *            starting y
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
				if (i == 1 || i == size) {

				}
				// if (i == 1)
				// setTile((tempx + 1), tempy, 103, Direction.RIGHT);
				// else if (i == size)
				// setTile((tempx + 1), tempy, 103, Direction.DOWN);
				else if (i == size - 1) {
					setTile(tempx, tempy, 107, Direction.UP);
					setTile((tempx + 1), tempy, 103, Direction.RIGHT);
				} else if (i == 2) {
					setTile(tempx, tempy, 107, Direction.DOWN);
					setTile((tempx + 1), tempy, 103, Direction.DOWN);
				} else {
					setTile(tempx, tempy, 106, Direction.UP);
					setTile((tempx + 1), tempy, 101, Direction.UP);
				}
				tempy++;
			}
			tempx = x;
		}

		// Road generation
		tempy -= size;
		while (tempx > 0 && tileMap[tempx][tempy] == 0)// Until end of map or
														// hit
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
				else if (i == (size + 1) / 2)
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
				if (i == 1 || i == size) {

				}
				// if (i == 1)
				// setTile((tempx - 1), tempy, 103, Direction.UP);
				// else if (i == size)
				// setTile((tempx - 1), tempy, 103, Direction.RIGHT);
				else if (i == size - 1) {
					setTile(tempx, tempy, 107, Direction.LEFT);
					setTile((tempx - 1), tempy, 103, Direction.UP);
				} else if (i == 2) {
					setTile(tempx, tempy, 107, Direction.RIGHT);
					setTile((tempx - 1), tempy, 103, Direction.RIGHT);
				} else {
					setTile(tempx, tempy, 106, Direction.RIGHT);
					setTile((tempx - 1), tempy, 101, Direction.UP);
				}
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
		tileMap[x][y] = (short) id;
		// Set bit 12/ 13 to indicate direction
		if (direction == Direction.RIGHT) {
		} else if (direction == Direction.LEFT) {
			tileMap[x][y] = (short) (tileMap[x][y] | (1 << 13));
		} else if (direction == Direction.DOWN) {
			tileMap[x][y] = (short) (tileMap[x][y] | (1 << 12));
			tileMap[x][y] = (short) (tileMap[x][y] | (1 << 13));
		}

	}

	public short[][] getMap() {
		return tileMap;
	}
}