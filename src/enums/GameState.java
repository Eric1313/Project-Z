/**
 * GameState object for storing and changing the game state of the game.
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @since 1.0
 * @version 1.0
 */
package enums;

import java.awt.Graphics;

import main.Game;
import entities.Inventory;

public class GameState {
	private Game game;
	private State state;
	private boolean debug;

	/**
	 * Constructor for the GameState.
	 * 
	 * @param game
	 *            the game.
	 */
	public GameState(Game game) {
		this.game = game;
	}

	/**
	 * Available states that can be used <li>{@link #LOBBY}</li> <li>
	 * {@link #INGAME}</li> <li>{@link #PAUSE}</li> <li>{@link #FINISH}</li> <li>
	 * {@link #DEATH}</li>
	 */
	public enum State {
		/**
		 * The main menu of the game.
		 */
		LOBBY,
		/**
		 * The actual game.
		 */
		INGAME,
		/**
		 * The pause menu.
		 */
		PAUSE,
		/**
		 * The finish menu after you finish a level.
		 */
		FINISH,
		/**
		 * The death menu if you die.
		 */
		DEATH,
		/**
		 * The help menu.
		 */
		HELP
	}

	/**
	 * Updates the selected state.
	 */
	public void update() {
		switch (this.state) {
		case LOBBY:
			this.game.getDisplay().getMain().update();
			break;
		case INGAME:
			this.game.getDisplay().getGameScreen().update();
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

	/**
	 * Renders the selected state.
	 * 
	 * @param g
	 *            the graphics object used to render to the screen.
	 */
	public void render(Graphics g) {
		switch (this.state) {
		case LOBBY:
			this.game.getDisplay().getMain().render(g);
			break;
		case INGAME:
			this.game.getDisplay().getGameScreen().render(g);
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

	/**
	 * Sets the state.
	 * 
	 * @param state
	 *            the state to be set to.
	 * @param pause
	 *            if the state is being set from the game then do not make a new
	 *            game.
	 */
	public void setState(State state, boolean pause) {
		this.state = state;
		switch (this.state) {
		case LOBBY:
			this.game.getDisplay().getMain().setup(this.game);
			break;
		case INGAME:
			if (!pause) {
				this.game.getDisplay().getGameScreen().setup(this.game);
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
			this.game.getDisplay().getHelp().setup(this.game, pause);
			break;
		}
	}

	/**
	 * Sets the state of the game. This overloaded method is used to make a new
	 * level.
	 * 
	 * @param state
	 *            the state to set to.
	 * @param pause
	 *            if you are pausing the game or not.
	 * @param size
	 *            the size of the next map.
	 * @param inventory
	 *            the player's current inventory so that it is saved.
	 * @param skinNo
	 *            the player's current skin color.
	 */
	public void setState(State state, boolean pause, int size,
			Inventory inventory, int skinNo) {
		this.state = state;
		switch (this.state) {
		case LOBBY:
			this.game.getDisplay().getMain().setup(this.game);
			break;
		case INGAME:
			if (!pause) {
				this.game.getDisplay().getGameScreen()
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
			this.game.getDisplay().getHelp().setup(this.game, pause);
			break;
		}
	}

	public State getGameState() {
		return state;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}