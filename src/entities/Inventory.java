package entities;

import items.Item;

/**
 * Inventory class for inventories of entities in Project Z.
 * 
 * @author Patrick Liu, Eric Chee, Allen Han, Alosha Reymer
 * @since 1.0
 * @version 1.0
 */
public class Inventory {
	public static final int NO_OF_ITEMS = 10;
	private Item[] items;

	public Inventory() {
		this.items = new Item[this.NO_OF_ITEMS];
	}

	public Item get(int itemNo) {
		if (itemNo < 0 || itemNo > this.NO_OF_ITEMS) {
			return this.items[itemNo];
		} else {
			return null;
		}
	}

	public int add(Item item) {
		for (int itemNo = 0; itemNo < this.items.length; itemNo++) {
			if (this.items[itemNo] == null) {
				this.items[itemNo] = item;
				return itemNo;
			}
		}
		
		return -1;
	}
	
	public int remove(Item item) {
		for (int itemNo = 0; itemNo < this.items.length; itemNo++) {
			if (this.items[itemNo] == item) {
				this.items[itemNo] = null;
				return itemNo;
			}
		}
		
		return -1;
	}
	
	public Item remove(int itemNo) {
		if (this.items[itemNo] != null) {
			Item removedItem = this.items[itemNo];
			this.items[itemNo] = null;
			return removedItem;
		} else {
			return null;
		}
	}
}