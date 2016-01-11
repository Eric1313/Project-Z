package utilities;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {
	private Point mouseLocation;

	public MouseHandler() {
		mouseLocation = new Point(0, 0);
	}

	public void mouseMoved(MouseEvent e) {
		mouseLocation = e.getPoint();
	}

	public void mouseDragged(MouseEvent e) {
		mouseLocation = e.getPoint();
	}

	public Point getMouseLocation() {
		return mouseLocation;
	}

}
