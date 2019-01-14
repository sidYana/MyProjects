package com.SpaceWars.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.SpaceWars.Util.DisplayQuadrants;

import processing.core.PApplet;
import processing.core.PVector;

public class GUIAlignmentUtil extends PApplet {

	private final float screenWidth;
	private final float screenHeight;

	public PVector cellSizes;

	private List<AlignmentCell> alignmentCells;

	public GUIAlignmentUtil(float screenWidth, float screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		alignmentCells = new ArrayList<>();
	}

	public void generateAlignments(PApplet context) {

		List<DisplayQuadrants> quadrantList = Arrays.asList(DisplayQuadrants.values());
		
		int i = 0;
		for (float col = 0; col < this.screenHeight && i < quadrantList.size() ; col += this.screenHeight / quadrantList.size(), i++) {
			DisplayQuadrants outerQuadrant = quadrantList.get(i);
			int j = 0;
			for (float row = 0; row < this.screenWidth && j < quadrantList.size(); row += this.screenWidth / quadrantList.size(), j++) {
				DisplayQuadrants innerQuadrant = quadrantList.get(j);
				AlignmentCell cell = new AlignmentCell(outerQuadrant, innerQuadrant, new PVector(row, col));
				alignmentCells.add(cell);
			}
		}
	}

	public void showAlignmentPoints(PApplet context) {
		PVector data;
		context.fill(255);
		context.textSize(10);
		context.fill(0);
		int i=1;
		for(DisplayQuadrants quad1 : DisplayQuadrants.values()) {
			int j=1;
			for(DisplayQuadrants quad2 : DisplayQuadrants.values()) {
				data = getVectorByQuadrants(quad1, quad2);
				context.ellipse(data.x, data.y, 10, 10);
				context.text(i+" , "+j, data.x+10, data.y+10);
				j++;
			}
			i++;
		}
		context.fill(255,0,0);
	}
	
	public PVector getVectorByQuadrants(DisplayQuadrants outer, DisplayQuadrants inner) {

		for (AlignmentCell cell : alignmentCells) {
			if (cell.getOuterQuadrant() == outer && cell.getInnerQuadrant() == inner) {
				return cell.getPosition();
			}
		}

		return new PVector();

	}

}
