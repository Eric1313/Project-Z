package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import enums.GameState.State;
import main.Game;

/**
 * Subclass of Screen that contains the highscores
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see Screen
 * @since 1.0
 * @version 1.0
 */
public class ScoreScreen extends Screen {
	private float colour = 30;
	private boolean decrease;
	private Rectangle back;
	private boolean backHover;

	/**
	 * Constructor for the high scores.
	 * 
	 * @param game
	 *            the game.
	 */
	public ScoreScreen(Game game) {
		super(game);
	}

	@Override
	/**
	 * Renders everything to the screen.
	 * 
	 * @param g
	 *            the graphics variable to render with.
	 */
	public void render(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;

		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Makes the background black
		g2D.setColor(Color.BLACK);
		g2D.fillRect(0, 0, game.getDisplay().getFrame().getWidth(), game.getDisplay().getFrame().getHeight());
		g.drawRect(0, 0, game.getDisplay().getFrame().getWidth(), game.getDisplay().getFrame().getHeight());

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

		// Draws the high scores to the screen
		g2D.setColor(Color.WHITE);
		g2D.setFont(game.getUiFontS());
		for (int i = 0; i < game.getScores()[0].length; i++) {
			g.drawString(String.format("%d.%35s%38s", i + 1, game.getScores()[0][i], game.getScores()[1][i]), 350,
					300 + (50 * i));

		}
		// Draws the header
		g2D.drawString(String.format("%s%27s%33s", "PLACEMENT", "LEVEL", "NAME"), 310, 270);
		g2D.setFont(game.getUiFontL());
		// Draws the title
		g.drawString("HIGHSCORES", 320, 200);

		// Draws the button
		g2D.setFont(game.getUiFont());
		FontMetrics fm = g2D.getFontMetrics();
		button(g2D, backHover, back, "BACK", 120 - fm.stringWidth("back") / 2, 705, 70, 725);
	}

	@Override
	/**
	 * Updates the screen.
	 */
	public void update() {
		if (back.contains(game.getDisplay().getMouseHandler().getMouseLocation())) {
			backHover = true;
			// Goes to the main menu
			if (game.getDisplay().getMouseHandler().isClick()) {
				game.getState().setState(State.LOBBY, false);
				game.getDisplay().getMouseHandler().setClick(false);
			}
		} else {
			backHover = false;
		}
	}

	/**
	 * Setup the score screen.
	 * 
	 * @param game
	 *            the game.
	 */
	public void setup(Game game) {
		this.game = game;
		back = new Rectangle(20, 635, 200, 100);
	}
}
