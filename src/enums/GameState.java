package enums;

import java.awt.Graphics;

import main.Game;

/**
 * GameState object for storing and changing the game state of the game.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see main.Game
 * @since 1.0
 * @version 1.0
 */
public class GameState {
	private Game game;

	public GameState(Game game) {
		this.game = game;
	}

	public enum State {
		LOBBY, INGAME
	}

	private State gameState;

	public void update() {
		switch (gameState) {
		case LOBBY:
			break;
		case INGAME:
			game.getDisplay().getGamePanel().update();
			break;
		}
	}

	public void render(Graphics g) {
		switch (gameState) {
		case LOBBY:
			break;
		case INGAME:
			game.getDisplay().getGamePanel().render(g);
			break;
		}
	}

	public State getGameState() {
		return gameState;
	}

	public void setGameState(State gameState) {
		this.gameState = gameState;
		switch (gameState) {
		case LOBBY:
			break;
		case INGAME:
			game.getDisplay().switchPanel("Game");
			game.getDisplay().getGamePanel().setup(game);
			break;
		}
	}
}
