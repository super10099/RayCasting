package core;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.awt.Color;
import java.awt.Graphics;

public class Player {

	
	private int x;
	private int y;
	
	private int moveIncrement = 10;
	private double thetaIncrement = (Math.PI*2) / 200;
	
	private int width = 10;
	private int height = 10;
	
	private double dir = 0; // in radians
	private int view_distance = 500;
	private double FOV = Math.toRadians(45);
	
	private java.awt.Color playerColor = java.awt.Color.RED;
	
	public Player(Point point) {
		x = point.x;
		y = point.y;
	}
	
	
	public void draw(Graphics g) {
		//draw player dot
		g.setColor(playerColor);
		g.fillOval(x - width/2, y - width/2, width, height);
		
		//Intersection dots
		g.setColor(Color.YELLOW);
		LinkedList<Point> intersectionPoints = getLineOfIntersections(view_distance, x, y, dir); // gets intersections
		Iterator<Point> it = intersectionPoints.iterator();
		
		//System.out.println(intersectionPoints);
		
		while (it.hasNext()) {
			Point point = it.next();
			g.fillRect(point.x - 2, point.y - 2, 4, 4);
		}
	}
	
	
	/*
	 * Get the quadrant given radians of direction
	 * 
	 * @param dir direction in radians
	 * @return quadrant
	 */
	public int getQuadrant(double dir) {
		int quad = 0;
		
		//verticle segment
		double ver_seg = Math.signum(Math.cos(dir)); 	// 1 = quad 1 and 4 
														// -1 = quad 2 and 3
		//horizontal segment
		double hor_seg = Math.signum(Math.sin(dir));	// 1 = quad 1 and 2
														// -1 == quad 3 and 4
		
		if (ver_seg == 1 && hor_seg == 1) { // 1st quad
			quad = 1;
		} else if (ver_seg == -1 && hor_seg == 1) { // 2nd quad
			quad = 2;
		} else if (ver_seg == -1 && hor_seg == -1) { // 3rd quad
			quad = 3;
		} else if (ver_seg == 1 && hor_seg == -1) { // 4th quad
			quad = 4;
		}
		
		
		return quad;
		
	}
	
	/*
	 * Checks if distance between p1 and p2 is less or equal than given distance
	 * 
	 */
	public boolean checkWithinRange(Point p1, Point p2, double dist) {
		
		double distbet = Point.distance(p1.x, p1.y, p2.x, p2.y);
		
		return  distbet <= dist;
		
	}
	
	
	
	/*
	 * This method will return a bunch of intersections on a line based on the given Intersections
	 * 
	 * 
	 * @return a linkedlist containing the intersections on a line
	 */
	public LinkedList<Point> getSpecificLineOfIntersections(Point playerPos, int view_distance, Intersections intersections){
		LinkedList<Point> points = new LinkedList<>();
		
		Point firstIntersectionPoint = intersections.getStartingPoint();
		Point incrementalPoint = intersections.getIncrementalPoint();
		
		boolean outOfRange = true;
		if (checkWithinRange(playerPos, firstIntersectionPoint, view_distance)) {
			outOfRange = false;
			points.add(firstIntersectionPoint);
		}
		
		while (!outOfRange) {
			
			Point newPoint = new Point();
			newPoint.setLocation(points.peekLast().x + incrementalPoint.x, points.peekLast().y + incrementalPoint.y);
			
			outOfRange = !checkWithinRange(playerPos, newPoint, view_distance);
			if(!outOfRange) {
				points.add(newPoint);
			}
		}
		
		
		return points;
	}
	
	
	/*
	 * will combine two line of intersections together (which is xconstant line of intersection and yconstant line of intersection
	 * 
	 * 
	 * @return a list of intersections including the xconstant and yconstant given radius or view_distance
	 */
	public LinkedList<Point> getLineOfIntersections(int view_distance, int x, int y, double dir) {
		
		Point playerPos = new Point(x, y);
		LinkedList<Point> points = new LinkedList<>();
		
		double tempTheta = dir - (FOV/2);
		//Loop as many pixels across
		for (int i=0; i<Engine.GamePanel.WIDTH; i++) {
			tempTheta += FOV/Engine.GamePanel.WIDTH;
			LinkedList<Point> ycons = getSpecificLineOfIntersections(playerPos, view_distance, getIntersections(tempTheta, "y", y, x));
			LinkedList<Point> xcons = getSpecificLineOfIntersections(playerPos, view_distance, getIntersections(tempTheta, "x", x, y));

			points.addAll(ycons);
			points.addAll(xcons);
		}
		
		
		return points;
		
	}
	
