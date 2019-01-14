package com.AI.NeuralNetwork;

public class NNConfig {

	private float[][] inputData;
	private float[][] targetData;
	private int epochs;
	private int[] networkShape;
	private float learningRate;
	private int checkTrainingRate;
	
	public NNConfig() {
	}

	public float[][] getInputData() {
		return inputData;
	}

	public void setInputData(float[][] inputData) {
		this.inputData = inputData;
	}

	public float[][] getTargetData() {
		return targetData;
	}

	public void setTargetData(float[][] targetData) {
		this.targetData = targetData;
	}

	public int getEpochs() {
		return epochs;
	}

	public void setEpochs(int epochs) {
		this.epochs = epochs;
	}

	public int[] getNetworkShape() {
		return networkShape;
	}

	public void setNetworkShape(int[] networkShape) {
		this.networkShape = networkShape;
	}

	public float getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(float learningRate) {
		this.learningRate = learningRate;
	}

	public int getCheckTrainingRate() {
		return checkTrainingRate;
	}

	public void setCheckTrainingRate(int checkTrainingRate) {
		this.checkTrainingRate = checkTrainingRate;
	}
	
}
