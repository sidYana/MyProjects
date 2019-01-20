package com.UniverseSim.QuadTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.UniverseSim.CollisionObjects.CustomEllipse;
import com.UniverseSim.CollisionObjects.CustomPoint;
import com.UniverseSim.CollisionObjects.CustomRectangle;
import com.UniverseSim.CollisionObjects.CustomShape;

import processing.core.PApplet;

public class QuadTreeV2 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int MAX_OBJECTS;
	private final int MAX_LEVELS = 5;

	private int level;
	private CustomRectangle boundary;
	private List<CustomShape> shapes;
	private QuadTreeV2[] subNodes;

	private double verticalMidpoint;
	private double horizontalMidpoint;
	
	private boolean topQuadrant = false;
	private boolean bottomQuadrant = false;
	private boolean leftQuadrant = false;
	private boolean rightQuadrant = false;
	
	public QuadTreeV2(int level, int max_objects, CustomRectangle shape) {
		this.level = level;
		this.MAX_OBJECTS = max_objects;
		this.shapes = new ArrayList<CustomShape>();
		this.boundary = shape;
		
		this.verticalMidpoint = boundary.getX() + boundary.getWidth() / 2;
		this.horizontalMidpoint = boundary.getY() + boundary.getHeight() / 2;
		
		this.subNodes = new QuadTreeV2[4];
	}

	public void clear() {
		shapes.clear();
		for (int index = 0; index < subNodes.length; index++) {
			if (subNodes[index] != null) {
				subNodes[index].clear();
				subNodes[index] = null;
			}
		}
	}

	private void split() {

		QuadTreeV2 NW = new QuadTreeV2(level + 1, MAX_OBJECTS, new CustomRectangle(boundary.getX(), boundary.getY(), boundary.getWidth() / 2, boundary.getHeight() / 2));
		QuadTreeV2 NE = new QuadTreeV2(level + 1, MAX_OBJECTS, new CustomRectangle(boundary.getX() + boundary.getWidth() / 2, boundary.getY(), boundary.getWidth() / 2, boundary.getHeight() / 2));
		QuadTreeV2 SW = new QuadTreeV2(level + 1, MAX_OBJECTS, new CustomRectangle(boundary.getX(), boundary.getY() + boundary.getHeight() / 2, boundary.getWidth() / 2, boundary.getHeight() / 2));
		QuadTreeV2 SE = new QuadTreeV2(level + 1, MAX_OBJECTS, new CustomRectangle(boundary.getX() + boundary.getWidth() / 2, boundary.getY() + boundary.getHeight() / 2, boundary.getWidth() / 2, boundary.getHeight() / 2));

		subNodes[0] = NW;
		subNodes[1] = NE;
		subNodes[2] = SW;
		subNodes[3] = SE;

	}

	private void getQuadrantsUsingRectangle(CustomRectangle range) {
		topQuadrant = range.getY() < horizontalMidpoint;
		bottomQuadrant = range.getY() + range.getHeight() > horizontalMidpoint;
		leftQuadrant = range.getX() < verticalMidpoint;
		rightQuadrant = range.getX() + range.getWidth() > verticalMidpoint;
	}
	
	private void getQuadrantsUsingEllipse(CustomEllipse range) {
		topQuadrant = range.getY() - range.getRadius() < horizontalMidpoint;
		bottomQuadrant = range.getY() + range.getRadius() > horizontalMidpoint;
		leftQuadrant = range.getX() - range.getRadius() < verticalMidpoint;
		rightQuadrant = range.getX() + range.getRadius() > verticalMidpoint;
	}
	
	private void getQuadrantsUsingPoint(CustomPoint range) {
		topQuadrant = range.getPosition().y < horizontalMidpoint;
		bottomQuadrant = range.getPosition().y > horizontalMidpoint;
		leftQuadrant = range.getPosition().x < verticalMidpoint;
		rightQuadrant = range.getPosition().x > verticalMidpoint;
	}
	
	private boolean[] getQuadrants(CustomShape range) throws Exception {
		
		boolean[] quads = new boolean[4];
		
		if(range instanceof CustomRectangle) {
			getQuadrantsUsingRectangle((CustomRectangle) range);
		} else if(range instanceof CustomEllipse) {
			getQuadrantsUsingEllipse((CustomEllipse) range);
		} else if(range instanceof CustomPoint) {
			getQuadrantsUsingPoint((CustomPoint) range);
		} else {
			throw new Exception("shape not supported");
		}
		
		if (leftQuadrant) {
			if (topQuadrant) {
				quads[0] = true;
			}
			if (bottomQuadrant) {
				quads[2] = true;
			}
		}
		if (rightQuadrant) {
			if (topQuadrant) {
				quads[1] = true;
			}
			if (bottomQuadrant) {
				quads[3] = true;
			}
		}

		return quads;
	}

	public void insert(CustomShape shape) throws Exception {

		if(shape==null) {
			return;
		}
		
		if (subNodes[0] != null) {
			boolean[] quadrants = getQuadrants(shape);
			for (int index = 0; index < quadrants.length; index++) {
				if (quadrants[index]) {
					subNodes[index].insert(shape);
				}
			}
			return;
		}

		shapes.add(shape);

		if (shapes.size() > MAX_OBJECTS && level < MAX_LEVELS) {
			if (subNodes[0] == null) {
				split();
			}
			for (int i = 0; i < shapes.size(); i++) {
				boolean[] quadrants = getQuadrants(shapes.get(i));
				for (int index = 0; index < quadrants.length; index++) {
					if (quadrants[index]) {
						subNodes[index].insert(shapes.get(i));
					}
				}
				shapes.remove(i);
			}
		}

	}

	public Set<CustomShape> query(Set<CustomShape> found, CustomShape range) throws Exception {

		if (found == null) {
			found = new HashSet<>();
		}

		if (subNodes[0] != null) {
			boolean[] quadrants = getQuadrants(range);
			for (int index = 0; index < quadrants.length; index++) {
				if (quadrants[index]) {
					subNodes[index].query(found, range);
				}
			}
		}
		found.addAll(shapes);
		return found;

	}

	public void show(PApplet context, boolean showTree, boolean showObjects) {

		if (showTree) {
			context.rect(boundary.getX(), boundary.getY(), boundary.getWidth(), boundary.getHeight());
		}

		if (showObjects) {
			for (CustomShape shape : shapes) {
				shape.show(context);
			}
		}

		if (subNodes[0] != null) {
			for (QuadTreeV2 subNode : subNodes) {
				subNode.show(context, showTree, showObjects);
			}
		}

	}

}
