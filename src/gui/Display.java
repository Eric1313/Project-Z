/**
 * Creates the display for the game
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @since 1.0
 * @version 1.0
 */
package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import utilities.KeyHandler;
import utilities.MouseHandler;

public class Display {
	private JFrame frame;
	private GameScreen game;
	private MainScreen main;
	private PauseScreen pause;
	private FinishScreen finish;
	private DeathScreen death;
	private ScoreScreen score;
	private HelpScreen help;
	private MouseHandler mouseHandler;
	private KeyHandler keyHandler;
	private String title;
	private int width;
	private int height;

	/**
	 * Constructor for the Display.
	 * 
	 * @param title
	 *            the title of the game.
	 * @param width
	 *            the width of the frame.
	 * @param height
	 *            the height of the frame.
	 */
	public Display(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		createDisplay();
	}

	/**
	 * Creates the display.
	 */
	private void createDisplay() {
		// Creates the main display
		game = new GameScreen();
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);

		// Sets the size of the game
		game.setPreferredSize(new Dimension(width, height));
		game.setMaximumSize(new Dimension(width, height));
		game.setMinimumSize(new Dimension(width, height));
		game.setFocusable(false);
		mouseHandler = new MouseHandler();
		// Adds a mouse to the game so that we can receive mouse input
		game.addMouseMotionListener(mouseHandler);
		game.addMouseListener(mouseHandler);
		game.addMouseWheelListener(mouseHandler);

		// Creates the other screens
		main = new MainScreen(null);
		pause = new PauseScreen(null);
		finish = new FinishScreen(null);
		death = new DeathScreen(null);
		score = new ScoreScreen(null);
		help = new HelpScreen(null);

		// Adds a key handler to the frame so we can receive key input
		keyHandler = new KeyHandler();
		frame.addKeyListener(keyHandler);
		frame.add(game);
		frame.pack();

		frame.setVisible(true);
	}

	public MainScreen getMain() {
		return main;
	}

	public PauseScreen getPause() {
		return pause;
	}

	public DeathScreen getDeath() {
		return death;
	}

	public FinishScreen getFinish() {
		return finish;
	}

	public HelpScreen getHelp() {
		return help;
	}

	public ScoreScreen getScore() {
		return score;
	}

	public JFrame getFrame() {
		return frame;
	}

	public GameScreen getGameScreen() {
		return game;
	}

	public MouseHandler getMouseHandler() {
		return mouseHandler;
	}

	public KeyHandler getKeyHandler() {
		return keyHandler;
	}
}
