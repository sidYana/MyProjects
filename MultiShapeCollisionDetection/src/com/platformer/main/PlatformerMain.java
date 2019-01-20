package com.platformer.main;

import com.platformer.camera.CameraControl;

import processing.core.PApplet;

public class PlatformerMain extends PApplet {

	private CameraControl camera;
	
	public PlatformerMain() {
		PApplet.main(PlatformerMain.class);
	}

	public void settings() {

		size(900,700);
		
	}

	public void setup() {

		camera = new CameraControl(width, height, 5000);
		
	}

	public void draw() {

		background(200);
		
	}

}
