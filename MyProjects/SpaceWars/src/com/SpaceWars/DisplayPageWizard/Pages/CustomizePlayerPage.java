package com.SpaceWars.DisplayPageWizard.Pages;

import com.SpaceWars.AssetLoader.GameAssetLoader;
import com.SpaceWars.DisplayPageWizard.DisplayWizard;
import com.SpaceWars.DisplayPageWizard.MenuTypes;
import com.SpaceWars.GameData.PlayerData;
import com.SpaceWars.Util.DisplayQuadrants;
import com.SpaceWars.Util.GUIAlignmentUtil;
import com.SpaceWars.pojo.ShipColors;
import com.SpaceWars.pojo.ShipTypes;

import g4p_controls.G4P;
import g4p_controls.GAlign;
import g4p_controls.GButton;
import g4p_controls.GDropList;
import g4p_controls.GEvent;
import g4p_controls.GLabel;
import g4p_controls.GTextField;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class CustomizePlayerPage implements Menu{

	private GTextField player_name_typed;
	private GButton player_data_submit;
	private GButton player_settings_exit;
	private GDropList ship_color_dropdown;
	private GDropList ship_type_dropdown;
	private GLabel player_name_label;
	private GLabel ship_color_label;
	private GLabel ship_type_label;
	
	private final String SHIP_COLORS_FILE = "/Assets/TextAssets/ship_color_file";
	private final String SHIP_TYPE_FILE = "/Assets/TextAssets/ship_type_file";
	
	private String currentShip;
	
	private PApplet context;
	private GUIAlignmentUtil alignments;
	
	private PlayerData playerData;
	private GameAssetLoader gameAssets;
	
	private PVector location;
	private PVector toLocation;
	private PImage shipImage;
	
	public CustomizePlayerPage(PApplet applet, GUIAlignmentUtil alignments, GameAssetLoader gameAssets) {
		
		this.context = applet;
		this.alignments = alignments;
		this.gameAssets = gameAssets;
		
		location = this.alignments.getVectorByQuadrants(DisplayQuadrants.TWO, DisplayQuadrants.FOUR);
		toLocation = this.alignments.getVectorByQuadrants(DisplayQuadrants.THREE, DisplayQuadrants.SEVEN);
		player_name_typed = new GTextField(applet, location.x, location.y, toLocation.x - location.x, toLocation.y - location.y, G4P.SCROLLBARS_NONE);
		player_name_typed.setPromptText("ENTER PLAYER NAME");
		player_name_typed.setOpaque(true);
		player_name_typed.addEventHandler(this, "player_name_typed_event");

		location = this.alignments.getVectorByQuadrants(DisplayQuadrants.TWO, DisplayQuadrants.ONE);
		toLocation = this.alignments.getVectorByQuadrants(DisplayQuadrants.THREE, DisplayQuadrants.FOUR);
		player_name_label = new GLabel(applet, location.x, location.y, toLocation.x - location.x, toLocation.y - location.y);
		player_name_label.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
		player_name_label.setText("PLAYER NAME");
		player_name_label.setTextBold();
		player_name_label.setOpaque(false);
		
		location = this.alignments.getVectorByQuadrants(DisplayQuadrants.FOUR, DisplayQuadrants.FOUR);
		toLocation = this.alignments.getVectorByQuadrants(DisplayQuadrants.NINE, DisplayQuadrants.SEVEN);
		ship_color_dropdown = new GDropList(applet, location.x, location.y, toLocation.x - location.x, toLocation.y - location.y);
		ship_color_dropdown.setItems(applet.loadStrings(SHIP_COLORS_FILE), 0);
		ship_color_dropdown.addEventHandler(this, "ship_color_dropdown_event");
		
		location = this.alignments.getVectorByQuadrants(DisplayQuadrants.SIX, DisplayQuadrants.FOUR);
		toLocation = this.alignments.getVectorByQuadrants(DisplayQuadrants.ELEVEN, DisplayQuadrants.SEVEN);
		ship_type_dropdown = new GDropList(applet, location.x, location.y, toLocation.x - location.x, toLocation.y - location.y);
		ship_type_dropdown.setItems(applet.loadStrings(SHIP_TYPE_FILE), 0);
		ship_type_dropdown.addEventHandler(this, "ship_type_dropdown_event");
		
		location = this.alignments.getVectorByQuadrants(DisplayQuadrants.FOUR, DisplayQuadrants.ONE);
		toLocation = this.alignments.getVectorByQuadrants(DisplayQuadrants.FIVE, DisplayQuadrants.FOUR);
		ship_color_label = new GLabel(applet, location.x, location.y, toLocation.x - location.x, toLocation.y - location.y);
		ship_color_label.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
		ship_color_label.setText("SHIP COLOR");
		ship_color_label.setTextBold();
		ship_color_label.setOpaque(false);
		
		location = this.alignments.getVectorByQuadrants(DisplayQuadrants.SIX, DisplayQuadrants.ONE);
		toLocation = this.alignments.getVectorByQuadrants(DisplayQuadrants.SEVEN, DisplayQuadrants.FOUR);
		ship_type_label = new GLabel(applet, location.x, location.y, toLocation.x - location.x, toLocation.y - location.y);
		ship_type_label.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
		ship_type_label.setText("SHIP TYPE");
		ship_type_label.setTextBold();
		ship_type_label.setOpaque(false);
		
		location = this.alignments.getVectorByQuadrants(DisplayQuadrants.FIFTEEN, DisplayQuadrants.THIRTEEN);
		toLocation = this.alignments.getVectorByQuadrants(DisplayQuadrants.SIXTEEN, DisplayQuadrants.SIXTEEN);
		player_data_submit = new GButton(applet, location.x, location.y, toLocation.x - location.x, toLocation.y - location.y);
		player_data_submit.setText("SUBMIT NEW DATA");
		player_data_submit.setTextBold();
		player_data_submit.addEventHandler(this, "player_data_submit_event");
		
		location = this.alignments.getVectorByQuadrants(DisplayQuadrants.FIFTEEN, DisplayQuadrants.TWO);
		toLocation = this.alignments.getVectorByQuadrants(DisplayQuadrants.SIXTEEN, DisplayQuadrants.FIVE);
		player_settings_exit = new GButton(applet, location.x, location.y, toLocation.x - location.x, toLocation.y - location.y);
		player_settings_exit.setText("GO TO MAIN MENU");
		player_settings_exit.setTextBold();
		player_settings_exit.addEventHandler(this, "player_settings_exit_event");
		
		playerData = new PlayerData();
		
		hide();
	}
	
	@Override
	public void drawPage() {
		drawSelectedShip();
	}

	private void drawSelectedShip() {
		location = this.alignments.getVectorByQuadrants(DisplayQuadrants.TWO, DisplayQuadrants.NINE);
		currentShip = ship_color_dropdown.getSelectedText() + "_" + ship_type_dropdown.getSelectedText().split("_")[1];
		shipImage = gameAssets.allShipImages.get(currentShip);
		context.image(shipImage, location.x, location.y, 175, 175);
	}
	
	@Override
	public void show() {
		fetchUserData();
		player_name_label.setVisible(true);
		player_name_typed.setVisible(true);
		player_data_submit.setVisible(true);
		player_settings_exit.setVisible(true);
		ship_color_dropdown.setVisible(true);
		ship_type_dropdown.setVisible(true);
		ship_color_label.setVisible(true);
		ship_type_label.setVisible(true);
	}

	@Override
	public void hide() {
		player_name_label.setVisible(false);
		player_name_typed.setVisible(false);
		player_data_submit.setVisible(false);
		player_settings_exit.setVisible(false);
		ship_color_dropdown.setVisible(false);
		ship_type_dropdown.setVisible(false);
		ship_color_label.setVisible(false);
		ship_type_label.setVisible(false);
	}
	
	public void fetchUserData() {
		player_name_typed.setText(playerData.getPlayerNameFromFile());
		String[] shipType = playerData.getPlayerShipTypeFromFile().split("_");
		ship_color_dropdown.setSelected(ShipColors.valueOf(shipType[0]).getLevelCode());
		ship_type_dropdown.setSelected(ShipTypes.valueOf("TYPE_"+shipType[1]).getTypeCode());
		currentShip = ship_color_dropdown.getSelectedText() + "_" + ship_type_dropdown.getSelectedText().split("_")[1];
	}
	
	public void player_name_typed_event(GTextField textfield, GEvent event) { /* code */ }

	public void player_data_submit_event(GButton button, GEvent event) { 
		PApplet.println("submit_player_name - GButton >> GEvent." + event + " @ " + context.millis());
		String given_player_name = player_name_typed.getText().toUpperCase();
		String given_player_ship_color = ship_color_dropdown.getSelectedText();
		String given_player_ship_type = ship_type_dropdown.getSelectedText();
		given_player_ship_type = given_player_ship_color + "_" + given_player_ship_type.split("_")[1];
		playerData.updatePlayerDataInFile(given_player_name, given_player_ship_type);
	}

	public void player_settings_exit_event(GButton button, GEvent event) { 
		PApplet.println("exit_player_settings - GButton >> GEvent." + event + " @ " + context.millis());
		DisplayWizard.setCurrentMenuType(MenuTypes.MAIN_MENU);
	}

	public void ship_color_dropdown_event(GDropList droplist, GEvent event) {
		PApplet.println("ship_color - GDropList >> GEvent." + event + " @ " + context.millis());
		PApplet.println(droplist.getSelectedText());
	}

	public void ship_type_dropdown_event(GDropList droplist, GEvent event) {
		PApplet.println("ship_type - GDropList >> GEvent." + event + " @ " + context.millis());
		PApplet.println(droplist.getSelectedText());
	}
	
}
