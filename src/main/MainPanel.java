package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import enums.GameState.State;

public class MainPanel extends Canvas {
	private static final long serialVersionUID = 1L;
	private Game game;
	private boolean hover;
	private Rectangle play;

	// Used for pulsating hand.
	private float colour = 30;
	private boolean decrease;

	public void render(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		// Make the background black
		g2D.setColor(Color.BLACK);
		g2D.fillRect(0, 0, game.getDisplay().getFrame().getWidth(), game
				.getDisplay().getFrame().getHeight());
		g.drawRect(0, 0, game.getDisplay().getFrame().getWidth(), game
				.getDisplay().getFrame().getHeight());
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
		g2D.setFont(game.getZombieFontBig());
		g2D.drawString("}", 375, 260);
		// Draw title
		g.drawImage(game.getMainMenu(), 0, 0, null);
		g2D.setColor(Color.WHITE);
		if (hover) {
			g2D.setPaint(Color.WHITE);
			g2D.fill(play);
		}
		g2D.draw(play);
		if (hover) {
			g.setColor(new Color(152, 0, 0));
			g.setFont(game.getZombieFont());
			g.drawString("}", 460, 390);
		}
		g.setFont(game.getUiFont());
		if (hover) {
			g.setColor(Color.BLACK);
		} else {
			g.setColor(Color.WHITE);
		}
		g.drawString("PLAY", 475, 367);
	}

	public void update() {
		if (play.contains(game.getDisplay().getMouseHandler()
				.getMouseLocation())) {
			hover = true;
			if (game.getDisplay().getMouseHandler().isClick()) {
				game.getState().setGameState(State.INGAME);
			}
		} else {
			hover = false;
		}
	}

	public void setup(Game game) {
		this.game = game;
		play = new Rectangle(412, 300, 200, 100);
	}
}
