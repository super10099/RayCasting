package rayessentials;

import java.util.LinkedList;
import java.awt.Point;

//the head is currentHit
public class Ray extends LinkedList<RayDot> {
	
	//lists
	private LinkedList<RayDot> xcons;
	private LinkedList<RayDot> ycons;
	
	//used to find closest hit dot
	private RayDot currentHit;
	
	//CONSTRUCTOR
	public Ray(LinkedList<RayDot> xcons, LinkedList<RayDot> ycons) {
		this.xcons = xcons;
		this.ycons = ycons;
	}
	
	/*
	 * constructs ray with all rayDots that lead to the closest hitted rayDot
	 */
	
	public void constructRay() {
		
		if (currentHit != null) {
			for (RayDot rayDot: xcons) {
				if (rayDot.getDistance() < currentHit.getDistance())
					add(rayDot);
			}
			
			for (RayDot rayDot: ycons) {
				if (rayDot.getDistance() < currentHit.getDistance())
					add(rayDot);
			}
		} else {
			addAll(xcons);
			addAll(ycons);
		}
	}
	
	
	
	/*
	 * gets closest rayDot (hitted)
	 * 
	 * 
	 */
	public void parse() {
		for (RayDot rayDot : xcons) {
			updateCurrentHit(rayDot);
		}
		
		for (RayDot rayDot : ycons) {
			updateCurrentHit(rayDot);
		}
		
		if (currentHit != null)
			addFirst(currentHit);
	}
	
	public void updateCurrentHit(RayDot rayDot) {
		if (rayDot.getHit()) {
			if (currentHit == null)
				currentHit = rayDot;
			else if (rayDot.getDistance() < currentHit.getDistance())
				currentHit = rayDot;
		}
	}
	
	
	/*
	 * 
	 * @return true if first is shorter than second
	 */
	private boolean compareDotDistance(RayDot first, RayDot second) {
		
		if (first.getDistance() < second.getDistance())
			return true;
		
		// default
		return false;
	}
	
}
