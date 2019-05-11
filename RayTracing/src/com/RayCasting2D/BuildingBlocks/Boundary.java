package com.RayCasting2D.BuildingBlocks;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class Boundary extends Drawable{

	private PVector a;
	private PVector b;
	
	private Boundary(PVector a, PVector b) {
		this.a = a;
		this.b = b;
	}
	public Boundary(PApplet context, PVector a, PVector b) {
		this(a, b);
		this.context = context;
	}
	
	public Boundary(PGraphics context, PVector a, PVector b) {
		this.pgContext = context;
		this.a = a;
		this.b = b;
	}
	
	@Override
	public void show() {
		if(context!=null) {
			context.stroke(255);
			context.line(a.x, a.y, b.x, b.y);
		}else if(pgContext!=null) {
			pgContext.stroke(255);
			pgContext.line(a.x, a.y, b.x, b.y);
		}
	}
	
	public float getX1() {
		return a.x;
	}
	
	public float getY1() {
		return a.y;
	}
	
	public float getX2() {
		return b.x;
	}
	
	public float getY2() {
		return b.y;
	}
}
