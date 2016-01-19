package map;

import items.Item;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.ListIterator;

import main.Game;
import utilities.Assets;
import utilities.GameCamera;
import utilities.MouseHandler;
import entities.Entity;
import entities.Inventory;
import entities.Player;
import entities.Zombie;

public class World {
	private Game game;
	private Player player;
	private AffineTransform originalTransform;
	private GameCamera camera;
	private MouseHandler mouse;
	private int width;
	private int height;
	private Item hoverItem;

	private ArrayList<Entity> entitiesDamaged;
	private ArrayList<Integer> damage;
	private ArrayList<Long> damageTicks;

	private Map map;
	private short[][] baseTiles;
	private short[][] upperTiles;
	private Chunk[][] chunkMap;
	private Rectangle[][] solidTiles;
	private Rectangle flag;

	// Controls what is being rendered
	private int row;
	private int col;
	private final int renderControl = 32;
	private float previousXOffset;
	private float previousYOffset;
	private int yChange;
	private int xChange;
	private boolean initializeOffset;

	/**
	 * Constructor for World.
	 * 
	 * @param game
	 *            the game.
	 * @param mapWidth
	 *            the width of the map.
	 * @param mapHeight
	 *            the height of the map
	 */
	public World(Game game, int mapWidth, int mapHeight, Inventory inventory,
			int skinNo) {
		this.width = mapWidth;
		this.height = mapHeight;
		this.game = game;

		// Try to generate the map
		try {
			map = new Map(mapWidth, mapHeight, this.game);
			chunkMap = map.getChunkMap();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Gets the tiles that are rendered below the player
		baseTiles = map.getMap();

		// Gets the tiles that are rendered above the player
		upperTiles = map.getUpperTileMap();

		// Spawns the player in the map
		if (inventory == null)
			player = new Player(new Point((int) map.getPlayerCoordinate()
					.getX() * 32, (int) map.getPlayerCoordinate().getY() * 32),
					null, true, game, map,
					(int) Math.floor((Math.random() * 6)));
		else {
			player = new Player(new Point((int) map.getPlayerCoordinate()
					.getX() * 32, (int) map.getPlayerCoordinate().getY() * 32),
					inventory, true, game, map, skinNo);
		}
		// Sets the player's image
		player.setImages(game.getPlayerImages()[0]);

		// Creates rectangles which will be used for collisions
		solidTiles = new Rectangle[26][34];

		// Saves various references to objects that will be used
		this.camera = game.getCamera();
		this.mouse = game.getDisplay().getMouseHandler();

		this.entitiesDamaged = new ArrayList<Entity>();
		this.damage = new ArrayList<Integer>();
		this.damageTicks = new ArrayList<Long>();
	}

	/**
	 * Renders the world
	 * 
	 * @param g
	 *            the graphics object used to render to the screen
	 */
	public void render(Graphics g) {
		// Initializes the offsets of the map
		if (!initializeOffset) {
			initializeOffsets();
			initializeOffset = true;

		}

		// Converts the graphics object to graphics 2D so we can use methods in
		// graphics 2D
		Graphics2D g2D = (Graphics2D) g;

		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Sets the original transformation of the map without any rotations
		if (originalTransform == null) {
			originalTransform = g2D.getTransform();
		}

		// Renders the part of the map that is below the player
		renderLower(g2D);
		// Updates the map being rendered based on the player's movement
		updateMapOffset();
		g2D.setTransform(originalTransform);

		// *****************************************************************************************************
		// ******************EVERYTHING BETWEEN THIS LINE AND THE NEXT IS YOURS
		// ERIC
		// *****************************************************************************************************
		int chunkX = Math.max((int) player.getPosition().getX() / 512, 2);
		int chunkY = Math.max((int) player.getPosition().getY() / 512, 2);
		for (int x = chunkX - 2; x < Math.min(chunkX + 3, map.getWidth() / 16); x++) {
			for (int y = chunkY - 2; y < Math.min(chunkY + 3,
					map.getHeight() / 16); y++) {
				for (int i = 0; i < chunkMap[x][y].getItems().size(); i++) {
					Item item = chunkMap[x][y].getItems().get(i);
					item.render(g);
				}
				for (int i = 0; i < chunkMap[x][y].getPassibleEntities().size(); i++) {
					Entity entity = chunkMap[x][y].getPassibleEntities().get(i);
					entity.render(g);
				}
				for (int i = 0; i < chunkMap[x][y].getSolidEntities().size(); i++) {
					Entity entity = chunkMap[x][y].getSolidEntities().get(i);
					entity.render(g);
				}
				for (int i = 0; i < chunkMap[x][y].getZombies().size(); i++) {
					Zombie zombie = chunkMap[x][y].getZombies().get(i);
					zombie.render(g);
				}
			}
		}
		// *****************************************************************************************************
		g2D.setTransform(originalTransform);
		// Renders the player
		player.render(g2D);
		// Renders the upper layer of tiles over the player (such as the trees)
		renderUpper(g2D);

		// Makes the map darker to simulate night
		g2D.setTransform(originalTransform);
		g2D.setColor(new Color(0f, 0f, 0f, .6f));
		g2D.fillRect(0, 0, game.getDisplay().getFrame().getWidth(), game
				.getDisplay().getFrame().getHeight());

		// *****************************************************************************************************
		// ******************EVERYTHING UNDER THIS LINE IS YOURS PATRICK
		// *****************************************************************************************************
		g2D.setFont(this.game.getUiFontXS());
		this.hoverItem = hoverItem();

		if (this.hoverItem != null) {
			FontMetrics fm = g.getFontMetrics();

			g.setColor(new Color(100, 100, 100, 150));
			g.fillRect(
					(int) (this.hoverItem.getPosition().x - camera.getxOffset())
							+ 15
							- fm.stringWidth(this.hoverItem.getName())
							/ 2
							- 15,
					(int) (this.hoverItem.getPosition().y - camera.getyOffset()) - 30,
					fm.stringWidth(this.hoverItem.getName()) + 30, 20);

			g.setColor(this.hoverItem.getColour());
			g.drawString(
					this.hoverItem.getName(),
					(int) (this.hoverItem.getPosition().x - camera.getxOffset())
							+ 15 - fm.stringWidth(this.hoverItem.getName()) / 2,
					(int) (this.hoverItem.getPosition().y - camera.getyOffset()) - 15);
		}

		for (int entity = 0; entity < this.entitiesDamaged.size(); entity++) {
			long currentTick = game.getTickCount();
			long difference = currentTick - this.damageTicks.get(entity);
			if (difference < 90) {
				g.setColor(new Color(200, 200, 200,
						(int) (255 - difference * 2)));
				g.drawString(
						this.damage.get(entity).toString(),
						(int) (this.entitiesDamaged.get(entity).getPosition().x - camera
								.getxOffset()) + 16,
						(int) (this.entitiesDamaged.get(entity).getPosition().y - camera
								.getyOffset()) - 32 - (int) (difference / 5));
				g.setColor(Color.BLACK);
			} else {
				this.entitiesDamaged.remove(entity);
				this.damage.remove(entity);
				this.damageTicks.remove(entity);
			}
		}

		if (player.getHealth() >= 0) {
			Composite original = g2D.getComposite();
			float transparency = (float) ((100.0 - player.getHealth()) / 100);
			g2D.setComposite(AlphaComposite.SrcOver.derive(transparency));
			g.drawImage(game.getBloodVisual(), 0, 0, null);
			g2D.setComposite(original);
		}
	}

	/**
	 * Initializes the offsets for the map.
	 */
	private void initializeOffsets() {
		camera.centerOnEntity(player);
		while ((camera.getxOffset() - xChange) >= renderControl) {
			col++;
			xChange += renderControl;
		}
		while ((camera.getyOffset() - yChange) >= renderControl) {
			row++;
			yChange += renderControl;
		}
	}

	/**
	 * Updates the map offsets based on the player's movement to keep the player
	 * in the map.
	 */
	private void updateMapOffset() {
		// Updates the x offset of the map
		if (previousXOffset < camera.getxOffset()) {
			if (xChange < 0) {
				xChange = -xChange;
			} else {
				if ((camera.getxOffset() - xChange) >= renderControl) {
					col++;
					xChange += renderControl;
				}
			}
		} else if (previousXOffset > camera.getxOffset()) {
			if (xChange > 0) {
				if ((camera.getxOffset() - xChange) <= -(renderControl - 31)) {
					col--;
					xChange -= renderControl;
				}
			}
		}
		// Updates the y offset of the map
		if (previousYOffset < camera.getyOffset()) {
			if (yChange < 0) {
				yChange = -yChange;
			} else {
				if ((camera.getyOffset() - yChange) >= renderControl) {
					row++;
					yChange += renderControl;
				}
			}
		} else if (previousYOffset > camera.getyOffset()) {
			if (yChange > 0) {
				if ((camera.getyOffset() - yChange) <= -(renderControl - 31)) {
					row--;
					yChange -= renderControl;
				}
			}

		}
		// Stores the previous offsets
		previousXOffset = camera.getxOffset();
		previousYOffset = camera.getyOffset();
	}

	/**
	 * Renders the part of the map under the player.
	 * 
	 * @param g2D
	 *            the graphics 2D object.
	 */
	private void renderLower(Graphics2D g2D) {
		int tileY = 0;
		int tileX = 0;
		for (int i = row; i < row + 26; i++) {
			tileX = 0;
			for (int j = col; j < col + 34; j++) {
				if (j >= baseTiles[0].length || i >= baseTiles.length) {
					break;
				}
				g2D.setTransform(originalTransform);
				// Rotates the tiles 180 decrease
				if ((baseTiles[j][i] & (1 << 12)) != 0
						&& ((baseTiles[j][i] & (1 << 13)) != 0)) {
					g2D.rotate(Math.toRadians(180), (int) (tileX
							* Assets.TILE_WIDTH - camera.getxOffset())
							+ xChange + Assets.TILE_WIDTH / 2, (int) (tileY
							* Assets.TILE_HEIGHT - camera.getyOffset()
							+ yChange + Assets.TILE_HEIGHT / 2));
				}
				// Rotates the tile 90 degrees
				else if ((baseTiles[j][i] & (1 << 12)) != 0) {
					g2D.rotate(Math.toRadians(90), (int) (tileX
							* Assets.TILE_WIDTH - camera.getxOffset())
							+ xChange + Assets.TILE_WIDTH / 2, (int) (tileY
							* Assets.TILE_HEIGHT - camera.getyOffset()
							+ yChange + Assets.TILE_HEIGHT / 2));
				} else if ((baseTiles[j][i] & (1 << 13)) != 0) {
					// Rotates the tiles 270 degrees
					g2D.rotate(Math.toRadians(-90), (int) (tileX
							* Assets.TILE_WIDTH - camera.getxOffset())
							+ xChange + Assets.TILE_WIDTH / 2, (int) (tileY
							* Assets.TILE_HEIGHT - camera.getyOffset()
							+ yChange + Assets.TILE_HEIGHT / 2));
				}
				if ((baseTiles[j][i] & (1 << 14)) != 0) {
					// Sets the collision tiles which are solid
					solidTiles[tileY][tileX] = new Rectangle((int) (tileX
							* Assets.TILE_WIDTH - camera.getxOffset())
							+ xChange, (int) (tileY * Assets.TILE_HEIGHT
							- camera.getyOffset() + yChange),
							Assets.TILE_WIDTH, Assets.TILE_HEIGHT);
				} else {
					solidTiles[tileY][tileX] = null;
				}
				// Gets the tile ID
				int id = (baseTiles[j][i] & 0xFFF);
				// Draws the tile to the screen
				g2D.drawImage(game.getTileImages()[(id / 100) - 1][(id % 100)],
						(int) (tileX * Assets.TILE_WIDTH - camera.getxOffset())
								+ xChange, (int) (tileY * Assets.TILE_HEIGHT
								- camera.getyOffset() + yChange), null);

				// Special collision rectangle for the checkpoint
				if (id == 211) {
					flag = new Rectangle(
							(int) (tileX * Assets.TILE_WIDTH - camera.getxOffset())
									+ xChange,
							(int) (tileY * Assets.TILE_HEIGHT
									- camera.getyOffset() + yChange),
							Assets.TILE_WIDTH, Assets.TILE_HEIGHT);
				}
				tileX++;
			}
			tileY++;
		}
	}

	/**
	 * Render the part of the map over the player.
	 * 
	 * @param g2D
	 *            the graphics 2D object used to draw everything.
	 */
	private void renderUpper(Graphics2D g2D) {
		int tileY = 0;
		int tileX = 0;
		for (int i = row; i < row + 26; i++) {
			tileX = 0;
			for (int j = col; j < col + 34; j++) {
				if (j >= upperTiles[0].length || i >= upperTiles.length) {
					break;
				}
				g2D.setTransform(originalTransform);
				if ((upperTiles[j][i] & (1 << 12)) != 0
						&& ((upperTiles[j][i] & (1 << 13)) != 0)) {
					g2D.rotate(Math.toRadians(180), (int) (tileX
							* Assets.TILE_WIDTH - camera.getxOffset())
							+ xChange + 16, (int) (tileY * Assets.TILE_HEIGHT
							- camera.getyOffset() + yChange + 16));
				}

				else if ((upperTiles[j][i] & (1 << 12)) != 0) {
					g2D.rotate(Math.toRadians(90), (int) (tileX
							* Assets.TILE_WIDTH - camera.getxOffset())
							+ xChange + 16, (int) (tileY * Assets.TILE_HEIGHT
							- camera.getyOffset() + yChange + 16));
				} else if ((upperTiles[j][i] & (1 << 13)) != 0) {
					g2D.rotate(Math.toRadians(-90), (int) (tileX
							* Assets.TILE_WIDTH - camera.getxOffset())
							+ xChange + 16, (int) (tileY * Assets.TILE_HEIGHT
							- camera.getyOffset() + yChange + 16));
				}
				// Gets the tile id
				int id = (upperTiles[j][i] & 0xFFF);
				// Draws the tile to the screen
				if (id != 0)
					g2D.drawImage(
							game.getTileImages()[(id / 100) - 1][(id % 100)],
							(int) (tileX * Assets.TILE_WIDTH - camera
									.getxOffset()) + xChange,
							(int) (tileY * Assets.TILE_HEIGHT
									- camera.getyOffset() + yChange), null);
				tileX++;
			}
			tileY++;
		}
	}

	public Item hoverItem() {
		int chunkX = Math.max((int) player.getPosition().getX() / 512, 2);
		int chunkY = Math.max((int) player.getPosition().getY() / 512, 2);
		for (int x = chunkX - 2; x < Math.min(chunkX + 3, map.getWidth() / 16); x++) {
			for (int y = chunkY - 2; y < Math.min(chunkY + 3,
					map.getHeight() / 16); y++) {
				for (ListIterator<Item> iterator = chunkMap[x][y].getItems()
						.listIterator(chunkMap[x][y].getItems().size()); iterator
						.hasPrevious();) {
					Item item = iterator.previous();
					Rectangle itemHitbox = new Rectangle(
							(int) (item.getPosition().x - camera.getxOffset()),
							(int) (item.getPosition().y - camera.getyOffset()),
							32, 32);
					if (itemHitbox.contains(mouse.getMouseLocation())
							&& Point.distance(player.getPosition().x,
									player.getPosition().y,
									item.getPosition().x, item.getPosition().y) <= 8 * 32) {
						item.setHover(true);
						return item;
					} else if (itemHitbox
							.intersects(new Rectangle((int) (player
									.getPosition().x - camera.getxOffset()),
									(int) (player.getPosition().y - camera
											.getyOffset()), 32, 32))) {
						item.setHover(true);
						return item;
					} else {
						item.setHover(false);
					}
				}
			}
		}

		return null;
	}

	public void damage(int damage, Entity entity) {
		this.entitiesDamaged.add(entity);
		this.damage.add(damage);
		this.damageTicks.add(this.game.getTickCount());
	}

	public Item getHoverItem() {
		return hoverItem;
	}

	public Rectangle getFlag() {
		return flag;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Player getPlayer() {
		return player;
	}

	public Map getMap() {
		// TODO Auto-generated method stub
		return map;
	}

	public Rectangle[][] getSolid() {
		return solidTiles;
	}

}
