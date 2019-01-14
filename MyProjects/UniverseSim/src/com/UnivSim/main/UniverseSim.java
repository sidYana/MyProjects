package com.UnivSim.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.AI.GeneticAlgorithm.GeneticAlgorithm;
import com.UniverseSim.CameraControl.CameraControl;
import com.UniverseSim.CollisionObjects.CustomRectangle;
import com.UniverseSim.Creatures.Creature;
import com.UniverseSim.Creatures.CreatureAgeComparator;
import com.UniverseSim.Creatures.CreatureHealthComparator;
import com.UniverseSim.GUIAlignmentsUtil.GUIAlignmentUtil;
import com.UniverseSim.QuadTree.QuadTreeV2;
import com.UniverseSim.ResourceLoader.PropertiesLoader;
import com.UniverseSim.TileMap.TileMapGenerator;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.event.MouseEvent;

public class UniverseSim extends PApplet {

	private int screenWidth;
	private int screenHeight;

	private int worldWidth;
	private int worldHeight;
	
	private TileMapGenerator tileMap;

	private PVector controlPosition;
	private CameraControl camera;
	private boolean[] creatureControl = new boolean[4];
	private Creature selectedCreature;
	private float zoom = 0;

	private float UIPanelWidth;
	private float UIPanelHeight;
	
	private PGraphics UIWindowPG;
	private UIWindowDisplay UIWindow;
	private GUIAlignmentUtil alignments;
	
	private float translateX = 0;
	private float translateY = 0;
	
	private int initialCreatures = 200;
	private int maintainMinimumCreatures = 100;
	
	private List<Integer> populationGraph;
	private List<Creature> allCreatures = new ArrayList<Creature>();
	private QuadTreeV2 allCreaturesQTree;
	private GeneticAlgorithm genes;
	private CreatureAIParams params;
	private boolean isPaused = false;
	private int simSpeed = 1;
	private int counter = 0;
	private int currentLargetsCreatureSize = 0;
	private float currentYearCounter=0;
	private float currentYear = 0;
	
	private Comparator<Creature> currentComparator = null;
	
	public UniverseSim() {
		screenWidth = Integer.parseInt(PropertiesLoader.getProperty("screenWidth"));
		screenHeight = Integer.parseInt(PropertiesLoader.getProperty("screenHeight"));
		worldWidth = Integer.parseInt(PropertiesLoader.getProperty("worldSizeWidth"));
		worldHeight = Integer.parseInt(PropertiesLoader.getProperty("worldSizeHeight"));
		tileMap = new TileMapGenerator(this);
		controlPosition = new PVector(screenWidth / 2, screenHeight / 2);
		camera = new CameraControl();
	}

	public static void main(String[] args) {
		PApplet.main(UniverseSim.class);
	}

	public void settings() {
		size(screenWidth, screenHeight, P3D);
	}

	public void setup() {
		perspective(PI/3.0f,(float)width/height,1,200000);
		
		UIPanelWidth = (float) (width*0.45);
		UIPanelHeight = height;
		UIWindow = new UIWindowDisplay();
		UIWindowPG = createGraphics((int) (UIPanelWidth), (int) UIPanelHeight, P3D);
		
		genes = new GeneticAlgorithm(initialCreatures, 20, 0.51, 0.01, worldWidth, worldHeight, this);
		allCreatures.addAll(genes.getPool());
		allCreaturesQTree = new QuadTreeV2(0, 4, new CustomRectangle(0, 0, worldWidth, worldHeight));
		params = new CreatureAIParams();
		populationGraph = new ArrayList<>();
		currentComparator = new CreatureAgeComparator();
	}

	public void draw() {
		background(0);
		drawUI();
		drawSim();
		renderViewPorts();
		Runtime.getRuntime().gc();
	}
	
	private void renderViewPorts() {
		image(UIWindowPG, (float) (width-(width*0.4)), 0);
	}

