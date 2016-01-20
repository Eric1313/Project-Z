package items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import entities.Player;
import enums.ItemEffect;
import enums.ItemState;
import main.Game;
import utilities.SoundEffect;

/**
 * Subclass of Item that represents a consumable item in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Item
 * @since 1.0
 * @version 1.0
 */
public class Consumable extends Item {
	private ItemEffect effect;
	private int durability;

	/**
	 * Constructs a new Consumable object.
	 * 
	 * @param itemID
	 *            the item ID.
	 * @param name
	 *            the name of the item.
	 * @param rarity
	 *            the rarity of the item (from 1-5).
	 * @param effectValue
	 *            the effect's value. In the case of weapons, this would be the
	 *            damage.
	 * @param state
	 *            whether the item is in an inventory or dropped in the world.
	 * @param images
	 *            an array of images of the item.
	 * @param clips
	 *            an array of audio clips played by item.
	 * @param game
	 *            the game to add the item to.
	 * @param effect
	 *            the effect of the item (e.g. heal).
	 * @param durability
	 *            the number of times the item can be used.
	 */
	public Consumable(int itemID, String name, int rarity, int effectValue, ItemState state, BufferedImage[] images,
			String[] clips, Game game, ItemEffect effect, int durability) {
		super(itemID, name, rarity, effectValue, state, images, clips, game);

		this.effect = effect;
		this.effectValue = effectValue;
		this.durability = durability;
	}

	/**
	 * Clones an item template for multiple use.
	 * 
	 * @param item
	 *            the item template.
	 */
	public Consumable(Consumable item) {
		super(item);

		this.effect = item.getEffect();
		this.durability = item.getDurability();
	}

	@Override
	public void use(Player player) {
		switch (this.getEffect()) {
		case HEAL:
			// Check if enough time has passed to use the item
			long currentTick = player.getGame().getTickCount();
			if (player.getHealth() < 100 && currentTick - player.getLastItemTick() > 210) {
				player.setLastItemTick(currentTick);
				// Play the item's sound
				new SoundEffect(clips[0]).play();

				// Run a thread to delay the use of the item
				Consumable consumable = this;
				Thread reloadPause = new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(3500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// Heal the player by the item's effect value
						player.setHealth(Math.min(100, player.getHealth() + consumable.getEffectValue()));
						// Remove 1 durability from the item
						// If the item is out of durability, remove it
						consumable.removeDurability();
						if (consumable.getDurability() <= 0) {
							player.removeItem(consumable);
						}
					}
				});
				reloadPause.start();
			}
			break;
		case AMMO:
			break;
		case SPEED_BUFF:
			// Check if enough time has passed to use the item
			player.setBaseMovementSpeed(player.getBaseMovementSpeed() + 1);

			// Play the item's sound
			new SoundEffect(clips[0]).play();
			Consumable consumable = this;

			//Delay before it kicks in
			Thread buffUseDelay = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(3500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					player.setBaseMovementSpeed(player.getBaseMovementSpeed() + 1);

					consumable.removeDurability();
					if (consumable.getDurability() <= 0) {
						player.removeItem(consumable);
					}
				}
			});
			buffUseDelay.start();
			

			// Thread to wait for duration to end
			Thread effectDuration = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(consumable.getEffectValue());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					player.setBaseMovementSpeed(player.getBaseMovementSpeed() - 1);
				}
			});
			effectDuration.start();

			break;
		default:
			break;
		}
	}

	public ItemEffect getEffect() {
		return this.effect;
	}

	public void setEffect(ItemEffect effect) {
		this.effect = effect;
	}

	public int getEffectValue() {
		return this.effectValue;
	}

	public void setEffectValue(int effectValue) {
		this.effectValue = effectValue;
	}

	public int getDurability() {
		return this.durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public void removeDurability() {
		this.durability--;
	}

	@Override
	public void renderTooltip(Graphics g, Point mouseLocation) {
		// Render the tooltip's background depending on its rarity
		g.setColor(new Color(getColour().getRed(), getColour().getGreen(), getColour().getBlue(), 75));
		g.fillRect(mouseLocation.x, mouseLocation.y - 175, 300, 175);

		// Write the item's name
		g.setColor(new Color(0, 0, 0, 200));
		g.setFont(this.game.getUiFont());
		g.drawString(this.name, mouseLocation.x + 20, mouseLocation.y - 125);

		// Write the item's rarity
		g.setFont(this.game.getUiFontXS());
		switch (this.rarity) {
		case 5:
			g.drawString("Common", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		case 4:
			g.drawString("Uncommon", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		case 3:
			g.drawString("Rare", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		case 2:
			g.drawString("Very Rare", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		case 1:
			g.drawString("Ultra Rare", mouseLocation.x + 20, mouseLocation.y - 105);
			break;
		}

		// Write the effect of the item and how much of the effect it does
		switch (this.effect) {
		case HEAL:
			g.drawString("Healing item", mouseLocation.x + 20, mouseLocation.y - 90);
			g.setFont(this.game.getUiFontS());
			g.drawString("Heals " + this.effectValue + " health", mouseLocation.x + 20, mouseLocation.y - 65);
			if (this.durability == 1) {
				g.drawString("Can be used " + this.durability + " time", mouseLocation.x + 20, mouseLocation.y - 40);
			} else {
				g.drawString("Can be used " + this.durability + " times", mouseLocation.x + 20, mouseLocation.y - 40);
			}
			break;
		case AMMO:
			g.drawString("Ammo", mouseLocation.x + 20, mouseLocation.y - 90);
			g.setFont(this.game.getUiFontS());
			g.drawString("Reloads " + this.durability + " ammo", mouseLocation.x + 20, mouseLocation.y - 65);
			break;
		case SPEED_BUFF:
			g.drawString("Buff", mouseLocation.x + 20, mouseLocation.y - 90);
			g.setFont(this.game.getUiFontS());
			g.drawString("Temporarily increases speed", mouseLocation.x + 20, mouseLocation.y - 65);
			if (this.durability == 1) {
				g.drawString("Can be used " + this.durability + " time", mouseLocation.x + 20, mouseLocation.y - 40);
			} else {
				g.drawString("Can be used " + this.durability + " times", mouseLocation.x + 20, mouseLocation.y - 40);
			}
			break;
		default:
			break;
		}
	}
}