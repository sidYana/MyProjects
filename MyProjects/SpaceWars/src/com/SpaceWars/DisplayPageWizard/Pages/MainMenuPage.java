package com.SpaceWars.DisplayPageWizard.Pages;

import java.awt.Font;

import com.SpaceWars.DisplayPageWizard.DisplayWizard;
import com.SpaceWars.DisplayPageWizard.MenuTypes;
import com.SpaceWars.SocketComm.SocketCommunicationUtil;
import com.SpaceWars.Util.DisplayQuadrants;
import com.SpaceWars.Util.GUIAlignmentUtil;

import g4p_controls.GAlign;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import g4p_controls.GLabel;
import g4p_controls.GPanel;
import g4p_controls.GTextField;
import processing.core.PApplet;
import processing.core.PVector;

public class MainMenuPage implements Menu {

	private GLabel main_logo;
	private GButton host_game_btn;
	private GButton connect_game_btn;
	private GButton main_menu_exit_btn;
	private GButton player_customize_btn;
	private GButton error_panel_ok_btn;
	private GPanel loading_panel;

	private PApplet context;
	private GUIAlignmentUtil alignments;
	private PVector location;
	private PVector toLocation;

	public MainMenuPage(PApplet applet, GUIAlignmentUtil alignments) {

		this.context = applet;
		this.alignments = alignments;

		location = this.alignments.getVectorByQuadrants(DisplayQuadrants.ONE, DisplayQuadrants.SIX);
		toLocation = this.alignments.getVectorByQuadrants(DisplayQuadrants.THREE, DisplayQuadrants.TWELVE);
		main_logo = new GLabel(applet, location.x, location.y, toLocation.x - location.x, toLocation.y - location.y);
		main_logo.setFont(new Font("Monospaced", Font.PLAIN, 50));
		main_logo.setTextAlign(GAlign.CENTER, GAlign.CENTER);
		main_logo.setText("SPACE WARS");
		main_logo.setTextBold();
		main_logo.setTextItalic();
		main_logo.setOpaque(false);

		location = alignments.getVectorByQuadrants(DisplayQuadrants.TWELVE, DisplayQuadrants.SEVEN);
		toLocation = alignments.getVectorByQuadrants(DisplayQuadrants.THIRTEEN, DisplayQuadrants.ELEVEN);
		connect_game_btn = new GButton(applet, location.x, location.y, toLocation.x - location.x, toLocation.y - location.y);
		connect_game_btn.setText("CONNECT TO A GAME");
		connect_game_btn.setTextBold();
		connect_game_btn.addEventHandler(this, "connect_game_btn_event");

		location = alignments.getVectorByQuadrants(DisplayQuadrants.THIRTEEN, DisplayQuadrants.SEVEN);
		toLocation = alignments.getVectorByQuadrants(DisplayQuadrants.FOURTEEN, DisplayQuadrants.ELEVEN);
		host_game_btn = new GButton(applet, location.x, location.y, toLocation.x - location.x, toLocation.y - location.y);
		host_game_btn.setText("HOST GAME");
		host_game_btn.setTextBold();
		host_game_btn.addEventHandler(this, "host_game_btn_event");

		location = alignments.getVectorByQuadrants(DisplayQuadrants.FOURTEEN, DisplayQuadrants.SEVEN);
		toLocation = alignments.getVectorByQuadrants(DisplayQuadrants.FIFTEEN, DisplayQuadrants.ELEVEN);
		player_customize_btn = new GButton(applet, location.x, location.y, toLocation.x - location.x, toLocation.y - location.y);
		player_customize_btn.setText("CUSTOMIZE PLAYER");
		player_customize_btn.setTextBold();
		player_customize_btn.addEventHandler(this, "player_customize_btn_event");

		location = alignments.getVectorByQuadrants(DisplayQuadrants.FIFTEEN, DisplayQuadrants.SEVEN);
		toLocation = alignments.getVectorByQuadrants(DisplayQuadrants.SIXTEEN, DisplayQuadrants.ELEVEN);
		main_menu_exit_btn = new GButton(applet, location.x, location.y, toLocation.x - location.x, toLocation.y - location.y);
		main_menu_exit_btn.setText("EXIT GAME");
		main_menu_exit_btn.setTextBold();
		main_menu_exit_btn.addEventHandler(this, "main_menu_exit_btn_event");

		location = alignments.getVectorByQuadrants(DisplayQuadrants.SEVEN, DisplayQuadrants.FIVE);
		toLocation = alignments.getVectorByQuadrants(DisplayQuadrants.TEN, DisplayQuadrants.THIRTEEN);
		loading_panel = new GPanel(applet, location.x, location.y, toLocation.x - location.x, toLocation.y - location.y, "");
		loading_panel.setCollapsible(false);
		loading_panel.setDraggable(false);
		loading_panel.setTextBold();
		loading_panel.setOpaque(true);
		loading_panel.addEventHandler(this, "loading_panel_event");

		error_panel_ok_btn = new GButton(applet, 0, loading_panel.getHeight() - 30, loading_panel.getWidth(), 30);
		error_panel_ok_btn.setText("OK");
		error_panel_ok_btn.addEventHandler(this, "button3_click1");

		loading_panel.addControl(error_panel_ok_btn);

		hide();
	}

