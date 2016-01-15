package entities;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import enums.ItemEffect;
import items.*;
import items.Throwable;
import main.Game;
import map.Map;
import utilities.Assets;

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
	public static final int SPRINT_COST = Player.MAX_STAMINA / 100;

	private boolean exhausted = false;
	private int stamina;

	private int selectedItem = 0;

	public Player(boolean solid, Game game) {
		super(solid, game);
		this.movementSpeed = Player.MOVEMENT_SPEED;
		this.stamina = Player.MAX_STAMINA;
	}

	public Player(Point position, boolean solid, Game game, Map map) {
		super(32, 32, position, solid, game, map);
		this.movementSpeed = Player.MOVEMENT_SPEED;
		this.stamina = Player.MAX_STAMINA;
		addItem(this.game.getItems().get(0));
		addItem(this.game.getItems().get(0));
		addItem(this.game.getItems().get(0));
		addItem(this.game.getItems().get(0));
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
		g.drawImage(this.getImages()[0], (int) (this.getPosition().x - this.game.getCamera().getxOffset()),
				(int) (this.getPosition().y - this.game.getCamera().getyOffset()), null);
	}

	// TODO Getters & setters VS protected?
	// Reorganize code; looks messy
	public void update() {
		if (this.stamina < Player.SPRINT_COST) {
			this.exhausted = true;
		} else if (this.stamina > Player.MIN_STAMINA) {
			this.exhausted = false;
		}
		
		if (this.game.getDisplay().getMouseHandler().isClick()) {
			useItem();
			this.game.getDisplay().getMouseHandler().setClick(false);
		}

		if (this.game.getDisplay().getKeyHandler().isShift() && !exhausted
				&& (this.game.getDisplay().getKeyHandler().isUp() || this.game.getDisplay().getKeyHandler().isDown()
						|| this.game.getDisplay().getKeyHandler().isLeft()
						|| this.game.getDisplay().getKeyHandler().isRight())) {
			this.movementSpeed = Player.MOVEMENT_SPEED * 2;
			this.stamina -= Player.SPRINT_COST;
			this.health = 99;
		} else {
			this.movementSpeed = Player.MOVEMENT_SPEED;
			if (this.stamina < Player.MAX_STAMINA) {
				this.stamina++;
			}
		}

		this.selectedItem = this.game.getDisplay().getKeyHandler().getLastNumber();

		this.getPosition().setLocation(this.getPosition().getX(), this.getPosition().getY() + yMove());
		this.getPosition().setLocation(this.getPosition().getX() + xMove(), this.getPosition().getY());
		if (position.getX() < 0)
			position.setLocation(0, position.getY());
		else if (position.getX() > Assets.TILE_WIDTH * (game.getDisplay().getGamePanel().getWorld().getWidth() - 1))
			position.setLocation(Assets.TILE_WIDTH * (game.getDisplay().getGamePanel().getWorld().getWidth() - 1),
					position.getY());
		if (position.getY() < 0)
			position.setLocation(position.getX(), 0);
		else if (position.getY() + 32 > Assets.TILE_HEIGHT
				* (game.getDisplay().getGamePanel().getWorld().getHeight() - 1))
			position.setLocation(position.getX(),
					Assets.TILE_HEIGHT * (game.getDisplay().getGamePanel().getWorld().getHeight() - 1) - 32);

		this.game.getCamera().centerOnEntity(this);

		if (this.game.getDisplay().getKeyHandler().isUp() || this.game.getDisplay().getKeyHandler().isDown()
				|| this.game.getDisplay().getKeyHandler().isRight()
				|| this.game.getDisplay().getKeyHandler().isLeft()) {
			if (this.game.getDisplay().getKeyHandler().isShift())
				makeNoise(400, true);
			else
				makeNoise(300, true);
		}
	}

	private int xMove() {
		int xMove = 0;
		if (this.game.getDisplay().getKeyHandler().isLeft()) {
			xMove = -this.movementSpeed;
		}
		if (this.game.getDisplay().getKeyHandler().isRight()) {
			xMove = this.movementSpeed;
		}
		int playerX = (int) (this.getPosition().x - this.game.getCamera().getxOffset());
		int playerY = (int) (this.getPosition().y - this.game.getCamera().getyOffset());
		if (xMove > 0) {// Moving right
			for (int j = 0; j < game.getDisplay().getGamePanel().getWorld().getSolid().length; j++) {
				for (int i = 0; i < game.getDisplay().getGamePanel().getWorld().getSolid()[0].length; i++) {
					if (game.getDisplay().getGamePanel().getWorld().getSolid()[j][i] != null) {
						if (playerX + xMove + Assets.TILE_WIDTH >= game
								.getDisplay().getGamePanel().getWorld()
								.getSolid()[j][i].getX()
								&& playerX + xMove + Assets.TILE_WIDTH <= game
										.getDisplay().getGamePanel().getWorld()
										.getSolid()[j][i].getX() + 32
								&& ((playerY >= game.getDisplay()
										.getGamePanel().getWorld().getSolid()[j][i]
										.getY() && playerY <= game.getDisplay()
										.getGamePanel().getWorld().getSolid()[j][i]
										.getY() + 32) || (playerY
										+ Assets.TILE_HEIGHT >= game
										.getDisplay().getGamePanel().getWorld()
										.getSolid()[j][i].getY() && playerY
										+ Assets.TILE_HEIGHT <= game
										.getDisplay().getGamePanel().getWorld()
										.getSolid()[j][i].getY() + 32))) {
							this.getPosition().setLocation(
									game.getDisplay().getGamePanel().getWorld()
											.getSolid()[j][i].getX() - 33,
									this.getPosition().getY());
							return 0;
						}
					}
				}
			}
		} else if (xMove < 0) {// Moving Left
			for (int j = 0; j < game.getDisplay().getGamePanel().getWorld().getSolid().length; j++) {
				for (int i = 0; i < game.getDisplay().getGamePanel().getWorld().getSolid()[0].length; i++) {
					if (game.getDisplay().getGamePanel().getWorld().getSolid()[j][i] != null) {
						if (playerX + xMove >= game.getDisplay().getGamePanel()
								.getWorld().getSolid()[j][i].getX()
								&& playerX + xMove <= game.getDisplay()
										.getGamePanel().getWorld().getSolid()[j][i]
										.getX() + 32
								&& ((playerY >= game.getDisplay()
										.getGamePanel().getWorld().getSolid()[j][i]
										.getY() && playerY <= game.getDisplay()
										.getGamePanel().getWorld().getSolid()[j][i]
										.getY() + 32) || (playerY
										+ Assets.TILE_HEIGHT >= game
										.getDisplay().getGamePanel().getWorld()
										.getSolid()[j][i].getY() && playerY
										+ Assets.TILE_HEIGHT <= game
										.getDisplay().getGamePanel().getWorld()
										.getSolid()[j][i].getY() + 32))) {
							this.getPosition().setLocation(
									game.getDisplay().getGamePanel().getWorld().getSolid()[j][i].getX() + 64,
									this.getPosition().getY());
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
		int playerX = (int) (this.getPosition().x - this.game.getCamera().getxOffset());
		int playerY = (int) (this.getPosition().y - this.game.getCamera().getyOffset());
		if (yMove < 0) {// Moving up
			for (int j = 0; j < game.getDisplay().getGamePanel().getWorld().getSolid().length; j++) {
				for (int i = 0; i < game.getDisplay().getGamePanel().getWorld().getSolid()[0].length; i++) {
					if (game.getDisplay().getGamePanel().getWorld().getSolid()[j][i] != null) {
						if (playerY + yMove >= game.getDisplay().getGamePanel()
								.getWorld().getSolid()[j][i].getY()
								&& playerY + yMove <= game.getDisplay()
										.getGamePanel().getWorld().getSolid()[j][i]
										.getY() + Assets.TILE_HEIGHT
								&& ((playerX >= game.getDisplay()
										.getGamePanel().getWorld().getSolid()[j][i]
										.getX() && playerX <= game.getDisplay()
										.getGamePanel().getWorld().getSolid()[j][i]
										.getX() + Assets.TILE_WIDTH) || (playerX
										+ Assets.TILE_WIDTH >= game
										.getDisplay().getGamePanel().getWorld()
										.getSolid()[j][i].getX() && playerX
										+ Assets.TILE_WIDTH <= game
										.getDisplay().getGamePanel().getWorld()
										.getSolid()[j][i].getX()
										+ Assets.TILE_WIDTH))) {
							this.getPosition().setLocation(
									this.getPosition().getX(),
									game.getDisplay().getGamePanel().getWorld()
											.getSolid()[j][i].getY() + 33);
							return 0;
						}
					}
				}
			}
		} else if (yMove > 0) {// Moving down
			for (int j = 0; j < game.getDisplay().getGamePanel().getWorld().getSolid().length; j++) {
				for (int i = 0; i < game.getDisplay().getGamePanel().getWorld().getSolid()[0].length; i++) {
					if (game.getDisplay().getGamePanel().getWorld().getSolid()[j][i] != null) {
						if (playerY + yMove + Assets.TILE_HEIGHT >= game
								.getDisplay().getGamePanel().getWorld()
								.getSolid()[j][i].getY()
								&& playerY + yMove + Assets.TILE_HEIGHT <= game
										.getDisplay().getGamePanel().getWorld()
										.getSolid()[j][i].getY()
										+ Assets.TILE_HEIGHT
								&& ((playerX >= game.getDisplay()
										.getGamePanel().getWorld().getSolid()[j][i]
										.getX() && playerX <= game.getDisplay()
										.getGamePanel().getWorld().getSolid()[j][i]
										.getX() + Assets.TILE_WIDTH) || (playerX
										+ Assets.TILE_WIDTH >= game
										.getDisplay().getGamePanel().getWorld()
										.getSolid()[j][i].getX() && playerX
										+ Assets.TILE_WIDTH <= game
										.getDisplay().getGamePanel().getWorld()
										.getSolid()[j][i].getX()
										+ Assets.TILE_WIDTH))) {
							this.getPosition().setLocation(
									this.getPosition().getX(),
									game.getDisplay().getGamePanel().getWorld()
											.getSolid()[j][i].getY() - 33);
							return 0;
						}
					}
				}
			}
		}
		return yMove;
	}

	private void useItem(Item item) {
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
						this.removeItem(item);
					}
				}
				break;
			case AMMO:
				break;
			}
		} else if (item instanceof Melee) {
			
		} else if (item instanceof Firearm) {
			
		} else if (item instanceof Throwable) {
			
		}
	}
	
	public void useItem() {
		useItem(getItem(this.selectedItem));
	}
}