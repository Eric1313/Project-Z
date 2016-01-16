package utilities;

import items.Item;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.ListIterator;

import main.Game;
import map.Chunk;
import map.Map;
import entities.Player;
import entities.Zombie;

public class World {
	private Game game;
	private Player player;
	private Arc2D flashLight;
	private Rectangle[][] solid;
	private AffineTransform originalTransform;
	private GameCamera camera;
	private MouseHandler mouse;
	private short[][] tileId;
	private Chunk[][] chunkMap;
	private Map map;
	private int width;
	private int height;
	private Item hoverItem;

	// Controls what is being rendered
	private int row;
	private int col;
	private final int renderControl = 32;
	private float previousXOffset;
	private float previousYOffset;
	private int yChange;
	private int xChange;
	private boolean initializeOffset;

	public World(Game game, int width, int height) {
		this.width = width;
		this.height = height;
		this.game = game;
		try {
			map = new Map(width, height, this.game);
			chunkMap = map.getChunkMap();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		tileId = map.getMap();
		// TODO Randomly place the player into the world rather than putting it
		// in the top left corner
		// player = new Player(new Point(
		// (int) map.getPlayerCoordinate().getX() * 32, (int) map
		// .getPlayerCoordinate().getY() * 32), true, game, map);
		player = new Player(new Point(50, 50), true, game, map);
		player.setImages(game.getPlayer()[0]);
		this.row = (int) (player.getPosition().getY() / 32);
		this.col = (int) (player.getPosition().getX() / 32);
		// this.yChange = (int) (player.getPosition().getY() - 500);
		// this.xChange = (int) (player.getPosition().getX() - 500);
		this.yChange = 0;
		this.xChange = 0;
		flashLight = new Arc2D.Double();
		solid = new Rectangle[26][34];
		this.camera = game.getCamera();
		this.mouse = game.getDisplay().getMouseHandler();
	}

	public void render(Graphics g) {
		if (!initializeOffset) {
			initializeOffsets();
			initializeOffset = true;

		}
		double angle = Math.atan2(
				((player.getPosition().getY()) + 16 - camera.getyOffset())
						- mouse.getMouseLocation().getY(), (player
						.getPosition().getX() + 16 - camera.getxOffset())
						- mouse.getMouseLocation().getX())
				- Math.PI / 2;

		Graphics2D g2D = (Graphics2D) g;
		if (originalTransform == null) {
			originalTransform = g2D.getTransform();
		}

		g2D.rotate(angle, player.getPosition().getX() - camera.getxOffset()
				+ 16, player.getPosition().getY() - camera.getyOffset() + 16);
		flashLight.setArcByCenter(
				player.getPosition().getX() - camera.getxOffset() + 16, player
						.getPosition().getY() - camera.getyOffset() + 16, 500,
				50, 80, Arc2D.PIE);
		g2D.clip(flashLight);

		int tileY = 0;
		int tileX = 0;
		for (int i = row; i < row + 26; i++) {
			tileX = 0;
			for (int j = col; j < col + 34; j++) {
				if (j >= tileId[0].length || i >= tileId.length) {
					break;
				}
				g2D.setTransform(originalTransform);
				if ((tileId[j][i] & (1 << 12)) != 0
						&& ((tileId[j][i] & (1 << 13)) != 0)) {
					g2D.rotate(Math.toRadians(180), (int) (tileX
							* Assets.TILE_WIDTH - camera.getxOffset())
							+ xChange + 16, (int) (tileY * Assets.TILE_HEIGHT
							- camera.getyOffset() + yChange + 16));
				}

				else if ((tileId[j][i] & (1 << 12)) != 0) {
					g2D.rotate(Math.toRadians(90), (int) (tileX
							* Assets.TILE_WIDTH - camera.getxOffset())
							+ xChange + 16, (int) (tileY * Assets.TILE_HEIGHT
							- camera.getyOffset() + yChange + 16));
				} else if ((tileId[j][i] & (1 << 13)) != 0) {
					g2D.rotate(Math.toRadians(-90), (int) (tileX
							* Assets.TILE_WIDTH - camera.getxOffset())
							+ xChange + 16, (int) (tileY * Assets.TILE_HEIGHT
							- camera.getyOffset() + yChange + 16));
				}
				if ((tileId[j][i] & (1 << 14)) != 0) {
					solid[tileY][tileX] = new Rectangle((int) (tileX
							* Assets.TILE_WIDTH - camera.getxOffset())
							+ xChange - 3, (int) (tileY * Assets.TILE_HEIGHT
							- camera.getyOffset() + yChange - 2), 32, 32);

					 g2D.draw(solid[tileY][tileX]);
					 g2D.setTransform(originalTransform);
					 g2D.drawString(tileY + "," + tileX,
					 (int) solid[tileY][tileX].getX(),
					 (int) solid[tileY][tileX].getY());

				} else {
					solid[tileY][tileX] = null;
				}
				int id = (tileId[j][i] & 0xFFF);
				g.drawImage(game.getTiles()[(id / 100) - 1][(id % 100)],
						(int) (tileX * Assets.TILE_WIDTH - camera.getxOffset())
								+ xChange, (int) (tileY * Assets.TILE_HEIGHT
								- camera.getyOffset() + yChange), null);
				tileX++;
			}
			tileY++;
		}

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
		previousXOffset = camera.getxOffset();
		previousYOffset = camera.getyOffset();
		g2D.setTransform(originalTransform);

		// draw Zombies
		int chunkX = Math.max((int) player.getPosition().getX() / 512, 3);
		int chunkY = Math.max((int) player.getPosition().getY() / 512, 3);
		for (int x = chunkX - 3; x < chunkX + 4; x++) {
			for (int y = chunkY - 3; y < chunkY + 4; y++) {
//				for (Iterator<Zombie> iterator = chunkMap[x][y].getZombies()
//						.iterator(); iterator.hasNext();) {
//					Zombie zombie = iterator.next();
//					zombie.render(g);
//				}
				for (int i=0;i<chunkMap[x][y].getZombies().size();i++)
				{
					chunkMap[x][y].getZombies().get(i).render(g);
				}
				for (Iterator<Item> iterator = chunkMap[x][y].getItems()
						.iterator(); iterator.hasNext();) {
					Item item = iterator.next();
					item.render(g);
				}
			}
		}

		g2D.rotate(angle, player.getPosition().getX() - camera.getxOffset()
				+ 16, player.getPosition().getY() - camera.getyOffset() + 16);
		GradientPaint gp = new GradientPaint((float) player.getPosition()
				.getX() - camera.getxOffset() + 16, (float) player
				.getPosition().getY() - camera.getyOffset() + 16, new Color(0,
				0, 0, 0), (float) (player.getPosition().getX()
				- camera.getxOffset() + 350 * (float) Math.cos(Math
				.toRadians(90))), (float) (player.getPosition().getY()
				- camera.getyOffset() - 350 * (float) Math.sin(Math
				.toRadians(90))), new Color(0, 0, 0));
		g2D.setPaint(gp);
		g2D.fill(flashLight);
		g2D.draw(flashLight);

		g2D.setClip(null);
		player.render(g);

		g2D.setTransform(originalTransform);
		g2D.setColor(new Color(0f, 0f, 0f, .5f));
		g2D.fillRect(0, 0, game.getDisplay().getFrame().getWidth(), game
				.getDisplay().getFrame().getHeight());

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
		return solid;
	}

	private void initializeOffsets() {
		if ((camera.getyOffset() - yChange) >= renderControl) {
			yChange += camera.getyOffset() - 32;
		}
		if ((camera.getxOffset() - xChange) >= renderControl) {
			xChange += camera.getxOffset() - 32;
		}
	}

	public Item hoverItem() {
		int chunkX = Math.max((int) player.getPosition().getX() / 512, 2);
		int chunkY = Math.max((int) player.getPosition().getY() / 512, 2);
		for (int x = chunkX - 2; x < chunkX + 3; x++) {
			for (int y = chunkY - 2; y < chunkY + 3; y++) {
				for (ListIterator<Item> iterator = chunkMap[x][y].getItems()
						.listIterator(chunkMap[x][y].getItems().size()); iterator
						.hasPrevious();) {
					Item item = iterator.previous();
					Rectangle itemHitbox = new Rectangle(
							(int) (item.getPosition().x - camera.getxOffset()),
							(int) (item.getPosition().y - camera.getyOffset()) + 32,
							32, 32);
					if (itemHitbox.contains(mouse.getMouseLocation())
							&& Math.sqrt(Math.pow(
									(player.getPosition().x - item
											.getPosition().x), 2)
									+ Math.pow((player.getPosition().y - item
											.getPosition().y), 2)) <= 8 * 32) {
						item.setHover(true);
						return item;
					} else if (itemHitbox
							.intersects(new Rectangle((int) (player
									.getPosition().x - camera.getxOffset()),
									(int) (player.getPosition().y - camera
											.getyOffset()) + 32, 32, 32))) {
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

	public Item getHoverItem() {
		return hoverItem;
	}
}
