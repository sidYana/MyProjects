package com.SpaceWars.pojo;

public enum ShipTypes {
	TYPE_1 (0),
	TYPE_2 (1),
	TYPE_3 (2),
	TYPE_4 (3),
	TYPE_5 (4);
	
	private final int typeCode;
	
	private ShipTypes(int typeCode) {
		this.typeCode = typeCode;
	}
	
	public int getTypeCode() {
		return this.typeCode;
	}
	
}
