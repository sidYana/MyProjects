package com.UniverseSim.Creatures;

import com.UniverseSim.CollisionObjects.CustomPoint;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class Eye extends CustomPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float currentColorValue;
	private EyeType eyeType;

	public enum EyeType {
		DETECT_CREATURES, DETECT_FOOD
	}

	public void draw(PApplet context) {
		if (eyeType == EyeType.DETECT_FOOD) {
			context.strokeWeight(1);
			context.stroke(0);
			context.fill(currentColorValue);
			context.ellipse(getX(), getY(), 20, 20);
			context.noFill();
			context.noStroke();
		}
	}

	public void draw(PGraphics context) {
		context.fill(currentColorValue);
		context.ellipse(getX(), getY(), 10, 10);
		context.noFill();
	}

	public Eye(PVector pos, EyeType type) {
		super(pos.x, pos.y);
		eyeType = type;
	}

	public void setCurrentEyeColor(float color) {
		currentColorValue = color;
	}

	public float getCurrentColorValue() {
		return currentColorValue;
	}

	public void setPosition(PVector pos) {
		super.setPosition(pos.x, pos.y);
	}

	public PVector getPosition() {
		return super.getPosition();
	}

	public EyeType getEyeType() {
		return eyeType;
	}

}
