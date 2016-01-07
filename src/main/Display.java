package main;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display {
	private JFrame frame;
	private GamePanel gamePanel;
	private JPanel panelContainer;
	private CardLayout cardLayout;
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
		panelContainer.setLayout(cardLayout);

		cardLayout = new CardLayout();
		gamePanel = new GamePanel();
		panelContainer.add(gamePanel, "Game");
		cardLayout.show(panelContainer, "Game");

		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);

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
}
