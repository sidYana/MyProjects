package com.RayCasting2D.BuildingBlocks;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class Particle extends Drawable {

	private PVector position;
	private List<Ray> rays;
	private int fov = 45;
	private float increment = 0.5f;
	private float heading;
	
	public Particle(PApplet context, PVector position) {
		this.context = context;
		this.position = position;
		rays = new ArrayList<>();
		for (float angle = -fov/2; angle < fov/2; angle += increment) {
			rays.add(new Ray(context, position, PApplet.radians(angle)));
		}
	}

	public Particle(PGraphics context, PVector position) {
		this.pgContext = context;
		this.position = position;
		rays = new ArrayList<>();
		for (float angle = -fov/2; angle < fov/2; angle += increment) {
			rays.add(new Ray(context, position, PApplet.radians(angle)));
		}
	}

	public List<Float> lookAtAndGetScenes(List<Boundary> walls) {
		List<Float> scene = new ArrayList<>();
		for (Ray ray : rays) {
			float record = Float.POSITIVE_INFINITY;
			PVector closest = null;
			for (Boundary wall : walls) {
				PVector intersection = ray.cast(wall);
				if (intersection != null) {
					float distance = PVector.dist(position, intersection);
					float a = ray.getHeading() - heading;
					distance *= PApplet.cos(a);
					if (distance < record) {
						record = distance;
						closest = intersection;
					}
				}
			}
			if (closest != null) {
				if (context != null) {
					context.stroke(255, 80);
					context.line(position.x, position.y, closest.x, closest.y);
				} else if (pgContext != null) {
					pgContext.stroke(255, 80);
					pgContext.line(position.x, position.y, closest.x, closest.y);
				}
			}
			scene.add(record);
		}
		return scene;
	}
	
	public void lookAt(List<Boundary> walls) {
		for (Ray ray : rays) {
			float record = Float.POSITIVE_INFINITY;
			PVector closest = null;
			for (Boundary wall : walls) {
				PVector intersection = ray.cast(wall);
				if (intersection != null) {
					float distance = PVector.dist(position, intersection);
					if (distance < record) {
						record = distance;
						closest = intersection;
					}
				}
			}
			if (closest != null) {
				if (context != null) {
					context.stroke(255, 80);
					context.line(position.x, position.y, closest.x, closest.y);
				} else if (pgContext != null) {
					pgContext.stroke(255, 80);
					pgContext.line(position.x, position.y, closest.x, closest.y);
				}
			}
		}
	}

	public void rotate(float angle) {
		this.heading+=angle;
		float radian = -fov/2;
		for(int index = 0; index < rays.size(); index++) {
			rays.get(index).setAngle(PApplet.radians(radian)+heading);
			radian+=increment;
		}
	}
	
	public void update(int x, int y) {
		this.position.set(x, y);
	}
	
	@Override
	public void show() {
		if(context!=null) {
			context.fill(255);
			context.ellipse(position.x, position.y, 10, 10);
		}else if(pgContext!=null) {
			pgContext.fill(255);
			pgContext.ellipse(position.x, position.y, 10, 10);
		}
	}

	public void move(int i) {
	    PVector velocity = PVector.fromAngle(heading);
		velocity.setMag(i);
		position.add(velocity);
	}

}
