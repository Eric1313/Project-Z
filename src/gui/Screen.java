/**
 * A generic screen
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @since 1.0
 * @version 1.0
 */
package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.Game;

public abstract class 	Screen {
	protected Game game;

	public Screen(Game game) {
		this.game = game;
	}

	public abstract void render(Graphics g);

	public abstract void update();

	/**
	 * Creates the button on the screen.
	 * 
	 * @param g2D
	 *            the graphics object used to draw on to the screen.
	 * @param hover
	 *            if the object is being hovered over or not.
	 * @param box
	 *            the box of the button.
	 * @param text
	 *            the text to be displayed on the button.
	 * @param textX
	 *            the x location of the button.
	 * @param textY
	 *            the y location of the button.
	 * @param handX
	 *            the x location of the hand.
	 * @param handY
	 *            the y location of the hand.
	 */
	public void button(Graphics2D g2D, boolean hover, Rectangle box,
			String text, int textX, int textY, int handX, int handY) {
		g2D.setColor(Color.WHITE);
		if (hover) {
			g2D.setPaint(Color.WHITE);
			g2D.fill(box);
		}
		// Draws the box of the button
		g2D.draw(box);
		if (hover) {
			g2D.setColor(new Color(152, 0, 0));
			g2D.setFont(game.getZombieFont());
			// Draws the hand on the button
			g2D.drawString("}", handX, handY);
		}
		if (game != null) {
		}
		g2D.setFont(game.getUiFont());
		if (hover) {
			g2D.setColor(Color.BLACK);
		} else {
			g2D.setColor(Color.WHITE);
		}
		// Draws the text on the button
		g2D.drawString(text, textX, textY);
	}
}
