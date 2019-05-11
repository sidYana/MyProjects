package com.RayCasting2D.BuildingBlocks;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class Ray extends Drawable {

	private PVector position;
	private PVector direction;

	public Ray(PApplet context, PVector start) {
		this.context = context;
		this.position = start;
		this.direction = new PVector(100, 0);
	}

	public Ray(PGraphics context, PVector start) {
		this.pgContext = context;
		this.position = start;
		this.direction = new PVector(100, 0);
	}

	public Ray(PApplet context, PVector start, float angle) {
		this(context, start);
		this.direction = PVector.fromAngle(angle);
	}

	public Ray(PGraphics context, PVector start, float angle) {
		this(context, start);
		this.direction = PVector.fromAngle(angle);
	}

	public void lookAt(float x, float y) {
		direction.x = x - position.x;
		direction.y = y - position.y;
		direction.normalize();
	}

	public PVector cast(Boundary wall) {

		final float x1 = wall.getX1();
		final float y1 = wall.getY1();
		final float x2 = wall.getX2();
		final float y2 = wall.getY2();

		final float x3 = position.x;
		final float y3 = position.y;
		final float x4 = position.x + direction.x;
		final float y4 = position.y + direction.y;

		final float den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

		if (den == 0) {
			return null;
		}

		final float t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / den;
		final float u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / den;

		if (t > 0 && t < 1 && u > 0) {
			PVector intersection = new PVector();
			intersection.x = x1 + t * (x2 - x1);
			intersection.y = y1 + t * (y2 - y1);
			return intersection;
		}
		return null;
	}

	@Override
	public void show() {
		if (context != null) {
			context.stroke(255);
			context.pushMatrix();
			context.translate(position.x, position.y);
			context.line(0, 0, direction.x, direction.y);
			context.popMatrix();
		} else if (pgContext != null) {
			pgContext.stroke(255);
			pgContext.pushMatrix();
			pgContext.translate(position.x, position.y);
			pgContext.line(0, 0, direction.x, direction.y);
			pgContext.popMatrix();
		}
	}

	public PVector getAngle() {
		return direction;
	}
	
	public void setAngle(float f) {
		this.direction = PVector.fromAngle(f);
	}

	public float getHeading() {
		return direction.heading();
	}

}