	@Override
	public void drawPage() {

	}

	@Override
	public void show() {
		main_logo.setVisible(true);
		host_game_btn.setVisible(true);
		connect_game_btn.setVisible(true);
		main_menu_exit_btn.setVisible(true);
		player_customize_btn.setVisible(true);
	}

	@Override
	public void hide() {
		main_logo.setVisible(false);
		host_game_btn.setVisible(false);
		connect_game_btn.setVisible(false);
		main_menu_exit_btn.setVisible(false);
		player_customize_btn.setVisible(false);
		loading_panel.setVisible(false);
	}

	public void showErrorPanel(String textToShow, Exception exception) {
		loading_panel.setText(textToShow);
		loading_panel.setTextBold();
		loading_panel.setVisible(true);
	}

	public void connect_game_btn_event(GButton source, GEvent event) {
		PApplet.println("connect_game - GButton >> GEvent." + event + " @ " + context.millis());
		hide();
		if (SocketCommunicationUtil.connectToServer(new PVector(context.width, context.height))) {
			DisplayWizard.setCurrentMenuType(MenuTypes.LOBBY);
		} else {
			SocketCommunicationUtil.disconnectClient();
			showErrorPanel("Could not connect...", null);
			show();
		}
	}

	public void host_game_btn_event(GButton button, GEvent event) {
		PApplet.println("host_game - GButton >> GEvent." + event + " @ " + context.millis());
		SocketCommunicationUtil.startDiscoveryServer();
		SocketCommunicationUtil.startServer();
		if (SocketCommunicationUtil.connectToServer(new PVector(context.width, context.height))) {
			DisplayWizard.setCurrentMenuType(MenuTypes.LOBBY);
		} else {
			SocketCommunicationUtil.disconnectClient();
			SocketCommunicationUtil.stopDiscoveryServer();
			SocketCommunicationUtil.stopServer();
			showErrorPanel("Could not connect...", null);
		}
	}

	public void player_customize_btn_event(GButton button, GEvent event) {
		PApplet.println("customize_player - GButton >> GEvent." + event + " @ " + context.millis());
		DisplayWizard.setCurrentMenuType(MenuTypes.CUSTOMIZE_PLAYER);
	}

	public void main_menu_exit_btn_event(GButton button, GEvent event) {
		PApplet.println("exit_game - GButton >> GEvent." + event + " @ " + context.millis());
		SocketCommunicationUtil.disconnectClient();
		SocketCommunicationUtil.stopDiscoveryServer();
		SocketCommunicationUtil.stopServer();
		System.exit(0);
	}

	public void loading_panel_event(GPanel source, GEvent event) {
		PApplet.println("loading_panel - GPanel >> GEvent." + event + " @ " + context.millis());
	}

	public void button3_click1(GButton source, GEvent event) {
		PApplet.println("error_panel_ok_btn - GButton >> GEvent." + event + " @ " + context.millis());
		loading_panel.setVisible(false);
	}

	public void textarea1_change1(GTextField textfield, GEvent event) {

	}

}
