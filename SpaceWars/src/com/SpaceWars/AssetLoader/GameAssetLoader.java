package com.SpaceWars.AssetLoader;

import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PImage;

public class GameAssetLoader{

	private Map<String, PImage> assetMap;
	private PApplet context;
	
	public Map<String, PImage> allShipImages;
	public Map<String, PImage> allAsteroidImages;
	public Map<String, PImage> allUIComponents;
	public Map<String, PImage> allWeaponImages;
	public Map<String, PImage> allShipEffects;
	
	public GameAssetLoader(PApplet context) {
		this.context = context;
		
		allUIComponents = loadUIComponents();
		allAsteroidImages = loadAsteroidAssets();
		allShipImages = loadShipAssets();
		allShipEffects = loadShipEffectsAssets();
		allWeaponImages = loadWeaponAssets();
		
	}
	
	public Map<String, PImage> loadUIComponents(){
		assetMap = new HashMap<String, PImage>();
		assetMap.put("HEALTH_SQUARE", context.loadImage("/Assets/UIAssets/squareRed.png"));
		assetMap.put("ENERGY_SQUARE", context.loadImage("/Assets/UIAssets/squareBlue.png"));
		assetMap.put("SHOOT_BTN", context.loadImage("/Assets/UIAssets/crossair_red.png"));
		assetMap.put("SHIELD_BTN", context.loadImage("/Assets/UIAssets/shield_gold.png"));
		assetMap.put("SPEED_BTN", context.loadImage("/Assets/UIAssets/bolt_gold.png"));
		assetMap.put("LOADING_SYMBOL", context.loadImage("/Assets/UIAssets/loading-x.gif"));
		return assetMap;
	}
	
	public Map<String, PImage> loadShipAssets() {

		assetMap = new HashMap<>();
		assetMap.put("BLACK_1", context.loadImage("/Assets/ships/ship_types_1/enemyBlack1.png"));
		assetMap.put("BLUE_1", context.loadImage("/Assets/ships/ship_types_1/enemyBlue1.png"));
		assetMap.put("GREEN_1", context.loadImage("/Assets/ships/ship_types_1/enemyGreen1.png"));
		assetMap.put("ORANGE_1", context.loadImage("/Assets/ships/ship_types_1/enemyRed1.png"));

		assetMap.put("BLACK_2", context.loadImage("/Assets/ships/ship_types_1/enemyBlack2.png"));
		assetMap.put("BLUE_2", context.loadImage("/Assets/ships/ship_types_1/enemyBlue2.png"));
		assetMap.put("GREEN_2", context.loadImage("/Assets/ships/ship_types_1/enemyGreen2.png"));
		assetMap.put("ORANGE_2", context.loadImage("/Assets/ships/ship_types_1/enemyRed2.png"));

		assetMap.put("BLACK_3", context.loadImage("/Assets/ships/ship_types_1/enemyBlack3.png"));
		assetMap.put("BLUE_3", context.loadImage("/Assets/ships/ship_types_1/enemyBlue3.png"));
		assetMap.put("GREEN_3", context.loadImage("/Assets/ships/ship_types_1/enemyGreen3.png"));
		assetMap.put("ORANGE_3", context.loadImage("/Assets/ships/ship_types_1/enemyRed3.png"));

		assetMap.put("BLACK_4", context.loadImage("/Assets/ships/ship_types_1/enemyBlack4.png"));
		assetMap.put("BLUE_4", context.loadImage("/Assets/ships/ship_types_1/enemyBlue4.png"));
		assetMap.put("GREEN_4", context.loadImage("/Assets/ships/ship_types_1/enemyGreen4.png"));
		assetMap.put("ORANGE_4", context.loadImage("/Assets/ships/ship_types_1/enemyRed4.png"));

		assetMap.put("BLACK_5", context.loadImage("/Assets/ships/ship_types_1/enemyBlack5.png"));
		assetMap.put("BLUE_5", context.loadImage("/Assets/ships/ship_types_1/enemyBlue5.png"));
		assetMap.put("GREEN_5", context.loadImage("/Assets/ships/ship_types_1/enemyGreen5.png"));
		assetMap.put("ORANGE_5", context.loadImage("/Assets/ships/ship_types_1/enemyRed5.png"));
		return assetMap;
	}

