package com.RayCasting2D.MapLayout;

import java.util.ArrayList;
import java.util.List;

import com.RayCasting2D.BuildingBlocks.Boundary;
import com.RayCasting2D.BuildingBlocks.Drawable;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class LevelMap extends Drawable{

	private List<Boundary> boundaries = null;
	private int[][] map = {
			{1,1,1,1,1,1,1,1,1,1},
			{1,0,0,0,0,0,1,0,0,1},
			{1,0,1,1,1,0,0,0,0,1},
			{1,0,1,0,1,0,0,1,1,1},
			{1,0,1,0,0,0,0,0,0,1},
			{1,0,1,0,0,0,0,0,0,1},
			{1,0,1,0,1,0,0,1,1,1},
			{1,0,1,1,1,0,0,0,0,1},
			{1,0,0,0,0,0,1,0,0,1},
			{1,1,1,1,1,1,1,1,1,1},
	};
	
	public LevelMap(PApplet context) {
		this.context = context;
	}

	public LevelMap(PGraphics context) {
		this.pgContext = context;
	}
	
	private List<Boundary> generateBlock(float row, float col, float w, float h) {
		List<Boundary> block = new ArrayList<>();
		block.add(new Boundary(pgContext, new PVector(row, col), new PVector(row + w, col)));
		block.add(new Boundary(pgContext, new PVector(row + w, col), new PVector(row + w, col + h)));
		block.add(new Boundary(pgContext, new PVector(row + w, col + h), new PVector(row, col + h)));
		block.add(new Boundary(pgContext, new PVector(row, col + h), new PVector(row, col)));
		return block;
	}
	
	public List<Boundary> generateBoundaries() {
		
		float w = pgContext.width/map.length;
		float h = pgContext.height/map[0].length;
		boundaries = new ArrayList<>();
		for(int row = 0; row < map.length; row++) {
			for(int col = 0; col < map[0].length; col++) {
				if(map[row][col] == 1) {
					boundaries.addAll(generateBlock(row * w, col * h, w, h));
				}
			}
		}
		return boundaries;
	}
	
	@Override
	public void show() {
		if(context!=null) {
			
		}else if(pgContext!=null) {
			
		}
	}
	
}