	private void drawUI() {
		
		if(selectedCreature!=null && !selectedCreature.isAlive()) {
			selectedCreature = null;
		}
		
		UIWindowPG.beginDraw();
		UIWindowPG.background(0);
		UIWindowPG.strokeWeight(2);
		UIWindowPG.stroke(255);
		UIWindowPG.noFill();
		UIWindowPG.rect(0, 0, UIWindowPG.width, UIWindowPG.height);
		drawPopulationGraph();
		UIWindowPG.strokeWeight(1);
		if(selectedCreature != null) {
			drawCreatureInUIPanel();
		}else {
			drawTopCreaturesTable();
		}
		
		UIWindowPG.noStroke();
		UIWindowPG.endDraw();
	}

	private void drawTopCreaturesTable() {
		
		//sorter button
		UIWindowPG.noFill();
		UIWindowPG.strokeWeight(1);
		UIWindowPG.stroke(0,255,0);
		UIWindowPG.rect(10, 10, 120, 30);
		UIWindowPG.text("Next Comparator", 20, 30);
		UIWindowPG.text(currentComparator.getClass().getSimpleName(), 175, 25);
		
		//table panel
		UIWindowPG.strokeWeight(2);
		UIWindowPG.fill(0);
		UIWindowPG.rect(10, 50, UIPanelWidth-80, UIPanelHeight*0.4f);
		UIWindowPG.noStroke();
		UIWindowPG.noFill();
		UIWindowPG.strokeWeight(1);
		
		List<Creature> currentSelection = allCreatures;
		Collections.sort(currentSelection, currentComparator);
		currentSelection = currentSelection.subList(0, 5);
		
		PVector displayPosition = new PVector(100, 80);
		for (Creature creature : currentSelection) {
			UIWindowPG.stroke(255);
			UIWindowPG.fill(255);
			UIWindowPG.strokeWeight(10);
			if (currentComparator instanceof CreatureAgeComparator) {
				UIWindowPG.text("Age="+creature.getFitness(), 200, displayPosition.y);
			}else if(currentComparator instanceof CreatureHealthComparator) {
				UIWindowPG.text("Health="+creature.getHealth(), 200, displayPosition.y);
			}
			UIWindowPG.strokeWeight(1);
			UIWindowPG.noFill();
			UIWindowPG.noStroke();
			
			float panelPos = width - UIWindowPG.width;
			
			if(mouseX > panelPos + displayPosition.x - 20 && 
			   mouseX < panelPos + displayPosition.x + 20 &&
			   mouseY > panelPos + displayPosition.y - 20 && 
			   mouseY < panelPos + displayPosition.y + 20) {
				
				if(mousePressed) {
					selectedCreature = creature;
				}
				
			}
			
			creature.drawCreatrure(UIWindowPG, displayPosition);
			displayPosition.add(0, 50);
		}
		
	}

	private void drawCreatureInUIPanel() {
		
		PVector displayPosition = new PVector(100, 80);		
		selectedCreature.drawCreatrure(UIWindowPG, displayPosition);
		
		float posX = 200;
		float posY = 80;
		float separation = 50;
		float barWidth = 250;
		float barHeight = 50;
		Map<String, Float> data = selectedCreature.getCreatureData();
		
		UIWindowPG.fill(0,255,0);
		drawBar("Health:"+data.get("Health").toString(), posX, posY, PApplet.map(data.get("Health"), 0, 300, 0, 100), barHeight);
		//limit
		UIWindowPG.fill(255,0,0);
		drawBar("",posX+PApplet.map(25, 0, 300, 0, 100),posY,1,barHeight);
		
		UIWindowPG.fill(0,255,0);
		drawBar("Hunger:"+data.get("Hunger").toString(), posX, posY+separation, data.get("Hunger"), barHeight);
		//Lower limit
		UIWindowPG.fill(255,0,0);
		drawBar("",posX+PApplet.map(25, 0, 100, 0, 100),posY+separation,1,barHeight);
		//Upper limit
		UIWindowPG.fill(255,0,0);
		drawBar("",posX+PApplet.map(75, 0, 100, 0, 100),posY+=separation,1,barHeight);
		
		UIWindowPG.fill(0,255,0);
		drawBar("Attack:"+data.get("Attack").toString(), posX, posY+=separation, PApplet.map(data.get("Attack"), 0, 1, 0, 100), barHeight);
		
		UIWindowPG.fill(0,255,0);
		drawBar("Reproduce:"+data.get("Reproduce").toString(), posX, posY+separation, PApplet.map(data.get("Reproduce"), 0, 1, 0, 100), barHeight);
		//Upper limit
		UIWindowPG.fill(255,0,0);
		drawBar("",posX+PApplet.map(0.3f, 0, 1, 0, 100),posY+separation,1,barHeight);
		//Lower limit
		UIWindowPG.fill(255,0,0);
		drawBar("",posX+PApplet.map(0.7f, 0, 1, 0, 100),posY+=separation,1,barHeight);
		
		UIWindowPG.fill(0,255,0);
		drawBar("Eat:"+data.get("Eat").toString(), posX, posY+separation, PApplet.map(data.get("Eat"), 0, 1, 0, 100), barHeight);
		//limit
		UIWindowPG.fill(255,0,0);
		drawBar("",posX+PApplet.map(0.55f, 0, 1, 0, 100),posY+=separation,1,barHeight);
		
		UIWindowPG.noStroke();
		UIWindowPG.noFill();
		
		translateX = -selectedCreature.getPosition().x;
		translateY = -selectedCreature.getPosition().y;
		
	}
	