	public Map<String, PImage> loadAsteroidAssets() {
		assetMap = new HashMap<>();
		assetMap.put("BROWN_BIG_1", context.loadImage("/Assets/background_assets/asteroids/meteorBrown_big1.png"));
		assetMap.put("BROWN_BIG_2", context.loadImage("/Assets/background_assets/asteroids/meteorBrown_big2.png"));
		assetMap.put("BROWN_BIG_3", context.loadImage("/Assets/background_assets/asteroids/meteorBrown_big3.png"));
		assetMap.put("BROWN_BIG_4", context.loadImage("/Assets/background_assets/asteroids/meteorBrown_big4.png"));

		assetMap.put("GREY_BIG_1", context.loadImage("/Assets/background_assets/asteroids/meteorGrey_big1.png"));
		assetMap.put("GREY_BIG_2", context.loadImage("/Assets/background_assets/asteroids/meteorGrey_big2.png"));
		assetMap.put("GREY_BIG_3", context.loadImage("/Assets/background_assets/asteroids/meteorGrey_big3.png"));
		assetMap.put("GREY_BIG_4", context.loadImage("/Assets/background_assets/asteroids/meteorGrey_big4.png"));

		assetMap.put("BROWN_MED_1", context.loadImage("/Assets/background_assets/asteroids/meteorBrown_med1.png"));
		assetMap.put("BROWN_MED_2", context.loadImage("/Assets/background_assets/asteroids/meteorBrown_med3.png"));

		assetMap.put("GREY_MED_1", context.loadImage("/Assets/background_assets/asteroids/meteorGrey_med1.png"));
		assetMap.put("GREY_MED_2", context.loadImage("/Assets/background_assets/asteroids/meteorGrey_med2.png"));

		assetMap.put("BROWN_SML_1", context.loadImage("/Assets/background_assets/asteroids/meteorBrown_small1.png"));
		assetMap.put("BROWN_SML_2", context.loadImage("/Assets/background_assets/asteroids/meteorBrown_small2.png"));

		assetMap.put("GREY_SML_1", context.loadImage("/Assets/background_assets/asteroids/meteorGrey_small1.png"));
		assetMap.put("GREY_SML_2", context.loadImage("/Assets/background_assets/asteroids/meteorGrey_small2.png"));

		assetMap.put("BROWN_TNY_1", context.loadImage("/Assets/background_assets/asteroids/meteorBrown_tiny1.png"));
		assetMap.put("BROWN_TNY_2", context.loadImage("/Assets/background_assets/asteroids/meteorBrown_tiny2.png"));

		assetMap.put("GREY_TNY_1", context.loadImage("/Assets/background_assets/asteroids/meteorGrey_tiny1.png"));
		assetMap.put("GREY_TNY_2", context.loadImage("/Assets/background_assets/asteroids/meteorGrey_tiny2.png"));
		
		return assetMap;
	}
	
	public Map<String, PImage> loadWeaponAssets() {
		assetMap = new HashMap<>();

		assetMap.put("ORANGE_LASER", context.loadImage("Assets/ships/ship_weapon_types/laserRed16.png"));
		assetMap.put("BLUE_LASER", context.loadImage("Assets/ships/ship_weapon_types/laserBlue16.png"));
		assetMap.put("GREEN_LASER", context.loadImage("Assets/ships/ship_weapon_types/laserGreen10.png"));

		assetMap.put("ORANGE_BLAST", context.loadImage("Assets/ships/ship_weapon_types/laserRed08.png"));
		assetMap.put("BLUE_BLAST", context.loadImage("Assets/ships/ship_weapon_types/laserBlue16.png"));
		assetMap.put("GREEN_BLAST", context.loadImage("Assets/ships/ship_weapon_types/laserGreen14.png"));

		return assetMap;
	}

	public Map<String, PImage> loadShipEffectsAssets() {
		assetMap = new HashMap<>();
		assetMap.put("SHIELD_EFFECT", context.loadImage("Assets/ships/ship_other_assets/shield3.png"));
		return assetMap;
	}
	
}
