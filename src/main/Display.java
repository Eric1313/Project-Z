package main;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import utilities.KeyHandler;
import utilities.MouseHandler;

public class Display {
	private JFrame frame;
	private GamePanel gamePanel;
	private MainPanel main;
	private PausePanel pause;
	private FinishPanel finish;
	private DeathPanel death;
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

		gamePanel = new GamePanel();
		main = new MainPanel();
		pause = new PausePanel();
		finish = new FinishPanel();
		death = new DeathPanel();

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
		main.addMouseMotionListener(mouseHandler);
		main.addMouseListener(mouseHandler);
		main.addMouseWheelListener(mouseHandler);

		pause.setPreferredSize(new Dimension(width, height));
		pause.setMaximumSize(new Dimension(width, height));
		pause.setMinimumSize(new Dimension(width, height));
		pause.setFocusable(false);
		pause.addMouseMotionListener(mouseHandler);
		pause.addMouseListener(mouseHandler);
		pause.addMouseWheelListener(mouseHandler);

		finish.setPreferredSize(new Dimension(width, height));
		finish.setMaximumSize(new Dimension(width, height));
		finish.setMinimumSize(new Dimension(width, height));
		finish.setFocusable(false);
		finish.addMouseMotionListener(mouseHandler);
		finish.addMouseListener(mouseHandler);
		finish.addMouseWheelListener(mouseHandler);

		death.setPreferredSize(new Dimension(width, height));
		death.setMaximumSize(new Dimension(width, height));
		death.setMinimumSize(new Dimension(width, height));
		death.setFocusable(false);
		death.addMouseMotionListener(mouseHandler);
		death.addMouseListener(mouseHandler);
		death.addMouseWheelListener(mouseHandler);

		keyHandler = new KeyHandler();
		frame.addKeyListener(keyHandler);
		frame.add(gamePanel);
		frame.pack();

		frame.setVisible(true);
	}

	public MainPanel getMain() {
		return main;
	}

	public PausePanel getPause() {
		return pause;
	}

	public DeathPanel getDeath() {
		return death;
	}

	public FinishPanel getFinish() {
		return finish;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void switchPanel(String panel) {
		cardLayout.show(panelContainer, panel);
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public MouseHandler getMouseHandler() {
		return mouseHandler;
	}

	public KeyHandler getKeyHandler() {
		return keyHandler;
	}
}