	private void drawBar(String text, float posX, float posY, float barWidth, float barHeight) {
		UIWindowPG.strokeWeight(1);
		UIWindowPG.rect(posX, posY-10, barWidth, barHeight);
		UIWindowPG.noStroke();
		UIWindowPG.fill(255,0,0);
		UIWindowPG.text(text, posX+5, posY+20);
		UIWindowPG.noFill();
	}
	
	private void drawSim() {
		createCreatureTree();
		pushMatrix();
		translate(translateX, translateY, zoom);
		tileMap.drawTileMap(this);
		drawCreatures();
		showMousePoint();
		popMatrix();
		
		UIWindowPG.rect(UIPanelWidth - 190, 10, 120, 30);
		UIWindowPG.text("Prev Comparator", UIPanelWidth - 180, 30);
		
	}

	private void showMousePoint() {
		fill(255,0,0);
		ellipse(mouseX-translateX, mouseY-translateY, 25, 25);
		noFill();
	}

	private void drawCreatures() {
		if (currentYearCounter >= 5) {
			currentYear+=0.10f;
			currentYearCounter=0;
		}else {
			currentYearCounter++;
		}
		for (ListIterator<Creature> itr = allCreatures.listIterator(); itr.hasNext();) {
			Creature creature = itr.next();
			if(creature.isAlive()) {
				if (!isPaused) {
					params.setTileMap(tileMap);
					params.setCreatureQTree(allCreaturesQTree);
					params.setGenetic(genes);
					params = creature.AIInfluenceCreature(params);
					tileMap = params.getTileMap();
					
					for(Creature newCreature : params.getNewCreatures()) {
						itr.add(newCreature);
					}
					params.setNewCreatures(new ArrayList<>());
				}
				creature.drawCreatrure(this);
			} else {
				itr.remove();
			}
		}
		
	}

	private void drawPopulationGraph() {
		if(counter % 60 == 2) {
			if(populationGraph.size() < 101) {
				populationGraph.add(allCreatures.size());
			} else {
				for(int i=0;i<populationGraph.size() - 100;i++) {
					populationGraph.remove(0);
				}
			}
			counter=0;
		}
		counter++;
		float uiWidth = UIWindowPG.width;
		float uiHeight = UIWindowPG.height;
		
		float separation = uiWidth/100;
		
		float posX = 0;
		float posY = uiHeight;
		UIWindowPG.fill(255);
		UIWindowPG.text("Total Creatures:"+allCreatures.size(), 50, 350);
		UIWindowPG.text("Year:"+currentYear, 50, 400);
		UIWindowPG.fill(0,255,0);
		for(Integer dataPoint : populationGraph) {
			UIWindowPG.rect(posX, posY, 4, -dataPoint);
			posX+=4;
		}
		UIWindowPG.noFill();
	}

