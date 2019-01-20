package com.collisionDetection.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.collisionDetection.DetectableShapes.CustomEllipse;
import com.collisionDetection.DetectableShapes.CustomPoint;
import com.collisionDetection.DetectableShapes.CustomRectangle;
import com.collisionDetection.DetectableShapes.CustomShape;
import com.collisionDetection.QTree.QuadTreeV2;

import processing.core.PApplet;
import processing.event.MouseEvent;

public class CollisionDetectionMain extends PApplet {

	private QuadTreeV2 quadTree;
	private CustomShape range;
	private CustomRectangle boundary;
	private List<CustomShape> allShapes;
	private Set<CustomShape> found;
	private boolean[] debug_enabled;

	private List<CustomShape> ranges;
	private int index = 0;

	public static void main(String[] args) {
		PApplet.main(CollisionDetectionMain.class);
	}

	public void settings() {
		size(900, 700);
	}

	public void setup() {

		boundary = new CustomRectangle(0, 0, width, height);
		allShapes = new ArrayList<CustomShape>();
		quadTree = new QuadTreeV2(0, 1, boundary);
		debug_enabled = new boolean[2];

		ranges = new ArrayList<>();
		ranges.add(new CustomRectangle(mouseX, mouseY, 50, 50));
		ranges.add(new CustomEllipse(mouseX, mouseY, 50, 50));
		ranges.add(new CustomPoint(mouseX, mouseY));
	
		generateShapesRandomly(200);
	}

	public void draw() {

		background(0);

		stroke(255);
		noFill();

		quadTree.show(this, false, true);

		if (ranges.get(index) instanceof CustomRectangle) {
			range = (CustomRectangle) ranges.get(index);
		} else if (ranges.get(index) instanceof CustomEllipse) {
			range = (CustomEllipse) ranges.get(index);
		} else if (ranges.get(index) instanceof CustomPoint) {
			range = (CustomPoint) ranges.get(index);
		}

		range.setPosition(mouseX, mouseY);

		range.show(this);

		fill(255, 0, 0);

		try {
			found = quadTree.query(null, range);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (CustomShape shape : found) {
			if (range.colliding(shape)) {
				fill(255, 0, 0);
				if (shape instanceof CustomPoint) {
					stroke(255, 0, 0);
					shape.show(this);
					stroke(255);
				} else {
					shape.show(this);
				}
				noFill();
			}
		}
	}

	public void keyPressed() {

		if (key == 'z' || key == 'Z') {
			debug_enabled[0] = !debug_enabled[0];
		}

		if (key == 'x' || key == 'X') {
			debug_enabled[1] = !debug_enabled[1];
		}

	}

	public void keyReleased() {
		if (key == 'a' || key == 'A') {
			if (index == 0) {
				index = ranges.size() - 1;
			} else {
				--index;
			}
		}

		if (key == 'd' || key == 'D') {
			if (index == ranges.size() - 1) {
				index = 0;
			} else {
				++index;
			}
		}
	}

	public void mouseWheel(MouseEvent event) {
		
	}

	public void mouseReleased() {
		CustomShape shape = null;
		if (ranges.get(index) instanceof CustomRectangle) {
			shape = new CustomRectangle(mouseX, mouseY, 50, 50);
		} else if (ranges.get(index) instanceof CustomEllipse) {
			shape = new CustomEllipse(mouseX, mouseY, 50, 50);
		} else if (ranges.get(index) instanceof CustomPoint) {
			shape = new CustomPoint(mouseX, mouseY);
		}
		insertShape(shape);
	}

	private void insertShape(CustomShape shape) {
		try {
			quadTree.insert(shape);
		} catch (Exception e) {
			e.printStackTrace();
		}
		allShapes.add(shape);
	}

	private void generateShapesRandomly(int total) {
		Random random = new Random();
		for (int i = 0; i < total; i++) {
			CustomShape temp = null;
			int num = random.nextInt(3);
			if (num == 0) {
				temp = new CustomRectangle(random(0, width), random(0, height), random(1, 50), random(1, 50));
			} else if (num == 1) {
				float diameter = random(2, 50); 
				temp = new CustomEllipse(random(0, width), random(0, height), diameter, diameter);
			} else if (num == 2) {
				temp = new CustomPoint(random(0, width), random(0, height));
			}

			if (temp != null) {
				insertShape(temp);
			}
		}
	}
	
}
