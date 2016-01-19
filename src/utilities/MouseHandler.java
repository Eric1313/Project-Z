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
		this.mouseLocation = new Point(0, 0);
	}

	@Override
	public void mousePressed(MouseEvent mouse) {
		this.click = true;
	}
	
	@Override
	public void mouseReleased(MouseEvent mouse) {
		this.click = false;
	}

	@Override
	public void mouseMoved(MouseEvent mouse) {
		this.mouseLocation = mouse.getPoint();
	}

	@Override
	public void mouseDragged(MouseEvent mouse) {
		this.mouseLocation = mouse.getPoint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mouse) {
		this.mouseWheel = mouse.getWheelRotation();
	}

	public Point getMouseLocation() {
		return this.mouseLocation;
	}

	public boolean isClick() {
		return this.click;
	}

	public void setClick(boolean click) {
		this.click = click;
	}

	public int getMouseWheel() {
		return this.mouseWheel;
	}

	public void setMouseWheel(int mouseWheel) {
		this.mouseWheel = mouseWheel;
	}
}