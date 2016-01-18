package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;

import enums.GameState.State;
import enums.ItemState;
import items.Firearm;
import items.Item;
import items.Melee;
import items.Throwable;
import main.Game;
import main.World;
import map.Map;
import utilities.Assets;
import utilities.GameCamera;
import utilities.KeyHandler;
import utilities.MouseHandler;

/**
 * Subclass of Mob that represents a player in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Mob
 * @since 1.0
 * @version 1.0
 */
public class Player extends Mob {
	public static final int MOVEMENT_SPEED = 2;
	public static final int MAX_STAMINA = 300;
	public static final int MIN_STAMINA = MAX_STAMINA / 10;
	public static final int SPRINT_COST = Player.MAX_STAMINA / 300;
	private MouseHandler mouse;
	private GameCamera camera;
	private KeyHandler key;
	private World world;
	private Rectangle hitbox;
	private boolean exhausted = false;
	private int stamina;
	private long lastDamageTick = -60;
	private short[][] tiles;

	private int selectedItemNumber = 0;
	private Item selectedItem;
	private int skinNo;

	private long lastItemTick = -60;

	private boolean swinging;
	private long swingTick;
	private double swingAngle;
	private double swingAngleRange;

	private boolean shoot;

	public Player(Point position, boolean solid, Game game, Map map, int skinNo) {
		super(32, 32, position, solid, game, map);
		this.skinNo = skinNo;
		this.movementSpeed = Player.MOVEMENT_SPEED;
		this.stamina = Player.MAX_STAMINA;

		this.mouse = game.getDisplay().getMouseHandler();
		this.camera = game.getCamera();
		this.key = game.getDisplay().getKeyHandler();
		this.world = game.getDisplay().getGamePanel().getWorld();

		this.addItem(this.game.getItem(200));
		this.addItem(this.game.getItem(300));
		this.selectedItem = this.getItem(selectedItemNumber);
	}

	/**
	 * Gets the player's center coordinate relative to the game camera.
	 * 
	 * @return the player's center coordinate.
	 */
	public Point getPlayerCenter() {
		return new Point((int) (this.getPosition().x - camera.getxOffset() + 16),
				(int) (this.getPosition().y - camera.getyOffset()) + 16);
	}

	public int getStamina() {
		return this.stamina;
	}

	@Override
	public void damage(int health) {
		long currentTick = this.game.getTickCount();
		if (currentTick - lastDamageTick > 60) {
			super.damage(health);
			this.lastDamageTick = currentTick;
		}
		if (this.getHealth() <= 0) {
			game.getState().setGameState(State.DEATH, false);
		}
	}

	public int getSelectedItem() {
		return selectedItemNumber;
	}

	public void pickUpItem() {
		Item hoverItem = this.game.getDisplay().getGamePanel().getWorld().getHoverItem();

		if (hoverItem != null && !isFull() && Point.distance(this.position.x, this.position.y,
				hoverItem.getPosition().x, hoverItem.getPosition().y) <= 1 * 32) {
			hoverItem.setState(ItemState.INVENTORY);
			this.getChunkMap()[hoverItem.getPosition().x / 512][hoverItem.getPosition().y / 512].remove(hoverItem);
			addItem(hoverItem);
		}
	}

	public void useItem() {
		Item item = getItem(this.selectedItemNumber);

		if (item != null) {
			item.use(this);
		}
	}

	public long getLastItemTick() {
		return lastItemTick;
	}

	public void setLastItemTick(long lastItemTick) {
		this.lastItemTick = lastItemTick;
	}

	public void swing(long tick, double angle, double angleRange) {
		this.swinging = true;
		this.swingTick = tick;
		this.swingAngle = angle;
		this.swingAngleRange = angleRange;
	}

