package com.SpaceWars.DisplayPageWizard.Pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.SpaceWars.AssetLoader.GameAssetLoader;
import com.SpaceWars.Camera.CameraControl;
import com.SpaceWars.DisplayPageWizard.DisplayWizard;
import com.SpaceWars.DisplayPageWizard.MenuTypes;
import com.SpaceWars.Main.SpaceWarsMain;
import com.SpaceWars.SocketComm.SocketCommunicationUtil;
import com.SpaceWars.Util.DisplayQuadrants;
import com.SpaceWars.Util.GUIAlignmentUtil;
import com.SpaceWars.pojo.Bullet;
import com.SpaceWars.pojo.DataTransferPOJO;
import com.SpaceWars.pojo.Player;

import g4p_controls.GButton;
import g4p_controls.GEvent;
import processing.core.PApplet;
import processing.core.PVector;

public class GamePage implements Menu {

	private PApplet context;
	private GUIAlignmentUtil alignments;
	private GameAssetLoader assets;
	private CameraControl myCamera;

	private GButton exit_game_btn;

	private PVector location;
	private PVector toLocation;

	private Player player;
	private DataTransferPOJO data;
	private Map<String, Player> allPlayers;
	private List<PVector> stars;

	public GamePage(PApplet applet, GUIAlignmentUtil alignments, GameAssetLoader assets) {

		this.context = applet;
		this.alignments = alignments;
		this.assets = assets;
		this.myCamera = new CameraControl(applet.width, applet.height);
		this.stars = new ArrayList<PVector>();

		float x_pos;
		float y_pos;
		for (int i = 0; i < 200; i++) {
			x_pos = applet.random(myCamera.getWorldSize().x);
			y_pos = applet.random(myCamera.getWorldSize().x);
			stars.add(new PVector(x_pos, y_pos));
		}

		location = this.alignments.getVectorByQuadrants(DisplayQuadrants.TWO, DisplayQuadrants.FOURTEEN);
		toLocation = this.alignments.getVectorByQuadrants(DisplayQuadrants.THREE, DisplayQuadrants.SIXTEEN);
		exit_game_btn = new GButton(applet, location.x, location.y, toLocation.x - location.x,
				toLocation.y - location.y);
		exit_game_btn.setText("Exit Game");
		exit_game_btn.addEventHandler(this, "exit_game_btn_event");

		hide();

	}

	@Override
	public void drawPage() {

		player = SocketCommunicationUtil.getClient().getMyPlayer();
		data = SocketCommunicationUtil.communicateWithServer(player, "NONE");

		if (data != null) {
			allPlayers = data.getAllPlayers();
		} else {
			allPlayers = null;
		}

		drawGame(allPlayers);

	}

	private void drawGame(Map<String, Player> players) {

		if (players != null) {

			player = SocketCommunicationUtil.getClient().getMyPlayer();
			PVector adjustedCamera = myCamera.adjustCamera(player.getMyShip().getLocation());
			context.pushMatrix();
			context.translate(-adjustedCamera.x, -adjustedCamera.y);

			drawBackground();
			drawBackgroundObjects(players);
			drawPlayers(players);
			takePlayerInput();

			context.popMatrix();

			drawInGameUI();
			influenceMyPlayer();
			SocketCommunicationUtil.updatePlayer(player);
		}

	}

	private void drawBackground() {

		context.background(0);
		context.stroke(255);
		context.noFill();
		context.rect(0, 0, myCamera.getWorldSize().x, myCamera.getWorldSize().y);
		context.noStroke();
		context.fill(255);

		for (int i = 0; i < 200; i++) {
			context.ellipse(stars.get(i).x, stars.get(i).y, context.random(1, 7), context.random(1, 7));
		}

	}

	private void drawBackgroundObjects(Map<String, Player> players) {

	}

