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
import java.util.PriorityQueue;

import enums.GameState.State;
import enums.ItemState;
import items.Firearm;
import items.Item;
import items.Melee;
import items.Throwable;
import main.Game;
import map.Map;
import map.World;
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

	private int stamina;
	private boolean exhausted;
	private int selectedItemNumber = 0;
	private Item selectedItem;
	private int skinNo;
	private Rectangle hitbox;

	private World world;
	private short[][] tiles;
	private GameCamera camera;
	private KeyHandler key;
	private MouseHandler mouse;

	/**
	 * Checks the last time the player was damaged in order to give the player
	 * invincibility frames.
	 */
	private long lastDamageTick = -60;

	/**
	 * Checks the last time the player used an item in order to create a delay
	 * between using items.
	 */
	private long lastItemTick = -60;

	private boolean swinging;
	private boolean shooting;

	/**
	 * Stores the tick that the player started swinging a melee weapon.
	 */
	private long swingTick;
	private double swingAngle;
	private double swingAngleRange;

	/**
	 * Constructs a new Player object.
	 * 
	 * @param position
	 *            the coordinates of the player in the map in terms of pixels.
	 * @param inventory
	 *            the starting inventory of the player. If null is entered, the
	 *            player will spawn with a default inventory.
	 * @param solid
	 *            whether or not the player is solid.
	 * @param game
	 *            the game to add the entity to.
	 * @param map
	 *            the map to add the entity to.
	 * @param skinNo
	 *            a number representing the player's skin.
	 */
	public Player(Point position, Inventory inventory, boolean solid,
			Game game, Map map, int skinNo) {
		super(32, 32, position, solid, game, map);

		if (inventory != null) {
			this.inventory = inventory;
		} else {
			this.addItem(this.game.getItem(200));
			this.addItem(this.game.getItem(300));
			this.addItem(this.game.getItem(304));
			this.addItem(this.game.getItem(400));
			this.addItem(this.game.getItem(303));
			this.addItem(this.game.getItem(302));
			this.addItem(this.game.getItem(103));
			this.addItem(this.game.getItem(103));

		}

		this.skinNo = skinNo;
		this.movementSpeed = Player.MOVEMENT_SPEED;
		this.stamina = Player.MAX_STAMINA;

		this.world = this.game.getDisplay().getGamePanel().getWorld();
		this.camera = this.game.getCamera();
		this.key = this.game.getDisplay().getKeyHandler();
		this.mouse = this.game.getDisplay().getMouseHandler();

		this.selectedItem = this.getItem(this.selectedItemNumber);
	}

	/**
	 * Gets the player's center coordinate relative to the game camera.
	 * 
	 * @return the player's center coordinate.
	 */
	public Point getPlayerCenter() {
		return new Point(
				(int) (this.getPosition().getX() - camera.getxOffset() + this.width / 2),
				(int) (this.getPosition().getY() - camera.getyOffset())
						+ this.height / 2);
	}

	@Override
	public void damage(int health) {
		// Check if the 60 ticks (1 second) have passed since being damaged last
		// If enough ticks have passed, damage the player
		long currentTick = this.game.getTickCount();
		if (currentTick - lastDamageTick > 60) {
			super.damage(health);
			this.lastDamageTick = currentTick;
		}

		// If the player's health reaches zero, change the game state to the
		// game over screen
		if (this.health <= 0) {
			this.game.getState().setGameState(State.DEATH, false);
		}
	}

	/**
	 * Attempts to pick up an item that the player is touching.
	 */
	public void pickUpItem() {
		// Get the item that's being hovered over
		Item hoverItem = this.game.getDisplay().getGamePanel().getWorld()
				.getHoverItem();

		// Check if the player's inventory is not full
		// Make sure that the player is close enough to the item (32 pixels)
		if (hoverItem != null
				&& !this.isFull()
				&& Point.distance(this.position.getX(), this.position.getY(),
						hoverItem.getPosition().getX(), hoverItem.getPosition()
								.getY()) <= 32) {
			// Remove the item from the chunk that the item was in
			// Add the item to the player's inventory
			hoverItem.setState(ItemState.INVENTORY);
			this.chunkMap[hoverItem.getPosition().x / 512][hoverItem
					.getPosition().y / 512].remove(hoverItem);
			this.addItem(hoverItem);
		}
	}

	/**
	 * Uses the player's current item
	 */
	public void useItem() {
		Item item = getItem(this.selectedItemNumber);

		if (item != null) {
			item.use(this);
		}
	}

	/**
	 * Swings the player's melee weapon.
	 * 
	 * @param tick
	 *            the tick of when the player started to swing the weapon.
	 * @param angle
	 *            the middle angle of the swing (in radians).
	 * @param angleRange
	 *            the angle range on both sides of the swing (in radians).
	 */
	public void swing(long tick, double angle, double angleRange) {
		this.swinging = true;
		this.swingTick = tick;
		this.swingAngle = angle;
		this.swingAngleRange = angleRange;
	}

	/**
	 * Shoots the player's firearm weapon.
	 */
	public void shoot() {
		this.shooting = true;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;

		AffineTransform originalTransform = g2D.getTransform();

		double angle = Math.atan2(
				((position.getY()) + 16 - camera.getyOffset())
						- mouse.getMouseLocation().getY(),
				(position.getX() + 16 - camera.getxOffset())
						- mouse.getMouseLocation().getX())
				- Math.PI / 2;

		this.selectedItem = this.getItem(selectedItemNumber);
		if (selectedItem != null) {
			if (selectedItem instanceof Melee && this.swinging) {
				long difference = this.game.getTickCount() - this.swingTick;
				if (difference <= ((Melee) selectedItem).getSwingSpeed()) {
					// TODO lol
					g2D.rotate(
							this.swingAngle
									- this.swingAngleRange
									+ (this.swingAngleRange
											* 2
											/ difference
											* ((Melee) selectedItem)
													.getSwingSpeed() * 1.0),
							position.getX() - camera.getxOffset() + 16,
							position.getY() - camera.getyOffset() + 16);
					g2D.drawImage(
							selectedItem.getImages()[0],
							(int) (this.getPosition().x - camera.getxOffset() + 10),
							(int) (this.getPosition().y - camera.getyOffset() - 10),
							null);
				} else {
					this.swinging = false;
					g2D.rotate(angle, position.getX() - camera.getxOffset()
							+ 16, position.getY() - camera.getyOffset() + 16);
					g2D.drawImage(
							selectedItem.getImages()[0],
							(int) (this.getPosition().x - camera.getxOffset() + 10),
							(int) (this.getPosition().y - camera.getyOffset() - 10),
							null);
				}
			} else {
				g2D.rotate(angle, position.getX() - camera.getxOffset() + 16,
						position.getY() - camera.getyOffset() + 16);
				if (selectedItem instanceof Firearm) {
					g2D.drawImage(
							this.getItem(selectedItemNumber).getImages()[2],
							(int) (this.getPosition().x - camera.getxOffset() + 10),
							(int) (this.getPosition().y - camera.getyOffset() - 10),
							null);
					if (this.shooting) {
						g2D.setColor(new Color(255, 255, 0));
						g2D.fillOval(
								(int) (this.getPosition().x
										- camera.getxOffset() + 25),
								(int) (this.getPosition().y
										- camera.getyOffset() - 18), 6, 12);
						this.shooting = false;
					}
				} else {
					g2D.drawImage(
							this.getItem(selectedItemNumber).getImages()[0],
							(int) (this.getPosition().x - camera.getxOffset() + 10),
							(int) (this.getPosition().y - camera.getyOffset() - 10),
							null);
				}
			}
		} else {
			g2D.rotate(angle, position.getX() - camera.getxOffset() + 16,
					position.getY() - camera.getyOffset() + 16);
		}

		g2D.drawImage(this.getImages()[skinNo],
				(int) (this.getPosition().x - camera.getxOffset()),
				(int) (this.getPosition().y - camera.getyOffset()), null);

		g2D.setTransform(originalTransform);

		if (this.getItem(selectedItemNumber) != null
				&& selectedItem instanceof Throwable) {
			Throwable throwable = ((Throwable) selectedItem);
			g2D.drawOval(
					(int) (this.getPlayerCenter().x - throwable.getRange()),
					(int) (this.getPlayerCenter().y - throwable.getRange()),
					2 * throwable.getRange(), 2 * throwable.getRange());
		}
	}

	public void update() {
		this.world = this.game.getDisplay().getGamePanel().getWorld();

		// If the player presses 'ESC', pause the game
		if (this.key.isEsc()) {
			this.game.getState().setGameState(State.PAUSE, false);
			this.key.setEsc(false);
		}

		// If the player presses 'Q', drop their selected item
		if (this.key.isQ()) {
			this.dropItem(this.selectedItemNumber);
			this.key.setQ(false);
		}

		// If the player presses 'E', try to pick up an item
		if (this.key.isE()) {
			this.pickUpItem();
			this.key.setE(false);
		}

		// If the player presses 'R', try to reload their gun
		if (this.key.isR()) {
			Item item = this.getItem(this.selectedItemNumber);
			if (item != null && item instanceof Firearm) {
				((Firearm) item).reload(this);
			}
		}

		// If the player clicks with their mouse, use their selected item
		if (this.mouse.isClick()) {
			this.useItem();
			if ((this.selectedItem instanceof Firearm) && !((Firearm) this.selectedItem).isAutomatic()) {
				this.mouse.setClick(false);
			}
		}

		// If the player has less stamina than what is required to sprint, the
		// player is exhausted
		// Otherwise, if the player reaches the minimum stamina (not required
		// amount), then the player is no longer exhausted
		if (this.stamina < Player.SPRINT_COST) {
			this.exhausted = true;
		} else if (this.stamina > Player.MIN_STAMINA) {
			this.exhausted = false;
		}

		// If the player is sprinting, double their movement speed and drain
		// their stamina
		// If the player is sneaking, halve their movement speed
		// As long as the player is not sprinting, regenerate their stamina by 1
		// until it is full
		if (this.key.isShift()
				&& !this.exhausted
				&& (this.key.isUp() || this.key.isDown() || this.key.isLeft() || this.key
						.isRight())) {
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

		// Update the key handler's last number with the mouse wheel
		// Scrolling down increases the selected item number and loops back
		// around
		// Reset the mouse handler's mouse wheel
		this.key.setLastNumber(this.key.getLastNumber()
				+ this.mouse.getMouseWheel());
		this.mouse.setMouseWheel(0);

		// If the selected item number has change, update the player's selected
		// item
		if (this.selectedItemNumber != this.key.getLastNumber()) {
			this.selectedItemNumber = this.key.getLastNumber();
			this.selectedItem = this.getItem(this.selectedItemNumber);
		}

		this.getPosition().setLocation(this.getPosition().getX(),
				this.getPosition().getY() + yMove());
		this.getPosition().setLocation(this.getPosition().getX() + xMove(),
				this.getPosition().getY());
		int row = (int) ((((this.getPosition().y - camera.getyOffset()) / 32.0)));
		int col = (int) ((this.getPosition().x - camera.getxOffset()) / 32);
		if (world.getSolid()[row][col] != null && yMove() == 0 && xMove() == 0
				&& key.isRight() && key.isLeft() && key.isUp() && key.isDown()) {
			if (hitbox.intersects(world.getSolid()[row][col])) {
				this.getPosition().setLocation(this.getPosition().getX(),
						this.getPosition().getY() + 32);
			}
		}
		if (position.getX() < 0)
			position.setLocation(0, position.getY());
		else if (position.getX() > Assets.TILE_WIDTH * (world.getWidth() - 1))
			position.setLocation(Assets.TILE_WIDTH * (world.getWidth() - 1),
					position.getY());
		if (position.getY() < 0)
			position.setLocation(position.getX(), 0);
		else if (position.getY() + 32 > Assets.TILE_HEIGHT
				* (world.getHeight() - 1))
			position.setLocation(position.getX(),
					Assets.TILE_HEIGHT * (world.getHeight() - 1) - 32);

		this.game.getCamera().centerOnEntity(this);

		// If the player is moving, make noise of 100px/200px/300px radius
		// depending on if the player is sneaking, walking, or sprinting
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
		hitbox = new Rectangle(
				(int) (this.position.getX() - camera.getxOffset()) + xMove,
				(int) (this.position.getY() - camera.getyOffset()),
				Assets.TILE_WIDTH, Assets.TILE_HEIGHT);
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
		hitbox = new Rectangle(
				(int) (this.position.getX() - camera.getxOffset()),
				(int) (this.position.getY() - camera.getyOffset() + yMove),
				Assets.TILE_WIDTH, Assets.TILE_HEIGHT);
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
		if (tiles == null) {
			this.tiles = this.world.getMap().getMap();
		}

		double slope = (line.y2 - line.y1) / (line.x2 - line.x1);

		if (line.x2 > line.x1) {
			for (int i = 0; i < Math.abs(line.x2 - line.x1); i++) {
				int tileX = (this.position.x + i) / 32;
				int tileY = ((int) (this.position.y + (i * slope))) / 32;
				if (!(tileX < 0 || tileY < 0 || tileX > (tiles.length - 1) || tileY > (tiles[0].length - 1))) {
					if ((tiles[tileX][tileY] & (1 << 14)) != 0) {
						int hitX = this.position.x + i;
						int hitY = (int) (this.position.y + (i * slope));
						if (hitY > line.getY1()) {
							hitY -= 32;
						}
						if (hitX > line.getX1()) {
							hitX -= 32;
						}
						return new Point(hitX, hitY);
					}
				}
			}
		} else {
			for (int i = 0; i > -Math.abs(line.x2 - line.x1); i--) {
				int tileX = (this.position.x + i) / 32;
				int tileY = ((int) (this.position.y + (i * slope))) / 32;
				if (!(tileX < 0 || tileY < 0 || tileX > (tiles.length - 1) || tileY > (tiles[0].length - 1))) {
					if ((tiles[(this.position.x + i) / 32][((int) (this.position.y + (i * slope))) / 32] & (1 << 14)) != 0) {
						int hitX = this.position.x + i;
						int hitY = (int) (this.position.y + (i * slope));
						if (hitY > line.getY1()) {
							hitY -= 32;
						}
						if (hitX > line.getX1()) {
							hitX -= 32;
						}
						return new Point(hitX, hitY);
					}
				}
			}
		}

		return new Point((int) line.x2, (int) line.y2);
	}

	public PriorityQueue<Entity> projectileTracer(Line2D.Double line,
			int damage, int range) {
		PriorityQueue<Entity> entitiesCollided = new PriorityQueue<Entity>();

		// ArrayList<Entity> validCollisions = new ArrayList<Entity>();

		double maxDistance = range;

		if (tiles == null)
			this.tiles = this.world.getMap().getMap();

		double slope = (line.y2 - line.y1) / (line.x2 - line.x1);

		if (line.x2 > line.x1) {
			for (int i = 0; i < range; i++) {
				int tileX = (this.position.x + i) / 32;
				int tileY = ((int) (this.position.y + (i * slope))) / 32;
				if (!(tileX < 0 || tileY < 0 || tileX > (tiles.length - 1) || tileY > (tiles[0].length - 1))) {
					if ((tiles[tileX][tileY] & (1 << 14)) != 0) {
						maxDistance = (Math.sqrt(Math.pow(i, 2)
								+ Math.pow((i * slope), 2)));
						break;
					}
				}
			}
		} else {
			for (int i = 0; i > -range; i--) {
				int tileX = (this.position.x + i) / 32;
				int tileY = ((int) (this.position.y + (i * slope))) / 32;
				if (!(tileX < 0 || tileY < 0 || tileX > (tiles.length - 1) || tileY > (tiles[0].length - 1))) {
					if ((tiles[(this.position.x + i) / 32][((int) (this.position.y + (i * slope))) / 32] & (1 << 14)) != 0) {
						maxDistance = (Math.sqrt(Math.pow(i, 2)
								+ Math.pow((i * slope), 2)));
						break;
					}
				}
			}
		}

		int chunkX = Math.max(this.position.x / 512, 3);
		int chunkY = Math.max(this.position.y / 512, 3);

		for (int x = chunkX - 3; x < Math.min(chunkX + 4, map.getWidth() / 16); x++) {
			for (int y = chunkY - 3; y < Math.min(chunkY + 4,
					map.getWidth() / 16); y++) {
				ArrayList<Zombie> zombies = getChunkMap()[x][y].getZombies();
				for (int zombie = 0; zombie < zombies.size(); zombie++) {
					Zombie currentZombie = zombies.get(zombie);
					if (line.intersects(currentZombie.getPosition().x,
							currentZombie.getPosition().y, 32, 32)) {
						double distance = Point.distance(this.position.x,
								this.position.y, currentZombie.getPosition().x,
								currentZombie.getPosition().y);
						if (distance < maxDistance) {
							currentZombie.setRelativeDistance((int) distance);
							entitiesCollided.add(currentZombie);
						}
					}
				}

			}
		}

		return entitiesCollided;

	}

	public int meleeCollision(Arc2D arc, int damage) {
		int noOfEnemies = 0;

		int chunkX = Math.max(this.position.x / 512, 1);
		int chunkY = Math.max(this.position.y / 512, 1);
		for (int x = chunkX - 1; x < Math.min(chunkX + 2, map.getWidth() / 16); x++) {
			for (int y = chunkY - 1; y < Math.min(chunkY + 2,
					map.getWidth() / 16); y++) {
				ArrayList<Zombie> zombies = getChunkMap()[x][y].getZombies();
				for (int zombie = 0; zombie < zombies.size(); zombie++) {
					Zombie currentZombie = zombies.get(zombie);
					if (arc.intersects(currentZombie.getPosition().x,
							currentZombie.getPosition().y, 32, 32)) {
						currentZombie.damage(damage);
						noOfEnemies++;
					}
				}

				ArrayList<Entity> entities = getChunkMap()[x][y]
						.getSolidEntities();
				for (int entity = 0; entity < entities.size(); entity++) {
					Entity currentEntity = entities.get(entity);
					if (arc.intersects(currentEntity.getPosition().x,
							currentEntity.getPosition().y, 32, 32)) {
						currentEntity.damage(damage);
						noOfEnemies++;
					}
				}
			}
		}

		return noOfEnemies;
	}

	public int getStamina() {
		return this.stamina;
	}

	public int getSelectedItem() {
		return this.selectedItemNumber;
	}

	public int getSkinNo() {
		return this.skinNo;
	}

	public MouseHandler getMouse() {
		return this.mouse;
	}

	public long getLastItemTick() {
		return this.lastItemTick;
	}

	public void setLastItemTick(long lastItemTick) {
		this.lastItemTick = lastItemTick;
	}
}