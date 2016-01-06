package entities;

import java.awt.Point;

public abstract class Mob extends Entity {
	public Mob() {
		super();
	}
	
	public Mob(Point position) {
		super(position);
	}
}