	/*
	 * The vital function of getting the incremental value in either the x dimension or y dimension given a constant dimension.
	 * 
	 * @param dir the current degree of direction
	 * @param constantVar the variable being kept constant: x or y
	 * @param constantVal the value of the constant variable
	 * @return the incremental value of the nonconstant variable.
	 */
	public Intersections getIntersections(double dir, String constantVar, int constantVal, int inconstantVal) {
		Intersections intersections = new Intersections();
		int dx = 0, dy = 0;
		int fx = 0, fy = 0;
		
		if (constantVar.equalsIgnoreCase("x")) { // use tangent
			
			double y_unit = -Math.tan(dir); //tangent needs to be negated to match the coordinate system of java
			int x_origin = (int) (Engine.GridPanel.gap * Math.signum(Math.cos(dir))); // gets the directional sign of X-dimension
			dy = (int) (x_origin * (y_unit)); // gets dx based on the sign of y_origin
			dx = x_origin; //does not needs to be negated, since only the y-dimension is flipped, not the x-dimension.
			
			//setting the starting point given x constant
			int quad = getQuadrant(dir);
			//setting fx
			if (quad == 1 | quad == 4) {
				fx = (Math.round(constantVal / Engine.GridPanel.gap) + 1) * Engine.GridPanel.gap;
			} else if (quad == 2 || quad == 3) {
				fx = Math.round(constantVal / Engine.GridPanel.gap) * Engine.GridPanel.gap;
			}
			
			//setting fy
			if (quad == 0 | quad == 1 | quad == 2) {
				fy = (int) (inconstantVal - Math.abs((fx - constantVal) * Math.tan(dir)));
			} else if (quad == 3 | quad == 4) {
				fy = (int) (inconstantVal + Math.abs((fx - constantVal) * Math.tan(dir)));
			}
			
		} else if (constantVar.equalsIgnoreCase("y")) { // use inverse tangent
			
			double x_unit = Math.tan(dir);
			int y_origin = (int) (Engine.GridPanel.gap * Math.signum(Math.sin(dir))); // gets the directional sign of Y-dimension
			dx = (int) (y_origin * (1/x_unit)); // gets dx based on the sign of y_origin
			dy = -y_origin; //since the grid coordinate in java is flipped horizontally (y-dimension), we also need to flip the sign
			
			//setting the starting point given y constant
			int quad = getQuadrant(dir);
			
			//setting fy
			if (quad == 1 | quad == 2) {
				fy = Math.round(constantVal / Engine.GridPanel.gap) * Engine.GridPanel.gap;
			} else if (quad == 3 | quad == 4) {
				fy = (Math.round(constantVal / Engine.GridPanel.gap) + 1) * Engine.GridPanel.gap;
			}
			
			//setting fx
			if (quad == 1 | quad == 4) {
				fx = (int) (inconstantVal + Math.abs((fy - constantVal) / Math.tan(dir)));
			} else if (quad == 2 || quad == 3) {
				fx = (int) (inconstantVal - Math.abs((fy - constantVal) / Math.tan(dir)));
			}
			
		} else {
			System.err.println("x or y" + ": not " + constantVar);
		}
			
		intersections.setStartingPoint(fx, fy);
		intersections.setIncrementalPoint(dx, dy);
		return intersections;
	}
	
	private class Intersections {
		private Point startingPoint = new Point();
		private Point incrementalPoint = new Point();
		
		//GETTERS
		public void setStartingPoint(int x, int y) {
			startingPoint.setLocation(x, y);
		}
		
		public void setIncrementalPoint(int x, int y) {
			incrementalPoint.setLocation(x, y);
		}
		
		
		//SETTERS
		public Point getStartingPoint() {
			return startingPoint;
		}
		
		
		public Point getIncrementalPoint() {
			return incrementalPoint;
		}
		
	}
	
	public void update() {
		
		if (Engine.inputHandler.left.isPressed())
			x -= moveIncrement;
		if (Engine.inputHandler.right.isPressed())
			x += moveIncrement;
		if (Engine.inputHandler.up.isPressed())
			y -= moveIncrement;
		if (Engine.inputHandler.down.isPressed())
			y += moveIncrement;
		
		if (Engine.inputHandler.turnLeft.isPressed())
			dir += thetaIncrement;
			dir = Math.round(dir * 100.0)/100.0;
		if (Engine.inputHandler.turnRight.isPressed())
			dir -= thetaIncrement;
			dir = Math.round(dir * 100.0)/100.0;
				
	}
}
