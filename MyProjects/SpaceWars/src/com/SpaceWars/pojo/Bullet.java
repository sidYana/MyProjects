package com.SpaceWars.pojo;

import java.io.Serializable;

import processing.core.PApplet;
import processing.core.PVector;

public class Bullet extends PApplet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int energyConsumed = 15;
	private int damageDealt = 5;
	private PVector startPos;
	private PVector location;
	private PVector oldLocation;
	private float rotation;
	private float internalRotation;
	private float speed;
	private String bulletType="";
	private boolean bulletHit = false;
	
	public Bullet(PVector shipPosition, PVector target, float angle) {
		this.location = new PVector(shipPosition.x, shipPosition.y);
		this.startPos = new PVector(shipPosition.x, shipPosition.y);
		this.oldLocation = target;
		//this.bulletType = bulletType;
		this.internalRotation = angle;
		this.rotation = atan2(oldLocation.y - location.y, oldLocation.x - location.x) / PI * 180;
		this.speed = 15;
	}

	public PVector getLocation() {
		return location;
	}

	public void setLocation(PVector location) {
		this.location = location;
	}
	
	public int getEnergyConsumed() {
		return energyConsumed;
	}

	public void setEnergyConsumed(int energyConsumed) {
		this.energyConsumed = energyConsumed;
	}

	public int getDamageDealt() {
		return damageDealt;
	}

	public void setDamageDealt(int damageDealt) {
		this.damageDealt = damageDealt;
	}
	
	public PVector getStartPos() {
		return startPos;
	}

	public void setStartPos(PVector startPos) {
		this.startPos = startPos;
	}

	public String getBulletType() {
		return bulletType;
	}

	public void setBulletType(String bulletType) {
		this.bulletType = bulletType;
	}

	public float getRotation() {
		return rotation;
	}

	public boolean isBulletHit() {
		return bulletHit;
	}

	public void setBulletHit(boolean bulletHit) {
		this.bulletHit = bulletHit;
	}
	
	public float getInternalRotation() {
		return internalRotation;
	}
	
	public void update() {
		// move the bullet
		location.add(cos(rotation / 180 * PI) * speed, sin(rotation / 180 * PI) * speed);
	}

}
