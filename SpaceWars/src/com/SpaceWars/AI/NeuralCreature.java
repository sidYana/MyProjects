package com.SpaceWars.AI;

import org.jblas.util.Random;

public class NeuralCreature implements Comparable<NeuralCreature> {

	private NeuralNetworks brain;
	private float fitnessFunction;
	private creatureStatusLevels currentStatus;

	private NeuralCreature() {
		fitnessFunction = Random.nextInt(100);
		currentStatus = creatureStatusLevels.ALIVE;
	}

	public NeuralCreature(int[] networkShape) {
		this();
		brain = new NeuralNetworks(networkShape);
	}

	public NeuralCreature(NeuralNetworks brain) {
		this();
		this.brain = brain;
	}

	public NeuralNetworks getBrain() {
		return brain;
	}

	public void addToFitnessFunction(float val) {
		fitnessFunction += val;
	}

	public float getFitnessFunction() {
		return fitnessFunction;
	}

	public void setCreatureStatus(creatureStatusLevels level) {
		currentStatus = level;
	}

	public creatureStatusLevels getCreatureStatus() {
		return currentStatus;
	}

	@Override
	public int compareTo(NeuralCreature o) {

		if (this.getFitnessFunction() > o.getFitnessFunction()) {
			return -1;
		} else if (this.getFitnessFunction() < o.getFitnessFunction()) {
			return 1;
		} else {
			return 0;
		}
	}

}
