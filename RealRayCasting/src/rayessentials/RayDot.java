package rayessentials;

import java.awt.Point;

import core.Engine;

public class RayDot extends Point {
	
	private boolean hit = false; // if dot intersects a collideable cell
	private double distanceToPlayer;
	private Point cell;
	
	//EMPTY constructor
	public RayDot() {
	}
	
	public RayDot(int x, int y) {
		super(x, y);
	}
	
	public boolean checkHit() {
		
		try {
			if (Engine.getCellCollision(cell) != 0) {
				hit = true;
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException ex) {}
		return false;
	}
	
	public boolean getHit() {
		return hit;
	}
	
	public double getDistance() {
		return distanceToPlayer;
	}
	
	public void setDistance(double dist) {
		distanceToPlayer = dist;
	}
	
	public void setCell(Point cellPoint) {
		cell = cellPoint;
	}
	
	public Point getCell() {
		return cell;
	}
}
