package com.UniverseSim.CollisionObjects;

import processing.core.PApplet;
import processing.core.PVector;

public class CustomPoint implements CustomShape{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PVector position;
	
	public CustomPoint(float x, float y) {
		position = new PVector(x, y);
	}
	
	@Override
	public boolean colliding(CustomShape shape) {
		return CollisionLogic.collides(this, shape);
	}

	@Override
	public void show(PApplet context) {
		context.strokeWeight(3);
		context.point(position.x, position.y);
		context.strokeWeight(1);
	}

	public void setPosition(float x, float y) {
		position.set(x, y);
	}
	
	public PVector getPosition() {
		return position;
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
}
