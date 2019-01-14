package com.SpaceWars.Controls;

import java.util.Map;

import com.SpaceWars.Camera.CameraControl;
import com.SpaceWars.pojo.Player;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class VJoystick {

	private PApplet source;
	private PVector joystick;
	private PVector innerJoystick;
	private PVector targetPos = new PVector();
	private float joystickDist = 0;
	private float joystickAngle;
	private Map<String, PImage> uiImages;

	private PVector shootPos;
	private PVector shieldPos;
	private PVector speedPos;

	public VJoystick(PApplet source, Map<String, PImage> uiImages) {
		this.source = source;
		this.uiImages = uiImages;

		joystick = new PVector(250, source.height - 150);
		innerJoystick = new PVector(joystick.x, joystick.y);

		shootPos = new PVector(source.width - joystick.x, joystick.y - 75);
		shieldPos = new PVector(source.width - joystick.x - 75, joystick.y + 20);
		speedPos = new PVector(source.width - joystick.x + 75, joystick.y + 20);

	}

	public PVector simulateJoystick(Player myPlayer, CameraControl myCamera) {

		source.fill(222);
		source.strokeWeight(4);
		source.ellipse(joystick.x, joystick.y, 190, 190);
		source.fill(100);
		source.strokeWeight(1);
		source.ellipse(joystick.x, joystick.y, 70, 70);
		source.fill(125);
		source.strokeWeight(20);
		source.line(joystick.x, joystick.y, innerJoystick.x, innerJoystick.y);
		source.strokeWeight(4);
		source.ellipse(innerJoystick.x, innerJoystick.y, 70, 70);
		source.fill(0);
		source.strokeWeight(1);
		if (source.mousePressed && PApplet.dist(joystick.x, joystick.y, source.mouseX, source.mouseY) < 160) {
			innerJoystick.set(source.mouseX, source.mouseY);

			joystickDist = PApplet.dist(innerJoystick.x, innerJoystick.y, joystick.x, joystick.y);
			joystickAngle = PApplet.atan2(joystick.y - innerJoystick.y, joystick.x - innerJoystick.x);

			targetPos.set(-3 * joystickDist * PApplet.cos(joystickAngle), -3 * joystickDist * PApplet.sin(joystickAngle));
			
			PVector targetTemp = new PVector(myPlayer.getMyShip().getLocation().x+targetPos.x, myPlayer.getMyShip().getLocation().y+targetPos.y);

			targetTemp.x = ((targetTemp.x < 0) ? (0) : (targetTemp.x));
			targetTemp.x = ((targetTemp.x > myCamera.getWorldSize().x - 1) ? (myCamera.getWorldSize().x - 1)
					: (targetTemp.x));
			targetTemp.y = ((targetTemp.y < 0) ? (0) : (targetTemp.y));
			targetTemp.y = ((targetTemp.y > myCamera.getWorldSize().y - 1) ? (myCamera.getWorldSize().y - 1)
					: (targetTemp.y));

			myPlayer.getMyShip().setTarget(targetTemp);
			
		} else {
			innerJoystick.set(joystick);

			joystickDist = 0;

			// targetPos.set(0, 0);
		}

		return targetPos;
	}

	public void simulateButtons(Player myPlayer) {
//		source.noFill();
//		source.ellipse(source.width - joystick.x, joystick.y, 150, 150);

		if (source.mousePressed && PApplet.dist(shootPos.x, shootPos.y, source.mouseX, source.mouseY) < 35) {
			source.fill(255);
			float theta = myPlayer.getMyShip().getVelocity().heading();
			myPlayer.getMyShip().shoot(theta);
		} else {
			source.fill(222);
		}
		source.ellipse(shootPos.x, shootPos.y, 70, 70);
		source.image(uiImages.get("SHOOT_BTN"), shootPos.x, shootPos.y, 45, 45);
		if (source.mousePressed && PApplet.dist(shieldPos.x, shieldPos.y, source.mouseX, source.mouseY) < 35) {
			source.fill(255);
			myPlayer.getMyShip().useShield();
		} else {
			myPlayer.getMyShip().deactivateShield();
			source.fill(222);
		}
		source.ellipse(shieldPos.x, shieldPos.y, 70, 70);
		source.image(uiImages.get("SHIELD_BTN"), shieldPos.x, shieldPos.y, 45, 45);
		if (source.mousePressed && PApplet.dist(speedPos.x, speedPos.y, source.mouseX, source.mouseY) < 35) {
			source.fill(255);
			myPlayer.getMyShip().speedBoost();
		} else {
			source.fill(222);
		}
		source.ellipse(speedPos.x, speedPos.y, 70, 70);
		source.image(uiImages.get("SPEED_BTN"), speedPos.x, speedPos.y, 45, 45);

	}

}
