package main;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display {
	private JFrame frame;
	private GamePanel gamePanel;
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
		frame = new JFrame(title);
		gamePanel = new GamePanel();

		frame.setSize(width, height);
		frame.add(gamePanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	public JFrame getFrame() {
		return frame;
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}
}

class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public GamePanel() {
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}