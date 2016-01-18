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
		LOBBY, INGAME, PAUSE, FINISH
	}

	private State gameState;

	public void update() {
		switch (gameState) {
		case LOBBY:
			game.getDisplay().getMain().update();
			break;
		case INGAME:
			game.getDisplay().getGamePanel().update();
			break;
		case PAUSE:
			game.getDisplay().getPause().update();
			break;
		case FINISH:
			game.getDisplay().getFinish().update();
			break;
		}
	}

	public void render(Graphics g) {
		switch (gameState) {
		case LOBBY:
			game.getDisplay().getMain().render(g);
			break;
		case INGAME:
			game.getDisplay().getGamePanel().render(g);
			break;
		case PAUSE:
			game.getDisplay().getPause().render(g);
			break;
		case FINISH:
			game.getDisplay().getFinish().render(g);
			break;
		}
	}

	public State getGameState() {
		return gameState;
	}

	public void setGameState(State gameState, boolean pause) {
		this.gameState = gameState;
		switch (gameState) {
		case LOBBY:
			game.getDisplay().getMain().setup(game);
			break;
		case INGAME:
			if (!pause)
				game.getDisplay().getGamePanel().setup(game);
			break;
		case PAUSE:
			game.getDisplay().getPause().setup(game);
			break;
		case FINISH:
			game.getDisplay().getFinish().setup(game);
			break;
		}
	}
}
