package com.SpaceWars.AI;

import java.util.ArrayList;
import java.util.List;

import org.jblas.DoubleMatrix;

import processing.core.PApplet;
import processing.core.PVector;

public class NeuralNetworks extends PApplet {

	private List<DoubleMatrix> nnWeights;
	private DoubleMatrix outputMatrix;
	private int[] networkShape;

	public NeuralNetworks(int[] networkShape) {
		this.networkShape = networkShape;
		generateWeights(this.networkShape);
	}
	
	public NeuralNetworks(int[] networkShape,List<DoubleMatrix> newWeights) {
		this.networkShape = networkShape;
		this.nnWeights = newWeights;
	}

	public static void main(String[] args) {
		int[] myNNShape = { 2,3,5,8,5,3,2 };
		NeuralNetworks nn = new NeuralNetworks(myNNShape);
		double[] myInput = {1,2};
		for(double val : nn.predict(myInput)) {
			System.out.println(val);
		}
	}

	private DoubleMatrix randomize(DoubleMatrix input) {
		return input.sub(0.5);
	}

	private void generateWeights(int[] networkShape) {
		nnWeights = new ArrayList<DoubleMatrix>();
		DoubleMatrix nnWeight;
		PVector shape = new PVector();

		for (int index = 0; index < networkShape.length - 1; index++) {
			shape.set(networkShape[index + 1], networkShape[index]);
			nnWeight = DoubleMatrix.rand((int) shape.x, (int) shape.y);
			nnWeight = randomize(nnWeight);
			nnWeights.add(nnWeight);
		}
	}

	private double applySigmoid(double value) {
		return 1 / (1 - Math.exp(-value));
	}

	private DoubleMatrix applyMap(DoubleMatrix matrix, String activationFunctionType) {
		DoubleMatrix result = matrix;
		for (int row = 0; row < matrix.rows; row++) {
			for (int col = 0; col < matrix.columns; col++) {
				if (activationFunctionType == "SIGMOID") {
					matrix.put(row, col, applySigmoid(matrix.get(row, col)));
				}
			}
		}
		return result;
	}

	public List<DoubleMatrix> getNNWeights(){
		return nnWeights;
	}
	
	public double[] predict(double[] input) {
		outputMatrix = new DoubleMatrix(input);
		for (int index = 0; index < nnWeights.size(); index++) {
			outputMatrix = nnWeights.get(index).mmul(outputMatrix);
			outputMatrix = applyMap(outputMatrix, "SIGMOID");
		}
		return outputMatrix.toArray();
	}

	public int[] getNNShape() {
		return networkShape;
	}
	
}