	private void createCreatureTree() {
		
		for(;allCreatures.size() < maintainMinimumCreatures;) {
			allCreatures.add(genes.createPrimordialCreature(allCreatures));
		}
		
		allCreaturesQTree.clear();
		for(Creature creature : allCreatures) {
			try {
				allCreaturesQTree.insert(creature);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void keyPressed() {
		if (key == CODED) {
			if (keyCode == LEFT) {
				creatureControl[2] = true;
				controlPosition.sub(10, 0);
			} else if (keyCode == RIGHT) {
				creatureControl[3] = true;
				controlPosition.add(10, 0);
			} else if (keyCode == UP) {
				creatureControl[0] = true;
				controlPosition.sub(0, 10);
			} else if (keyCode == DOWN) {
				creatureControl[1] = true;
				controlPosition.add(0, 10);
			}
		}

		if(key == 'n' || key =='N') {
			if(simSpeed>1)
				simSpeed-=1;
		}else if(key == 'm' || key == 'M') {
			if(simSpeed<10) {
				simSpeed+=1;
			}
		}
		
		if(key == 'c' || key =='C') {
			selectedCreature = null;
		}
		
		if (key == 'w' || key == 'W') {
			creatureControl[0] = true;
		} else if (key == 's' || key == 'S') {
			creatureControl[1] = true;
		} else if (key == 'a' || key == 'A') {
			creatureControl[2] = true;
		} else if (key == 'd' || key == 'D') {
			creatureControl[3] = true;
		}

	}

	public void keyReleased() {

		if (key == CODED) {
			if (keyCode == LEFT) {
				creatureControl[2] = false;
			} else if (keyCode == RIGHT) {
				creatureControl[3] = false;
			} else if (keyCode == UP) {
				creatureControl[0] = false;
			} else if (keyCode == DOWN) {
				creatureControl[1] = false;
			}
		}

		if (key == 'q' || key == 'Q') {
			System.out.println("new map");
			createNewMap();
		}
		
		if (key == 'p' || key == 'P') {
			System.out.println("Paused");
			isPaused=!isPaused;
		}
		
//		if (key == 's' || key == 'S') {
//			System.out.println("save map");
//			saveTileMap();
//		}
		
		if (key == 'r' || key == 'R') {
			System.out.println("Reset");
			translateX = 0;
			translateY = 0;
			zoom = -500;
		}
		
		if (key == 'w' || key == 'W') {
			creatureControl[0] = false;
		} else if (key == 's' || key == 'S') {
			creatureControl[1] = false;
		} else if (key == 'a' || key == 'A') {
			creatureControl[2] = false;
		} else if (key == 'd' || key == 'D') {
			creatureControl[3] = false;
		}
	}

	private void saveTileMap() {
		tileMap.saveCurrentMap(this);
	}

	public void mouseDragged(MouseEvent e) {
		translateX += 5*(mouseX - pmouseX);
		translateY += 5*(mouseY - pmouseY);
	}
	
	public void mouseClicked() {
		
		for(Creature creature : allCreatures) {
			if(mouseX<=(creature.getX()+translateX+50) && 
			   mouseY<=(creature.getY()+translateY+50) && 
			   mouseX>=(creature.getX()+translateX-50) && 
			   mouseY>=(creature.getY()+translateY-50)) {
				
				selectedCreature = creature;
				System.out.println("Creature selected");
			}
		}
		
	}
	
	public void mouseReleased() {
		
		if (mouseX > (width - UIPanelWidth) + 70 && mouseX < (width - UIPanelWidth) + 190 && mouseY > 10 && mouseY < 40) {
			
			if(currentComparator instanceof CreatureAgeComparator) {
				currentComparator = new CreatureHealthComparator();
			} else if(currentComparator instanceof CreatureHealthComparator) {
				currentComparator = new CreatureAgeComparator();
			}
			
		}

	}
	
	private void createNewMap() {
		tileMap = null;
		tileMap = new TileMapGenerator(this);
	}

	public void mouseWheel(MouseEvent event) {
		zoom += 100 * event.getCount();
	}

}
