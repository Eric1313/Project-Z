package enums;

/**
 * GameState object for storing and changing the game state of the game.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @see main.Game
 * @since 1.0
 * @version 1.0
 */
public class GameState {
	public enum State {
		LOBBY, INGAME
	}

	private State gameState;

	public void update() {
		switch (gameState) {
		case LOBBY:
			break;
		case INGAME:
			break;
		}
	}

	public State getGameState() {
		return gameState;
	}

	public void setGameState(State gameState) {
		this.gameState = gameState;
	}
}