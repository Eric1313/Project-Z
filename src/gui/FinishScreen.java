package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import main.Game;
import entities.Zombie;
import enums.GameState.State;

public class FinishScreen extends Screen {
	private static final long serialVersionUID = 1L;
	private float colour = 30;
	private boolean decrease;
	private Rectangle next;
	private Rectangle main;
	private Rectangle exit;
	private boolean hoverNext;
	private boolean hoverMain;
	private boolean hoverExit;
	private int level;

	public FinishScreen(Game game) {
		super(game);
	}

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
		g2D.setFont(game.getZombieFontXL());
		g2D.drawString("}", 200, 650);
		g2D.setFont(game.getUiFontL());
		g2D.setColor(Color.WHITE);
		g2D.drawString("LEVEL " + level + " COMPLETE", 250, 200);
		// Draw play button
		g2D.setFont(game.getUiFont());
		FontMetrics fm = g2D.getFontMetrics();

		button(g2D, hoverNext, next, "NEXT", 512 - fm.stringWidth("NEXT") / 2,
				367, 460, 390);
		button(g2D, hoverMain, main, "MENU", 512 - fm.stringWidth("MENU") / 2,
				487, 460, 510);
		button(g2D, hoverExit, exit, "QUIT", 512 - fm.stringWidth("QUIT") / 2,
				607, 460, 630);
	}

	public void update() {
		if (next.contains(game.getDisplay().getMouseHandler()
				.getMouseLocation())) {
			hoverNext = true;
			if (game.getDisplay().getMouseHandler().isClick()) {
				game.getDisplay().getMouseHandler().setClick(false);
				Zombie.damage = (game.getLevel()) * 5;
				Zombie.zombieHealth = 100 + game.getLevel() * 50;
				game.getState().setState(
						State.INGAME,
						false,
						400 + 160 * game.getLevel(),
						game.getDisplay().getGameScreen().getWorld().getPlayer()
								.getInventory(),
						game.getDisplay().getGameScreen().getWorld().getPlayer()
								.getSkinNo());
			}
		} else {
			hoverNext = false;
		}
		if (main.contains(game.getDisplay().getMouseHandler()
				.getMouseLocation())) {
			hoverMain = true;
			if (game.getDisplay().getMouseHandler().isClick()) {
				game.getDisplay().getMouseHandler().setClick(false);
				game.getState().setState(State.LOBBY, false);
			}
		} else {
			hoverMain = false;
		}
		if (exit.contains(game.getDisplay().getMouseHandler()
				.getMouseLocation())) {
			hoverExit = true;
			if (game.getDisplay().getMouseHandler().isClick()) {
				System.exit(0);
			}
		} else {
			hoverExit = false;
		}
		game.getDisplay().getMouseHandler().setClick(false);
		// game.getDisplay().getGamePanel().getWorld();
		// game.getDisplay().getGamePanel().getWorld().getNoOfZombie();
	}

	public void setup(Game game) {
		this.game = game;
		next = new Rectangle(412, 300, 200, 100);
		main = new Rectangle(412, 420, 200, 100);
		exit = new Rectangle(412, 540, 200, 100);
		level = game.getLevel();
		game.setLevel(game.getLevel() + 1);
	}
}
