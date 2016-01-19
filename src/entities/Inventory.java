package entities;

import enums.ItemState;
import items.Item;

/**
 * Inventory class for inventories of entities in Project Z.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Entity
 * @since 1.0
 * @version 1.0
 */
public class Inventory {
	public static final int NO_OF_ITEMS = 10;
	private Item[] items;
	private int noOfItems;

	public Inventory() {
		this.items = new Item[Inventory.NO_OF_ITEMS];
		this.noOfItems = 0;
	}

	/**
	 * Gets the item that is in a given slot of the inventory.
	 * 
	 * @param itemNo
	 *            the item number (from 0-9) of the item to find.
	 * @return the item in the item number's slot. Returns null if it is empty.
	 */
	public Item get(int itemNo) {
		if (itemNo < 0 || itemNo > this.items.length) {
			return null;
		} else {
			return this.items[itemNo];
		}
	}

	/**
	 * Adds an item to the inventory.<br>
	 * Adds the item to the first available slot (lowest item number available).
	 * 
	 * @param item
	 *            the item to add to the inventory.
	 * @return the slot number that the item took. Returns -1 if it was not
	 *         added (meaning the inventory is full).
	 */
	public int add(Item item) {
		for (int itemNo = 0; itemNo < this.items.length; itemNo++) {
			if (this.items[itemNo] == null) {
				item.setState(ItemState.INVENTORY);
				this.items[itemNo] = item;
				this.noOfItems++;
				return itemNo;
			}
		}

		return -1;
	}

	/**
	 * Removes an item from the inventory.
	 * 
	 * @param item
	 *            a reference to the item to remove.
	 * @return the slot number that the item was removed from. Returns -1 if it
	 *         was not removed (meaning the item was not found).
	 */
	public int remove(Item item) {
		for (int itemNo = 0; itemNo < this.items.length; itemNo++) {
			if (this.items[itemNo] == item) {
				this.items[itemNo] = null;
				this.noOfItems--;
				return itemNo;
			}
		}

		return -1;
	}

	/**
	 * Gets the number of items currently in the inventory.
	 * 
	 * @return the number of items currently in the inventory.
	 */
	public int getNoOfItems() {
		return this.noOfItems;
	}
}