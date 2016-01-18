package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import enums.GameState.State;

public class MainPanel extends Canvas {
	private static final long serialVersionUID = 1L;
	private Game game;
	private boolean hoverPlay;
	private boolean hoverHelp;
	private boolean hoverExit;
	private Rectangle play;
	private Rectangle help;
	private Rectangle exit;
	// Used for pulsating hand.
	private float colour = 30;
	private boolean decrease;

	public void render(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;

		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Make the background black
		g2D.setColor(Color.BLACK);
		g2D.fillRect(0, 0, game.getDisplay().getFrame().getWidth(), game.getDisplay().getFrame().getHeight());
		g.drawRect(0, 0, game.getDisplay().getFrame().getWidth(), game.getDisplay().getFrame().getHeight());
		// Draws the hand
		// Pulsates the hand
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
		g2D.setFont(game.getZombieFontL());
		g2D.drawString("}", 375, 260);
		// Draw title
		g.drawImage(game.getMainMenu(), 0, 0, null);
		// Draw play button
		button(g2D, hoverPlay, play, "PLAY", 475, 367, 460, 390);
		button(g2D, hoverHelp, help, "HELP", 475, 487, 460, 510);
		button(g2D, hoverExit, exit, "QUIT", 475, 607, 460, 630);
	}

	public void button(Graphics2D g2D, boolean hover, Rectangle box, String text, int textX, int textY, int handX,
			int handY) {
		g2D.setColor(Color.WHITE);
		if (hover) {
			g2D.setPaint(Color.WHITE);
			g2D.fill(box);
		}
		g2D.draw(box);
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
		g2D.drawString(text, textX, textY);
	}

	public void update() {
		if (play.contains(game.getDisplay().getMouseHandler().getMouseLocation())) {
			hoverPlay = true;
			if (game.getDisplay().getMouseHandler().isClick()) {
				game.getState().setGameState(State.INGAME, false);
				game.getDisplay().getMouseHandler().setClick(false);
			}
		} else {
			hoverPlay = false;
		}
		if (help.contains(game.getDisplay().getMouseHandler().getMouseLocation())) {
			hoverHelp = true;
			if (game.getDisplay().getMouseHandler().isClick()) {
				game.getState().setGameState(State.HELP, false);
				game.getDisplay().getMouseHandler().setClick(false);
			}
		} else {
			hoverHelp = false;
		}
		if (exit.contains(game.getDisplay().getMouseHandler().getMouseLocation())) {
			hoverExit = true;
			if (game.getDisplay().getMouseHandler().isClick()) {
				System.exit(0);
			}
		} else {
			hoverExit = false;
		}
		game.getDisplay().getMouseHandler().setClick(false);
	}

	public void setup(Game game) {
		this.game = game;
		play = new Rectangle(412, 300, 200, 100);
		help = new Rectangle(412, 420, 200, 100);
		exit = new Rectangle(412, 540, 200, 100);
	}
}
