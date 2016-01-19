/*
 * Handles the key input from the player
 * @author Allen Han, Eric Chee, Patrick Liu, Alosha Reymer
 * @version January 4th, 2016
 */
package utilities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private boolean shift;
	private boolean ctrl;
	private boolean q;
	private boolean e;
	private boolean r;
	private boolean esc;
	private boolean stop;
	private int lastNumber;

	@Override
	/**
	 * Toggles the key that is pressed
	 */
	public void keyPressed(KeyEvent key) {
		if (!stop) {
			toggle(key, true);
		}
	}

	@Override
	/**
	 * Toggles the key that is released
	 */
	public void keyReleased(KeyEvent key) {
		toggle(key, false);
		stop = false;
	}

	/**
	 * Toggles the key
	 * 
	 * @param key
	 *            the key that was pressed or released
	 * @param pressed
	 *            if the key is pressed or not
	 */
	private void toggle(KeyEvent key, boolean pressed) {
		// Sets the pressed key to true or false
		if (key.getKeyCode() == KeyEvent.VK_W) {
			up = pressed;
		}
		if (key.getKeyCode() == KeyEvent.VK_S) {
			down = pressed;
		}
		if (key.getKeyCode() == KeyEvent.VK_A) {
			left = pressed;
		}
		if (key.getKeyCode() == KeyEvent.VK_D) {
			right = pressed;
		}
		if (key.getKeyCode() == KeyEvent.VK_SHIFT) {
			shift = pressed;
		}
		if (key.getKeyCode() == KeyEvent.VK_CONTROL) {
			ctrl = pressed;
		}
		if (key.getKeyCode() == KeyEvent.VK_Q) {
			q = pressed;
		}
		if (key.getKeyCode() == KeyEvent.VK_E) {
			this.e = pressed;
		}
		if (key.getKeyCode() == KeyEvent.VK_R) {
			this.setR(pressed);
		}
		if (key.getKeyCode() >= 48 && key.getKeyCode() <= 57 && pressed) {
			if (key.getKeyCode() == 48) {
				this.lastNumber = 9;
			} else {
				this.lastNumber = key.getKeyCode() - 49;
			}
		}
		if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
			esc = pressed;
		}
	}

	@Override
	public void keyTyped(KeyEvent key) {
	}

	/**
	 * Sets the last number key (0-9) that was pressed
	 * 
	 * @param lastNumber
	 *            the number that was pressed
	 */
	public void setLastNumber(int lastNumber) {
		if (lastNumber < 0) {
			this.lastNumber = 10 + lastNumber;
		} else if (lastNumber > 9) {
			this.lastNumber = 10 - lastNumber;
		} else {
			this.lastNumber = lastNumber;
		}
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

	public boolean isCtrl() {
		return ctrl;
	}

	public boolean isQ() {
		return q;
	}

	public void setQ(boolean q) {
		this.q = q;
		this.stop = true;
	}

	public boolean isE() {
		return e;
	}

	public void setE(boolean e) {
		this.e = e;
		this.stop = true;
	}

	public boolean isR() {
		return r;
	}

	public void setR(boolean r) {
		this.r = r;
		this.stop = true;
	}

	public boolean isEsc() {
		return esc;
	}

	public void setEsc(boolean esc) {
		this.esc = esc;
		this.stop = true;
	}

	public int getLastNumber() {
		return this.lastNumber;
	}
}