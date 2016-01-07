package entities;

import java.awt.Point;

public abstract class Mob extends Entity {
	public Mob(boolean solid) {
		super(solid);
	}
	
	public Mob(Point position, boolean solid) {
		super(position, solid);
	}
}