	public void shoot() {
		this.shoot = true;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;

		AffineTransform originalTransform = g2D.getTransform();

		double angle = Math.atan2(((position.getY()) + 16 - camera.getyOffset()) - mouse.getMouseLocation().getY(),
				(position.getX() + 16 - camera.getxOffset()) - mouse.getMouseLocation().getX()) - Math.PI / 2;

		this.selectedItem = this.getItem(selectedItemNumber);
		if (selectedItem != null) {
			if (selectedItem instanceof Melee && this.swinging) {
				long difference = this.game.getTickCount() - this.swingTick;
				if (difference <= ((Melee) selectedItem).getSwingSpeed()) {
					// TODO lol
					g2D.rotate(
							this.swingAngle - this.swingAngleRange
									+ (this.swingAngleRange * 2 / difference * ((Melee) selectedItem).getSwingSpeed()
											* 1.0),
							position.getX() - camera.getxOffset() + 16, position.getY() - camera.getyOffset() + 16);
					g2D.drawImage(selectedItem.getImages()[0], (int) (this.getPosition().x - camera.getxOffset() + 10),
							(int) (this.getPosition().y - camera.getyOffset() - 10), null);
				} else {
					this.swinging = false;
					g2D.rotate(angle, position.getX() - camera.getxOffset() + 16,
							position.getY() - camera.getyOffset() + 16);
					g2D.drawImage(selectedItem.getImages()[0], (int) (this.getPosition().x - camera.getxOffset() + 10),
							(int) (this.getPosition().y - camera.getyOffset() - 10), null);
				}
			} else {
				g2D.rotate(angle, position.getX() - camera.getxOffset() + 16,
						position.getY() - camera.getyOffset() + 16);
				if (selectedItem instanceof Firearm) {
					g2D.drawImage(this.getItem(selectedItemNumber).getImages()[2],
							(int) (this.getPosition().x - camera.getxOffset() + 10),
							(int) (this.getPosition().y - camera.getyOffset() - 10), null);
					if (this.shoot) {
						g2D.setColor(new Color(255, 255, 0));
						g2D.fillOval((int) (this.getPosition().x - camera.getxOffset() + 25),
								(int) (this.getPosition().y - camera.getyOffset() - 18), 6, 12);
						this.shoot = false;
					}
				} else {
					g2D.drawImage(this.getItem(selectedItemNumber).getImages()[0],
							(int) (this.getPosition().x - camera.getxOffset() + 10),
							(int) (this.getPosition().y - camera.getyOffset() - 10), null);
				}
			}
		} else {
			g2D.rotate(angle, position.getX() - camera.getxOffset() + 16, position.getY() - camera.getyOffset() + 16);
		}

		g2D.drawImage(this.getImages()[skinNo], (int) (this.getPosition().x - camera.getxOffset()),
				(int) (this.getPosition().y - camera.getyOffset()), null);

		g2D.setTransform(originalTransform);

		if (this.getItem(selectedItemNumber) != null && selectedItem instanceof Throwable) {
			Throwable throwable = ((Throwable) selectedItem);
			g2D.drawOval((int) (this.getPlayerCenter().x - throwable.getRange()),
					(int) (this.getPlayerCenter().y - throwable.getRange()), 2 * throwable.getRange(),
					2 * throwable.getRange());
		}
	}

