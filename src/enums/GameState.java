package enums;

import java.awt.Graphics;

import utilities.Assets;
import utilities.World;

public class GameState {
	private World world;

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

	public void render(Graphics g) {
		switch (gameState) {
		case LOBBY:
			break;
		case INGAME:
			world.render(g);
			break;
		}
	}

	public State getGameState() {
		return gameState;
	}

	public void setGameState(State gameState, Assets assets) {
		this.gameState = gameState;
		if (gameState == State.INGAME) {
			world = new World(assets);
		}
	}
}
