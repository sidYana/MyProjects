package com.UniverseSim.CollisionObjects;

import java.io.Serializable;

import processing.core.PApplet;

public interface CustomShape extends Serializable {

	public boolean colliding(CustomShape shape);
	
	public void show(PApplet context);
	
	public void setPosition(float x, float y);
	
}