	public void update() {
		if (key.isEsc()) {
			game.getState().setGameState(State.PAUSE, false);
			key.setEsc(false);
		}
		world = game.getDisplay().getGamePanel().getWorld();
		if (this.stamina < Player.SPRINT_COST) {
			this.exhausted = true;
		} else if (this.stamina > Player.MIN_STAMINA) {
			this.exhausted = false;
		}

		if (mouse.isClick()) {
			useItem();
			mouse.setClick(false);
		}

		if (key.isQ()) {
			dropItem(this.selectedItemNumber);
			key.setQ(false);
		}

		if (key.isE()) {
			pickUpItem();
			key.setE(false);
		}
		if (key.isR()) {
			Item item = getItem(this.selectedItemNumber);
			if (item != null && item instanceof Firearm) {
				((Firearm) item).reload(this);
			}
		}
		if (key.isShift() && !exhausted && (key.isUp() || key.isDown() || key.isLeft() || key.isRight())) {

			this.movementSpeed = Player.MOVEMENT_SPEED * 2;
			this.stamina -= Player.SPRINT_COST;
		} else if (key.isCtrl()) {
			this.movementSpeed = Player.MOVEMENT_SPEED / 2;
			if (this.stamina < Player.MAX_STAMINA) {
				this.stamina++;
			}
		} else {
			this.movementSpeed = Player.MOVEMENT_SPEED;
			if (this.stamina < Player.MAX_STAMINA) {
				this.stamina++;
			}
		}

		key.setLastNumber(key.getLastNumber() + mouse.getMouseWheel());
		mouse.setMouseWheel(0);

		if (this.selectedItemNumber != key.getLastNumber()) {
			if (getItem(selectedItemNumber) != null) {
				this.selectedItem = getItem(this.selectedItemNumber);

			}
			this.selectedItemNumber = key.getLastNumber();
			if (getItem(selectedItemNumber) != null) {
				this.selectedItem = getItem(this.selectedItemNumber);
			}
		}

		this.getPosition().setLocation(this.getPosition().getX(), this.getPosition().getY() + yMove());
		this.getPosition().setLocation(this.getPosition().getX() + xMove(), this.getPosition().getY());
		int row = (int) ((((this.getPosition().y - camera.getyOffset()) / 32.0)));
		int col = (int) ((this.getPosition().x - camera.getxOffset()) / 32);
		if (world.getSolid()[row][col] != null && yMove() == 0 && xMove() == 0 && key.isRight() && key.isLeft()
				&& key.isUp() && key.isDown()) {
			if (hitbox.intersects(world.getSolid()[row][col])) {
				System.out.println("Shit");
				this.getPosition().setLocation(this.getPosition().getX(), this.getPosition().getY() + 32);
			}
		}
		if (position.getX() < 0)
			position.setLocation(0, position.getY());
		else if (position.getX() > Assets.TILE_WIDTH * (world.getWidth() - 1))
			position.setLocation(Assets.TILE_WIDTH * (world.getWidth() - 1), position.getY());
		if (position.getY() < 0)
			position.setLocation(position.getX(), 0);
		else if (position.getY() + 32 > Assets.TILE_HEIGHT * (world.getHeight() - 1))
			position.setLocation(position.getX(), Assets.TILE_HEIGHT * (world.getHeight() - 1) - 32);

		this.game.getCamera().centerOnEntity(this);
		if (key.isUp() || key.isDown() || key.isRight() || key.isLeft()) {
			if (key.isShift()) {
				makeNoise(300, true);
			} else if (key.isCtrl()) {
				makeNoise(100, true);
			} else {
				makeNoise(200, true);
			}
		}

		if (world.getFlag() != null) {
			if (world.getFlag().intersects(hitbox)) {
				game.getState().setGameState(State.FINISH, false);
			}
		}
	}

	private int xMove() {
		int xMove = 0;
		if (key.isLeft()) {
			xMove = -this.movementSpeed;
		}
		if (key.isRight()) {
			xMove = this.movementSpeed;
		}
		hitbox = new Rectangle((int) (this.position.getX() - camera.getxOffset()) + xMove,
				(int) (this.position.getY() - camera.getyOffset()), Assets.TILE_WIDTH, Assets.TILE_HEIGHT);
		int row = (int) ((((this.getPosition().y - camera.getyOffset()) / 32.0)));
		int col = (int) ((this.getPosition().x - camera.getxOffset()) / 32);
		if (xMove > 0) {// Moving right
			if (row != 0)
				row -= 1;
			for (int i = row; i < world.getSolid().length; i++) {
				for (int j = col; j < world.getSolid()[0].length; j++) {
					if (world.getSolid()[i][j] != null) {
						if (hitbox.intersects(world.getSolid()[i][j])) {
							return 0;
						}
					}
				}
			}
		} else if (xMove < 0) {// Moving Left
			if (row != 0)
				row -= 1;
			for (int i = row; i < world.getSolid().length; i++) {
				for (int j = col; j >= 0; j--) {
					if (world.getSolid()[i][j] != null) {
						if (hitbox.intersects(world.getSolid()[i][j])) {
							return 0;
						}
					}
				}
			}
		}
		return xMove;
	}

