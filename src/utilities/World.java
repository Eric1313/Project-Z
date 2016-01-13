package utilities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.io.FileNotFoundException;
import java.util.Iterator;

import main.Game;
import map.Chunk;
import map.Map;
import entities.Player;
import entities.Zombie;
import entities.ZombieThread;

public class World {
	private Game game;
	private Player player;
	Arc2D flashLight;
	private AffineTransform originalTransform;
	private short[][] tileId;
	private Chunk[][] chunkMap; 
	private Map map;
	private int width;
	private int height;

	// Controls what is being rendered
	private int row;
	private int col;
	private final int renderControl = 32;
	private float previousXOffset;
	private float previousYOffset;
	private int yChange;
	private int xChange;

	public World(Game game, int width, int height) {
		this.width = width;
		this.height = height;
		this.game = game;
		try {
			map = new Map(width, height,this.game);
			chunkMap=map.getChunkMap();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		tileId = map.getMap();
		// TODO Randomly place the player into the world rather than putting it
		// in the top left corner
		player = new Player(new Point(0, 0), true, game,map);
		player.setImages(game.getPlayer()[0]);
		this.row = 0;
		this.col = 0;
		this.yChange = 0;
		this.xChange = 0;
	}

	public void render(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		if (originalTransform == null) {
			originalTransform = g2D.getTransform();
		}
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
					g2D.rotate(
							Math.toRadians(180),
							(int) (tileX * Assets.TILE_WIDTH - game.getCamera()
									.getxOffset()) + xChange + 13,
							(int) (tileY * Assets.TILE_HEIGHT
									- game.getCamera().getyOffset() + yChange + 14));
				}

				else if ((tileId[j][i] & (1 << 12)) != 0) {
					g2D.rotate(
							Math.toRadians(90),
							(int) (tileX * Assets.TILE_WIDTH - game.getCamera()
									.getxOffset()) + xChange + 13,
							(int) (tileY * Assets.TILE_HEIGHT
									- game.getCamera().getyOffset() + yChange + 14));
				} else if ((tileId[j][i] & (1 << 13)) != 0) {
					g2D.rotate(
							Math.toRadians(-90),
							(int) (tileX * Assets.TILE_WIDTH - game.getCamera()
									.getxOffset()) + xChange + 13,
							(int) (tileY * Assets.TILE_HEIGHT
									- game.getCamera().getyOffset() + yChange + 14));
				}
				int id = (tileId[j][i] & 0xFFF);

				g.drawImage(game.getTiles()[(id / 100) - 1][(id % 100)],
						(int) (tileX * Assets.TILE_WIDTH - game.getCamera()
								.getxOffset()) + xChange - 3, (int) (tileY
								* Assets.TILE_HEIGHT
								- game.getCamera().getyOffset() + yChange - 2),
						null);
				tileX++;
			}
			tileY++;
		}

		if (previousXOffset < game.getCamera().getxOffset()) {
			if (xChange < 0) {
				xChange = -xChange;
			} else {
				if ((game.getCamera().getxOffset() - xChange) >= renderControl) {
					col++;
					xChange += renderControl;
				}
			}
		} else if (previousXOffset > game.getCamera().getxOffset()) {
			if (xChange > 0) {
				if ((game.getCamera().getxOffset() - xChange) <= -(renderControl - 31)) {
					col--;
					xChange -= renderControl;
				}
			}
		}
		if (previousYOffset < game.getCamera().getyOffset()) {
			if (yChange < 0) {
				yChange = -yChange;
			} else {
				if ((game.getCamera().getyOffset() - yChange) >= renderControl) {
					row++;
					yChange += renderControl;
				}
			}
		} else if (previousYOffset > game.getCamera().getyOffset()) {
			if (yChange > 0) {

				// System.out.println(game.getCamera().getyOffset() +
				// yChange);
				if ((game.getCamera().getyOffset() - yChange) <= -(renderControl - 31)) {
					row--;
					yChange -= renderControl;
				}
			}

		}
		previousXOffset = game.getCamera().getxOffset();
		previousYOffset = game.getCamera().getyOffset();
		g2D.setTransform(originalTransform);
		double angle = Math.atan2(
				((player.getPosition().getY()) + 16 - game.getCamera()
						.getyOffset())
						- game.getDisplay().getMouseHandler()
								.getMouseLocation().getY(), (player
						.getPosition().getX() + 16 - game.getCamera()
						.getxOffset())
						- game.getDisplay().getMouseHandler()
								.getMouseLocation().getX())
				- Math.PI / 2;
		g2D.rotate(angle, player.getPosition().getX()
				- game.getCamera().getxOffset() + 16, player.getPosition()
				.getY() - game.getCamera().getyOffset() + 16);
		player.render(g);
		flashLight = new Arc2D.Double(player.getPosition().getX() - 184, player
				.getPosition().getY() - 190, 400, 400, 40, 140, Arc2D.PIE);
		g2D.clip(flashLight);
		g2D.setTransform(originalTransform);
		g2D.setColor(new Color(0f, 0f, 0f, .8f));
		g2D.fillRect(0, 0, game.getDisplay().getFrame().getWidth(), game
				.getDisplay().getFrame().getHeight());
		g2D.fill(flashLight);
		g2D.draw(flashLight);
		g2D.setClip(null);

		int chunkX =  Math.max((int)player.getPosition().getX() / 512,2);
		int chunkY =  Math.max((int)player.getPosition().getY() / 512,2);
		for (int x = chunkX - 2; x < chunkX + 3; x++) {
			for (int y = chunkY - 2; y < chunkY + 3; y++) {
				for (Iterator<Zombie> iterator = chunkMap[x][y].getZombies().iterator(); iterator
						.hasNext();) {
					Zombie zombie = iterator.next();
					zombie.render(g);
				}
			}
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
}