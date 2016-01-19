package gui;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import utilities.KeyHandler;
import utilities.MouseHandler;

public class Display {
	private JFrame frame;
	private GameScreen gamePanel;
	private MainScreen main;
	private PauseScreen pause;
	private FinishScreen finish;
	private DeathScreen death;
	private HelpScreen help;
	private JPanel panelContainer;
	private CardLayout cardLayout;
	private MouseHandler mouseHandler;
	private KeyHandler keyHandler;
	private String title;
	private int width;
	private int height;

	public Display(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		createDisplay();
	}

	private void createDisplay() {
		panelContainer = new JPanel();
		cardLayout = new CardLayout();
		panelContainer.setLayout(cardLayout);

		gamePanel = new GameScreen();
		main = new MainScreen(null);
		pause = new PauseScreen(null);
		finish = new FinishScreen(null);
		death = new DeathScreen(null);
		help = new HelpScreen(null);

		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);

		gamePanel.setPreferredSize(new Dimension(width, height));
		gamePanel.setMaximumSize(new Dimension(width, height));
		gamePanel.setMinimumSize(new Dimension(width, height));
		gamePanel.setFocusable(false);
		mouseHandler = new MouseHandler();
		gamePanel.addMouseMotionListener(mouseHandler);
		gamePanel.addMouseListener(mouseHandler);
		gamePanel.addMouseWheelListener(mouseHandler);

		main.setPreferredSize(new Dimension(width, height));
		main.setMaximumSize(new Dimension(width, height));
		main.setMinimumSize(new Dimension(width, height));
		main.setFocusable(false);
		main.addMouseListener(mouseHandler);

		pause.setPreferredSize(new Dimension(width, height));
		pause.setMaximumSize(new Dimension(width, height));
		pause.setMinimumSize(new Dimension(width, height));
		pause.setFocusable(false);
		pause.addMouseListener(mouseHandler);

		finish.setPreferredSize(new Dimension(width, height));
		finish.setMaximumSize(new Dimension(width, height));
		finish.setMinimumSize(new Dimension(width, height));
		finish.setFocusable(false);
		finish.addMouseListener(mouseHandler);

		death.setPreferredSize(new Dimension(width, height));
		death.setMaximumSize(new Dimension(width, height));
		death.setMinimumSize(new Dimension(width, height));
		death.setFocusable(false);
		death.addMouseListener(mouseHandler);

		help.setPreferredSize(new Dimension(width, height));
		help.setMaximumSize(new Dimension(width, height));
		help.setMinimumSize(new Dimension(width, height));
		help.setFocusable(false);
		help.addMouseListener(mouseHandler);

		keyHandler = new KeyHandler();
		frame.addKeyListener(keyHandler);
		frame.add(gamePanel);
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

	public JFrame getFrame() {
		return frame;
	}

	public void switchPanel(String panel) {
		cardLayout.show(panelContainer, panel);
	}

	public GameScreen getGamePanel() {
		return gamePanel;
	}

	public MouseHandler getMouseHandler() {
		return mouseHandler;
	}

	public KeyHandler getKeyHandler() {
		return keyHandler;
	}
}
