package com.collisionDetection.DetectableShapes;

import processing.core.PApplet;
import processing.core.PVector;

public class CustomRectangle implements CustomShape {

	private float x;
	private float y;
	private float width;
	private float height;

	public CustomRectangle(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean colliding(CustomShape shape) {
		return CollisionLogic.collides(this, shape);
	}

	@Override
	public void show(PApplet context) {
		context.rect(x, y, width, height);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof CustomRectangle) {
			CustomRectangle rect = (CustomRectangle) o;
			if (rect.getX() == x && rect.getY() == y && rect.getWidth() == width && rect.getHeight() == height) {
				return true;
			}
		}
		return false;
	}

	public PVector getCenter() {
		return new PVector(x, y);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	@Override
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

}