	private void drawPlayers(Map<String, Player> players) {

		context.fill(255, 255, 0);
		context.imageMode(PApplet.CENTER);
		for (String playerName : players.keySet()) {

			Player otherPlayer = players.get(playerName);
			/**
			 * Flip from -PI/2 to +PI/2 for player ships as images are in reverse
			 */
			float theta = otherPlayer.getMyShip().getVelocity().heading() - PApplet.PI / 2;

			for (Bullet bullet : otherPlayer.getMyShip().getBullets()) {
				context.pushMatrix();
				context.translate(bullet.getLocation().x, bullet.getLocation().y);
				context.rotate(bullet.getInternalRotation() + PApplet.PI / 2);

				if (bullet.isBulletHit()) {
					context.image(assets.allWeaponImages.get("ORANGE_BLAST"), 0, 0);
				} else {
					context.image(assets.allWeaponImages.get("ORANGE_LASER"), 0, 0);
				}
				context.popMatrix();
			}

			context.stroke(255, 0, 0);
			context.noFill();
			context.ellipse(otherPlayer.getMyShip().getTarget().x, otherPlayer.getMyShip().getTarget().y, 20, 20);// ship
																													// destination
			// redical
			context.stroke(0);
			context.pushMatrix();
			context.translate(otherPlayer.getMyShip().getLocation().x, otherPlayer.getMyShip().getLocation().y);
			context.rotate(theta);
			if (otherPlayer.getMyShip().isDead()) {
				context.image(assets.allWeaponImages.get("ORANGE_BLAST"), 0, 0, 150, 150);
			} else {
				if (PApplet.dist(otherPlayer.getMyShip().getLocation().x, otherPlayer.getMyShip().getLocation().y,
						otherPlayer.getMyShip().getTarget().x, otherPlayer.getMyShip().getTarget().y) > 25) {
					context.fill(255, 0, 0);
					context.ellipse(0, -30, 20, context.random(20, 60));
					context.fill(255, 255, 0);
					context.ellipse(0, -25, 10, context.random(20, 50));
					context.fill(255, 255, 200);
					context.ellipse(0, -20, 5, context.random(15, 40));
					context.noFill();
				}
				context.image(assets.allShipImages.get(otherPlayer.getMyShip().getShipTypeSelected()), 0, 0, 50, 50);
				context.rotate(PApplet.PI);
				if (otherPlayer.getMyShip().getShieldStatus()) {
					context.image(assets.allShipEffects.get("SHIELD_EFFECT"), 0, 0, 90, 90);
				}
			}
			context.popMatrix();
			context.textSize(15);
			context.fill(255, 255, 0);
			context.text(playerName, otherPlayer.getMyShip().getLocation().x - 50,
					otherPlayer.getMyShip().getLocation().y + 50);

		}
		context.imageMode(PApplet.CORNER);

	}

	private void takePlayerInput() {

		if (context.mousePressed && player != null) {

			PVector targetTemp = new PVector(context.mouseX + myCamera.getCamera().x,
					context.mouseY + myCamera.getCamera().y);

			targetTemp.x = ((targetTemp.x < 0) ? (0) : (targetTemp.x));
			targetTemp.x = ((targetTemp.x > myCamera.getWorldSize().x - 1) ? (myCamera.getWorldSize().x - 1)
					: (targetTemp.x));
			targetTemp.y = ((targetTemp.y < 0) ? (0) : (targetTemp.y));
			targetTemp.y = ((targetTemp.y > myCamera.getWorldSize().y - 1) ? (myCamera.getWorldSize().y - 1)
					: (targetTemp.y));

			player.getMyShip().setTarget(targetTemp);

		}

		if (SpaceWarsMain.getIfKeyPressed(0)) {
			float theta = player.getMyShip().getVelocity().heading();
			player.getMyShip().shoot(theta);
		}

		if (SpaceWarsMain.getIfKeyPressed(1)) {
			player.getMyShip().useShield();
		} else {
			player.getMyShip().deactivateShield();
		}

		if (SpaceWarsMain.getIfKeyPressed(2)) {
			player.getMyShip().speedBoost();
		}

	}

	private void drawInGameUI() {

		int playerHealth = player.getMyShip().getHealth();
		int playerEnergy = player.getMyShip().getEnergy();

		for (int i = 0; i < 100; i += 10) {
			if (i < playerHealth) {
				context.image(assets.allUIComponents.get("HEALTH_SQUARE"), 20 + (i * 2), 30, 15, 15);
			}
			if (i < playerEnergy) {
				context.image(assets.allUIComponents.get("ENERGY_SQUARE"), 20 + (i * 2), 60, 15, 15);
			}
		}

		if (player != null) {
			// targetFromStick = myJoystick.simulateJoystick(myPlayer, myCamera);
			// myJoystick.simulateButtons(myPlayer);
		}

	}

	private void influenceMyPlayer() {
		player.getMyShip().manipulateShip();
	}

	@Override
	public void show() {
		exit_game_btn.setVisible(true);
	}

	@Override
	public void hide() {
		exit_game_btn.setVisible(false);
	}

	public void exit_game_btn_event(GButton source, GEvent event) {
		PApplet.println("exit_game_btn_event - GButton >> GEvent." + event + " @ " + context.millis());

		SocketCommunicationUtil.disconnectClient();
		SocketCommunicationUtil.stopServer();
		DisplayWizard.setCurrentMenuType(MenuTypes.MAIN_MENU);

	}

}
