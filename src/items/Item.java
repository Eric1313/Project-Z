package items;

import java.awt.image.BufferedImage;
import enums.ItemState;
/**
 * Abstract Item class for all items in Project Z.
 * 
 * @author Patrick Liu, Eric Chee, Allen Han, Alosha Reymer
 * @since 1.0
 * @version 1.0
 */
public abstract class Item {
	protected BufferedImage[] images;
	protected int itemID;
	protected String name;
	protected boolean held=false;
	protected ItemState state;

	public Item(String name, int itemID, BufferedImage[] images,ItemState state) {
		this.name = name;
		this.itemID = itemID;
		this.images = images;
		this.state=state;
	}

	/**
	 * @return the images
	 */
	public BufferedImage[] getImages() {
		return images;
	}

	/**
	 * @param images
	 *            the images to set
	 */
	public void setImages(BufferedImage[] images) {
		this.images = images;
	}

	/**
	 * @return the itemID
	 */
	public int getItemID() {
		return itemID;
	}

	/**
	 * @param itemID
	 *            the itemID to set
	 */
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the held
	 */
	public boolean isHeld() {
		return held;
	}

	/**
	 * @param held the held to set
	 */
	public void setHeld(boolean held) {
		this.held = held;
	}
	

}