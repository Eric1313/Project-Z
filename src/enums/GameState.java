package enums;

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
