package com.UniverseSim.CameraControl;

import com.UniverseSim.ResourceLoader.PropertiesLoader;

import processing.core.PApplet;
import processing.core.PVector;

public class CameraControl {

	private PVector viewPort;
	private PVector worldSize;
	private PVector camera;

	public CameraControl() {
		
		int screenWidth = Integer.parseInt(PropertiesLoader.getProperty("screenWidth"));
		int screenHeight = Integer.parseInt(PropertiesLoader.getProperty("screenHeight"));
		int worldSizeWidth = Integer.parseInt(PropertiesLoader.getProperty("worldSizeWidth"));
		int worldSizeHeight = Integer.parseInt(PropertiesLoader.getProperty("worldSizeHeight"));
		
		viewPort = new PVector(screenWidth, screenHeight);
		worldSize = new PVector(worldSizeWidth, worldSizeHeight);
		camera = new PVector();
	}

	public void adjustCamera(PVector position, PApplet context) {
		camera.x = position.x - viewPort.x / 2;
		camera.y = position.y - viewPort.y / 2;
		context.pushMatrix();
		context.translate(-camera.x, -camera.y);
	}

	public void readjustCamera(PApplet context) {
		context.popMatrix();
	}
	
	public PVector getWorldSize() {
		return worldSize;
	}

	public void setWorldSize(PVector worldSize) {
		this.worldSize = worldSize;
	}

}
