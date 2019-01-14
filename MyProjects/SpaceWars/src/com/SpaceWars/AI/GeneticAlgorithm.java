package com.SpaceWars.AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jblas.DoubleMatrix;
import org.jblas.util.Random;

public class GeneticAlgorithm {

	private int populationSize = 20;
	private List<NeuralCreature> population;
	private float crossoverRate = 0.1f;
	private float mutationRate = 0.55f;
	private int[] networkShape = { 10, 500, 500, 10 };

	public static void main(String[] args) {
		GeneticAlgorithm gene = new GeneticAlgorithm();
		gene.generateNewPopulation();
	}

	public GeneticAlgorithm() {
		population = new ArrayList<>();
		for (int count = 0; count < populationSize; count++) {
			NeuralCreature creature = new NeuralCreature(networkShape);
			population.add(creature);
		}
	}

	private NeuralCreature crossover(NeuralCreature parentA, NeuralCreature parentB) {
		NeuralNetworks parentABrain = parentA.getBrain();
		NeuralNetworks parentBBrain = parentB.getBrain();
		int[] layers = parentABrain.getNNShape();
		List<DoubleMatrix> childBrainLayers = new ArrayList<>();
		DoubleMatrix childBrainLayer;
		double parentValue;
		for (int layer = 0; layer < layers.length-1; layer++) {
			int rows = parentABrain.getNNWeights().get(layer).rows;
			int cols = parentABrain.getNNWeights().get(layer).columns;
			childBrainLayer = new DoubleMatrix(rows, cols);
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					if (Math.random() > mutationRate) {
						if (Math.random() > crossoverRate) {
							parentValue = parentABrain.getNNWeights().get(layer).get(row, col);
						} else {
							parentValue = parentBBrain.getNNWeights().get(layer).get(row, col);
						}
					} else {
						parentValue = Math.random() - 0.5;
					}
					childBrainLayer.put(row, col, parentValue);
				}
			}
			childBrainLayers.add(childBrainLayer);
		}
		NeuralCreature child = new NeuralCreature(new NeuralNetworks(networkShape, childBrainLayers));
		return child;
	}

	public void generateNewPopulation() {
		Collections.sort(population);
		population = population.subList(0, 5);

		NeuralCreature parentA, parentB, child;
		for (int i = 0; i < populationSize - population.size(); i++) {
			parentA = population.get(Random.nextInt(5));
			parentB = population.get(Random.nextInt(5));
			child = crossover(parentA, parentB);
			population.add(child);
		}

	}

}
