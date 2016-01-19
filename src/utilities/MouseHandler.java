/*
 * Handles the mouse input from the player
 * @author Allen Han, Eric Chee, Patrick Liu, Alosha Reymer
 * @version January 4th, 2016
 */
package utilities;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseHandler extends MouseAdapter {
	private boolean click;
	private Point mouseLocation;
	private int mouseWheel;

	/**
	 * The constructor for the mouse handler.
	 */
	public MouseHandler() {
		// Sets the initial point of the mouse to (0,0)
		mouseLocation = new Point(0, 0);
	}

	/**
	 * When the mouse is pressed, click is true.
	 */
	public void mousePressed(MouseEvent mouse) {
		this.click = true;
	}

	/**
	 * Sets the new mouse position when the mouse is moved.
	 */
	public void mouseMoved(MouseEvent mouse) {
		mouseLocation = mouse.getPoint();
	}

	/**
	 * Sets the new mouse position when the mouse is dragged.
	 */
	public void mouseDragged(MouseEvent mouse) {
		mouseLocation = mouse.getPoint();
	}

	/**
	 * Gets the movement of the mouse wheel.
	 */
	public void mouseWheelMoved(MouseWheelEvent mouse) {
		this.mouseWheel = mouse.getWheelRotation();
	}

	public Point getMouseLocation() {
		return mouseLocation;
	}

	public boolean isClick() {
		return click;
	}

	public void setClick(boolean click) {
		this.click = click;
	}

	public int getMouseWheel() {
		return mouseWheel;
	}

	public void setMouseWheel(int mouseWheel) {
		this.mouseWheel = mouseWheel;
	}
}