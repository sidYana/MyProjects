package com.AI.GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jblas.FloatMatrix;

import com.AI.NeuralNetwork.NeuralNetwork;
import com.game.flappyBird.PlayerCreature;
import com.game.flappyBird.PlayerCreatureComparator;

public class GeneticAlgorithm {

	List<PlayerCreature> pool;
	double crossoverRate;
	double mutationRate;

	int poolSize;
	int selectionSize;
	
	float screenWidth, screenHeight;

	long generationCount;
	
	public GeneticAlgorithm(int poolSize, int selectionSize, double crossoverRate, double mutationRate, float width, float height) {
		this.poolSize = poolSize;
		this.selectionSize = selectionSize;
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;
		this.screenWidth = width;
		this.screenHeight = height;
		this.generationCount = 0;
		this.pool = new ArrayList<>();
		for (int i = 0; i < poolSize; i++) {
			pool.add(new PlayerCreature(width, height));
		}
	}

	public List<PlayerCreature> getPool() {
		return pool;
	}

	public void recreatePool(List<PlayerCreature> creatures) {

		pool =creatures;
		Collections.sort(pool, new PlayerCreatureComparator());

		pool = pool.subList(0, selectionSize);

		List<PlayerCreature> newPool = new ArrayList<>();
		
		for(int i=0;i<poolSize;i++) {
			newPool.add(crossover());
		}
		
		pool = newPool;

		generationCount++;
		
	}

	private PlayerCreature crossover() {

		int parentAIndex = (int) (Math.random() * selectionSize);
		int parentBIndex = (int) (Math.random() * selectionSize);

		PlayerCreature parentA = pool.get(parentAIndex);
		PlayerCreature parentB = pool.get(parentBIndex);
		PlayerCreature child;

		if (parentAIndex == parentBIndex) {
			child = parentA;
		} else {

			child = pickFromTwo(parentA, parentB);

		}
		return child;

	}

	private PlayerCreature pickFromTwo(PlayerCreature parentA, PlayerCreature parentB) {

		NeuralNetwork parentA_brain = parentA.getBrain();
		NeuralNetwork parentB_brain = parentB.getBrain();

		FloatMatrix[] parentA_weights = new FloatMatrix[2];
		parentA_weights[0] = parentA_brain.getWeights_ih();
		parentA_weights[1] = parentA_brain.getWeights_ho();

		FloatMatrix[] parentB_weights = new FloatMatrix[2];
		parentB_weights[0] = parentB_brain.getWeights_ih();
		parentB_weights[1] = parentB_brain.getWeights_ho();

		NeuralNetwork newBrain = new NeuralNetwork(matrixCrossoverAndMutate(parentA_weights[0], parentB_weights[0]),
				matrixCrossoverAndMutate(parentA_weights[1], parentB_weights[1]));

		return new PlayerCreature(screenWidth, screenHeight, newBrain);

	}

	private FloatMatrix matrixCrossoverAndMutate(FloatMatrix A, FloatMatrix B) {
		FloatMatrix C = FloatMatrix.zeros(A.rows, A.columns);

		for (int row = 0; row < A.rows; row++) {
			for (int col = 0; col < A.columns; col++) {
				if (Math.random() > mutationRate) {
					C.put(row, col, (Math.random() > crossoverRate) ? A.get(row, col) : B.get(row, col));
				}else {
					C.put(row, col, (float) (-0.5 + Math.random()*0.5));
				}
			}
		}

		return C;

	}
	
	public long getGenerationCount() {
		return generationCount;
	}

}
