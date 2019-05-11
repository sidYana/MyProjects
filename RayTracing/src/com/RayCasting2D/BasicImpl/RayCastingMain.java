package com.RayCasting2D.BasicImpl;

import java.util.ArrayList;
import java.util.List;

import com.RayCasting2D.BuildingBlocks.Boundary;
import com.RayCasting2D.BuildingBlocks.Particle;

import processing.core.PApplet;
import processing.core.PVector;

public class RayCastingMain extends PApplet {

	private List<Boundary> walls;
	private Particle particle;

	public static void main(String[] args) {
		PApplet.main(RayCastingMain.class);
	}

	public void settings() {
		size(500, 500);
	}

	public void setup() {
		particle = new Particle(this, new PVector(50, 250));
		generateWalls();
	}

	private void generateWalls() {
		walls = new ArrayList<>();
		generateBoundaryWalls();
		generateRandomWalls();
	}
	
	private void generateBoundaryWalls() {
		walls.add(new Boundary(this, new PVector(0, 0), new PVector(width,0)));
		walls.add(new Boundary(this, new PVector(width, 0), new PVector(width,height)));
		walls.add(new Boundary(this, new PVector(width, height), new PVector(0,height)));
		walls.add(new Boundary(this, new PVector(0, height), new PVector(0,0)));
	}
	
	private void generateRandomWalls() {
		for(int i = 0; i < 5 ; i++) {
			PVector a = new PVector(random(width), random(height));
			PVector b = new PVector(random(width), random(height));
			walls.add(new Boundary(this, a, b));
		}
	}
	
	public void keyPressed() {
		if(key=='r') {
			generateWalls();
		}
	}
	
	public void draw() {
		background(0);
		particle.update(mouseX, mouseY);
		for (Boundary wall : walls) {
			wall.show();
		}
		particle.lookAt(walls);
		particle.show();
	}
}
