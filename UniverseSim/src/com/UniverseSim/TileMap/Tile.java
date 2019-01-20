package com.UniverseSim.TileMap;

import java.io.Serializable;

import org.apache.commons.math3.ode.EquationsMapper;

import com.UniverseSim.CollisionObjects.CustomRectangle;

import processing.core.PApplet;
import processing.event.MouseEvent;

public class Tile extends CustomRectangle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float maxFoodCapacity;
	private float currentFoodLevel;
	private float foodRegrowRate;
	private TileTypes tileType;
	private float alpha = 0;
	private int counter = 0;
	public enum TileTypes {
		OCEAN, FOOD
	}

	public Tile(float x, float y, float width, float height, float maxFoodCapacity, float foodRegrowRate) {
		super(x, y, width, height);

		this.maxFoodCapacity = 0;
		this.currentFoodLevel = 0;
		this.foodRegrowRate = 0;
		
		if (maxFoodCapacity < 0.4) {
			tileType = TileTypes.OCEAN;
		} else {
			tileType = TileTypes.FOOD;
			this.maxFoodCapacity = PApplet.map(maxFoodCapacity, 0.4f, 1f, 0, 255);
			this.currentFoodLevel = PApplet.map(maxFoodCapacity, 0.4f, 1f, 0, 255);;
			this.foodRegrowRate = PApplet.map(maxFoodCapacity, 0f, 1f, 0, 10);
		}

	}

	public float eatFood() {
		if (tileType == TileTypes.FOOD) {
			if (currentFoodLevel <= 0) {
				return 0;
			} else {
				currentFoodLevel -= 2;
				return 2;
			}
		} else {
			return -2;
		}
	}

	public void growFood() {
		currentFoodLevel = (maxFoodCapacity <= currentFoodLevel) ? maxFoodCapacity : currentFoodLevel + foodRegrowRate;
	}

	public void drawTile(PApplet context) {
		if (tileType == TileTypes.OCEAN) {
			context.fill(0);
		} else {
			alpha = PApplet.map(currentFoodLevel, 0, 255, 255, 0);
			context.fill(alpha);
		}
		context.rect(getX(), getY(), getWidth(), getWidth());
		context.noFill();
		if (tileType == TileTypes.FOOD) {
			if(counter>=240) {
				growFood();
				counter=0;
			}
			counter++;
		}
	}

	
	public float getTileColor() {
		return alpha;
	}
	
	public TileTypes getTileType() {
		return tileType;
	}
	
	public float getMaxFoodCapacity() {
		return maxFoodCapacity;
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (int) (prime * result + getX());
        result = (int) (prime * result + getY());
        return result;
    }
	
	@Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tile other = (Tile) obj;
        if (getX() == other.getX() && getY() == other.getY() &&
        	maxFoodCapacity == other.getMaxFoodCapacity() && tileType == other.getTileType()) {
            return true;
        }else {
        	return false;
        }
        
    }       
	
}
