package com.SpaceWars.pojo;

public enum ShipColors {
	ORANGE (0),
	GREEN (1),
	BLUE (2),
	BLACK (3)
	;
	
	private final int colorCode;
	
	private ShipColors(int colorCode) {
		this.colorCode = colorCode;
	}
	
	public int getLevelCode() {
		return this.colorCode;
	}
	
}
