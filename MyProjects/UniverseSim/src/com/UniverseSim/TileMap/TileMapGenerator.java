package com.UniverseSim.TileMap;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.UniverseSim.CollisionObjects.CustomRectangle;
import com.UniverseSim.CollisionObjects.CustomShape;
import com.UniverseSim.QuadTree.QuadTreeV2;
import com.UniverseSim.ResourceLoader.PropertiesLoader;

import processing.core.PApplet;

public class TileMapGenerator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<Tile> tiles;

	private QuadTreeV2 qTree;

	private int tileSize = Integer.parseInt(PropertiesLoader.getProperty("tileSize"));
	private int worldSizeWidth = Integer.parseInt(PropertiesLoader.getProperty("worldSizeWidth"));
	private int worldSizeHeight = Integer.parseInt(PropertiesLoader.getProperty("worldSizeHeight"));

	public TileMapGenerator(PApplet context) {
		CustomRectangle boundary = new CustomRectangle(0, 0, worldSizeWidth, worldSizeHeight);
		qTree = new QuadTreeV2(0, 4, boundary);
		generateTileMap(context);
		System.out.println("generated");
		context.noiseSeed(context.millis());
	}

	public void generateTileMap(PApplet sketch) {
		tiles = new ArrayList<Tile>();

		float xOffset, yOffset;
		xOffset = 0;
		for (int i = 0; i < worldSizeWidth; i+=tileSize) {
			yOffset = 0;
			for (int j = 0; j < worldSizeHeight; j+=tileSize) {
				Tile tile = new Tile(i, j, tileSize, tileSize, sketch.noise(xOffset, yOffset), sketch.random(0, 1));
				tiles.add(tile);
				try {
					qTree.insert(tile);
				} catch (Exception e) {
					e.printStackTrace();
				}
				yOffset += 0.05;
			}
			xOffset += 0.05;
		}
	}

	public void drawTileMap(PApplet context) {
		context.noFill();
		context.stroke(255, 0, 0);
		context.strokeWeight(5);
		context.rect(0, 0, worldSizeWidth, worldSizeHeight);
		context.noStroke();
		for (Tile tile : tiles) {
			tile.drawTile(context);
		}
		context.stroke(1);
	}

	public Set<CustomShape> queryTileMap(CustomShape range) {
		try {
			return qTree.query(null, range);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new HashSet<>();
	}

	public void updateTileMap(Tile updatedTile) throws Exception {
		
		int index = tiles.indexOf(updatedTile);
		tiles.remove(index);
		tiles.add(index, updatedTile);

	}
	
	public void saveCurrentMap(PApplet context) {
		String filePath = "SavedData/Maps/";
		String fileName = "tileMap_"+context.millis();
		try {
			FileOutputStream out = new FileOutputStream(filePath+fileName);
			ObjectOutputStream oos = new ObjectOutputStream(out);
			System.out.println("Writing network to file:Started");
			oos.writeObject(this);
			System.out.println("Writing network to file:Finished");
			oos.close();
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file:"+fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
}
