package com.UniverseSim.CollisionObjects;

public class CollisionLogic {

	public static boolean collides(CustomShape shape1, CustomShape shape2) {

		if (shape1 instanceof CustomRectangle) {
			
			if (shape2 instanceof CustomRectangle) {
				
				return collides((CustomRectangle) shape1, (CustomRectangle) shape2);
				
			} else if (shape2 instanceof CustomEllipse) {
				
				return collides((CustomRectangle) shape1, (CustomEllipse) shape2);
				
			} else if(shape2 instanceof CustomPoint) {
				
				return collides((CustomRectangle) shape1, (CustomPoint) shape2);
				
			} else {
				
				return false;
				
			}
			
		} else if (shape1 instanceof CustomEllipse) {
			
			if (shape2 instanceof CustomRectangle) {
				
				return collides((CustomRectangle) shape2, (CustomEllipse) shape1);
				
			} else if (shape2 instanceof CustomEllipse) {
				
				return collides((CustomEllipse) shape1, (CustomEllipse) shape2);
				
			} else if (shape2 instanceof CustomPoint) {
				
				return collides((CustomEllipse) shape1, (CustomPoint) shape2);
				
			} else {
				
				return false;
				
			}
			
		} else if (shape1 instanceof CustomPoint) {
			
			if (shape2 instanceof CustomRectangle) {

				return collides((CustomRectangle) shape2, (CustomPoint) shape1);

			} else if (shape2 instanceof CustomEllipse) {

				return collides((CustomEllipse) shape2, (CustomPoint) shape1);

			} else if (shape2 instanceof CustomPoint) {

				return collides((CustomPoint) shape1, (CustomPoint) shape2);

			} else {

				return false;

			}
			
		}
		return false;
	}

	private static boolean collides(CustomPoint point1, CustomPoint point2) {
		
		if(point1.getX() == point2.getX() &&
		   point1.getY() == point2.getY()) {
			return true;
		}
		return false;
		
	}
	
	private static boolean collides(CustomEllipse circle, CustomPoint point) {
		
		float distX = point.getX() - circle.getX();
		float distY = point.getY() - circle.getY();
		float distance = (float) Math.sqrt((distX * distX) + (distY * distY));

		// if the distance is less than the circle's
		// radius the point is inside!
		if (distance <= circle.getRadius()) {
			return true;
		}
		return false;
		
	}
	
	private static boolean collides(CustomRectangle rect, CustomPoint point) {
		if (point.getX() >= rect.getX() && // right of the left edge AND
			point.getX() <= rect.getX() + rect.getWidth() && // left of the right edge AND
			point.getY() >= rect.getY() && // below the top AND
			point.getY() <= rect.getY() + rect.getHeight()) { // above the bottom
			return true;
		}
		return false;
	}
	
	private static boolean collides(CustomRectangle rect1, CustomRectangle rect2) {

		if (rect1.getX() + rect1.getWidth() >= rect2.getX() && 
			rect1.getX() <= rect2.getX() + rect2.getWidth() && 
			rect1.getY() + rect1.getHeight() >= rect2.getY() && 
			rect1.getY() <= rect2.getY() + rect2.getHeight()) {
			return true;
		}
		return false;
		
	}

	private static boolean collides(CustomEllipse circle1, CustomEllipse circle2) {

		float distX = circle1.getX() - circle2.getX();
		float distY = circle1.getY() - circle2.getY();
		float distance = (float) Math.sqrt((distX * distX) + (distY * distY));

		// if the distance is less than the sum of the circle's
		// radii, the circles are touching!
		if (distance <= (circle1.getRadius()) + (circle2.getRadius())) {
			return true;
		}
		return false;
		
	}

	private static boolean collides(CustomRectangle rect, CustomEllipse circle) {
		// temporary variables to set edges for testing
		float testX = circle.getX();
		float testY = circle.getY();

		// which edge is closest?
		if (testX < rect.getX()) {
			testX = rect.getX(); // test left edge
		} else if (testX > rect.getX() + rect.getWidth()) {
			testX = rect.getX() + rect.getWidth(); // right edge
		}

		if (testY < rect.getY()) {
			testY = rect.getY(); // top edge
		} else if (testY > rect.getY() + rect.getHeight()) {
			testY = rect.getY() + rect.getHeight(); // bottom edge
		}

		// get distance from closest edges
		float distX = circle.getX() - testX;
		float distY = circle.getY() - testY;
		double distance = Math.sqrt((distX * distX) + (distY * distY));

		// if the distance is less than the radius, collision!
		if (distance <= circle.getRadius()) {
			return true;
		}
		return false;
	}

}
