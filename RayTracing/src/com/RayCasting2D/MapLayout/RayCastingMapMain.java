package com.RayCasting2D.MapLayout;

import java.util.ArrayList;
import java.util.List;

import com.RayCasting2D.BuildingBlocks.Boundary;
import com.RayCasting2D.BuildingBlocks.Particle;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class RayCastingMapMain extends PApplet {

	private PGraphics mapViewport;
	private PGraphics raycastViewport;

	private List<Boundary> walls;
	private Particle player;
	private List<Float> scene;

	private LevelMap map = null;
	
	private boolean[] keyStrokes = new boolean[4];
	
	public static void main(String[] args) {
		PApplet.main(RayCastingMapMain.class);
	}

	public void settings() {
		size(1700, 1000);
	}

	public void setup() {
		mapViewport = createGraphics(width / 2, height);
		raycastViewport = createGraphics(width / 2, height);
		map = new LevelMap(mapViewport);
		walls = map.generateBoundaries();
		player = new Particle(mapViewport, new PVector(mapViewport.width / 2, mapViewport.height / 2));
	}

	private void generateWalls() {
		walls = new ArrayList<>();
		generateBoundaryWalls();
		generateRandomWalls();
	}

	private void generateBoundaryWalls() {
		walls.add(new Boundary(mapViewport, new PVector(0, 0), new PVector(mapViewport.width, 0)));
		walls.add(new Boundary(mapViewport, new PVector(mapViewport.width, 0),
				new PVector(mapViewport.width, mapViewport.height)));
		walls.add(new Boundary(mapViewport, new PVector(mapViewport.width, mapViewport.height),
				new PVector(0, mapViewport.height)));
		walls.add(new Boundary(mapViewport, new PVector(0, mapViewport.height), new PVector(0, 0)));
	}

	private void generateRandomWalls() {
		for (int i = 0; i < 5; i++) {
			PVector a = new PVector(random(mapViewport.width), random(mapViewport.height));
			PVector b = new PVector(random(mapViewport.width), random(mapViewport.height));
			walls.add(new Boundary(mapViewport, a, b));
		}
	}

	public void keyPressed() {

		if (key == CODED) {

			if (keyCode == LEFT) {
				player.rotate(-0.05f);
				keyStrokes[0] = true;
			}

			if (keyCode == RIGHT) {
				player.rotate(0.05f);
				keyStrokes[1] = true;
			}

			if (keyCode == UP) {
				player.move(1);
				keyStrokes[2] = true;
			}

			if (keyCode == DOWN) {
				player.move(-1);
				keyStrokes[3] = true;
			}
		}
		if (key == 'r') {
//			generateWalls();
		}
	}

	public void keyReleased() {
		if (key == CODED) {

			if (keyCode == LEFT) {
				keyStrokes[0] = false;
			}

			if (keyCode == RIGHT) {
				keyStrokes[1] = false;
			}

			if (keyCode == UP) {
				keyStrokes[2] = false;
			}

			if (keyCode == DOWN) {
				player.move(-1);
				keyStrokes[3] = false;
			}
		}
	}
	
	public void applyKeyStrokes() {
		if(keyStrokes[0] == true) {
			player.rotate(-0.05f);
		}
		
		if(keyStrokes[1] == true) {
			player.rotate(0.05f);
		}
		
		if(keyStrokes[2] == true) {
			player.move(1);
		}
		
		if(keyStrokes[3] == true) {
			player.move(-1);
		}
	}
	
	public void draw() {
		applyKeyStrokes();
		drawMapViewPort();
		drawRayCastViewPort();
		renderViewPorts();
	}

	private void renderViewPorts() {
		image(mapViewport, 0, 0);
		image(raycastViewport, width / 2, 0);
	}

	private void drawRayCastViewPort() {
		raycastViewport.beginDraw();
		raycastViewport.background(0);
		float sceneWidth = (float) raycastViewport.width / scene.size();

		for (int index = 0; index < scene.size(); index++) {
			float b = map(scene.get(index), 0, raycastViewport.width, 255, 0);
			float h = map(scene.get(index), 0, raycastViewport.width, raycastViewport.height, 0);
			raycastViewport.stroke(b);
			raycastViewport.fill(b);
			raycastViewport.rectMode(CENTER);
			raycastViewport.rect(index * sceneWidth + sceneWidth / 2, raycastViewport.height / 2, sceneWidth, h);
		}
		raycastViewport.endDraw();
	}

	private void drawMapViewPort() {
		mapViewport.beginDraw();
		mapViewport.background(0);
		// player.update(mouseX, mouseY);
		for (Boundary wall : walls) {
			wall.show();
		}
		scene = player.lookAtAndGetScenes(walls);
		player.show();
		mapViewport.endDraw();
	}

}
