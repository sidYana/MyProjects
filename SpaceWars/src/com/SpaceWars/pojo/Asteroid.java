package com.SpaceWars.pojo;

import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

public class Asteroid extends PApplet {

	private PVector location;
	private PVector velocity;
	private int size;
	private String asteroidType;

	private final int BIG = 150;
	private final int MED = 100;
	private final int SML = 50;
	private final int TNY = 10;

	private final float totalWidth;
	private final float totalHeight;
	
	public PVector getLocation() {
		return location;
	}

	public void setLocation(PVector location) {
		this.location = location;
	}

	public PVector getVelocity() {
		return velocity;
	}

	public void setVelocity(PVector velocity) {
		this.velocity = velocity;
	}

	public String getAsteroidType() {
		return asteroidType;
	}

	public void setAsteroidType(String asteroidType) {
		this.asteroidType = asteroidType;
	}

	public int getSize() {
		return size;
	}

	public Asteroid(float x, float y, String asteroidType) {
		location = new PVector(random(1, x - 1), random(1, y - 1));
		velocity = new PVector(random(-1, 1), random(-1, 1));
		
		totalWidth = x;
		totalHeight = y;
		
		this.asteroidType = asteroidType;

		List<String> types = Arrays.asList(asteroidType.split("_"));

		if (types.contains("BIG")) {
			this.size = BIG;
		} else if (types.contains("MED")) {
			this.size = MED;
		} else if (types.contains("SML")) {
			this.size = SML;
		} else if (types.contains("TNY")) {
			this.size = TNY;
		}
	}

	public void moveAsteroid() {

		if(location.x > totalWidth) {
			velocity.x = -velocity.x; 
		}else if(location.y > totalHeight) {
			velocity.y = -velocity.y; 
		}else if(location.x < 0) {
			velocity.x = -velocity.x; 
		}else if(location.y < 0) {
			velocity.y = -velocity.y; 
		}

		location.add(velocity);
		
	}

}
