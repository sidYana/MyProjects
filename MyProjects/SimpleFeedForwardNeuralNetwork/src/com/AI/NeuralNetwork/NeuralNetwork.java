package com.AI.NeuralNetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jblas.FloatMatrix;

public class NeuralNetwork implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5387687620938718562L;
	private float[][] inputData;
	private float[][] targetData;
	private int epochs;
	private float learningRate;
	private int checkTrainingRate; 
	private List<FloatMatrix> networkWeights;
	private int[] networkShape;
	private boolean isTrainable = false;
	
	public static void main(String[] args) {
		
		try {
			
			float[][] inputs = {
					{0,0,0},
					{0,0,1},
					{0,1,0},
					{0,1,1},
					{1,0,0},
					{1,0,1},
					{1,1,0},
					{1,1,1}
			};
			
			float[][] targets = {
					{0,0,0,0,0,0,0},
					{0,0,0,0,0,0,1},
					{0,0,0,0,0,1,0},
					{0,0,0,0,1,0,0},
					{0,0,0,1,0,0,0},
					{0,0,1,0,0,0,0},
					{0,1,0,0,0,0,0},
					{1,0,0,0,0,0,0}
			};
			
			NNConfig config = new NNConfig();
			config.setInputData(inputs);
			config.setTargetData(targets);
			config.setEpochs(5000);
			config.setLearningRate(0.05f);
			config.setCheckTrainingRate(1000);
			config.setNetworkShape(new int[] {inputs[0].length,5,5,targets[0].length});
			
			NeuralNetwork nn = new NeuralNetwork(config);
			
			nn.trainSimpleFeedForwardNN();
			
			NeuralNetworkUtils utils = new NeuralNetworkUtils();
			utils.writeNetworkToFile(nn, "network.ser");
			NeuralNetwork newNetwork = utils.readNetworkFromFile("network.ser");
			if(newNetwork != null) {
				System.out.println(Arrays.toString(newNetwork.predict(inputs[3])));
			}else {
				System.out.println("could not get NN File");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public NeuralNetwork(int[] networkShape) throws Exception {
		this.networkShape = networkShape;
		if(networkShape.length<2) {
			throw new Exception("network shape cannot be less than 2 layers");
		}
		generateNetworkWeights();
	}
	
	public NeuralNetwork(NNConfig config) {
		this.inputData = config.getInputData();
		this.targetData = config.getTargetData();
		this.epochs = config.getEpochs();
		this.learningRate = config.getLearningRate();
		this.checkTrainingRate = config.getCheckTrainingRate();
		this.networkShape = config.getNetworkShape();
		this.isTrainable = true;
		generateNetworkWeights();
	}
	
	private void generateNetworkWeights() {
		networkWeights = new ArrayList<FloatMatrix>();
		for(int count=0; count < networkShape.length-1; count++) {
			FloatMatrix temp = FloatMatrix.rand(networkShape[count+1], networkShape[count]).sub(0.5f);
			networkWeights.add(temp);
		}
	}

	public float[] predict(float[] inputs) {
		FloatMatrix result = new FloatMatrix(inputs);
		for(int count = 0; count < networkShape.length-1; count++) {
			result = networkWeights.get(count).mmul(result);
			result = applyActivationFunction(result);
		}
		return result.toArray();
	}
	
	public void trainSimpleFeedForwardNN() throws Exception {
		if(!isTrainable) {
			throw new Exception("Please configure the network before training it");
		}
		
		List<Integer> trainingIndexes = createTrainingIndexes(inputData.length);
		for(int epochCounter = 0; epochCounter < epochs; epochCounter++) {
			trainingIndexes = shuffleTrainingIndexes(trainingIndexes);
			for(int index : trainingIndexes) {
				System.out.println("epoch:"+epochCounter);
				trainOneInput(inputData[index], targetData[index]);
			}

			if(epochCounter % checkTrainingRate == 0) {
				System.out.println("epoch:"+epochCounter);
				for(float[] input : inputData) {
					float[] prediction = predict(input);
					System.out.println("input:"+Arrays.toString(input));
					System.out.println("prediction:"+Arrays.toString(prediction));
				}
			}
		}
		
		for(float[] input : inputData) {
			float[] prediction = predict(input);
			System.out.println("input:"+Arrays.toString(input));
			System.out.println("prediction:"+Arrays.toString(prediction));
		}
		
	}
	
	private void trainOneInput(float[] input, float[] target) {
		FloatMatrix inputMatrix = new FloatMatrix(input);
		FloatMatrix targetMatrix = new FloatMatrix(target);
		FloatMatrix result = inputMatrix;
		List<FloatMatrix> hiddenOutputs = new ArrayList<FloatMatrix>();
		
		hiddenOutputs.add(inputMatrix);
		for(FloatMatrix weight : networkWeights) {
			result = weight.mmul(result);
			result = applyActivationFunction(result);
			hiddenOutputs.add(result);
		}

		FloatMatrix outputError = targetMatrix.sub(hiddenOutputs.get(hiddenOutputs.size()-1));
		FloatMatrix gradient;
		for(int index = networkWeights.size()-1; index >=0; index--) {
			gradient = applyInverseGradient(hiddenOutputs.get(index+1));
			gradient = gradient.mul(outputError);
			gradient = gradient.mmul(hiddenOutputs.get(index).transpose());
			gradient = gradient.mul(learningRate);
			networkWeights.set(index, networkWeights.get(index).add(gradient));
			outputError = networkWeights.get(index).transpose().mmul(outputError);
		}
	}
	
	private List<Integer> createTrainingIndexes(int length){
		List<Integer> indexes = new ArrayList<Integer>(); 
		for(int index = 0; index < length; index++) {
			indexes.add(index);
		}
		return indexes;
	}
	
	private List<Integer> shuffleTrainingIndexes(List<Integer> indexes){
		Collections.shuffle(indexes);
		return indexes;
	}
	
	private FloatMatrix applyInverseGradient(FloatMatrix input) {
		FloatMatrix matrix = input;
		return matrix.mul(FloatMatrix.ones(matrix.rows, matrix.columns).sub(matrix));
	}
	
	private FloatMatrix applyActivationFunction(FloatMatrix matrix) {
		FloatMatrix newMatrix = new FloatMatrix(matrix.rows, matrix.columns);
		for (int i = 0; i < matrix.rows; i++) {
			for (int j = 0; j < matrix.columns; j++) {
				newMatrix.put(i, j, sigmoid(matrix.get(i, j)));
			}
		}
		return newMatrix;
	}

	private Float sigmoid(Float value) {
		return 1 /( (float) (1 + Math.exp(-value)));
	}

}
