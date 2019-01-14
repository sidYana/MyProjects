package com.collisionDetection.DetectableShapes;

import processing.core.PApplet;

public interface CustomShape {

	public boolean colliding(CustomShape shape);
	
	public void show(PApplet context);
	
	public void setPosition(float x, float y);
	
}
