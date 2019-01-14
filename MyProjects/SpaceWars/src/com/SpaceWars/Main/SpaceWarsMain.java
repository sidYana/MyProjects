package com.SpaceWars.Main;

import com.SpaceWars.DisplayPageWizard.DisplayWizard;

import g4p_controls.*;
import processing.core.PApplet;

public class SpaceWarsMain extends PApplet {

	private static boolean debug_enabled = false;
	private DisplayWizard wizard;

	private static boolean[] keySelection;

	public SpaceWarsMain() {

	}

	public static void main(String[] args) {
		PApplet.main(SpaceWarsMain.class);
	}

	public void settings() {
		size(900, 600);
		// fullScreen();
	}

	public void setup() {

		G4P.messagesEnabled(false);
		G4P.setGlobalColorScheme(GCScheme.BLUE_SCHEME);
		G4P.setCursor(ARROW);
		surface.setTitle("Space Wars");

		keySelection = new boolean[3];

		wizard = new DisplayWizard(this);
	}

	public void draw() {
		background(233);
		wizard.doDraw();
	}

	public static boolean isDebugEnabled() {
		return debug_enabled;
	}

	public void keyPressed() {
		if (key == ENTER) {
			debug_enabled = !debug_enabled;
		}

		if (key == ' ') {
			keySelection[0] = true;
		}

		if (key == 'z' || key == 'Z') {
			keySelection[1] = true;
		}

		if (key == CODED) {
			if (keyCode == CONTROL) {
				keySelection[2] = true;
			}
		}
	}

	public void keyReleased() {
		if (key == ' ') {
			keySelection[0] = false;
		}

		if (key == 'z' || key == 'Z') {
			keySelection[1] = false;
		}

		if (key == CODED) {
			if (keyCode == CONTROL) {
				keySelection[2] = false;
			}
		}
	}

	public static boolean getIfKeyPressed(int keyNum) {
		return keySelection[keyNum];
	}

}
