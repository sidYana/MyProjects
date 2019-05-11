package com.RayCasting2D.BuildingBlocks;

import processing.core.PApplet;
import processing.core.PGraphics;

public abstract class Drawable {
	
	protected PApplet context;
	protected PGraphics pgContext;
	
	public abstract void show();
}