	private int yMove() {
		int yMove = 0;
		if (this.game.getDisplay().getKeyHandler().isUp()) {
			yMove = -this.movementSpeed;
		}
		if (this.game.getDisplay().getKeyHandler().isDown()) {
			yMove = this.movementSpeed;
		}
		hitbox = new Rectangle((int) (this.position.getX() - camera.getxOffset()),
				(int) (this.position.getY() - camera.getyOffset() + yMove), Assets.TILE_WIDTH, Assets.TILE_HEIGHT);
		int row = (int) (((this.getPosition().y - camera.getyOffset()) / 32));
		int col = (int) (((this.getPosition().x - camera.getxOffset()) / 32.0));
		if (yMove < 0) {// Moving up
			if (col != 0)
				col -= 1;
			for (int i = row; i >= 0; i--) {
				for (int j = col; j < world.getSolid()[0].length; j++) {
					if (world.getSolid()[i][j] != null) {
						if (hitbox.intersects(world.getSolid()[i][j])) {
							return 0;
						}
					}
				}
			}
		} else if (yMove > 0) {// Moving down
			if (col != 0)
				col -= 1;
			for (int i = row; i < world.getSolid().length; i++) {
				for (int j = col; j < world.getSolid()[0].length; j++) {
					if (world.getSolid()[i][j] != null) {
						if (hitbox.intersects(world.getSolid()[i][j])) {
							return 0;
						}
					}
				}
			}
		}
		return yMove;
	}

	public Point calculatePointOfImpact(Line2D.Double line) {
		if (tiles == null)
			this.tiles = this.world.getMap().getMap();

		double slope = (line.y2 - line.y1) / (line.x2 - line.x1);

		if (line.x2 > line.x1)
			for (int i = 0; i < Math.abs(line.x2 - line.x1); i++) {
				int tileX = (this.position.x + i) / 32;
				int tileY = ((int) (this.position.y + (i * slope))) / 32;
				if (!(tileX < 0 || tileY < 0 || tileX > (tiles.length - 1) || tileY > (tiles[0].length - 1)))
					if ((tiles[tileX][tileY] & (1 << 14)) != 0) {
						int hitX = this.position.x + i;
						int hitY = (int) (this.position.y + (i * slope));
						if (hitY > line.getY1())
							hitY -= 32;
						if (hitX > line.getX1())
							hitX -= 32;
						return new Point(hitX, hitY);
					}
			}
		else
			for (int i = 0; i > -Math.abs(line.x2 - line.x1); i--) {
				int tileX = (this.position.x + i) / 32;
				int tileY = ((int) (this.position.y + (i * slope))) / 32;
				if (!(tileX < 0 || tileY < 0 || tileX > (tiles.length - 1) || tileY > (tiles[0].length - 1)))
					if ((tiles[(this.position.x + i) / 32][((int) (this.position.y + (i * slope))) / 32]

							& (1 << 14)) != 0) {
						int hitX = this.position.x + i;
						int hitY = (int) (this.position.y + (i * slope));
						if (hitY > line.getY1())
							hitY -= 32;
						if (hitX > line.getX1())
							hitX -= 32;
						return new Point(hitX, hitY);
					}
			}
		return new Point((int) line.x2, (int) line.y2);
	}

