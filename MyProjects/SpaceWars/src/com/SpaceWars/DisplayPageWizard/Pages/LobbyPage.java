package com.SpaceWars.DisplayPageWizard.Pages;

import java.awt.Font;
import java.util.Map;

import com.SpaceWars.AssetLoader.GameAssetLoader;
import com.SpaceWars.DisplayPageWizard.DisplayWizard;
import com.SpaceWars.DisplayPageWizard.MenuTypes;
import com.SpaceWars.SocketComm.SocketCommunicationUtil;
import com.SpaceWars.Util.DisplayQuadrants;
import com.SpaceWars.Util.GUIAlignmentUtil;
import com.SpaceWars.pojo.DataTransferPOJO;
import com.SpaceWars.pojo.Player;

import g4p_controls.GAlign;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import g4p_controls.GLabel;
import g4p_controls.GTimer;
import processing.core.PApplet;
import processing.core.PVector;

public class LobbyPage implements Menu {

	private GButton exit_lobby_btn;
	private GButton start_game_btn;
	private GLabel lobby_timer_label;
	private GTimer lobby_timer;

	private PApplet context;
	private GUIAlignmentUtil alignments;
	private GameAssetLoader assets;

	private PVector location;
	private PVector toLocation;

	private DataTransferPOJO data;
	private Map<String, Player> allPlayers;
	private Player player;

	private int counter;

	private boolean start_game = false;

	public LobbyPage(PApplet applet, GUIAlignmentUtil alignments, GameAssetLoader assets) {

		this.context = applet;
		this.alignments = alignments;
		this.assets = assets;

		location = alignments.getVectorByQuadrants(DisplayQuadrants.SEVEN, DisplayQuadrants.TWELVE);
		toLocation = alignments.getVectorByQuadrants(DisplayQuadrants.EIGHT, DisplayQuadrants.SIXTEEN);
		start_game_btn = new GButton(applet, location.x, location.y, toLocation.x - location.x,
				toLocation.y - location.y);
		start_game_btn.setText("START GAME");
		start_game_btn.setTextBold();
		start_game_btn.addEventHandler(this, "start_game_btn_event");

		location = alignments.getVectorByQuadrants(DisplayQuadrants.EIGHT, DisplayQuadrants.TWELVE);
		toLocation = alignments.getVectorByQuadrants(DisplayQuadrants.NINE, DisplayQuadrants.SIXTEEN);
		exit_lobby_btn = new GButton(applet, location.x, location.y, toLocation.x - location.x,
				toLocation.y - location.y);
		exit_lobby_btn.setText("EXIT LOBBY");
		exit_lobby_btn.setTextBold();
		exit_lobby_btn.addEventHandler(this, "exit_lobby_btn_event");

		location = this.alignments.getVectorByQuadrants(DisplayQuadrants.SIX, DisplayQuadrants.TWELVE);
		toLocation = this.alignments.getVectorByQuadrants(DisplayQuadrants.SEVEN, DisplayQuadrants.SIXTEEN);
		lobby_timer_label = new GLabel(applet, location.x, location.y, toLocation.x - location.x,
				toLocation.y - location.y);
		lobby_timer_label.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
		lobby_timer_label.setText("30");
		lobby_timer_label.setFont(new Font("Monospaced", Font.PLAIN, 20));
		lobby_timer_label.setTextBold();
		lobby_timer_label.setOpaque(false);

		lobby_timer = new GTimer(applet, this, "lobby_timer_event", 1000);

		hide();
	}

	@Override
	public void drawPage() {
		communicateWithServer();

		if (counter < 1) {
			DisplayWizard.setCurrentMenuType(MenuTypes.START_GAME);
		}
	}

	@Override
	public void show() {
		exit_lobby_btn.setVisible(true);
		if (SocketCommunicationUtil.isLocalServerRunning()) {
			start_game_btn.setVisible(true);
		}
		lobby_timer_label.setVisible(true);
		counter = 30;
		if (SocketCommunicationUtil.isLocalServerRunning()) {
			lobby_timer.start();
		}
	}

	@Override
	public void hide() {
		exit_lobby_btn.setVisible(false);
		start_game_btn.setVisible(false);
		lobby_timer_label.setVisible(false);
		lobby_timer.stop();
		counter = 30;
	}

	private void communicateWithServer() {
		player = SocketCommunicationUtil.getClient().getMyPlayer();

		if (SocketCommunicationUtil.isLocalServerRunning()) {
			player.setHosting(true);
			if (start_game) {
				player.setTimer(0);
			} else {
				player.setTimer(counter);
			}
		} else {
			player.setHosting(false);
		}

		data = SocketCommunicationUtil.communicateWithServer(player, "Lobby");
		if (data != null) {
			allPlayers = data.getAllPlayers();
		} else {
			allPlayers = null;
		}

		drawPlayerList(allPlayers);

	}

	private void drawPlayerList(Map<String, Player> playersList) {

		DisplayQuadrants[] playerNameQuadrantList = { DisplayQuadrants.FOUR, DisplayQuadrants.FIVE,
				DisplayQuadrants.SIX, DisplayQuadrants.SEVEN };

		DisplayQuadrants[] playerShipQuadrantList = { DisplayQuadrants.EIGHT, DisplayQuadrants.NINE,
				DisplayQuadrants.TEN, DisplayQuadrants.ELEVEN };
		int i = 0;
		if (playersList != null) {
			context.fill(0);
			context.textSize(30);
			for (String playerName : playersList.keySet()) {

				counter = playersList.get(playerName).getTimer();

				lobby_timer_label.setText("" + counter);

				location = alignments.getVectorByQuadrants(playerNameQuadrantList[i], DisplayQuadrants.TWO);
				toLocation = alignments.getVectorByQuadrants(playerNameQuadrantList[i + 1], DisplayQuadrants.SEVEN);
				context.text(playerName, location.x, location.y, toLocation.x - location.x, toLocation.y - location.y);

				location = alignments.getVectorByQuadrants(playerNameQuadrantList[i], playerShipQuadrantList[i]);
				String shipType = playersList.get(playerName).getMyShip().getShipTypeSelected();
				context.image(assets.allShipImages.get(shipType), location.x, location.y, 40, 40);
				i++;
			}
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void start_game_btn_event(GButton source, GEvent event) {
		PApplet.println("start_game - GButton >> GEvent." + event + " @ " + context.millis());
		lobby_timer.stop();
		
		start_game = true;
		communicateWithServer();
		start_game = false;
		SocketCommunicationUtil.stopDiscoveryServer();
		DisplayWizard.setCurrentMenuType(MenuTypes.START_GAME);
	}

	public void lobby_timer_event(GTimer source) {
		PApplet.println("timer1 - GTimer >> an event occured @ " + context.millis());
		counter--;
		lobby_timer_label.setText("" + counter);
		lobby_timer_label.setTextBold();
		if (counter == 0) {
			lobby_timer.stop();
		}
	}

	public void exit_lobby_btn_event(GButton source, GEvent event) {
		PApplet.println("exit_lobby - GButton >> GEvent." + event + " @ " + context.millis());
		SocketCommunicationUtil.disconnectClient();
		SocketCommunicationUtil.stopDiscoveryServer();
		SocketCommunicationUtil.stopServer();
		DisplayWizard.setCurrentMenuType(MenuTypes.MAIN_MENU);
	}

}
