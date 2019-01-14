package com.game.flappyBird;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jblas.FloatMatrix;

import com.AI.NeuralNetwork.NeuralNetwork;

import processing.core.PApplet;

public class PlayerCreature {

	public float posX;
	public float posY;

	public float gravityForce = 0.5f;
	public float lift = 0.8f;
	public float vel = 0f;

	public float screenWidth;
	public float screenHeight;

	private boolean isAlive = true;

	private NeuralNetwork brain;

	private double fitness = 0;
	
	public PlayerCreature(float width, float height) {

		this.posX = 100f;
		this.posY = height / 2;

		this.screenWidth = width;
		this.screenHeight = height;

		this.brain = new NeuralNetwork(5, 1, 12);

	}

	public PlayerCreature(float width, float height, NeuralNetwork newBrain) {
		this.posX = 100f;
		this.posY = height / 2;

		this.screenWidth = width;
		this.screenHeight = height;

		this.brain = newBrain;
	}
	
	public void jump() {
		vel += -lift;
	}

	public void drop() {
		vel += gravityForce;
		vel *= 0.9f;
		posY += vel;

		if (posY > screenHeight) {
			posY = screenHeight;
			vel = 0;
		}

		if (posY < 0) {
			posY = 0;
			vel = 0;
		}

	}

	public void update(Pipe pipe) {
		if (pipe.posX + pipe.width > posX) {
			float[] input_array = { posY / screenHeight, //player location Y-Axis 
									pipe.posX / screenWidth, //Pipe Location X-Axis
									(pipe.height - pipe.bottom) / screenHeight, //Pipe bottom height
									pipe.top / screenHeight, //Pipe Top height
									pipe.pipeWidth / screenWidth}; //Pipe width

			float[] output = brain.feedForward(input_array);

			if (output[0] > 0.6) {
				jump();
			} 
			drop();
			fitness+=0.25;
		}
	}

	public void userControl(PApplet context) {
		if(context.keyPressed) {
			if(context.key==' ') {
				jump();
			}
		}
		drop();
	}
	
	public void draw(PApplet context) {
		context.fill(255, 75);
		context.ellipse(posX, posY, 15, 15);
		context.noFill();
	}

	public Pipe findClosestPipe(List<Pipe> pipes, PApplet context) {
		
		Pipe closest = null;
		float closestDist = screenWidth + 1000;
		
		for(Pipe pipe : pipes) {
			float dist = pipe.posX - posX;
			if(dist < closestDist && dist > -pipe.pipeWidth) {
				closest = pipe;
				closestDist = dist;
			}
		}
		
		return closest;
	}
	
	public void drawInterconnections(Pipe pipe,  PApplet context) {
		
		context.stroke(255, 100);
		context.line(posX, posY, pipe.posX, pipe.top);
		context.line(posX, posY, pipe.posX, pipe.height - pipe.bottom);
		
	}
	
	public boolean isPlayerAlive() {
		return isAlive;
	}

	public void playerDied() {
		isAlive = false;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public NeuralNetwork getBrain() {
		return brain;
	}
	
	public void setBrain(NeuralNetwork brain) {
		this.brain = brain;
	}
	
}
