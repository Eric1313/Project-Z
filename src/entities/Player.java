package entities;

import items.Consumable;
import items.Firearm;
import items.Item;
import items.Melee;
import items.Throwable;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import main.Game;
import map.Map;
import utilities.Assets;
import utilities.GameCamera;
import utilities.KeyHandler;
import utilities.MouseHandler;
import utilities.World;
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

	private int selectedItem = 0;
	private int skinNo;
	
	private long lastItemTick = 0;

	public Player(boolean solid, Game game) {
		super(solid, game);
		this.movementSpeed = Player.MOVEMENT_SPEED;
		this.stamina = Player.MAX_STAMINA;
		this.mouse = game.getDisplay().getMouseHandler();
	}

	public Player(Point position, boolean solid, Game game, Map map, int skinNo) {
		super(32, 32, position, solid, game, map);
		this.skinNo = skinNo;
		this.movementSpeed = Player.MOVEMENT_SPEED;
		this.stamina = Player.MAX_STAMINA;
		addItem(new Consumable((Consumable) this.game.getItems().get(0)));
		addItem(new Consumable((Consumable) this.game.getItems().get(1)));
		addItem(new Melee((Melee) this.game.getItems().get(2)));
		addItem(new Firearm((Firearm) this.game.getItems().get(3)));
		addItem(new Throwable((Throwable) this.game.getItems().get(4)));
		this.mouse = game.getDisplay().getMouseHandler();
		this.camera = game.getCamera();
		this.key = game.getDisplay().getKeyHandler();
		this.world = game.getDisplay().getGamePanel().getWorld();
	}

	public int getStamina() {
		return this.stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public int getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(int selectedItem) {
		this.selectedItem = selectedItem;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(this.getImages()[skinNo],
				(int) (this.getPosition().x - camera.getxOffset()),

				(int) (this.getPosition().y - camera.getyOffset()), null);
	}

	// TODO Getters & setters VS protected?
	// Reorganize code; looks messy
	public void update() {
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
			dropItem();
			key.setQ(false);
		}

		if (key.isE()) {
			pickUpItem();
			key.setE(false);
		}

		if (key.isShift() && !exhausted && (key.isUp() || key.isDown() || key.isLeft() || key.isRight())) {
			this.movementSpeed = Player.MOVEMENT_SPEED * 2;
			this.stamina -= Player.SPRINT_COST;
		} else if (key.isCtrl()) {
			this.movementSpeed = Player.MOVEMENT_SPEED / 2;
		} else {
			this.movementSpeed = Player.MOVEMENT_SPEED;
			if (this.stamina < Player.MAX_STAMINA) {
				this.stamina++;
			}
		}
		
		

		key.setLastNumber(key.getLastNumber() + mouse.getMouseWheel());
		mouse.setMouseWheel(0);

		this.selectedItem = key.getLastNumber();

		this.getPosition().setLocation(this.getPosition().getX(), this.getPosition().getY() + yMove());
		this.getPosition().setLocation(this.getPosition().getX() + xMove(), this.getPosition().getY());
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
				makeNoise(400, true);
			} else if (key.isCtrl()) {
				makeNoise(100, true);
			} else {
				makeNoise(300, true);
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

	public void useItem() {
		Item item = getItem(this.selectedItem);

		if (item == null) {
			return;
		}
		if (item instanceof Consumable) {
			Consumable newItem = (Consumable) item;
			switch (newItem.getEffect()) {
			case HEAL:
				if (this.health < 100) {
					this.health = Math.min(100, this.health + newItem.getEffectValue());
					newItem.removeDurability();
					if (newItem.getDurability() <= 0) {
						this.removeItem(this.selectedItem);
					}
				}
				break;
			case AMMO:
				for (int itemNo = 0; itemNo < Inventory.NO_OF_ITEMS; itemNo++) {
					Item currentItem = getItem(itemNo);
					if (currentItem instanceof Firearm) {
						Firearm firearm = (Firearm) currentItem;

						if (firearm.getAmmoID() == newItem.getItemID() && !firearm.isFull()) {
							if (newItem.getDurability() <= 0) {
								this.removeItem(this.selectedItem);
							} else {
								firearm.setCurrentAmmo(firearm.getMaxAmmo());
								newItem.removeDurability();
							}
						}
					}
				}
				break;
			}
		} else if (item instanceof Melee) {
			Melee newItem = (Melee) item;
			double angle = Math.atan2(
					(position.y + 16 - game.getCamera().getyOffset())
							- game.getDisplay().getMouseHandler().getMouseLocation().y,
					(position.x + 16 - game.getCamera().getxOffset())
							- game.getDisplay().getMouseHandler().getMouseLocation().x)
					- Math.PI / 2;

			long currentTick = this.game.getTickCount();
			if (currentTick - this.lastItemTick > newItem.getRechargeTime()) {
				this.lastItemTick = currentTick;
			}
		} else if (item instanceof Firearm) {
			Firearm newItem = (Firearm) item;

			double angle = -Math.atan2(
					game.getDisplay().getMouseHandler().getMouseLocation().y
							- (position.y + 16 - game.getCamera().getyOffset()),
					game.getDisplay().getMouseHandler().getMouseLocation().x
							- (position.x + 16 - game.getCamera().getxOffset()));

			if (!newItem.isEmpty()) {
				long currentTick = this.game.getTickCount();
				if (currentTick - this.lastItemTick > newItem.getRateOfFire()) {
					this.lastItemTick = currentTick;

					int d = 32 * 16;

					Line2D.Double line = new Line2D.Double(new Point(this.position.x + 16, this.position.y + 16),
							new Point((int) (this.position.x + 16 + d * Math.cos(angle)),
									(int) (this.position.y + 16 - d * Math.sin(angle))));

					bulletCollision(line);
					
					makeNoise(1000, true);
					
					newItem.removeAmmo();
				}
			}
		} else if (item instanceof Throwable) {

		}
	}

	public void dropItem() {
		Item item = getItem(this.selectedItem);
		if (item != null) {
			item.setPosition(new Point(this.position.x, this.position.y));
			item.setState(ItemState.DROPPED);
			removeItem(this.selectedItem);
			this.chunkMap[this.position.x / 512][this.position.y / 512].add(item);
		}
	}

	public void pickUpItem() {
		Item hoverItem = this.game.getDisplay().getGamePanel().getWorld().getHoverItem();

		if (hoverItem != null && !isFull() && Point.distance(this.position.x, this.position.y,
				hoverItem.getPosition().x, hoverItem.getPosition().y) <= 1 * 32) {
			hoverItem.setState(ItemState.INVENTORY);
			this.chunkMap[hoverItem.getPosition().x / 512][hoverItem.getPosition().y / 512].remove(hoverItem);
			addItem(hoverItem);
		}
	}

	public Zombie bulletCollision(Line2D.Double line) {
		ArrayList<Entity> entitiesCollided = new ArrayList<Entity>();
		ArrayList<Zombie> zombiesCollided = new ArrayList<Zombie>();

		int chunkX = Math.max(this.position.x / 512, 3);
		int chunkY = Math.max(this.position.y / 512, 3);
		for (int x = chunkX - 3; x < Math.min(chunkX + 4,map.getWidth()/16-1); x++) {
			for (int y = chunkY - 3; y < Math.min(chunkY + 4,map.getWidth()/16-1); y++) {
				for (Iterator<Zombie> iterator = chunkMap[x][y].getZombies().iterator(); iterator.hasNext();) {
					Zombie zombie = iterator.next();
					if (line.intersects(zombie.getPosition().x, zombie.getPosition().y, 32, 32)) {
						zombiesCollided.add(zombie);
					}
				}

				for (Iterator<Entity> iterator = chunkMap[x][y].getSolidEntities().iterator(); iterator.hasNext();) {
					Entity entity = iterator.next();
					if (line.intersects(entity.getPosition().x, entity.getPosition().y, 32, 32)) {
						entitiesCollided.add(entity);
					}
				}
			}
		}

		Zombie closestZombie = null;
		double zombieDistance = 100 * 32;

		for (Iterator<Zombie> iterator = zombiesCollided.iterator(); iterator.hasNext();) {
			Zombie zombie = iterator.next();

			double distance = Point.distance(this.position.x, this.position.y, zombie.getPosition().x,
					zombie.getPosition().y);

			if (distance < zombieDistance) {
				zombieDistance = distance;
				closestZombie = zombie;
			}
		}

		if (closestZombie != null) {
			chunkMap[closestZombie.getPosition().x / 512][closestZombie.getPosition().y / 512]
					.removeZombie(closestZombie);
		}

		return closestZombie;
	}

	public Point getPlayerCenter() {
		return new Point(
				(int) (this.getPosition().x - camera.getxOffset() + 16),
				(int) (this.getPosition().y - camera.getyOffset()) + 16);
	}
}
