package com.UnivSim.main;

import java.util.List;

import com.AI.GeneticAlgorithm.GeneticAlgorithm;
import com.UniverseSim.Creatures.Creature;
import com.UniverseSim.QuadTree.QuadTreeV2;
import com.UniverseSim.TileMap.TileMapGenerator;

public class CreatureAIParams {

	private TileMapGenerator tileMap;
	private List<Creature> newCreatures;
	private QuadTreeV2 creatureQTree;
	private GeneticAlgorithm genetic;

	public TileMapGenerator getTileMap() {
		return tileMap;
	}

	public void setTileMap(TileMapGenerator tileMap) {
		this.tileMap = tileMap;
	}

	public List<Creature> getNewCreatures() {
		return newCreatures;
	}

	public void setNewCreatures(List<Creature> creatures) {
		this.newCreatures = creatures;
	}

	public QuadTreeV2 getCreatureQTree() {
		return creatureQTree;
	}

	public void setCreatureQTree(QuadTreeV2 creatureQTree) {
		this.creatureQTree = creatureQTree;
	}

	public GeneticAlgorithm getGenetic() {
		return genetic;
	}

	public void setGenetic(GeneticAlgorithm genetic) {
		this.genetic = genetic;
	}

}
