/**
 * Subclass of Screen that is the main menu
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

public class MainScreen extends Screen {
	private boolean hoverPlay;
	private boolean hoverHelp;
	private boolean hoverExit;
	private boolean hoverScore;
	private Rectangle play;
	private Rectangle help;
	private Rectangle exit;
	private Rectangle hack;
	private Rectangle scores;
	private int clicks;
	private float colour = 30;
	private boolean decrease;

	/**
	 * Constructor for the main menu.
	 * 
	 * @param game
	 *            the game.
	 */
	public MainScreen(Game game) {
		super(game);
	}

	@Override
	/**
	 * Renders the main menu.
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
		g2D.setFont(game.getZombieFontL());
		g2D.drawString("}", 375, 260);
		// Draw title
		g.drawImage(game.getMainMenu(), 0, 0, null);

		// Draws the buttons
		g2D.setFont(game.getUiFont());
		FontMetrics fm = g2D.getFontMetrics();
		button(g2D, hoverPlay, play, "PLAY", 512 - fm.stringWidth("PLAY") / 2,
				365, 460, 389);
		button(g2D, hoverHelp, help, "HELP", 512 - fm.stringWidth("HELP") / 2,
				470, 460, 494);
		button(g2D, hoverScore, scores, "SCORES",
				512 - fm.stringWidth("SCORES") / 2, 575, 460, 599);
		button(g2D, hoverExit, exit, "QUIT", 512 - fm.stringWidth("QUIT") / 2,
				680, 460, 704);

		// Displays the current level
		g2D.setColor(Color.WHITE);
		g2D.setFont(game.getUiFontS());
		g2D.drawString("LEVEL: " + game.getLevel(), 5, 25);
		// Credits
		g2D.setFont(game.getUiFontXS());
		g2D.drawString(
				"Ver. 1.0 CREATED BY ALLEN HAN, ALOSHA REYMER, ERIC CHEE, & PATRICK LIU",
				680, 760);
	}

	@Override
	/**
	 * Updates the main menu.
	 */
	public void update() {
		// Detects if a button is pressed
		if (play.contains(game.getDisplay().getMouseHandler()
				.getMouseLocation())) {
			hoverPlay = true;
			// Starts the game
			if (game.getDisplay().getMouseHandler().isClick()) {
				game.getState().setState(State.INGAME, false);
				game.getDisplay().getMouseHandler().setClick(false);
			}
		} else {
			hoverPlay = false;
		}
		if (help.contains(game.getDisplay().getMouseHandler()
				.getMouseLocation())) {
			hoverHelp = true;
			// Goes to the help menu.
			if (game.getDisplay().getMouseHandler().isClick()) {
				game.getState().setState(State.HELP, false);
				game.getDisplay().getMouseHandler().setClick(false);
			}
		} else {
			hoverHelp = false;
		}
		if (exit.contains(game.getDisplay().getMouseHandler()
				.getMouseLocation())) {
			hoverExit = true;
			// Exits the game
			if (game.getDisplay().getMouseHandler().isClick()) {
				System.exit(0);
			}
		} else {
			hoverExit = false;
		}
		if (scores.contains(game.getDisplay().getMouseHandler()
				.getMouseLocation())) {
			hoverScore = true;
			// Exits the game
			if (game.getDisplay().getMouseHandler().isClick()) {
				game.getState().setState(State.SCORE, false);
				game.getDisplay().getMouseHandler().setClick(false);
			}
		} else {
			hoverScore = false;
		}
		// If the giant hand at the beginning of the game is pressed 10 times
		// enable debug mode
		if (hack.contains(game.getDisplay().getMouseHandler()
				.getMouseLocation())) {
			if (game.getDisplay().getMouseHandler().isClick()) {
				clicks++;
				if (clicks >= 10) {
					game.getState().setDebug(true);
				}
				game.getDisplay().getMouseHandler().setClick(false);
			}
		}
		game.getDisplay().getMouseHandler().setClick(false);
	}

	/**
	 * Setup the main menu.
	 * 
	 * @param game
	 *            the game.
	 */
	public void setup(Game game) {
		this.game = game;
		// Creates the basic box for the buttons
		play = new Rectangle(412, 300, 200, 95);
		help = new Rectangle(412, 405, 200, 95);
		scores = new Rectangle(412, 510, 200, 95);
		exit = new Rectangle(412, 615, 200, 95);
		hack = new Rectangle(375, 30, 210, 260);
		this.clicks = 0;
		game.getState().setDebug(false);
	}
}
