package com.AI.NeuralNetwork;

import java.util.List;

import org.jblas.FloatMatrix;

public class NeuralNetwork {

	private FloatMatrix weights_ih;
	private FloatMatrix weights_ho;

	private float learningRate = 0.15f;
	
	public NeuralNetwork(int inputs, int outputs, int hidden) {

		weights_ih = FloatMatrix.rand(hidden, inputs).sub(0.5f);
		weights_ho = FloatMatrix.rand(outputs, hidden).sub(0.5f);
		
	}

	public NeuralNetwork(FloatMatrix weights_ih, FloatMatrix weights_ho) {
		
		this.weights_ih = weights_ih;
		this.weights_ho = weights_ho;
		
	}
	
	public float[] feedForward(float[] input_array) {

		FloatMatrix inputsM = new FloatMatrix(input_array);

		FloatMatrix hidden_layer = weights_ih.mmul(inputsM);
		hidden_layer = applyActivationFunction(hidden_layer);

		// Hidden to output
		FloatMatrix output_layer = weights_ho.mmul(hidden_layer);
		output_layer = applyActivationFunction(output_layer);
		
		return output_layer.toArray();
	}

	public float[] train(float[] input_array, float[] answer_array) {

		FloatMatrix inputsM = new FloatMatrix(input_array);
		FloatMatrix targetsM = new FloatMatrix(answer_array);

		FloatMatrix hidden_layer = weights_ih.mmul(inputsM);
		hidden_layer = applyActivationFunction(hidden_layer);

		// Hidden to output
		FloatMatrix output_layer = weights_ho.mmul(hidden_layer);
		output_layer = applyActivationFunction(output_layer);
		
		/////////////////////////////////////////////////////

		FloatMatrix weights_ho_T = weights_ho.transpose();
		
		FloatMatrix output_error = targetsM.sub(output_layer);
		FloatMatrix hidden_layer_T = hidden_layer.transpose();
		FloatMatrix desigmoid_final_outputs = desigmoid(output_layer);
		FloatMatrix gradient_ho = output_error.mul(desigmoid_final_outputs);
		gradient_ho = gradient_ho.mmul(hidden_layer_T);
		gradient_ho = gradient_ho.mul(learningRate);
		weights_ho = weights_ho.add(gradient_ho);

		FloatMatrix hidden_error = weights_ho_T.mmul(output_error);
		FloatMatrix input_layer_T = inputsM.transpose();
		FloatMatrix desigmoid_hidden_outputs = desigmoid(hidden_layer);
		FloatMatrix gradient_ih = hidden_error.mul(desigmoid_hidden_outputs);
		gradient_ih = gradient_ih.mmul(input_layer_T);
		gradient_ih = gradient_ih.mul(learningRate);
		weights_ih = weights_ih.add(gradient_ih);
		
		return output_error.toArray();
	}

	public FloatMatrix applyActivationFunction(FloatMatrix matrix) {
		FloatMatrix newMatrix = new FloatMatrix(matrix.rows, matrix.columns);
		for (int i = 0; i < matrix.rows; i++) {
			for (int j = 0; j < matrix.columns; j++) {
				newMatrix.put(i, j, sigmoid(matrix.get(i, j)));
			}
		}
		return newMatrix;
	}

	public FloatMatrix desigmoid(FloatMatrix matrix) {
		FloatMatrix desigmoidMatrix = matrix;
		
		desigmoidMatrix.mul(desigmoidMatrix.rsub(1));
		
		return desigmoidMatrix;
	}

	private Float sigmoid(Float value) {
		return 1 /( (float) (1 + Math.exp(-value)));
	}

	public FloatMatrix getWeights_ih() {
		return weights_ih;
	}
	
	public FloatMatrix getWeights_ho() {
		return weights_ho;
	}
	
}
