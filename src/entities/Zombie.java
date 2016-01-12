//package entities;
//
//import Mover;
//import Path;
//
//import java.awt.Point;
//import java.awt.Graphics;
//import java.util.Stack;
//
//import AStarPathFinder.Node;
//import main.Game;
//
///**
// * Subclass of Mob that represents a zombie enemy in Project Z.
// * 
// * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
// * @see Mob
// * @see entities.ZombieThread
// * @since 1.0
// * @version 1.0
// */
//public class Zombie extends Mob {
//	public static final int MOVEMENT_SPEED = 3;
//	private boolean hastarget=false;
//	private Stack path;
//	
//	public Zombie(boolean solid, Game game) {
//		super(solid, game);
//		this.movementSpeed = Zombie.MOVEMENT_SPEED;
//	}
//
//	public Zombie(Point position, boolean solid, Game game) {
//		super(32, 32, position, solid, game);
//		this.movementSpeed = Zombie.MOVEMENT_SPEED;
//	}
//	
//
//	
////	@Override
////	public void render(Graphics g) {
////	}
//}