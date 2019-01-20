package com.collisionDetection.DetectableShapes;

import processing.core.PApplet;

public class CustomEllipse implements CustomShape {

	private float x;
	private float y;
	private float width;
	private float height;
	
	public CustomEllipse(float x, float y, float width, float height) {
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
		context.ellipse(x, y, width, height);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof CustomEllipse) {
			CustomRectangle ellipse = (CustomRectangle) o;
			if (ellipse.getX() == x && ellipse.getY() == y && ellipse.getWidth() == width
					&& ellipse.getHeight() == height) {
				return true;
			}
		}
		return false;
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

	public void setDiameter(float diameter) {
		this.width = diameter;
		this.height = diameter;
	}
	
	public float getDiameter() {
		return width;
	}
	
	public float getRadius() {
		return width/2;
	}

	@Override
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
}
