/**
 * GameState object for storing and changing the game state of the game.
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see main.Game
 * @since 1.0
 * @version 1.0
 */
package enums;

import java.awt.Graphics;

import main.Game;
import entities.Inventory;

public class GameState {
	private Game game;

	/**
	 * Constructor for the GameState.
	 * @param game
	 */
	public GameState(Game game) {
		this.game = game;
	}

	public enum State {
		LOBBY, INGAME, PAUSE, FINISH, DEATH, HELP
	}

	private State gameState;

	public void update() {
		switch (this.gameState) {
		case LOBBY:
			this.game.getDisplay().getMain().update();
			break;
		case INGAME:
			this.game.getDisplay().getGamePanel().update();
			break;
		case PAUSE:
			this.game.getDisplay().getPause().update();
			break;
		case FINISH:
			this.game.getDisplay().getFinish().update();
			break;
		case DEATH:
			this.game.getDisplay().getDeath().update();
			break;
		case HELP:
			this.game.getDisplay().getHelp().update();
			break;
		}
	}

	public void render(Graphics g) {
		switch (this.gameState) {
		case LOBBY:
			this.game.getDisplay().getMain().render(g);
			break;
		case INGAME:
			this.game.getDisplay().getGamePanel().render(g);
			break;
		case PAUSE:
			this.game.getDisplay().getPause().render(g);
			break;
		case FINISH:
			this.game.getDisplay().getFinish().render(g);
			break;
		case DEATH:
			this.game.getDisplay().getDeath().render(g);
			break;
		case HELP:
			this.game.getDisplay().getHelp().render(g);
			break;
		}
	}

	public State getGameState() {
		return gameState;
	}

	public void setGameState(State gameState, boolean pause) {
		this.gameState = gameState;
		switch (this.gameState) {
		case LOBBY:
			this.game.getDisplay().getMain().setup(this.game);
			break;
		case INGAME:
			if (!pause) {
				this.game.getDisplay().getGamePanel().setup(this.game);
			}
			break;
		case PAUSE:
			this.game.getDisplay().getPause().setup(this.game);
			break;
		case FINISH:
			this.game.getDisplay().getFinish().setup(this.game);
			break;
		case DEATH:
			this.game.getDisplay().getDeath().setup(this.game);
			break;
		case HELP:
			this.game.getDisplay().getHelp().setup(this.game);
			break;
		}
	}

	public void setGameState(State gameState, boolean pause, int size,
			Inventory inventory, int skinNo) {
		this.gameState = gameState;
		switch (this.gameState) {
		case LOBBY:
			this.game.getDisplay().getMain().setup(this.game);
			break;
		case INGAME:
			if (!pause) {
				this.game.getDisplay().getGamePanel()
						.setup(this.game, size, inventory, skinNo);
			}
			break;
		case PAUSE:
			this.game.getDisplay().getPause().setup(this.game);
			break;
		case FINISH:
			this.game.getDisplay().getFinish().setup(this.game);
			break;
		case DEATH:
			this.game.getDisplay().getDeath().setup(this.game);
			break;
		case HELP:
			this.game.getDisplay().getHelp().setup(this.game);
			break;
		}
	}
}