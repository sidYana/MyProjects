package com.UnivSim.main;

import java.util.List;

import com.UniverseSim.Creatures.Creature;

import processing.core.PApplet;
import processing.core.PGraphics;

public class UIWindowDisplay {

	public UIWindowDisplay() {
		
	}
	
	public void show() {
		
	}
	
	public void hide() {
		
	}
	
	public void displayUIWindow(PGraphics pg) {
		
	}
	
	public void drawCreatureTable(PGraphics pg, List<Creature> creatures) {
		pg.beginDraw();
		pg.text("Total Creatures:"+creatures.size(), 200, 200);
		pg.endDraw();
	}
	
	public void drawMapLoaderMenu() {
		
	}
	
	public void showSelectedCreature(PGraphics pg, Creature creature) {
		
	}
	
}
