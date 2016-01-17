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

	public void render(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g.drawImage(game.getMainMenu(), 0, 0, null);

		if (hover) {
			g2D.setPaint(Color.WHITE);
			g2D.fill(play);
		}
		g2D.draw(play);
		if (hover) {
			g.setColor(Color.RED);
			g.setFont(game.getZombieFont());
			g.drawString("}", 477, 378);
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
			if (game.getDisplay().getMouseHandler().isClick()){
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
