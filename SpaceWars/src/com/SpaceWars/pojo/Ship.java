package com.SpaceWars.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

public class Ship extends PApplet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int health = 100;
	private int energy = 100;

	private boolean isDead = false;

	private int energyRechargeRate = 1;

	private float maxFireDist = 1200f;
	private float maxSpeed = 5f;

	private float maxForce = 0.22f;
	private float arriveRange = 110f;

	private PVector acceleration;
	private PVector velocity;
	private PVector target;
	private PVector location;
	private PVector worldSize;

	private int shootCount = 0;
	private int shootTime = 15;
	private boolean shoot = false;

	private int speedBoostCount = 0;
	private int speedBoostTime = 15;
	private final float LOWER_FIXED_SPEED = 7;
	private final float HIGHER_FIXED_SPEED = 13;
	private final float SPEED_BOOST_ENERGY_PENALTY = 15;

	private int shieldCount = 0;
	private int shieldTime = 15;
	private boolean shieldActive = false;
	private final int SHIELD_ENERGY_PENALTY = 15;

	private List<Bullet> bullets;

	private String shipTypeSelected = "NONE";

	public Ship(int width, int height, String shipType) {
		worldSize = new PVector(width, height);
		location = new PVector(random(1, width), random(1, height));
		velocity = new PVector(0f, 0f);
		acceleration = new PVector(0f, 0f);
		target = new PVector(location.x, location.y);
		bullets = new ArrayList<>();
		shipTypeSelected = shipType;
	}

	public PVector getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(PVector acceleration) {
		this.acceleration = acceleration;
	}

	public PVector getVelocity() {
		return velocity;
	}

	public void setVelocity(PVector velocity) {
		this.velocity = velocity;
	}

	public PVector getLocation() {
		return location;
	}

	public void setLocation(PVector location) {
		this.location = location;
	}

	public PVector getTarget() {
		return target;
	}

	public void setTarget(PVector target) {
		this.target = target;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getMaxForce() {
		return maxForce;
	}

	public void setMaxForce(float maxForce) {
		this.maxForce = maxForce;
	}

	public float getMaxFireDist() {
		return maxFireDist;
	}

	public void setMaxFireDist(float maxFireDist) {
		this.maxFireDist = maxFireDist;
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public void setBullets(List<Bullet> bullets) {
		this.bullets = bullets;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getEnergyRechargeRate() {
		return energyRechargeRate;
	}

	public void setEnergyRechargeRate(int energyRechargeRate) {
		this.energyRechargeRate = energyRechargeRate;
	}

	public void applyDamage(int damage) {
		health = (health > 0) ? (health - damage) : (health);
	}

	public String getShipTypeSelected() {
		return shipTypeSelected;
	}

	public void setShipTypeSelected(String shipTypeSelected) {
		this.shipTypeSelected = shipTypeSelected;
	}

	public boolean isShoot() {
		return shoot;
	}

	public void setShoot(boolean shoot) {
		this.shoot = shoot;
	}

	public boolean getShieldStatus() {
		return shieldActive;
	}

	public void deactivateShield() {
		shieldActive = false;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	private void update() {
		velocity.add(acceleration);
		velocity.limit(maxSpeed);
		location.add(velocity);
		acceleration.mult(0);
	}

//	private void seek() {
//
//		PVector desired = PVector.sub(target, location);
//
//		desired.normalize();
//		desired.mult(maxSpeed);
//		PVector steer = PVector.sub(desired, velocity);
//
//		steer.limit(maxForce);
//
//		applyForce(steer);
//	}

	private void arrive() {
		PVector desired = PVector.sub(target, location);
		float d = desired.mag();

		if (d < arriveRange) {
			float m = map(d, 10, arriveRange, 0, maxSpeed);
			desired.setMag(m);
		} else {
			desired.setMag(maxSpeed);
		}
		PVector steer = PVector.sub(desired, velocity);

		steer.limit(maxForce);

		applyForce(steer);
	}

	private void applyForce(PVector force) {
		acceleration.add(force);
	}

	public void manipulateShip() {
		// seek();
		reborn();
		arrive();
		update();
		propelBullets();
		adjustCounts();
	}

	private void reborn() {
		if (isDead) {
			isDead = false;
			health = 100;
			energy = 100;
			location.set(random(1, worldSize.x - 1), (random(1, worldSize.y - 1)));
			target.set(location);
		}
	}

	private void adjustCounts() {
		energy = (energy < 100 && shootCount < 10) ? energy + energyRechargeRate : energy;
		shootCount = (shootCount > shootTime) ? (0) : (shootCount + 1);
		speedBoostCount = (speedBoostCount > speedBoostTime) ? (0) : (speedBoostCount + 1);
		shieldCount = (shieldCount > shieldTime) ? (0) : (shieldCount + 1);
		setMaxSpeed(LOWER_FIXED_SPEED);
	}

	private void propelBullets() {
		for (int i = bullets.size() - 1; i >= 0; i--) {
			if ((dist(bullets.get(i).getStartPos().x, bullets.get(i).getStartPos().y, bullets.get(i).getLocation().x,
					bullets.get(i).getLocation().y) >= maxFireDist) || bullets.get(i).isBulletHit()) {
				bullets.remove(i);
			} else {
				bullets.get(i).update();
			}
		}
	}

	public void shoot(float angle) {

		float sourceX = location.x;
		float sourceY = location.y;

		float targetX = sourceX + cos(angle) * maxFireDist;
		float targetY = sourceY + sin(angle) * maxFireDist;

		if (energy > 0) {
			if (shootCount == shootTime) {
				bullets.add(new Bullet(location, new PVector(targetX, targetY), angle));
				energy -= bullets.get(0).getEnergyConsumed();
				shootCount = 0;
			}
		}

	}

	public void speedBoost() {
		if (energy > 0) {
			setMaxSpeed(HIGHER_FIXED_SPEED);
			if (speedBoostCount == speedBoostTime) {
				energy -= SPEED_BOOST_ENERGY_PENALTY;
				speedBoostCount = 0;
			}
		}
	}

	public void useShield() {

		if (energy > 0) {
			if (shieldCount == shieldTime) {
				shieldActive = true;
				energy -= SHIELD_ENERGY_PENALTY;
				shieldCount = 0;
			}
		} else {
			deactivateShield();
		}

	}

}
