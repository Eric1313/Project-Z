package entities;

import items.Consumable;
import items.Firearm;
import items.Item;
import items.Melee;
import items.Throwable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.PriorityQueue;

import main.Game;
import map.Map;
import map.World;
import utilities.Assets;
import utilities.GameCamera;
import utilities.KeyHandler;
import utilities.MouseHandler;
import enums.GameState.State;
import enums.ItemState;

/**
 * Subclass of Mob that represents a player in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Mob
 * @since 1.0
 * @version 1.0
 */
public class Player extends Mob {
	private int baseMovementSpeed = 2;
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
	public Player(Point position, Inventory inventory, boolean solid, Game game, Map map, int skinNo) {
		super(32, 32, position, solid, game, map);

		if (inventory != null) {
			this.inventory = inventory;
		} else if (game.getState().isDebug()) {
			this.addItem(this.game.getItem(304));
			((Firearm) this.getItem(0)).setCurrentAmmo(Integer.MAX_VALUE);
			this.addItem(this.game.getItem(106));
			((Consumable) this.getItem(1)).setDurability(Integer.MAX_VALUE);
			this.addItem(this.game.getItem(302));
			((Firearm) this.getItem(2)).setCurrentAmmo(Integer.MAX_VALUE);
			this.addItem(this.game.getItem(303));
			((Firearm) this.getItem(3)).setCurrentAmmo(Integer.MAX_VALUE);
			this.addItem(this.game.getItem(301));
			((Firearm) this.getItem(4)).setCurrentAmmo(Integer.MAX_VALUE);
			this.addItem(this.game.getItem(300));
			((Firearm) this.getItem(5)).setCurrentAmmo(Integer.MAX_VALUE);
		} else {
			this.addItem(this.game.getItem(200));
			this.addItem(this.game.getItem(300));
			this.addItem(this.game.getItem(400));
			this.addItem(this.game.getItem(301));
			this.addItem(this.game.getItem(101));
			this.addItem(this.game.getItem(102));

		}

		this.skinNo = skinNo;
		this.movementSpeed = getBaseMovementSpeed();
		this.stamina = Player.MAX_STAMINA;

		this.world = this.game.getDisplay().getGameScreen().getWorld();
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
		return new Point((int) (this.getPosition().getX() - camera.getxOffset() + this.width / 2),
				(int) (this.getPosition().getY() - camera.getyOffset()) + this.height / 2);
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
			this.game.getState().setState(State.DEATH, false);
		}
	}

	/**
	 * Attempts to pick up an item that the player is touching.
	 */
	public void pickUpItem() {
		// Get the item that's being hovered over
		Item hoverItem = this.game.getDisplay().getGameScreen().getWorld().getHoverItem();

		// Check if the player's inventory is not full
		// Make sure that the player is close enough to the item (32 pixels)
		if (hoverItem != null && !this.isFull() && Point.distance(this.position.getX(), this.position.getY(),
				hoverItem.getPosition().getX(), hoverItem.getPosition().getY()) <= 32) {
			// Remove the item from the chunk that the item was in
			// Add the item to the player's inventory
			hoverItem.setState(ItemState.INVENTORY);
			this.chunkMap[hoverItem.getPosition().x / 512][hoverItem.getPosition().y / 512].remove(hoverItem);
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

		// Store the original transform of the graphics variable
		AffineTransform originalTransform = g2D.getTransform();

		// Calculate the angle of the mouse
		double angle = Math.atan2(((position.getY()) + 16 - camera.getyOffset()) - mouse.getMouseLocation().getY(),
				(position.getX() + 16 - camera.getxOffset()) - mouse.getMouseLocation().getX()) - Math.PI / 2;

		// Render the selected item if the player is holding one
		this.selectedItem = this.getItem(selectedItemNumber);
		if (selectedItem != null) {
			// If the player is swinging their melee weapon, animate it
			// depending on its swing speed, angle and tick
			if (selectedItem instanceof Melee && this.swinging) {
				long difference = this.game.getTickCount() - this.swingTick;
				if (difference <= ((Melee) selectedItem).getSwingSpeed()) {
					g2D.rotate(
							this.swingAngle - this.swingAngleRange
									+ (this.swingAngleRange * 2 / difference * ((Melee) selectedItem).getSwingSpeed()
											* 1.0),
							position.getX() - camera.getxOffset() + 16, position.getY() - camera.getyOffset() + 16);
					g2D.drawImage(selectedItem.getImages()[0], (int) (this.getPosition().x - camera.getxOffset() + 10),
							(int) (this.getPosition().y - camera.getyOffset() - 10), null);
				} else {
					// If the current tick difference is past the swing speed,
					// stop swinging and rotate the graphics variable to the
					// mouse angle and draw the selected item
					this.swinging = false;
					g2D.rotate(angle, position.getX() - camera.getxOffset() + 16,
							position.getY() - camera.getyOffset() + 16);
					g2D.drawImage(selectedItem.getImages()[0], (int) (this.getPosition().x - camera.getxOffset() + 10),
							(int) (this.getPosition().y - camera.getyOffset() - 10), null);
				}
			} else {
				// If the player is not swinging, rotate the graphics variable
				// to the mouse's angle
				g2D.rotate(angle, position.getX() - camera.getxOffset() + 16,
						position.getY() - camera.getyOffset() + 16);
				if (selectedItem instanceof Firearm) {
					// If the player is holding a firearm, render the holding
					// gun sprite
					g2D.drawImage(this.getItem(selectedItemNumber).getImages()[2],
							(int) (this.getPosition().x - camera.getxOffset() + 10),
							(int) (this.getPosition().y - camera.getyOffset() - 10), null);
					// If the player is shooting their gun, draw a muzzle flash
					// and stop shooting
					if (this.shooting) {
						g2D.setColor(new Color(255, 255, 0));
						g2D.fillOval((int) (this.getPosition().x - camera.getxOffset() + 25),
								(int) (this.getPosition().y - camera.getyOffset() - 18), 6, 12);
						this.shooting = false;
					}
				} else {
					// If the item does not match either special case above,
					// render it in the player's hand normally
					g2D.drawImage(this.getItem(selectedItemNumber).getImages()[0],
							(int) (this.getPosition().x - camera.getxOffset() + 10),
							(int) (this.getPosition().y - camera.getyOffset() - 10), null);
				}
			}
		} else {
			// If the player is not holding an item, render the camera depending
			// on the mouse's angle anyway
			g2D.rotate(angle, position.getX() - camera.getxOffset() + 16, position.getY() - camera.getyOffset() + 16);
		}

		// Draw the player
		g2D.drawImage(this.getImages()[skinNo], (int) (this.getPosition().x - camera.getxOffset()),
				(int) (this.getPosition().y - camera.getyOffset()), null);

		// Reset the transform of the graphics variable to its original
		// transform
		g2D.setTransform(originalTransform);

		// If the player is holding a throwable, render a circle showing its
		// range
		if (this.getItem(selectedItemNumber) != null && selectedItem instanceof Throwable) {
			Throwable throwable = ((Throwable) selectedItem);
			g2D.drawOval((int) (this.getPlayerCenter().x - throwable.getRange()),
					(int) (this.getPlayerCenter().y - throwable.getRange()), 2 * throwable.getRange(),
					2 * throwable.getRange());
		}
	}

	public void update() {
		this.world = this.game.getDisplay().getGameScreen().getWorld();

		// If the player presses 'ESC', pause the game
		if (this.key.isEsc()) {
			this.game.getState().setState(State.PAUSE, false);
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
		// Set the mouse click to false unless using an automatic firearm
		if (this.mouse.isClick()) {
			this.useItem();
			if ((this.selectedItem instanceof Firearm)) {
				if (!((Firearm) this.selectedItem).isAutomatic()) {
					this.mouse.setClick(false);
				}
			} else {
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
		if (this.key.isShift() && !this.exhausted
				&& (this.key.isUp() || this.key.isDown() || this.key.isLeft() || this.key.isRight())) {
			this.movementSpeed = getBaseMovementSpeed() * 2;
			this.stamina -= Player.SPRINT_COST;
		} else if (key.isCtrl()) {
			this.movementSpeed = getBaseMovementSpeed() / 2;
			if (this.stamina < Player.MAX_STAMINA) {
				this.stamina++;
			}
		} else {
			this.movementSpeed = getBaseMovementSpeed();
			if (this.stamina < Player.MAX_STAMINA) {
				this.stamina++;
			}
		}

		// Update the key handler's last number with the mouse wheel
		// Scrolling down increases the selected item number and loops back
		// around
		// Reset the mouse handler's mouse wheel
		this.key.setLastNumber(this.key.getLastNumber() + this.mouse.getMouseWheel());
		this.mouse.setMouseWheel(0);

		// If the selected item number has change, update the player's selected
		// item
		if (this.selectedItemNumber != this.key.getLastNumber()) {
			this.selectedItemNumber = this.key.getLastNumber();
			this.selectedItem = this.getItem(this.selectedItemNumber);
		}

		// Player movement
		this.getPosition().setLocation(this.getPosition().getX(), this.getPosition().getY() + yMove());
		this.getPosition().setLocation(this.getPosition().getX() + xMove(), this.getPosition().getY());

		// This stops the player from getting stuck
		int row = (int) ((((this.getPosition().y - camera.getyOffset()) / 32.0)));
		int col = (int) ((this.getPosition().x - camera.getxOffset()) / 32);
		if (world.getSolid()[row][col] != null && yMove() == 0 && xMove() == 0 && key.isRight() && key.isLeft()
				&& key.isUp() && key.isDown()) {
			if (hitbox.intersects(world.getSolid()[row][col])) {
				this.getPosition().setLocation(this.getPosition().getX(), this.getPosition().getY() + 32);
			}
		}

		// Maps sure that the player stays inside the map
		if (position.getX() < 0)
			position.setLocation(0, position.getY());
		else if (position.getX() > Assets.TILE_WIDTH * (world.getWidth() - 1))
			position.setLocation(Assets.TILE_WIDTH * (world.getWidth() - 1), position.getY());
		if (position.getY() < 0)
			position.setLocation(position.getX(), 0);
		else if (position.getY() + 32 > Assets.TILE_HEIGHT * (world.getHeight() - 1))
			position.setLocation(position.getX(), Assets.TILE_HEIGHT * (world.getHeight() - 1) - 32);

		// Centers the camera on the player
		this.camera.centerOnEntity(this);

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

		// Checks if the player has reached the check point
		if (world.getFlag() != null) {
			if (world.getFlag().intersects(hitbox)) {
				game.getState().setState(State.FINISH, false);
			}
		}
	}

	/**
	 * The x movement of the player.
	 * 
	 * @return the amount to move.
	 */
	private int xMove() {
		int xMove = 0;
		// Sets the movement direction based on the key pressed
		if (key.isLeft()) {
			xMove = -this.movementSpeed;
		}
		if (key.isRight()) {
			xMove = this.movementSpeed;
		}
		// Creates the player hit box
		hitbox = new Rectangle((int) (this.position.getX() - camera.getxOffset()) + xMove,
				(int) (this.position.getY() - camera.getyOffset()), Assets.TILE_WIDTH, Assets.TILE_HEIGHT);
		int row = (int) ((((this.getPosition().y - camera.getyOffset()) / 32.0)));
		int col = (int) ((this.getPosition().x - camera.getxOffset()) / 32);
		if (xMove > 0) { // Moving right
			if (row != 0)
				row -= 1;
			// Checks if the player collides with anythings
			for (int i = row; i < world.getSolid().length; i++) {
				for (int j = col; j < world.getSolid()[0].length; j++) {
					if (world.getSolid()[i][j] != null) {
						if (hitbox.intersects(world.getSolid()[i][j])) {
							return 0;
						}
					}
				}
			}
		} else if (xMove < 0) { // Moving Left
			if (row != 0)
				row -= 1;
			// Checks if the player collides with anythings
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

	/**
	 * The y movement of the player.
	 * 
	 * @return the amount to move in the y axis.
	 */
	private int yMove() {
		int yMove = 0;
		// Sets the movement direction based on the key pressed
		if (this.game.getDisplay().getKeyHandler().isUp()) {
			yMove = -this.movementSpeed;
		}
		if (this.game.getDisplay().getKeyHandler().isDown()) {
			yMove = this.movementSpeed;
		}
		// Creates the player hit box
		hitbox = new Rectangle((int) (this.position.getX() - camera.getxOffset()),
				(int) (this.position.getY() - camera.getyOffset() + yMove), Assets.TILE_WIDTH, Assets.TILE_HEIGHT);
		int row = (int) (((this.getPosition().y - camera.getyOffset()) / 32));
		int col = (int) (((this.getPosition().x - camera.getxOffset()) / 32.0));
		if (yMove < 0) { // Moving up
			if (col != 0)
				col -= 1;
			// Checks if the player collides with anythings
			for (int i = row; i >= 0; i--) {
				for (int j = col; j < world.getSolid()[0].length; j++) {
					if (world.getSolid()[i][j] != null) {
						if (hitbox.intersects(world.getSolid()[i][j])) {
							return 0;
						}
					}
				}
			}
		} else if (yMove > 0) { // Moving down
			if (col != 0)
				col -= 1;
			// Checks if the player collides with anythings
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

	/**
	 * Calculates point of impact of a throwable.
	 * 
	 * @param line
	 *            the trajectory of throw.
	 * @return the point of impact.
	 */
	public Point calculatePointOfImpact(Line2D.Double line) {
		if (tiles == null) {
			this.tiles = this.world.getMap().getMap();
		}

		// Calculate slope of line
		double slope = (line.y2 - line.y1) / (line.x2 - line.x1);

		// If on the right side add to x when calculating
		if (line.x2 > line.x1) {
			// Cycle through each pixel on the line and check if it hit a solid
			// block
			for (int i = 0; i < Math.abs(line.x2 - line.x1); i++) {
				int tileX = (this.position.x + i) / 32;
				int tileY = ((int) (this.position.y + (i * slope))) / 32;
				if (!(tileX < 0 || tileY < 0 || tileX > (tiles.length - 1) || tileY > (tiles[0].length - 1))) {
					if ((tiles[tileX][tileY] & (1 << 14)) != 0) {
						int hitX = this.position.x + i;
						int hitY = (int) (this.position.y + (i * slope));
						// Shift point out of the wall
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
			// If on the left side subtract from x when calculating
		} else {
			for (int i = 0; i > -Math.abs(line.x2 - line.x1); i--) {
				// Cycle through each pixel on the line and check if it hit a
				// solid block
				int tileX = (this.position.x + i) / 32;
				int tileY = ((int) (this.position.y + (i * slope))) / 32;

				if (!(tileX < 0 || tileY < 0 || tileX > (tiles.length - 1) || tileY > (tiles[0].length - 1))) {
					if ((tiles[(this.position.x + i) / 32][((int) (this.position.y + (i * slope))) / 32]
							& (1 << 14)) != 0) {
						int hitX = this.position.x + i;
						int hitY = (int) (this.position.y + (i * slope));
						// Shift point out of the wall
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
		// If no collisions detected return the second point of the line
		return new Point((int) line.x2, (int) line.y2);
	}

	/**
	 * Checks for entity collisions with a projectile.
	 * 
	 * @param line
	 *            the trajectory of bullet.
	 * @param range
	 *            the range of bullet.
	 * @return a queue of hit entities sorted by distance.
	 */
	public PriorityQueue<Entity> projectileTracer(Line2D.Double line, int range) {
		// Queue of hit entities
		PriorityQueue<Entity> entitiesCollided = new PriorityQueue<Entity>();

		// Distance of bullet travel
		double maxDistance = range;

		// Set tile map if null
		if (tiles == null)
			this.tiles = this.world.getMap().getMap();

		// Calculate path of trajectory
		double slope = (line.y2 - line.y1) / (line.x2 - line.x1);

		// If on the right side add to x when calculating
		if (line.x2 > line.x1) {
			// Cycle through each pixel on the line and check if it hit a solid
			// block
			for (int i = 0; i < range; i++) {
				int tileX = (this.position.x + i) / 32;
				int tileY = ((int) (this.position.y + (i * slope))) / 32;
				if (!(tileX < 0 || tileY < 0 || tileX > (tiles.length - 1) || tileY > (tiles[0].length - 1))) {
					// Set max distance of bullet
					if ((tiles[tileX][tileY] & (1 << 14)) != 0) {
						maxDistance = (Math.sqrt(Math.pow(i, 2) + Math.pow((i * slope), 2)));
						break;
					}
				}
			}
			// If on the left side subtract from x when calculating
		} else {
			// Cycle through each pixel on the line and check if it hit a solid
			// block
			for (int i = 0; i > -range; i--) {
				int tileX = (this.position.x + i) / 32;
				int tileY = ((int) (this.position.y + (i * slope))) / 32;
				if (!(tileX < 0 || tileY < 0 || tileX > (tiles.length - 1) || tileY > (tiles[0].length - 1))) {
					// Set max distance of bullet
					if ((tiles[(this.position.x + i) / 32][((int) (this.position.y + (i * slope))) / 32]
							& (1 << 14)) != 0) {
						maxDistance = (Math.sqrt(Math.pow(i, 2) + Math.pow((i * slope), 2)));
						break;
					}
				}
			}
		}

		// Find chunk player is in
		int chunkX = Math.max(this.position.x / 512, 3);
		int chunkY = Math.max(this.position.y / 512, 3);

		// Cycle through a 7x7 chunk area around the player to check for
		// collsions
		for (int x = chunkX - 3; x < Math.min(chunkX + 4, map.getWidth() / 16); x++) {
			for (int y = chunkY - 3; y < Math.min(chunkY + 4, map.getWidth() / 16); y++) {
				// Cycle through zombies
				ArrayList<Zombie> zombies = getChunkMap()[x][y].getZombies();
				for (int zombie = 0; zombie < zombies.size(); zombie++) {
					Zombie currentZombie = zombies.get(zombie);
					// If in bullets path add to the list of hit entities
					if (line.intersects(currentZombie.getPosition().x, currentZombie.getPosition().y, 32, 32)) {
						double distance = Point.distance(this.position.x, this.position.y,
								currentZombie.getPosition().x, currentZombie.getPosition().y);
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

	/**
	 * Checks entity collision with a given arc.
	 * 
	 * @param arc
	 *            the arc to check.
	 * @param damage
	 *            the damage to damage the entities with.
	 * @return the number of entities collided with.
	 */
	public int meleeCollision(Arc2D arc, int damage) {
		int noOfEnemies = 0;

		// Check a 3x3 block of chunks around the player
		int chunkX = Math.max(this.position.x / 512, 1);
		int chunkY = Math.max(this.position.y / 512, 1);
		for (int x = chunkX - 1; x < Math.min(chunkX + 2, map.getWidth() / 16); x++) {
			for (int y = chunkY - 1; y < Math.min(chunkY + 2, map.getWidth() / 16); y++) {
				// Go through each zombie in the chunk and check if it intersect
				// the arc
				// If it does, damage the zombie and add 1 to the number of
				// enemies hit
				ArrayList<Zombie> zombies = getChunkMap()[x][y].getZombies();
				for (int zombie = 0; zombie < zombies.size(); zombie++) {
					Zombie currentZombie = zombies.get(zombie);
					if (arc.intersects(currentZombie.getPosition().x, currentZombie.getPosition().y, 32, 32)) {
						currentZombie.damage(damage);
						noOfEnemies++;
					}
				}

				// Go through each solid entity in the chunk and check if it
				// intersect the arc
				// If it does, damage the entity and add 1 to the number of
				// enemies hit
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

	public int getBaseMovementSpeed() {
		return baseMovementSpeed;
	}

	public void setBaseMovementSpeed(int i) {
		this.baseMovementSpeed = i;
	}
}