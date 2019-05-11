package com.UniverseSim.Creatures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.AI.NeuralNetwork.NeuralNetwork;
import com.UnivSim.main.CreatureAIParams;
import com.UniverseSim.CollisionObjects.CustomEllipse;
import com.UniverseSim.CollisionObjects.CustomPoint;
import com.UniverseSim.CollisionObjects.CustomShape;
import com.UniverseSim.Creatures.Eye.EyeType;
import com.UniverseSim.QuadTree.QuadTreeV2;
import com.UniverseSim.TileMap.Tile;
import com.UniverseSim.TileMap.Tile.TileTypes;
import com.UniverseSim.TileMap.TileMapGenerator;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class Creature extends CustomEllipse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long creatureIndex = 0;
	
	private float age=0;
	
	private float vel;
	private float acc;
	private float turn;
	private float turnAcc;
	
	public enum ReproductionType {
		SELF,MUTUAL,NONE;
	}
	
	private int creatureColor;
	
	private float health;
	private float hunger;
	private float reproduce;
	private float attack;
	private float eat;
	
	private PVector worldSize;
	
	private float eyesAngle = PConstants.PI/3;
	private float eyesDistance = 100; 
	
	private boolean isAlive = true;
	
	private Eye[] eyes = new Eye[5]; 
	
	private NeuralNetwork brain;
	
	private float[] memory;
	
	private boolean[] userControls;
	
	private Tile currentlyOnTile;
	
	private int ageCounter=0;
	private int healthCounter=0;
	private int hungerCounter=0;
	
	public Creature(long creatureIndex, PVector worldSize, PApplet context) throws Exception {
		super((float)Math.random()*worldSize.x, (float) Math.random()*worldSize.y, 50,50);
		this.creatureIndex = creatureIndex;
		this.worldSize = worldSize;
		creatureColor = context.color(context.random(1, 254), context.random(1, 254), context.random(1, 254));
		initializeBasicCreatureParams();
		initializeEyes();
		initializeBrain();
	}
	
	public Creature(long creatureIndex, PVector worldSize, PApplet context, NeuralNetwork newBrain) throws Exception {
		super((float)Math.random()*worldSize.x, (float) Math.random()*worldSize.y, 50,50);
		this.creatureIndex = creatureIndex;
		this.worldSize = worldSize;
		creatureColor = context.color(context.random(1, 254), context.random(1, 254), context.random(1, 254));
		initializeBasicCreatureParams();
		initializeEyes();
		initializeBrain(newBrain);
	}
	
	public Creature(long creatureIndex, PVector worldSize, int parentAColor, int parentBColor, PApplet context, NeuralNetwork newBrain) throws Exception {
		super((float)Math.random()*worldSize.x, (float) Math.random()*worldSize.y, 50,50);
		this.creatureIndex = creatureIndex;
		this.worldSize = worldSize;
		creatureColor = context.lerpColor(parentAColor, parentBColor, context.random(0.2f, 0.8f));
		initializeBasicCreatureParams();
		initializeEyes();
		initializeBrain(newBrain);
	}
	
	private void initializeBasicCreatureParams() {
		vel = 0f;
		acc = 0f;
		turn = (float) Math.random() - 0.5f ;
		health = 100;
		hunger = 100;
		reproduce = 0.001f;
		attack = 0.001f;
		memory = new float[4];
	}
	
	private void initializeBrain() throws Exception {
		brain = new NeuralNetwork(new int[] {13,10,9});
	}

	private void initializeBrain(NeuralNetwork newBrain) {
		brain = newBrain;
	}
	
	private void initializeEyes() {
		
		eyesDistance = eyesDistance+health*0.25f;
		
		float foodEye2Posx = (float)(getX())+eyesDistance*PApplet.cos((float)(6.28*turn-(eyesAngle/2)));
		float foodEye2Posy = (float)(getY())+eyesDistance*PApplet.sin((float)(6.28*turn-(eyesAngle/2)));
		PVector foodEye2 = new PVector(foodEye2Posx, foodEye2Posy);
		
		float foodEye3Posx = (float)(getX())+eyesDistance*PApplet.cos((float)(6.28*turn+(eyesAngle/2)));
		float foodEye3Posy = (float)(getY())+eyesDistance*PApplet.sin((float)(6.28*turn+(eyesAngle/2)));
		PVector foodEye3 = new PVector(foodEye3Posx, foodEye3Posy);
		
		eyes[0] = new Eye(new PVector(getX(), getY()), EyeType.DETECT_FOOD);
		eyes[1] = new Eye(foodEye2, EyeType.DETECT_FOOD);
		eyes[2] = new Eye(foodEye3, EyeType.DETECT_FOOD);
		eyes[3] = new Eye(foodEye2, EyeType.DETECT_CREATURES);
		eyes[4] = new Eye(foodEye3, EyeType.DETECT_CREATURES);
	}
	
	private float[] createBrainInputs() {
		
		float[] result =  {
			PApplet.norm(vel, -0.05f, 0.05f),
			PApplet.norm(turn, -0.025f, 0.025f),
			PApplet.norm(health, 0, 500),
			PApplet.norm(hunger, 0, 100),
			
			reproduce,
			attack,
			
			PApplet.norm(eyes[0].getCurrentColorValue(),0,255),
			PApplet.norm(eyes[1].getCurrentColorValue(),0,255),
			PApplet.norm(eyes[2].getCurrentColorValue(),0,255),

			memory[0],
			memory[1],
			memory[2],
			memory[3]
		};
		
		return result;
	}
	
	public CreatureAIParams AIInfluenceCreature(CreatureAIParams params) {
		
		if (isAlive()) {
			
			TileMapGenerator tileMap = params.getTileMap();
			
			for (int index = 0; index < eyes.length; index++) {
				if (eyes[index].getEyeType() == EyeType.DETECT_FOOD) {
					for (CustomShape shape : tileMap.queryTileMap(eyes[index])) {
						Tile tile = (Tile) shape;
						if(eyes[index].colliding(tile)) {
							eyes[index].setCurrentEyeColor(tile.getTileColor());
							break;
						}
					}
				}
			}
			
			CustomPoint currentPos = new CustomPoint(getX(), getY());
			for(CustomShape shape : tileMap.queryTileMap(currentPos)) {
				Tile tile = (Tile) shape;
				if(tile.colliding(currentPos)) {
					currentlyOnTile = tile;
					break;
				}
			}
			
			checkHealthParams();
			float[] inputs = createBrainInputs();
			float[] outputs = brain.predict(inputs);
			params.setNewCreatures(updateCreatureParams(outputs, params));
			
			try {
				tileMap.updateTileMap(currentlyOnTile);
				params.setTileMap(tileMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return params;
	}
	
	private void checkHealthParams() {
		
		if (ageCounter >= 5) {
			age += 0.10f;
			ageCounter = 0;
		} else {
			ageCounter++;
		}

		if (healthCounter >= 5) {
			if (hunger < 25 || hunger > 75) {
				health -= 1.0f;
			} else {
				health += 0.50f;
			}
			hunger -= 0.50f;
			healthCounter = 0;
		} else {
			healthCounter++;
		}

		if(health < 25) {
			isAlive = false;
		}
		
		hunger = PApplet.constrain(hunger, 0, 100);
		health = PApplet.constrain(health, 0, 300);
	}

	private List<Creature> updateCreatureParams(float[] outputs, CreatureAIParams params) {
		
		acc = outputs[0];
		turnAcc = outputs[1];
		reproduce = outputs[2];
		attack = outputs[3];
		eat = outputs[4];
		
		memory[0] = outputs[5];
		memory[1] = outputs[6];
		memory[2] = outputs[7];
		memory[3] = outputs[8];
		
		turnAcc = PApplet.map(turnAcc, 0, 1f, 0, 0.01f);
		
		if(turnAcc < 0.005f) {
			turn -= turnAcc;
		}else {
			turn += 0.01f-turnAcc;
		}

		if(turn < -6.28f || turn > 6.28f){
			turn = 0;
		}
		
		if(acc < 0.5) {
			vel -= acc;
		}else {
			vel += 1-acc;
		}
		
		vel = PApplet.constrain(vel, -7.5f, 7.5f);
		
		if(currentlyOnTile.getTileType() == TileTypes.OCEAN) {
			hunger-=2;
		} else if ( eat > 0.55f ) {
			hunger+=currentlyOnTile.eatFood();
		}
		
		List<Creature> babies = new ArrayList<Creature>();
		
		switch (getReproductionType()) {
		case SELF:
			if (health > 200) {
				health -= health * 0.5;
				age += 10;
				babies.add(params.getGenetic().createCreatureFromCrossover(this, this));
			}
			break;
		case MUTUAL:
			QuadTreeV2 qTree = params.getCreatureQTree();
			try {
				for (CustomShape shape : qTree.query(null, this)) {
					Creature creature = (Creature) shape;
					if (creature.getCreatureIndex() != getCreatureIndex()) {
						if (creature.colliding(this)) {
							if (creature.getReproductionType() == ReproductionType.MUTUAL) {
								if (health > 150 && creature.health > 150) {
									health -= health * 0.25;
									age += 25;
									babies.add(params.getGenetic().createCreatureFromCrossover(creature, this));
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case NONE:
			break;
		}
		
		float posX = getX() + vel*PApplet.cos((float)(6.28*turn));
		float posY = getY() + vel*PApplet.sin((float)(6.28*turn));
		
		if(posX > worldSize.x - 200 ) {
			posX = worldSize.x - 200;
			vel = 0;
		}
		
		if(posX < 200) {
			posX = 200;
		}
		
		if(posY > worldSize.y - 200 ) {
			posY = worldSize.y - 200;
			vel = 0;
		}
		
		if(posY < 200) {
			posY = 200;
		}
		
		setPosition(posX, posY);
		
		float modifiedEyeDistance = eyesDistance + getWidth();
		
		float foodEye2Posx = (float)(getX())+modifiedEyeDistance*PApplet.cos((float)(6.28*turn-(eyesAngle/2)));
		float foodEye2Posy = (float)(getY())+modifiedEyeDistance*PApplet.sin((float)(6.28*turn-(eyesAngle/2)));
		PVector foodEye2 = new PVector(foodEye2Posx, foodEye2Posy);
		
		float foodEye3Posx = (float)(getX())+modifiedEyeDistance*PApplet.cos((float)(6.28*turn+(eyesAngle/2)));
		float foodEye3Posy = (float)(getY())+modifiedEyeDistance*PApplet.sin((float)(6.28*turn+(eyesAngle/2)));
		PVector foodEye3 = new PVector(foodEye3Posx, foodEye3Posy);
		
		eyes[0].setPosition(new PVector(getX(), getY()));
		eyes[1].setPosition(foodEye2);
		eyes[2].setPosition(foodEye3);
		eyes[3].setPosition(foodEye2);
		eyes[4].setPosition(foodEye3);
		
		return babies;
		
	}

	public void userInfluenceCreature(boolean[] controls, TileMapGenerator tileMap) {
		userControls = controls;
		
		if(userControls[0] == true) {
			acc+=0.01;
		} else if(userControls[1] == true) {
			acc-=0.01;
		} else {
			acc=0;
			if(vel < 0) {
				vel+=0.5f;
			}else if(vel > 0) {
				vel-=0.5f;
			}else {
				vel=0;
			}
		}
		
		if(userControls[2] == true) {
			turn-=0.01;
		}else if(userControls[3] == true) {
			turn+=0.01;
		}
		
		turn = PApplet.constrain(turn, -0.025f, 0.025f);
		
		acc = PApplet.constrain(acc, -0.05f, 0.05f);
		
		vel+=acc;
		
		vel = PApplet.constrain(vel, -0.05f, 0.05f);

		setPosition(getX() + vel*PApplet.cos((float)(6.28*turn)), getY() + vel*PApplet.sin((float)(6.28*turn)));
		
		float modifiedEyeDistance = eyesDistance + getWidth();
		
		float foodEye2Posx = (float)(getX())+modifiedEyeDistance*PApplet.cos((float)(6.28*turn-(eyesAngle/2)));
		float foodEye2Posy = (float)(getY())+modifiedEyeDistance*PApplet.sin((float)(6.28*turn-(eyesAngle/2)));
		PVector foodEye2 = new PVector(foodEye2Posx, foodEye2Posy);
		
		float foodEye3Posx = (float)(getX())+modifiedEyeDistance*PApplet.cos((float)(6.28*turn+(eyesAngle/2)));
		float foodEye3Posy = (float)(getY())+modifiedEyeDistance*PApplet.sin((float)(6.28*turn+(eyesAngle/2)));
		PVector foodEye3 = new PVector(foodEye3Posx, foodEye3Posy);
		
		eyes[0].setPosition(new PVector(getX(), getY()));
		eyes[1].setPosition(foodEye2);
		eyes[2].setPosition(foodEye3);
		eyes[3].setPosition(foodEye2);
		eyes[4].setPosition(foodEye3);
		
		for(int index = 0;index < eyes.length;index++) {
			if(eyes[index].getEyeType() == EyeType.DETECT_FOOD) {
				for(CustomShape shape : tileMap.queryTileMap(eyes[index])){
					Tile tile = (Tile) shape;
					if(tile.colliding(eyes[index])) {
						eyes[index].setCurrentEyeColor(tile.getTileColor());
						break;
					}
				}
			}
		}
		
	}
	
	public void drawCreatrure(PApplet context) {
		
		drawBody(context);
		drawEyes(context);
	}
	
	public void drawCreatrure(PGraphics context, PVector displayPosition) {
		context.strokeWeight(1);
		drawBody(context, displayPosition);
		drawEyes(context, displayPosition);
		context.noStroke();
	}
	
	private void drawEyes(PApplet context) {
		for(Eye eye : eyes) {
			eye.draw(context);
			if(eye.getEyeType()==EyeType.DETECT_CREATURES) {
				context.stroke(0);
				context.fill(eye.getCurrentColorValue());
				context.line(getX(), getY(), eye.getX(), eye.getY());
				context.noFill();
				context.noStroke();
			}
		}
	}

	private void drawEyes(PGraphics context, PVector displayPosition) {
		
		float modifiedEyeDistance = 50;
		
		float foodEye2Posx = (float)(displayPosition.x)+modifiedEyeDistance*PApplet.cos((float)(6.28*turn-(eyesAngle/2)));
		float foodEye2Posy = (float)(displayPosition.y)+modifiedEyeDistance*PApplet.sin((float)(6.28*turn-(eyesAngle/2)));
		PVector foodEye2 = new PVector(foodEye2Posx, foodEye2Posy);
		
		float foodEye3Posx = (float)(displayPosition.x)+modifiedEyeDistance*PApplet.cos((float)(6.28*turn+(eyesAngle/2)));
		float foodEye3Posy = (float)(displayPosition.y)+modifiedEyeDistance*PApplet.sin((float)(6.28*turn+(eyesAngle/2)));
		PVector foodEye3 = new PVector(foodEye3Posx, foodEye3Posy);
		
		eyes[0].setPosition(displayPosition);
		eyes[1].setPosition(foodEye2);
		eyes[2].setPosition(foodEye3);
		
		context.stroke(0);
		for(Eye eye : eyes) {
			if(eye.getEyeType()==EyeType.DETECT_FOOD) {
				eye.draw(context);
				context.line(displayPosition.x, displayPosition.y, eye.getX(), eye.getY());
				
			}
		}
		context.noStroke();
	}
	
	private void drawBody(PApplet context) {
		context.fill(creatureColor);
		context.ellipse(getX(), getY(), health, health);
		context.noFill();
	}
	
	private void drawBody(PGraphics context, PVector displayPosition) {
		context.fill(creatureColor);
		context.ellipse(displayPosition.x, displayPosition.y, health*0.5f, health*0.5f);
		context.noFill();
	}
	
	public PVector getPosition() {
		return new PVector(getX(), getY());
	}
	
	public void setEyes(Eye[] updated) {
		this.eyes = updated;
	}
	
	public Eye[] getEyes() {
		return eyes;
	}
	
	public boolean isAlive() {
		return isAlive;
	}

	public Map<String, Float> getCreatureData() {
		
		Map<String,Float> creatureParams = new HashMap<>();
		
		creatureParams.put("Health",health);
		creatureParams.put("Hunger",hunger);
		creatureParams.put("Attack",attack);
		creatureParams.put("Reproduce",reproduce);
		creatureParams.put("Eat",eat);
		
		return creatureParams;
	}
	
	public float getFitness() {
		return age;
	}
	
	public NeuralNetwork getBrain() {
		return brain;
	}
	
	public int getCreatureColor() {
		return creatureColor;
	}
	
	public float getReproductionVal() {
		return reproduce;
	}
	
	public double getCreatureIndex() {
		return creatureIndex;
	}
	
	public ReproductionType getReproductionType() {
		if (reproduce >= 0 && reproduce < 0.3) {
			return ReproductionType.SELF;
		} else if (reproduce >= 0.7 && reproduce < 1) {
			return ReproductionType.MUTUAL;
		} else {
			return ReproductionType.NONE;
		}
	}

	public float getHealth() {
		return health;
	}
	
}
