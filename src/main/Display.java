package main;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import utilities.KeyHandler;
import utilities.MouseHandler;

public class Display {
	private JFrame frame;
	private GamePanel gamePanel;
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
		panelContainer.add(gamePanel, "Game");
		cardLayout.show(panelContainer, "Game");

		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		mouseHandler = new MouseHandler();
		frame.addMouseMotionListener(mouseHandler);
		//gamePanel.addMouseListener(mouseHandler);
		keyHandler = new KeyHandler();
		frame.addKeyListener(keyHandler);
		frame.add(panelContainer);
		
		frame.setVisible(true);
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
