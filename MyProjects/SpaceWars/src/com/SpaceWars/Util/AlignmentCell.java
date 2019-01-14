package com.SpaceWars.Util;

import processing.core.PVector;

public class AlignmentCell {

	private DisplayQuadrants outerQuadrant;
	private DisplayQuadrants innerQuadrant;
	private PVector position;
	
	public AlignmentCell(DisplayQuadrants outerQuadrant, DisplayQuadrants innerQuadrant, PVector position) {
		this.setOuterQuadrant(outerQuadrant);
		this.setInnerQuadrant(innerQuadrant);
		this.position = position;
	}
	
	public PVector getPosition() {
		return position;
	}

	public DisplayQuadrants getOuterQuadrant() {
		return outerQuadrant;
	}

	public void setOuterQuadrant(DisplayQuadrants outerQuadrant) {
		this.outerQuadrant = outerQuadrant;
	}

	public DisplayQuadrants getInnerQuadrant() {
		return innerQuadrant;
	}

	public void setInnerQuadrant(DisplayQuadrants innerQuadrant) {
		this.innerQuadrant = innerQuadrant;
	}
	
}
