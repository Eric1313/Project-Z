package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import enums.GameState.State;

public class HelpPanel extends Canvas {
	private static final long serialVersionUID = 1L;
	private Game game;
	private boolean previousHover;
	private boolean nextHover;
	private Rectangle previous;
	private Rectangle next;
	private int image;

	public void render(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(game.getHelp()[image], 0, 0, null);
		if (image != 0) {
			button(g2D, previousHover, previous, "PREVIOUS", 50, 705, 70, 725);
		}
		if (image != 5){
			button(g2D, nextHover, next, "NEXT", 866, 705, 855, 725);
		}else {
			button(g2D, nextHover, next, "MAIN", 866, 705, 855, 725);
		}
	}

	public void button(Graphics2D g2D, boolean hover, Rectangle box,
			String text, int textX, int textY, int handX, int handY) {
		if (hover) {
			g2D.setPaint(Color.WHITE);
			g2D.fill(box);
		} else {
			g2D.setColor(Color.BLACK);
			g2D.fill(box);
		}
		g2D.setColor(Color.WHITE);
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
		if (previous.contains(game.getDisplay().getMouseHandler()
				.getMouseLocation())
				&& image != 0) {
			previousHover = true;
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
			if (game.getDisplay().getMouseHandler().isClick() && image != 5) {
				image++;
				game.getDisplay().getMouseHandler().setClick(false);
			} else if (game.getDisplay().getMouseHandler().isClick()
					&& image == 5) {
				game.getState().setGameState(State.LOBBY, false);
				game.getDisplay().getMouseHandler().setClick(false);
			}
		} else {
			nextHover = false;
		}
		game.getDisplay().getMouseHandler().setClick(false);
	}

	public void setup(Game game) {
		this.game = game;
		previous = new Rectangle(20, 635, 200, 100);
		next = new Rectangle(804, 635, 200, 100);
		image = 0;
	}
}
