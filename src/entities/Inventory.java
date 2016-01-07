package entities;

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

	public Inventory() {
		this.items = new Item[Inventory.NO_OF_ITEMS];
	}

	/**
	 * Gets the Item that is in a given slot of the Inventory.
	 * 
	 * @param itemNo
	 *            the item number (from 0-9) of the Item to find.
	 * @return the Item in the item number's slot. Returns null if it is empty.
	 */
	public Item get(int itemNo) {
		if (itemNo < 0 || itemNo > this.items.length) {
			return this.items[itemNo];
		} else {
			return null;
		}
	}

	/**
	 * Gets the item number of a given Item in the Inventory.
	 * 
	 * @param item
	 *            the Item to find in the Inventory.
	 * @return the item number of the Item searching for. Returns -1 if it was
	 *         not found.
	 */
	public int get(Item item) {
		for (int itemNo = 0; itemNo < this.items.length; itemNo++) {
			if (this.items[itemNo] == item) {
				return itemNo;
			}
		}

		return -1;
	}

	/**
	 * Adds an Item to the Inventory.<br>
	 * Adds the Item to the first available slot (lowest item number available).
	 * 
	 * @param item
	 *            the Item to add to the Inventory.
	 * @return the slot number that the Item took. Returns -1 if it was not
	 *         added (meaning the Inventory is full).
	 */
	public int add(Item item) {
		for (int itemNo = 0; itemNo < this.items.length; itemNo++) {
			if (this.items[itemNo] == null) {
				this.items[itemNo] = item;
				return itemNo;
			}
		}

		return -1;
	}

	/**
	 * Removes an item from the Inventory.
	 * 
	 * @param item
	 *            a reference to the Item to remove.
	 * @return the slot number that the Item was removed from. Returns -1 if it
	 *         was not removed (meaning the Item was not found).
	 */
	public int remove(Item item) {
		for (int itemNo = 0; itemNo < this.items.length; itemNo++) {
			if (this.items[itemNo] == item) {
				this.items[itemNo] = null;
				return itemNo;
			}
		}

		return -1;
	}

	/**
	 * Removes an item from the Inventory.
	 * 
	 * @param itemNo
	 *            the item number (from 0-9) of the Item to remove.
	 * @return the Item that was removed. Returns null if there was no item
	 *         removed.
	 */
	public Item remove(int itemNo) {
		if (itemNo >= this.items.length || itemNo < 0
				|| this.items[itemNo] == null) {
			return null;
		} else {
			Item removedItem = this.items[itemNo];
			this.items[itemNo] = null;
			return removedItem;
		}
	}
}