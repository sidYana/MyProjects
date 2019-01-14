package com.AI.GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jblas.FloatMatrix;

import com.AI.NeuralNetwork.NeuralNetwork;
import com.UniverseSim.Creatures.Creature;
import com.UniverseSim.Creatures.CreatureAgeComparator;

import processing.core.PApplet;
import processing.core.PVector;

public class GeneticAlgorithm {

	private List<Creature> pool;
	private double crossoverRate;
	private double mutationRate;

	private static long creatureCounter = 0L; 
	
	private int poolSize;
	private int selectionSize;
	
	private float worldWidth, worldHeight;

	private long generationCount;
	
	private PApplet context;
	
	public GeneticAlgorithm(int poolSize, int selectionSize, double crossoverRate, double mutationRate, float width, float height, PApplet context) {
		this.poolSize = poolSize;
		this.selectionSize = selectionSize;
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;
		this.worldWidth = width;
		this.worldHeight = height;
		this.generationCount = 0;
		this.pool = new ArrayList<>();
		this.context = context;
		for (int i = 0; i < poolSize; i++) {
			try {
				pool.add(new Creature(getNextCreatureIndex(), new PVector(width, height), context));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public List<Creature> getPool() {
		return pool;
	}

	public Creature createPrimordialCreature(List<Creature> creatures) {
		pool = creatures;
		Collections.sort(pool, new CreatureAgeComparator());
		pool = pool.subList(0, selectionSize);
		
		Creature A = pool.get((int)Math.random() * selectionSize);
		Creature B = pool.get((int)Math.random() * selectionSize);
		
		return crossover(A , B, true);
	}
	
	public Creature createCreatureFromCrossover(Creature A, Creature B) {
				
		return crossover(A, B, false);
		
	}
		
	private Creature crossover(Creature A, Creature B, boolean isPrimordial) {

		List<FloatMatrix> parentABrainWeights = A.getFitness() > B.getFitness() ? A.getBrain().getWeights() : B.getBrain().getWeights();
		List<FloatMatrix> parentBBrainWeights = A.getFitness() > B.getFitness() ? B.getBrain().getWeights() : A.getBrain().getWeights();
		
		List<FloatMatrix> newBrainWeights = new ArrayList<>();

		for (int index = 0; index < parentABrainWeights.size(); index++) {
			newBrainWeights.add(matrixCrossoverAndMutate(parentABrainWeights.get(index), parentBBrainWeights.get(index)));
		}
		NeuralNetwork nn = new NeuralNetwork(newBrainWeights);

		try {
			if (isPrimordial) {
				return new Creature(getNextCreatureIndex(), new PVector(worldWidth, worldHeight), context, nn);
			} else {
				return new Creature(getNextCreatureIndex(), new PVector(worldWidth, worldHeight), A.getCreatureColor(), B.getCreatureColor(), context, nn);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

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
	
	public static long getNextCreatureIndex() {
		return ++creatureCounter;
	}
	
	public long getGenerationCount() {
		return generationCount;
	}

}
