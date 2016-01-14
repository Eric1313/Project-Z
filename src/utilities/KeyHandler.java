package utilities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private boolean shift;
	private int lastNumber;

	@Override
	public void keyPressed(KeyEvent key) {
		toggle(key, true);
	}

	@Override
	public void keyReleased(KeyEvent key) {
		toggle(key, false);
	}

	private void toggle(KeyEvent e, boolean pressed) {
		// Sets the pressed key to true or false
		if (e.getKeyCode() == KeyEvent.VK_W) {
			up = pressed;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			down = pressed;
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			left = pressed;
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			right = pressed;
		}
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shift = pressed;
		}
		
		if (e.getKeyCode() >= 48 && e.getKeyCode() <= 57) {
			this.lastNumber = e.getKeyCode() - 49;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean isUp() {
		return up;
	}

	public boolean isDown() {
		return down;
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return right;
	}
	
	public boolean isShift() {
		return shift;
	}
	
	public int getLastNumber() {
		return this.lastNumber;
	}
}
