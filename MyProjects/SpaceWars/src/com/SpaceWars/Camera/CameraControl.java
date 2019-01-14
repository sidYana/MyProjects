package com.SpaceWars.Camera;

import processing.core.PVector;

public class CameraControl {

	int worldSizeMultiplier = 2;

	private PVector viewPort;
	private PVector worldSize;
	private PVector minOffset;
	private PVector maxOffset;
	private PVector camera;

	public CameraControl(int screenWidth, int screenHeight) {
		viewPort = new PVector(screenWidth, screenHeight);
		worldSize = new PVector(worldSizeMultiplier * 1000, worldSizeMultiplier * 1000);
		minOffset = new PVector(0, 0);
		maxOffset = new PVector(worldSize.x - viewPort.x, worldSize.y - viewPort.y);
		camera = new PVector();
	}

	public PVector adjustCamera(PVector position) {
		camera.x = position.x - viewPort.x / 2;
		camera.y = position.y - viewPort.y / 2;
		return camera;
	}

	public PVector getViewPort() {
		return viewPort;
	}

	public void setViewPort(PVector viewPort) {
		this.viewPort = viewPort;
	}

	public PVector getWorldSize() {
		return worldSize;
	}

	public void setWorldSize(PVector worldSize) {
		this.worldSize = worldSize;
	}

	public PVector getMinOffset() {
		return minOffset;
	}

	public void setMinOffset(PVector minOffset) {
		this.minOffset = minOffset;
	}

	public PVector getMaxOffset() {
		return maxOffset;
	}

	public void setMaxOffset(PVector maxOffset) {
		this.maxOffset = maxOffset;
	}

	public PVector getCamera() {
		return camera;
	}

	public void setCamera(PVector camera) {
		this.camera = camera;
	}

}