	public Entity projectileTracer(Line2D.Double line, int damage, int range) {
		ArrayList<Entity> entitiesCollided = new ArrayList<Entity>();
		ArrayList<Zombie> zombiesCollided = new ArrayList<Zombie>();

		int chunkX = Math.max(this.position.x / 512, 3);
		int chunkY = Math.max(this.position.y / 512, 3);
		for (int x = chunkX - 3; x < Math.min(chunkX + 4, map.getWidth() / 16); x++) {
			for (int y = chunkY - 3; y < Math.min(chunkY + 4, map.getWidth() / 16); y++) {
				ArrayList<Zombie> zombies = getChunkMap()[x][y].getZombies();
				for (int zombie = 0; zombie < zombies.size(); zombie++) {
					Zombie currentZombie = zombies.get(zombie);
					if (line.intersects(currentZombie.getPosition().x, currentZombie.getPosition().y, 32, 32)) {
						zombiesCollided.add(currentZombie);
					}
				}

				ArrayList<Entity> entities = getChunkMap()[x][y].getSolidEntities();
				for (int entity = 0; entity < entities.size(); entity++) {
					Entity currentEntity = entities.get(entity);
					if (line.intersects(currentEntity.getPosition().x, currentEntity.getPosition().y, 32, 32)) {
						entitiesCollided.add(currentEntity);
					}
				}
			}
		}

		Zombie closestZombie = null;
		double maxDistance = range;

		if (tiles == null)
			this.tiles = this.world.getMap().getMap();

		double slope = (line.y2 - line.y1) / (line.x2 - line.x1);

		if (line.x2 > line.x1)
			for (int i = 0; i < 1024; i++) {
				int tileX = (this.position.x + i) / 32;
				int tileY = ((int) (this.position.y + (i * slope))) / 32;
				if (!(tileX < 0 || tileY < 0 || tileX > (tiles.length - 1) || tileY > (tiles[0].length - 1)))
					if ((tiles[tileX][tileY] & (1 << 14)) != 0) {
						maxDistance = (Math.sqrt(Math.pow(i, 2) + Math.pow((i * slope), 2)));
						break;
					}
			}
		else
			for (int i = 0; i > -1024; i--) {
				int tileX = (this.position.x + i) / 32;
				int tileY = ((int) (this.position.y + (i * slope))) / 32;
				if (!(tileX < 0 || tileY < 0 || tileX > (tiles.length - 1) || tileY > (tiles[0].length - 1)))
					if ((tiles[(this.position.x + i) / 32][((int) (this.position.y + (i * slope))) / 32]

							& (1 << 14)) != 0) {
						maxDistance = (Math.sqrt(Math.pow(i, 2) + Math.pow((i * slope), 2)));
						break;
					}
			}

		for (Iterator<Zombie> iterator = zombiesCollided.iterator(); iterator.hasNext();) {
			Zombie zombie = iterator.next();

			double distance = Point.distance(this.position.x, this.position.y, zombie.getPosition().x,
					zombie.getPosition().y);

			if (distance < maxDistance) {
				maxDistance = distance;
				closestZombie = zombie;
			}
		}

		Entity closestEntity = null;
		double entityDistance = 100 * 32;

		for (Iterator<Entity> iterator = entitiesCollided.iterator(); iterator.hasNext();) {

			Entity entity = iterator.next();

			double distance = Point.distance(this.position.x, this.position.y, entity.getPosition().x,
					entity.getPosition().y);

			if (distance < entityDistance) {
				entityDistance = distance;
				closestEntity = entity;
			}
		}

		if (entityDistance > maxDistance) {
			closestEntity = closestZombie;
		}

		if (closestEntity != null) {
			closestEntity.damage(damage);
		}

		return closestEntity;
	}

	public int meleeCollision(Arc2D arc, int damage) {
		int noOfEnemies = 0;

		int chunkX = Math.max(this.position.x / 512, 1);
		int chunkY = Math.max(this.position.y / 512, 1);
		for (int x = chunkX - 1; x < Math.min(chunkX + 2, map.getWidth() / 16); x++) {
			for (int y = chunkY - 1; y < Math.min(chunkY + 2, map.getWidth() / 16); y++) {
				ArrayList<Zombie> zombies = getChunkMap()[x][y].getZombies();
				for (int zombie = 0; zombie < zombies.size(); zombie++) {
					Zombie currentZombie = zombies.get(zombie);
					if (arc.intersects(currentZombie.getPosition().x, currentZombie.getPosition().y, 32, 32)) {
						currentZombie.damage(damage);
						noOfEnemies++;
					}
				}

				ArrayList<Entity> entities = getChunkMap()[x][y].getSolidEntities();
				for (int entity = 0; entity < entities.size(); entity++) {
					Entity currentEntity = entities.get(entity);
					if (arc.intersects(currentEntity.getPosition().x, currentEntity.getPosition().y, 32, 32)) {
						currentEntity.damage(damage);
						noOfEnemies++;
					}
				}
			}
		}

		return noOfEnemies;

	}

	public MouseHandler getMouse() {
		return mouse;
	}
}