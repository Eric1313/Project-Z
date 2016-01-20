package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import main.Game;
import enums.GameState.State;

/**
 * Subclass of Screen that is displayed when the user needs help
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Screen
 * @since 1.0
 * @version 1.0
 */
public class HelpScreen extends Screen {
	private boolean previousHover;
	private boolean nextHover;
	private Rectangle previous;
	private Rectangle next;
	private boolean pause;
	private int image;

	/**
	 * Constructor for the HelpScreen.
	 * 
	 * @param game
	 *            the game.
	 */
	public HelpScreen(Game game) {
		super(game);
	}

	@Override
	/**
	 * Renders the help screen onto the frame.
	 */
	public void render(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// Draws the instruction onto the screen
		g.drawImage(game.getHelp()[image], 0, 0, null);

		// Draws the buttons to the screen
		g2D.setFont(game.getUiFont());
		FontMetrics fm = g2D.getFontMetrics();
		if (image != 0) {
			button(g2D, previousHover, previous, "PREVIOUS",
					120 - fm.stringWidth("PREVIOUS") / 2, 705, 70, 725);
		}
		if (image != game.getHelp().length - 1) {
			button(g2D, nextHover, next, "NEXT",
					904 - fm.stringWidth("NEXT") / 2, 705, 855, 725);
		} else if (!pause) {
			button(g2D, nextHover, next, "MENU",
					904 - fm.stringWidth("MENU") / 2, 705, 855, 725);
		} else {
			button(g2D, nextHover, next, "PAUSE",
					904 - fm.stringWidth("PAUSE") / 2, 705, 855, 725);
		}
	}

	@Override
	/**
	 * Updates the help screen.
	 */
	public void update() {
		// Detects if a button is pressed
		if (previous.contains(game.getDisplay().getMouseHandler()
				.getMouseLocation())
				&& image != 0) {
			previousHover = true;
			// Goes to the previous instruction
			if (game.getDisplay().getMouseHandler().isClick()) {
				image--;
				game.getDisplay().getMouseHandler().setClick(false);
			}
		} else {
			previousHover = false;
		}
		if (next.contains(game.getDisplay().getMouseHandler()
				.getMouseLocation())) {
			nextHover = true;
			// Goes to the next instruction
			if (game.getDisplay().getMouseHandler().isClick()
					&& image != game.getHelp().length - 1) {
				image++;
				game.getDisplay().getMouseHandler().setClick(false);
				// Returns to the menu
			} else if (game.getDisplay().getMouseHandler().isClick()
					&& image == game.getHelp().length - 1 && !pause) {
				game.getState().setState(State.LOBBY, false);
				game.getDisplay().getMouseHandler().setClick(false);
				// Returns to the pause screen
			} else if (game.getDisplay().getMouseHandler().isClick()
					&& image == game.getHelp().length - 1 && pause) {
				game.getState().setState(State.PAUSE, false);
				game.getDisplay().getMouseHandler().setClick(false);
			}
		} else {
			nextHover = false;
		}
		game.getDisplay().getMouseHandler().setClick(false);
	}

	/**
	 * Setup the help screen.
	 * 
	 * @param game
	 *            the game.
	 * @param pause
	 *            if the screen linking this one is the pause screen or not.
	 */
	public void setup(Game game, boolean pause) {
		this.game = game;
		this.pause = pause;
		// Creates the outlines for the buttons
		previous = new Rectangle(20, 635, 200, 100);
		next = new Rectangle(804, 635, 200, 100);
		image = 0;
	}

	@Override
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
		// Draws the button outline
		if (hover) {
			g2D.setPaint(Color.WHITE);
			g2D.fill(box);
		} else {
			g2D.setColor(Color.BLACK);
			g2D.fill(box);
		}
		g2D.setColor(Color.WHITE);
		g2D.draw(box);
		// Draws the zombie hand
		if (hover) {
			g2D.setColor(new Color(152, 0, 0));
			g2D.setFont(game.getZombieFont());
			g2D.drawString("}", handX, handY);
		}
		g2D.setFont(game.getUiFont());
		if (hover) {
			g2D.setColor(Color.BLACK);
		} else {
			g2D.setColor(Color.WHITE);
		}
		// Draws the button text
		g2D.drawString(text, textX, textY);
	}
}
