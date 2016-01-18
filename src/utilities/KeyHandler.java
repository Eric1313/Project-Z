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
	public void keyPressed(KeyEvent key) {
		if (!stop) {
			toggle(key, true);
		}
	}

	@Override
	public void keyReleased(KeyEvent key) {
		toggle(key, false);
		stop = false;
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
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			ctrl = pressed;
		}
		if (e.getKeyCode() == KeyEvent.VK_Q) {
			q = pressed;
		}
		if (e.getKeyCode() == KeyEvent.VK_E) {
			this.e = pressed;
		}
		if (e.getKeyCode() == KeyEvent.VK_R) {
			this.setR(pressed);
		}
		if (e.getKeyCode() >= 48 && e.getKeyCode() <= 57 && pressed) {
			if (e.getKeyCode() == 48) {
				this.lastNumber = 9;
			} else {
				this.lastNumber = e.getKeyCode() - 49;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			esc = pressed;
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

	public void setLastNumber(int lastNumber) {
		if (lastNumber < 0) {
			this.lastNumber = 10 + lastNumber;
		} else if (lastNumber > 9) {
			this.lastNumber = 10 - lastNumber;
		} else {
			this.lastNumber = lastNumber;
		}
	}
}