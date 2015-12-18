package states;

import java.awt.Graphics;

import main.Game;

public abstract class State {
	private static State currentState = null;

	protected static Game game;

	public State(Game game) {
		State.game = game;
	}

	public abstract void update();

	public abstract void render(Graphics g);

	public static void setState(State state) {
		currentState = state;
	}

	public static State getState() {
		return currentState;
	}
}
