/**
 * Subclass of Screen that is displayed when the game is paused
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Screen
 * @since 1.0
 * @version 1.0
 */
package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import main.Game;
import enums.GameState.State;

public class PauseScreen extends Screen {
	private float colour = 30;
	private boolean decrease;

	private Rectangle resume;
	private Rectangle main;
	private Rectangle help;
	private boolean hoverResume;
	private boolean hoverMain;
	private boolean hoverHelp;

	/**
	 * Constructor for the pause screen.
	 * 
	 * @param game
	 *            the game.
	 */
	public PauseScreen(Game game) {
		super(game);
	}

	/**
	 * Renders the pause screen to the frame.
	 */
	public void render(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// Make the background black
		g2D.setColor(Color.BLACK);
		g2D.fillRect(0, 0, game.getDisplay().getFrame().getWidth(), game
				.getDisplay().getFrame().getHeight());
		g.drawRect(0, 0, game.getDisplay().getFrame().getWidth(), game
				.getDisplay().getFrame().getHeight());
		// Changes the color of the hand
		g.setColor(new Color((int) colour, 0, 0));
		if (!decrease || colour == 30) {
			decrease = false;
			colour += 0.5;
		} else {
			colour -= 0.5;
		}
		if (colour == 160) {
			decrease = true;
		}
		// Draws the hand
		g2D.setFont(game.getZombieFontXL());
		g2D.drawString("}", 200, 650);

		// Draws the "Paused" title
		g2D.setFont(game.getUiFontL());
		g2D.setColor(Color.WHITE);
		g2D.drawString("PAUSED", 400, 200);

		// Draws the buttons
		g2D.setFont(game.getUiFont());
		FontMetrics fm = g2D.getFontMetrics();
		button(g2D, hoverResume, resume, "RESUME",
				512 - fm.stringWidth("RESUME") / 2, 367, 460, 390);
		button(g2D, hoverHelp, help, "HELP", 512 - fm.stringWidth("HELP") / 2,
				487, 460, 510);
		button(g2D, hoverMain, main, "MENU", 512 - fm.stringWidth("MENU") / 2,
				607, 460, 630);
	}

	/**
	 * Updates the pause screen.
	 */
	public void update() {
		// Detects if a button is pressed
		if (game.getDisplay().getKeyHandler().isEsc()) {
			// If escape is pressed unpauses the game
			game.getState().setState(State.INGAME, true);
			game.getDisplay().getKeyHandler().setEsc(false);
		}
		if (resume.contains(game.getDisplay().getMouseHandler()
				.getMouseLocation())) {
			hoverResume = true;
			// Resumes the game
			if (game.getDisplay().getMouseHandler().isClick()) {
				game.getDisplay().getMouseHandler().setClick(false);
				game.getState().setState(State.INGAME, true);
			}
		} else {
			hoverResume = false;
		}
		if (main.contains(game.getDisplay().getMouseHandler()
				.getMouseLocation())) {
			hoverMain = true;
			// Goes back to the main menu
			if (game.getDisplay().getMouseHandler().isClick()) {
				game.getDisplay().getMouseHandler().setClick(false);
				game.getState().setState(State.LOBBY, false);
			}
		} else {
			hoverMain = false;
		}
		if (help.contains(game.getDisplay().getMouseHandler()
				.getMouseLocation())) {
			hoverHelp = true;
			// Displays the help
			if (game.getDisplay().getMouseHandler().isClick()) {
				game.getDisplay().getMouseHandler().setClick(false);
				game.getState().setState(State.HELP, true);
			}
		} else {
			hoverHelp = false;
		}
		game.getDisplay().getMouseHandler().setClick(false);
	}

	/**
	 * Setup the pause screen.
	 * 
	 * @param game
	 *            the game.
	 */
	public void setup(Game game) {
		this.game = game;
		// Outlines for the buttons.FS
		resume = new Rectangle(412, 300, 200, 100);
		main = new Rectangle(412, 540, 200, 100);
		help = new Rectangle(412, 420, 200, 100);
	}
}