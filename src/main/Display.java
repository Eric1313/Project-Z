package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

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
		//gamePanel.setBackground(Color.BLACK);
//		panelContainer.add(gamePanel, "Game");
//		cardLayout.show(panelContainer, "Game");

		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		gamePanel.setPreferredSize(new Dimension(width, height));
		gamePanel.setMaximumSize(new Dimension(width, height));
		gamePanel.setMinimumSize(new Dimension(width, height));
		// Canvas is set to false so that input can be sent to the JFrame
		gamePanel.setFocusable(false);
		mouseHandler = new MouseHandler();
		gamePanel.addMouseMotionListener(mouseHandler);
		gamePanel.addMouseListener(mouseHandler);
		gamePanel.addMouseWheelListener(mouseHandler);
		// gamePanel.addMouseListener(mouseHandler);
		keyHandler = new KeyHandler();
		frame.addKeyListener(keyHandler);
		frame.add(gamePanel);
		frame.pack();

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
